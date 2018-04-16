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
import java.util.Map;
import jetbrick.template.runtime.InterpretContext;

public final class AstDirectiveMacro extends AstDirective {
    private final String name;
    private final List<String> argumentNames;
    private final Map<String, Class<?>> symbols;
    private final AstStatementList statements;

    public AstDirectiveMacro(String name, List<String> argumentNames, Map<String, Class<?>> symbols, AstStatementList statements, Position position) {
        super(position);
        this.name = name;
        this.argumentNames = argumentNames;
        this.symbols = symbols;
        this.statements = statements;
    }

    @Override
    public void execute(InterpretContext ctx) {
        statements.execute(ctx);
    }

    public String getName() {
        return name;
    }

    public List<String> getArgumentNames() {
        return argumentNames;
    }

    public Map<String, Class<?>> getSymbols() {
        return symbols;
    }

    public AstStatementList getStatements() {
        return statements;
    }

    @Override
    public Position getPosition() {
        return position;
    }

}
