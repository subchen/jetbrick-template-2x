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

public final class AstDirectiveBreak extends AstDirective {
    private final AstExpression conditionExpression;

    public AstDirectiveBreak(AstExpression conditionExpression, Position position) {
        super(position);
        this.conditionExpression = conditionExpression;
    }

    @Override
    public void execute(InterpretContext ctx) {
        if (conditionExpression != null) {
            Object value = conditionExpression.execute(ctx);
            if (ALU.isTrue(value)) {
                ctx.setSignal(InterpretContext.SIGNAL_BREAK);
            }
        } else {
            ctx.setSignal(InterpretContext.SIGNAL_BREAK);
        }
    }

}
