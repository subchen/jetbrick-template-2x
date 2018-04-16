/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.resolver.tag;

import jetbrick.bean.ExecutableUtils;
import jetbrick.bean.KlassInfo;
import jetbrick.bean.MethodInfo;
import jetbrick.template.JetAnnotations;
import jetbrick.template.resolver.SignatureUtils;
import jetbrick.template.runtime.JetTagContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用于查找全局注册的 Tag.
 */
public final class TagInvokerResolver {
    private static final Logger log = LoggerFactory.getLogger(TagInvokerResolver.class);

    private final ConcurrentMap<String, TagInvoker> cache = new ConcurrentHashMap<String, TagInvoker>(64);
    private final Map<String, List<MethodInfo>> tagMap = new HashMap<String, List<MethodInfo>>(32);

    /**
     * 注册一组 Tag 扩展
     */
    public void register(Class<?> cls) {
        KlassInfo klass = KlassInfo.create(cls);
        int i = 0;
        for (MethodInfo method : klass.getDeclaredMethods()) {
            if (method.isStatic() && method.isPublic()) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length > 0 && JetTagContext.class.isAssignableFrom(parameterTypes[0])) {
                    register(method);
                    i++;
                }
            }
        }
        log.info("import {} tags from {}", i, cls);
    }

    /**
     * 注册一个 Tag 扩展
     */
    public void register(MethodInfo method) {
        String name = method.getName();
        JetAnnotations.Name rename = method.getMethod().getAnnotation(JetAnnotations.Name.class);
        if (rename != null && !"".equals(rename.value())) {
            name = rename.value();
        }

        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append(name).append('(');
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(parameterTypes[i].getName());
            }
            sb.append(')');
            log.debug("import tag: {}", sb.toString());
        }

        List<MethodInfo> methods = tagMap.get(name);
        if (methods == null) {
            methods = new ArrayList<MethodInfo>(4);
            tagMap.put(name, methods);
        }
        methods.add(method);
    }

    /**
     * 根据参数类型，查找一个匹配的 Tag（带缓存）.
     */
    public TagInvoker resolve(String name, Class<?>[] argumentTypes) {
        String signature = SignatureUtils.getFunctionSignature(name, argumentTypes);
        TagInvoker found = cache.get(signature);
        if (found != null) {
            return found;
        }

        TagInvoker tag = doGetTagInvoker(name, argumentTypes);

        if (tag != null) {
            cache.put(signature, tag);
            return tag;
        }

        return null;
    }

    /**
     * 根据参数类型，查找一个匹配的 tag.
     */
    private TagInvoker doGetTagInvoker(String name, Class<?>[] argumentTypes) {
        List<MethodInfo> tags = tagMap.get(name);
        if (tags != null) {
            int length = argumentTypes.length;
            Class<?>[] types = new Class<?>[length + 1];
            types[0] = JetTagContext.class;
            System.arraycopy(argumentTypes, 0, types, 1, length);
            MethodInfo tag = ExecutableUtils.searchExecutable(tags, null, types);
            if (tag != null) {
                return new TagInvoker(tag);
            }
        }
        return null;
    }

}
