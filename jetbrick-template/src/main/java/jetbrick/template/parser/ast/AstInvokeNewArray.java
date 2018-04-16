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

import java.lang.reflect.Array;
import java.util.List;
import jetbrick.template.Errors;
import jetbrick.template.JetSecurityManager;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;

public final class AstInvokeNewArray extends AstExpression {
    private final Class<?> cls;
    private final AstExpression[] expressions;
    private boolean unsafe;

    public AstInvokeNewArray(Class<?> cls, List<AstExpression> expressions, Position position) {
        super(position);
        this.cls = cls;
        this.expressions = expressions.toArray(new AstExpression[0]);
        this.unsafe = true;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        if (unsafe) {
            JetSecurityManager securityManager = ctx.getSecurityManager();
            if (securityManager != null) {
                try {
                    securityManager.checkAccess(cls);
                } catch (RuntimeException e) {
                    throw new InterpretException(e).set(position);
                }
            }
            unsafe = false;
        }

        int length = expressions.length;
        if (length == 1) {
            Object size = expressions[0].execute(ctx);
            validate(ctx, 0, size);
            return Array.newInstance(cls, ((Number) size).intValue());
        } else {
            int[] dimensions = new int[length];
            for (int i = 0; i < length; i++) {
                Object size = expressions[i].execute(ctx);
                validate(ctx, i, size);
                dimensions[i] = ((Number) size).intValue();
            }
            return Array.newInstance(cls, dimensions);
        }
    }

    private void validate(InterpretContext ctx, int index, Object o) {
        if (o == null) {
            throw new InterpretException(Errors.EXPRESSION_ARRAY_LENGTH_NULL).set(expressions[index].getPosition());
        }
        Class<?> cls = o.getClass();
        if (cls == Integer.class || cls == Short.class || cls == Byte.class) {
            return;
        }

        throw new InterpretException(Errors.VARIABLE_TYPE_MISMATCH, index, cls.getName(), "int").set(expressions[index].getPosition());
    }

}
