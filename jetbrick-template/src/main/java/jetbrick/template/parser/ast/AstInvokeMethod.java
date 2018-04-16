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
package jetbrick.template.parser.ast;

import jetbrick.template.Errors;
import jetbrick.template.JetSecurityManager;
import jetbrick.template.resolver.ParameterUtils;
import jetbrick.template.resolver.SignatureUtils;
import jetbrick.template.resolver.method.MethodInvoker;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;
import jetbrick.util.ArrayUtils;

public final class AstInvokeMethod extends AstExpression {
    private final AstExpression objectExpression;
    private final String name;
    private final AstExpressionList argumentList;
    private final boolean nullSafe;
    private MethodInvoker last;
    private boolean unsafe;

    public AstInvokeMethod(AstExpression objectExpression, String name, AstExpressionList argumentList, boolean nullSafe, Position position) {
        super(position);
        this.objectExpression = objectExpression;
        this.name = name;
        this.argumentList = argumentList;
        this.nullSafe = nullSafe;
        this.last = null;
        this.unsafe = true;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        Object object = objectExpression.execute(ctx);
        if (object == null) {
            if (nullSafe || ctx.getTemplate().getOption().isSafecall()) {
                return null;
            }
            throw new InterpretException(Errors.EXPRESSION_OBJECT_IS_NULL).set(position);
        } else if (object == ALU.VOID) {
            throw new InterpretException(Errors.EXPRESSION_OBJECT_IS_VOID).set(position);
        }

        Object[] arguments;
        if (argumentList == null) {
            arguments = ArrayUtils.EMPTY_OBJECT_ARRAY;
        } else {
            arguments = argumentList.execute(ctx);
        }

        return doInvoke(ctx, last, object, arguments);
    }

    private Object doInvoke(InterpretContext ctx, MethodInvoker invoker, Object object, Object[] arguments) throws InterpretException {
        boolean useLatest = (invoker != null);

        if (invoker == null) {
            Class<?> objectClass = objectExpression.getResultType(ctx.getValueStack(), object);
            Class<?>[] argumentTypes = ParameterUtils.getParameterTypes(arguments);
            invoker = ctx.getGlobalResolver().resolveMethod(objectClass, name, argumentTypes, false);

            if (invoker == null) {
                String signature = SignatureUtils.getMethodSignature(objectClass, name, argumentTypes);
                throw new InterpretException(Errors.METHOD_NOT_FOUND, signature).set(position);
            }

            this.last = invoker; // 找到一个新的  invoker
        }

        if (unsafe) {
            JetSecurityManager securityManager = ctx.getSecurityManager();
            if (securityManager != null) {
                try {
                    invoker.checkAccess(securityManager);
                } catch (RuntimeException e) {
                    throw new InterpretException(e).set(position);
                }
            }
            unsafe = false;
        }

        try {
            Object result = invoker.invoke(object, arguments);
            if (result != null) {
                return result;
            } else {
                return (invoker.isVoidResult()) ? ALU.VOID : null;
            }
        } catch (InterpretException e) {
            throw e;
        } catch (RuntimeException e) {
            if (useLatest && Errors.isReflectIllegalArgument(e)) {
                // 重新查找匹配的 Invoker
                return doInvoke(ctx, null, object, arguments);
            }

            throw new InterpretException(Errors.METHOD_INVOKE_ERROR, invoker.getSignature()).cause(e).set(position);
        }
    }
}
