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

import jetbrick.bean.ConstructorInfo;
import jetbrick.bean.KlassInfo;
import jetbrick.template.Errors;
import jetbrick.template.JetSecurityManager;
import jetbrick.template.resolver.SignatureUtils;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;
import jetbrick.util.ArrayUtils;

public final class AstInvokeNewObject extends AstExpression {
    private final Class<?> type;
    private final AstExpressionList argumentList;
    private boolean unsafe;

    public AstInvokeNewObject(Class<?> type, AstExpressionList argumentList, Position position) {
        super(position);
        this.type = type;
        this.argumentList = argumentList;
        this.unsafe = true;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        Object[] arguments;
        if (argumentList == null) {
            arguments = ArrayUtils.EMPTY_OBJECT_ARRAY;
        } else {
            arguments = argumentList.execute(ctx);
        }

        int length = arguments.length;
        Class<?>[] parameterTypes = new Class<?>[length];
        for (int i = 0; i < length; i++) {
            Object arg = arguments[i];
            parameterTypes[i] = (arg == null) ? null : arg.getClass();
        }
        ConstructorInfo constructor = KlassInfo.create(type).searchDeclaredConstructor(parameterTypes);
        if (constructor == null) {
            String signature = SignatureUtils.getMethodSignature(type, "<init>", parameterTypes);
            throw new InterpretException(Errors.CONSTRUCTOR_NOT_FOUND, signature).set(position);
        }

        if (unsafe) {
            JetSecurityManager securityManager = ctx.getSecurityManager();
            if (securityManager != null) {
                try {
                    securityManager.checkAccess(constructor.getConstructor());
                } catch (RuntimeException e) {
                    throw new InterpretException(e).set(position);
                }
            }
            unsafe = false;
        }

        try {
            return constructor.newInstance(arguments);
        } catch (RuntimeException e) {
            throw new InterpretException(Errors.CONSTRUCTOR_INVOKE_ERROR, type.getName()).cause(e).set(position);
        }
    }

}
