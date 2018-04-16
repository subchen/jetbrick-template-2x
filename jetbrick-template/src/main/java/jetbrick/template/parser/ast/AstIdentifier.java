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
import jetbrick.template.runtime.ValueStack;

public final class AstIdentifier extends AstExpression {
    private final String name;

    public AstIdentifier(String name, Position position) {
        super(position);
        this.name = name;
    }

    @Override
    public Object execute(InterpretContext ctx) {
        return ctx.getValueStack().getValue(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public Class<?> getResultType(ValueStack valueStack, Object object) {
        Class<?> type = valueStack.getType(name);
        if (type == null) {
            return object == null ? null : object.getClass();
        }
        return type;
    }
}
