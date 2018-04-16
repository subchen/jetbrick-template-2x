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

import java.util.List;
import jetbrick.template.Errors;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;

public final class AstExpressionList extends AstExpression {
    private static final AstExpression[] EMPTY_ARRAY = new AstExpression[0];
    private final AstExpression[] expressions;

    public AstExpressionList(List<AstExpression> expressions, Position position) {
        super(position);
        this.expressions = expressions.toArray(EMPTY_ARRAY);
    }

    @Override
    public Object[] execute(InterpretContext ctx) {
        int length = expressions.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++) {
            Object obj = expressions[i].execute(ctx);
            if (obj == ALU.VOID) {
                throw new InterpretException(Errors.EXPRESSION_ARGUMENT_IS_VOID).set(expressions[i].getPosition());
            }
            objects[i] = obj;
        }
        return objects;
    }
}
