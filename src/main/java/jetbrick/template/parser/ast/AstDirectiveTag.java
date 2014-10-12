/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
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

public final class AstDirectiveTag extends AstStatement {
    private final String name;
    private final AstExpressionList argumentList;
    private final Position position;
    private final JetTagContext tagContext;
    private TagInvoker last;

    public AstDirectiveTag(String name, AstExpressionList argumentList, AstStatementList statements, Position position) {
        this.name = name;
        this.argumentList = argumentList;
        this.position = position;
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

        try {
            try {
                // 尝试匹配最近一次使用的 function
                doInvoke(ctx, last, arguments);
            } catch (IllegalArgumentException e) {
                if (Errors.isReflectArgumentNotMatch(e)) {
                    // 重新查找匹配的函数
                    doInvoke(ctx, null, arguments);
                }
                throw e;
            }
        } catch (InterpretException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new InterpretException(e).set(position);
        }
    }

    private void doInvoke(InterpretContext ctx, TagInvoker invoker, Object[] arguments) throws InterpretException {
        if (invoker == null) {
            Class<?>[] argumentTypes = ParameterUtils.getParameterTypes(arguments);
            invoker = ctx.getGlobalResolver().resolveTag(name, argumentTypes);

            if (invoker == null) {
                String signature = SignatureUtils.getFunctionSignature(name, argumentTypes);
                throw new InterpretException(Errors.TAG_NOT_FOUND, signature).set(position);
            }

            this.last = invoker; // 找到一个新的 function
        }

        invoker.invoke(tagContext, arguments);
    }
}
