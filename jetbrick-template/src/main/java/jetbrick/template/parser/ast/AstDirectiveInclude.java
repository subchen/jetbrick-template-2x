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

import java.util.Collections;
import java.util.Map;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.Errors;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;
import jetbrick.util.PathUtils;

public final class AstDirectiveInclude extends AstDirective {
    private final AstExpression fileExpression;
    private final AstExpression parametersExpression;
    private final String returnName;

    public AstDirectiveInclude(AstExpression fileExpression, AstExpression parametersExpression, String returnName, Position position) {
        super(position);
        this.fileExpression = fileExpression;
        this.parametersExpression = parametersExpression;
        this.returnName = returnName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(InterpretContext ctx) throws InterpretException {
        Object file = fileExpression.execute(ctx);
        if (file == null) {
            throw new InterpretException(Errors.EXPRESSION_NTH_ARGUMENT_IS_NULL, "1st").set(fileExpression.getPosition());
        }
        if (!(file instanceof String)) {
            throw new InterpretException(Errors.VARIABLE_TYPE_MISMATCH, "1st", file.getClass(), "String").set(fileExpression.getPosition());
        }

        Object parameters;
        if (parametersExpression == null) {
            parameters = Collections.emptyMap();
        } else {
            parameters = parametersExpression.execute(ctx);
            if (parameters == null) {
                parameters = Collections.emptyMap();
            } else if (!(parameters instanceof Map)) {
                throw new InterpretException(Errors.VARIABLE_TYPE_MISMATCH, "2nd", parameters.getClass(), "Map<String, Object>").set(parametersExpression.getPosition());
            }
        }

        try {
            String fileName = PathUtils.getRelativePath(ctx.getTemplate().getName(), (String) file);
            ctx.doIncludeCall(fileName, (Map<String, Object>) parameters, returnName);
        } catch (ResourceNotFoundException e) {
            throw new InterpretException(Errors.INCLUDE_FILE_NOT_FOUND, file).set(fileExpression.getPosition());
        }
    }

}
