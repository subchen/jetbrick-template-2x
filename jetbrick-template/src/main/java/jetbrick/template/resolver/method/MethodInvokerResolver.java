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
package jetbrick.template.resolver.method;

import jetbrick.bean.ExecutableUtils;
import jetbrick.bean.KlassInfo;
import jetbrick.bean.MethodInfo;
import jetbrick.template.JetAnnotations;
import jetbrick.template.resolver.SignatureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用于查找全局注册的扩展方法.
 */
public final class MethodInvokerResolver {
    private static final Logger log = LoggerFactory.getLogger(MethodInvokerResolver.class);

    private final ConcurrentMap<String, MethodInvoker> cache = new ConcurrentHashMap<String, MethodInvoker>(256);
    private final Map<String, List<MethodInfo>> extensionMethodMap = new HashMap<String, List<MethodInfo>>(32);

    /**
     * 注册一组方法扩展
     */
    public void register(Class<?> cls) {
        KlassInfo klass = KlassInfo.create(cls);
        int i = 0;
        for (MethodInfo method : klass.getDeclaredMethods()) {
            if (method.isStatic() && method.isPublic() && method.getParameterCount() > 0) {
                register(method);
                i++;
            }
        }
        log.info("import {} methods from {}", i, cls);
    }

    /**
     * 注册一个方法扩展
     */
    public void register(MethodInfo method) {
        String name = method.getName();
        JetAnnotations.Name rename = method.getMethod().getAnnotation(JetAnnotations.Name.class);
        if (rename != null && !"".equals(rename.value())) {
            name = rename.value();
        }

        if (log.isInfoEnabled()) {
            Class<?>[] parameterTypes = method.getParameterTypes();

            StringBuffer sb = new StringBuffer();
            sb.append(parameterTypes[0].getSimpleName()).append('.');
            sb.append(name).append('(');
            for (int i = 1; i < parameterTypes.length; i++) {
                if (i > 1) {
                    sb.append(',');
                }
                sb.append(parameterTypes[i].getName());
            }
            sb.append(')');
            log.debug("import method: {}", sb.toString());
        }

        List<MethodInfo> methods = extensionMethodMap.get(name);
        if (methods == null) {
            methods = new ArrayList<MethodInfo>(4);
            extensionMethodMap.put(name, methods);
        }
        methods.add(method);
    }

    /**
     * 根据参数类型，查找一个匹配的方法（带缓存）.
     * @param isStatic
     */
    public MethodInvoker resolve(Class<?> clazz, String name, Class<?>[] argumentTypes, boolean isStatic) {
        String signature = SignatureUtils.getMethodSignature(clazz, name, argumentTypes);
        MethodInvoker found = cache.get(signature);
        if (found != null) {
            return found;
        }

        MethodInvoker method = doGetMethodInvoker(clazz, name, argumentTypes, isStatic);

        if (method != null) {
            cache.put(signature, method);
            return method;
        }

        return null;
    }

    /**
     * 根据参数类型，查找一个匹配的方法.
     * @param isStatic
     */
    private MethodInvoker doGetMethodInvoker(Class<?> clazz, String name, Class<?>[] argumentTypes, boolean isStatic) {
        KlassInfo klass = KlassInfo.create(clazz);

        // 查找 Class 自己的方法
        MethodInfo method = klass.searchMethod(name, argumentTypes);
        if (method != null && method.isPublic()) {
            if ((isStatic && method.isStatic()) || (!isStatic && !method.isStatic())) {
                return new ClassBuildinMethodInvoker(method);
            } else {
                return null;
            }
        }

        if (!isStatic) {
            // 查找已注册的扩展方法
            List<MethodInfo> methods = extensionMethodMap.get(name);
            if (methods != null) {
                int length = argumentTypes.length;
                Class<?>[] extensionTypes = new Class<?>[length + 1];
                extensionTypes[0] = clazz;
                System.arraycopy(argumentTypes, 0, extensionTypes, 1, length);
                method = ExecutableUtils.searchExecutable(methods, null, extensionTypes);
                if (method != null) {
                    return new ExtensionMethodInvoker(method);
                }
            }
        }

        return null;
    }
}
