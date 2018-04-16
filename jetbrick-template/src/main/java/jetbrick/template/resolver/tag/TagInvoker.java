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

import jetbrick.bean.MethodInfo;
import jetbrick.template.resolver.ParameterUtils;
import jetbrick.template.runtime.JetTagContext;

public final class TagInvoker {
    private final MethodInfo method;
    private final int length;
    private final Class<?> varArgsClass;

    public TagInvoker(MethodInfo method) {
        this.method = method;
        this.length = method.getParameterCount();

        if (method.isVarArgs()) {
            Class<?>[] types = method.getParameterTypes();
            this.varArgsClass = types[types.length - 1].getComponentType();
        } else {
            this.varArgsClass = null;
        }
    }

    public void invoke(JetTagContext ctx, Object[] arguments) {
        arguments = ParameterUtils.getActualArguments(arguments, length, varArgsClass, 1);
        arguments[0] = ctx; // 第一个参数

        method.invoke(null, arguments);
    }
}
