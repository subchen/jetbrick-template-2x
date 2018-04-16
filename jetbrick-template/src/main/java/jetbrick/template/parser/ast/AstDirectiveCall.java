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
import jetbrick.template.JetTemplate;
import jetbrick.template.JetTemplateMacro;
import jetbrick.template.resolver.ParameterUtils;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;
import jetbrick.util.ArrayUtils;

public final class AstDirectiveCall extends AstDirective {
    private final String name;
    private final AstExpressionList argumentList;
    private JetTemplateMacro last;

    public AstDirectiveCall(String name, AstExpressionList argumentList, Position position) {
        super(position);
        this.name = name;
        this.argumentList = argumentList;
    }

    @Override
    public void execute(InterpretContext ctx) throws InterpretException {
        Object[] arguments;
        if (argumentList == null) {
            arguments = ArrayUtils.EMPTY_OBJECT_ARRAY;
        } else {
            arguments = argumentList.execute(ctx);
        }

        JetTemplateMacro macro = last;
        JetTemplate template = ctx.getTemplate();
        if (macro == null || template.isReloadable()) {
            Class<?>[] argumentTypes = ParameterUtils.getParameterTypes(arguments);
            macro = template.resolveMacro(name, argumentTypes, true);
            if (macro == null) {
                throw new InterpretException(Errors.MACRO_NOT_FOUND, name).set(position);
            }
            this.last = macro;
        }

        ctx.doMacroCall(macro, arguments);
    }

}
