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
import java.util.Map;
import jetbrick.template.Errors;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;

public final class AstInvokeIndexGet extends AstExpression {
    private final AstExpression objectExpression;
    private final AstExpression indexExpression;
    private final boolean nullSafe;

    public AstInvokeIndexGet(AstExpression objectExpression, AstExpression indexExpression, boolean nullSafe, Position position) {
        super(position);
        this.objectExpression = objectExpression;
        this.indexExpression = indexExpression;
        this.nullSafe = nullSafe;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        Object object = objectExpression.execute(ctx);
        if (object == null) {
            if (nullSafe || ctx.getTemplate().getOption().isSafecall()) {
                return null;
            }
            throw new InterpretException(Errors.EXPRESSION_OBJECT_IS_NULL).set(objectExpression.getPosition());
        } else if (object == ALU.VOID) {
            throw new InterpretException(Errors.EXPRESSION_OBJECT_IS_VOID).set(objectExpression.getPosition());
        }

        Object index = indexExpression.execute(ctx);
        if (index == null) {
            throw new InterpretException(Errors.EXPRESSION_INDEX_IS_NULL).set(indexExpression.getPosition());
        } else if (index == ALU.VOID) {
            throw new InterpretException(Errors.EXPRESSION_INDEX_IS_VOID).set(indexExpression.getPosition());
        }

        Class<?> objectClass = object.getClass();
        Class<?> indexClass = index.getClass();

        if (objectClass.isArray()) {
            if (indexClass == Integer.class) {
                int i = ((Number) index).intValue();
                if (objectClass.getComponentType().isPrimitive()) {
                    return Array.get(object, i);
                } else {
                    return ((Object[]) object)[i];
                }
            }
        } else if (List.class.isAssignableFrom(objectClass)) {
            if (indexClass == Integer.class) {
                int i = ((Number) index).intValue();
                return ((List<?>) object).get(i);
            }
        } else if (Map.class.isAssignableFrom(objectClass)) {
            if (indexClass == String.class) {
                return ((Map<?, ?>) object).get(index);
            }
        }

        throw new InterpretException(Errors.OPERATION_BINARY_UNDEFINED, "[]", object.getClass(), index.getClass()).set(position);
    }
}
