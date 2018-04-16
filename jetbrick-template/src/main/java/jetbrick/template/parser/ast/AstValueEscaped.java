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

import java.io.IOException;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;
import jetbrick.util.ExceptionUtils;
import jetbrick.util.StringEscapeUtils;

public final class AstValueEscaped extends AstStatement {
    private final AstExpression expression;

    public AstValueEscaped(AstExpression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(InterpretContext ctx) throws InterpretException {
        Object value = expression.execute(ctx);
        if (value != null && value != ALU.VOID) {
            String xml = StringEscapeUtils.escapeXml(value.toString());
            try {
                ctx.getWriter().print(xml);
            } catch (IOException e) {
                throw ExceptionUtils.unchecked(e);
            }
        }
    }
}
