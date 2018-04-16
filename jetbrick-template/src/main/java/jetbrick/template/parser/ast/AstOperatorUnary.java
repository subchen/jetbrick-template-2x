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
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;

/**
 *  <h2>Unary Operator</h2>
 *  <p>
 *  See the <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.1">Java 7 Specs</a><br>
 *  <ul>
 *    <li>Each dimension expression in an array creation expression</li>
 *    <li>The index expression in an array access expression </li>
 *    <li>The operand of a unary plus operator + </li>
 *    <li>The operand of a unary minus operator - </li>
 *    <li>The operand of a bitwise complement operator ~ </li>
 *    <li>Each operand, separately, of a shift operator &gt;&gt;, &gt;&gt;&gt;, or &lt;&lt; </li>
 *  </ul>
 */
public final class AstOperatorUnary extends AstExpression {
    private final int operator;
    private final AstExpression expression;

    public AstOperatorUnary(int operator, AstExpression expression, Position position) {
        super(position);
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        Object o = expression.execute(ctx);
        if (o == null) {
            throw new InterpretException(Errors.EXPRESSION_OBJECT_IS_NULL).set(expression.getPosition());
        }

        try {
            Object value;
            switch (operator) {
            case Tokens.PLUS:
                value = ALU.positive(o);
                break;
            case Tokens.MINUS:
                value = ALU.negative(o);
                break;
            case Tokens.BIT_NOT:
                value = ALU.bitNot(o);
                break;
            default:
                throw new UnsupportedOperationException();
            }

            return value;

        } catch (IllegalStateException e) {
            throw new InterpretException(e).set(position);
        }
    }
}
