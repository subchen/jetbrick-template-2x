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
import jetbrick.util.tuple.NameValuePair;

public final class AstConstantMapEntry extends AstExpression {
    private final String name;
    private final AstExpression valueExpression;

    public AstConstantMapEntry(String name, AstExpression valueExpression, Position position) {
        super(position);
        this.name = name;
        this.valueExpression = valueExpression;
    }

    @Override
    public NameValuePair<String, Object> execute(InterpretContext ctx) {
        Object value = valueExpression.execute(ctx);
        return new NameValuePair<String, Object>(name, value);
    }
}
