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
package jetbrick.template.resolver.property;

import jetbrick.bean.FieldInfo;
import jetbrick.template.JetSecurityManager;

/**
 * 访问 object.field
 */
final class FieldGetter implements Getter {
    private final FieldInfo field;

    public FieldGetter(FieldInfo field) {
        this.field = field;
    }

    @Override
    public void checkAccess(JetSecurityManager securityManager) {
        securityManager.checkAccess(field.getField());
    }

    @Override
    public Object get(Object object) {
        return field.get(object);
    }
}
