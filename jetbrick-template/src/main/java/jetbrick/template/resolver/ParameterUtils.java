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

import java.lang.reflect.Array;
import jetbrick.util.ArrayUtils;

public final class ParameterUtils {

    /**
     * 获取参数列表对应的参数类型
     */
    public static Class<?>[] getParameterTypes(Object[] arguments) {
        Class<?>[] argumentsTypes;
        int length = arguments.length;
        if (length == 0) {
            argumentsTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        } else {
            argumentsTypes = new Class<?>[length];
            for (int i = 0; i < length; i++) {
                Object object = arguments[i];
                argumentsTypes[i] = (object == null) ? null : object.getClass();
            }
        }
        return argumentsTypes;
    }

    /**
     * 生成方法调用的实际参数
     */
    public static Object[] getActualArguments(Object[] arguments, int actualLength, Class<?> varArgsClass, int offset) {
        if (varArgsClass != null) {
            Object[] args = new Object[actualLength];

            int fixedArgsLen = actualLength - offset - 1; // 固定参数个数

            if (fixedArgsLen > 0) {
                // 先复制固定参数
                System.arraycopy(arguments, 0, args, offset, fixedArgsLen);
            }

            // 处理可变参数
            int varArgsLen = arguments.length - fixedArgsLen;
            if (varArgsLen == 0) {
                args[actualLength - 1] = Array.newInstance(varArgsClass, 0);
            } else {
                Object varArgs = null;
                if (varArgsLen == 1) {
                    Object arg = arguments[fixedArgsLen];
                    if (arg == null) {
                        varArgs = Array.newInstance(varArgsClass, 0);
                    } else if (Object[].class.isAssignableFrom(arg.getClass())) {
                        varArgs = arg;
                    }
                }
                if (varArgs == null) {
                    varArgs = Array.newInstance(varArgsClass, varArgsLen);
                    System.arraycopy(arguments, fixedArgsLen, varArgs, 0, varArgsLen);
                }
                args[actualLength - 1] = varArgs;
            }
            return args;
        }

        if (offset > 0) {
            Object[] args = new Object[actualLength];
            int length = actualLength - offset;
            if (length > 0) {
                System.arraycopy(arguments, 0, args, offset, length);
            }
            return args;
        }

        return arguments;
    }
}
