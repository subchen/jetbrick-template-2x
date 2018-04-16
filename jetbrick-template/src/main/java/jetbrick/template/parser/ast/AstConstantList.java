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

import java.util.Arrays;
import jetbrick.template.runtime.InterpretContext;

public final class AstConstantList extends AstExpression {
    private final AstExpressionList argumentList;

    public AstConstantList(AstExpressionList argumentList, Position position) {
        super(position);
        this.argumentList = argumentList;
    }

    @Override
    public Object execute(InterpretContext ctx) {
        Object[] objects = argumentList.execute(ctx);
        return Arrays.asList(objects);
    }
}
