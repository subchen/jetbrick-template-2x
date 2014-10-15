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

import jetbrick.bean.Getter;
import jetbrick.template.Errors;
import jetbrick.template.resolver.SignatureUtils;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;

public final class AstInvokeField extends AstExpression {
    private final AstExpression objectExpression;
    private final String name;
    private Getter last;

    public AstInvokeField(AstExpression objectExpression, String name, Position position) {
        super(position);
        this.objectExpression = objectExpression;
        this.name = name;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        Object object = objectExpression.execute(ctx);
        if (object == null) {
            if (ctx.getTemplate().getOption().isSafecall()) {
                return null;
            }
            throw new InterpretException(Errors.OBJECT_IS_NULL).set(position);
        }

        return doInvokeGetter(ctx, last, object);
    }

    private Object doInvokeGetter(InterpretContext ctx, Getter getter, Object object) throws InterpretException {
        boolean useLatest = (getter != null);

        if (getter == null) {
            Class<?> objectClass = objectExpression.getResultType(ctx.getValueStack(), object);
            getter = ctx.getGlobalResolver().resolveGetter(objectClass, name);

            if (getter == null) {
                String signature = SignatureUtils.getFieldSignature(objectClass, name);
                throw new InterpretException(Errors.PROPERTY_NOT_FOUND, signature).set(position);
            }

            this.last = getter; // 找到一个新的 getter
        }

        try {
            return getter.get(object);
        } catch (IllegalArgumentException e) {
            if (useLatest && Errors.isReflectArgumentNotMatch(e)) {
                // 重新查找匹配的 Getter
                doInvokeGetter(ctx, null, object);
            }
            throw new InterpretException(Errors.PROPERTY_GET_ERROR).cause(e).set(position);
        }
    }
}
