/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.parser.ast;

import jetbrick.template.Errors;
import jetbrick.template.resolver.ParameterUtils;
import jetbrick.template.resolver.SignatureUtils;
import jetbrick.template.resolver.method.MethodInvoker;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;
import jetbrick.util.ArrayUtils;

public final class AstInvokeMethodStatic extends AstExpression {
    private final Class<?> cls;
    private final String name;
    private final AstExpressionList argumentList;
    private MethodInvoker last;

    public AstInvokeMethodStatic(Class<?> cls, String name, AstExpressionList argumentList, Position position) {
        super(position);
        this.cls = cls;
        this.name = name;
        this.argumentList = argumentList;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        Object[] arguments;
        if (argumentList == null) {
            arguments = ArrayUtils.EMPTY_OBJECT_ARRAY;
        } else {
            arguments = argumentList.execute(ctx);
        }

        try {
            try {
                // 尝试匹配最近一次使用的 method
                return doInvoke(ctx, last, arguments);
            } catch (IllegalArgumentException e) {
                if (Errors.isReflectArgumentNotMatch(e)) {
                    // 重新查找匹配的函数
                    return doInvoke(ctx, null, arguments);
                }
                throw e;
            }
        } catch (InterpretException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new InterpretException(Errors.STATIC_METHOD_INVOKE_ERROR, cls.getName(), name).cause(e).set(position);
        }
    }

    private Object doInvoke(InterpretContext ctx, MethodInvoker invoker, Object[] arguments) throws InterpretException {
        if (invoker == null) {
            Class<?>[] argumentTypes = ParameterUtils.getParameterTypes(arguments);
            invoker = ctx.getGlobalResolver().resolveMethod(cls, name, argumentTypes, true);

            if (invoker == null) {
                String signature = SignatureUtils.getMethodSignature(cls, name, argumentTypes);
                throw new InterpretException(Errors.METHOD_STATIC_NOT_FOUND, signature).set(position);
            }

            this.last = invoker; // 找到一个新的 function
        }

        return invoker.invoke(null, arguments);
    }
}
