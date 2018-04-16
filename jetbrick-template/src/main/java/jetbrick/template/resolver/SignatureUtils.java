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
package jetbrick.template.resolver;

public final class SignatureUtils {

    /**
     * 获取一个自定义的方法签名(唯一标示符, 也是 cache 的 key)
     */
    public static String getMethodSignature(Class<?> clazz, String name, Class<?>[] argumentTypes) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(clazz.getName());
        sb.append('#').append(name).append('(');
        for (int i = 0; i < argumentTypes.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            Class<?> type = argumentTypes[i];
            if (type == null) {
                sb.append("<null>");
            } else {
                sb.append(type.getName());
            }
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * 获取一个自定义的函数签名(唯一标示符, 也是 cache 的 key)
     */
    public static String getFunctionSignature(String name, Class<?>[] argumentTypes) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(name).append('(');
        for (int i = 0; i < argumentTypes.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            Class<?> type = argumentTypes[i];
            if (type == null) {
                sb.append("<null>");
            } else {
                sb.append(type.getName());
            }
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * 获取一个字段的签名(唯一标示符, 也是 cache 的 key)
     */
    public static String getFieldSignature(Class<?> clazz, String name) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(clazz.getName()).append('#').append(name);
        return sb.toString();
    }

}
