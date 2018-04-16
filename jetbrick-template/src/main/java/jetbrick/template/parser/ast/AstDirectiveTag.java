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
import jetbrick.template.resolver.ParameterUtils;
import jetbrick.template.resolver.SignatureUtils;
import jetbrick.template.resolver.tag.TagInvoker;
import jetbrick.template.runtime.*;
import jetbrick.util.ArrayUtils;

public final class AstDirectiveTag extends AstDirective {
    private final String name;
    private final AstExpressionList argumentList;
    private final JetTagContext tagContext;
    private TagInvoker last;

    public AstDirectiveTag(String name, AstExpressionList argumentList, AstStatementList statements, Position position) {
        super(position);
        this.name = name;
        this.argumentList = argumentList;
        this.tagContext = new JetTagContext(statements);
    }

    @Override
    public void execute(InterpretContext ctx) throws InterpretException {
        Object[] arguments;
        if (argumentList == null) {
            arguments = ArrayUtils.EMPTY_OBJECT_ARRAY;
        } else {
            arguments = argumentList.execute(ctx);
        }

        doInvoke(ctx, last, arguments);
    }

    private void doInvoke(InterpretContext ctx, TagInvoker invoker, Object[] arguments) throws InterpretException {
        boolean useLatest = (invoker != null);

        if (invoker == null) {
            Class<?>[] argumentTypes = ParameterUtils.getParameterTypes(arguments);
            invoker = ctx.getGlobalResolver().resolveTag(name, argumentTypes);

            if (invoker == null) {
                String signature = SignatureUtils.getFunctionSignature(name, argumentTypes);
                throw new InterpretException(Errors.TAG_NOT_FOUND, signature).set(position);
            }

            this.last = invoker; // 找到一个新的 Invoker
        }

        try {
            invoker.invoke(tagContext, arguments);
        } catch (InterpretException e) {
            throw e;
        } catch (RuntimeException e) {
            if (useLatest && Errors.isReflectIllegalArgument(e)) {
                // 重新查找匹配的 Invoker
                doInvoke(ctx, null, arguments);
            }
            throw new InterpretException(e).set(position);
        }
    }
}
