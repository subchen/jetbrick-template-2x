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

import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;

public final class AstOperatorEquals extends AstExpression {
    private final int operator;
    private final AstExpression lhs;
    private final AstExpression rhs;

    public AstOperatorEquals(int operator, AstExpression lhs, AstExpression rhs, Position position) {
        super(position);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        Object o1 = lhs.execute(ctx);

        Object value;
        switch (operator) {
        case Tokens.IDENTICALLY_EQUAL:
            value = (o1 == rhs.execute(ctx));
            break;
        case Tokens.IDENTICALLY_EQUAL_NOT:
            value = (o1 != rhs.execute(ctx));
            break;
        case Tokens.EQ:
            value = ALU.equals(o1, rhs.execute(ctx));
            break;
        case Tokens.NE:
            value = ALU.equals(o1, rhs.execute(ctx)).booleanValue() ? Boolean.FALSE : Boolean.TRUE;
            break;
        case Tokens.AND:
            value = ALU.isTrue(o1) && ALU.isTrue(rhs.execute(ctx)) ? Boolean.TRUE : Boolean.FALSE;
            break;
        case Tokens.OR:
            value = ALU.isTrue(o1) || ALU.isTrue(rhs.execute(ctx)) ? Boolean.TRUE : Boolean.FALSE;
            break;
        case Tokens.NOT:
            value = ALU.isTrue(o1) ? Boolean.FALSE : Boolean.TRUE;
            break;
        default:
            throw new UnsupportedOperationException();
        }

        return value;
    }
}
