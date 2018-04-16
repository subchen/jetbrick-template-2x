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
package jetbrick.template.resolver.function;

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
 * 用于查找全局注册的扩展函数.
 */
public final class FunctionInvokerResolver {
    private static final Logger log = LoggerFactory.getLogger(FunctionInvokerResolver.class);

    private final ConcurrentMap<String, FunctionInvoker> cache = new ConcurrentHashMap<String, FunctionInvoker>(64);
    private final Map<String, List<MethodInfo>> functionMap = new HashMap<String, List<MethodInfo>>(32);

    /**
     * 注册一组 function 扩展
     */
    public void register(Class<?> cls) {
        KlassInfo klass = KlassInfo.create(cls);
        int i = 0;
        for (MethodInfo method : klass.getDeclaredMethods()) {
            if (method.isStatic() && method.isPublic()) {
                register(method);
                i++;
            }
        }
        log.info("import {} functions from {}", i, cls);
    }

    /**
     * 注册一个 function 扩展
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
            log.debug("import function: {}", sb.toString());
        }

        List<MethodInfo> methods = functionMap.get(name);
        if (methods == null) {
            methods = new ArrayList<MethodInfo>(4);
            functionMap.put(name, methods);
        }
        methods.add(method);
    }

    /**
     * 根据参数类型，查找一个匹配的 function（带缓存）.
     */
    public FunctionInvoker resolve(String name, Class<?>[] argumentTypes) {
        String signature = SignatureUtils.getFunctionSignature(name, argumentTypes);
        FunctionInvoker found = cache.get(signature);
        if (found != null) {
            return found;
        }

        FunctionInvoker function = doGetFunctionInvoker(name, argumentTypes);

        if (function != null) {
            cache.put(signature, function);
            return function;
        }

        return null;
    }

    /**
     * 根据参数类型，查找一个匹配的 function.
     */
    private FunctionInvoker doGetFunctionInvoker(String name, Class<?>[] argumentTypes) {
        List<MethodInfo> functions = functionMap.get(name);
        if (functions != null) {
            MethodInfo function = ExecutableUtils.searchExecutable(functions, null, argumentTypes);
            if (function != null) {
                return new ExtensionFunctionInvoker(function);
            }
        }

        return null;
    }

}
