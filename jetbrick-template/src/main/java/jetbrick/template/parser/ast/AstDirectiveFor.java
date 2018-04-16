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
import jetbrick.template.parser.Symbols;
import jetbrick.template.runtime.*;

public final class AstDirectiveFor extends AstDirective {
    private final String identifier;
    private final AstExpression expression;
    private final AstStatementList statements;
    private final AstStatementList elseStatements;

    public AstDirectiveFor(String identifier, AstExpression expression, AstStatementList statements, AstStatementList elseStatements, Position position) {
        super(position);
        this.identifier = identifier;
        this.expression = expression;
        this.statements = statements;
        this.elseStatements = elseStatements;
    }

    @Override
    public void execute(InterpretContext ctx) throws InterpretException {
        Object result = expression.execute(ctx);

        JetForIterator it = new JetForIterator(result);
        if (it.getSize() > 0) {
            ValueStack valueStack = ctx.getValueStack();

            // save outside variable which will be reset in #for
            it.setOuter((JetForIterator) valueStack.getValue(Symbols.FOR));
            Object outsideIdentifier = valueStack.getValue(identifier);

            // set this.for
            valueStack.setLocal(Symbols.FOR, it);

            while (it.hasNext()) {
                Object item = it.next();
                try {
                    valueStack.setLocal(identifier, item);
                } catch (IllegalStateException e) {
                    throw new InterpretException(Errors.FOR_ITERATOR_ERROR, it.getIndex()).cause(e).set(expression.getPosition());
                }

                statements.execute(ctx);

                // 处理 break, continue, return, stop 语句
                int signal = ctx.getSignal();
                if (signal != InterpretContext.SIGNAL_NONE) {
                    if (signal == InterpretContext.SIGNAL_BREAK) {
                        ctx.setSignal(InterpretContext.SIGNAL_NONE); // clear signal
                        break;
                    } else if (signal == InterpretContext.SIGNAL_CONTINUE) {
                        ctx.setSignal(InterpretContext.SIGNAL_NONE); // clear signal
                        continue;
                    } else {
                        return;
                    }
                }
            }

            // reset outside variable
            valueStack.setLocal(identifier, outsideIdentifier);
            valueStack.setLocal(Symbols.FOR, it.getOuter());

        } else if (elseStatements != null) {
            elseStatements.execute(ctx);
        }
    }
}
