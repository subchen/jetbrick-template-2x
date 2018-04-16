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

import jetbrick.bean.FieldInfo;
import jetbrick.bean.KlassInfo;
import jetbrick.template.Errors;
import jetbrick.template.JetSecurityManager;
import jetbrick.template.resolver.SignatureUtils;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;

public final class AstInvokeFieldStatic extends AstExpression {
    private final Class<?> cls;
    private final String name;
    private FieldInfo field;
    private boolean unsafe;

    public AstInvokeFieldStatic(Class<?> cls, String name, Position position) {
        super(position);
        this.cls = cls;
        this.name = name;
        this.field = null;
        this.unsafe = true;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        if (field == null) {
            if ("class".equals(name)) {
                return cls;
            }

            field = KlassInfo.create(cls).getDeclaredField(name);
            if (field == null) {
                String signature = SignatureUtils.getFieldSignature(cls, name);
                throw new InterpretException(Errors.STATIC_FIELD_NOT_FOUND, signature).set(position);
            }
        }

        if (unsafe) {
            JetSecurityManager securityManager = ctx.getSecurityManager();
            if (securityManager != null) {
                try {
                    securityManager.checkAccess(field.getField());
                } catch (RuntimeException e) {
                    throw new InterpretException(e).set(position);
                }
            }
            unsafe = false;
        }

        try {
            return field.get(null);
        } catch (Exception e) {
            throw new InterpretException(Errors.STATIC_FIELD_GET_ERROR, cls.getName(), name).cause(e).set(position);
        }
    }
}
