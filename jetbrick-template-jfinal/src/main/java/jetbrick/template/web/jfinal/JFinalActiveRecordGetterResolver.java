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
package jetbrick.template.web.jfinal;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import jetbrick.template.JetSecurityManager;
import jetbrick.template.resolver.property.Getter;
import jetbrick.template.resolver.property.GetterResolver;

/**
 * 访问 model.name
 */
final class JFinalActiveRecordGetterResolver implements GetterResolver {

    @Override
    public Getter resolve(Class<?> clazz, String name) {
        if (Model.class.isAssignableFrom(clazz)) {
            return new ModelGetter(name);
        }
        if (Record.class.isAssignableFrom(clazz)) {
            return new RecordGetter(name);
        }
        return null;
    }

    /**
     * 访问 model.name
     */
    static final class ModelGetter implements Getter {
        private final String name;

        public ModelGetter(String name) {
            this.name = name;
        }

        @Override
        public void checkAccess(JetSecurityManager securityManager) {
        }

        @Override
        public Object get(Object model) {
            return ((Model<?>) model).get(name);
        }
    }

    /**
     * 访问 record.name
     */
    static final class RecordGetter implements Getter {
        private final String name;

        public RecordGetter(String name) {
            this.name = name;
        }

        @Override
        public void checkAccess(JetSecurityManager securityManager) {
        }

        @Override
        public Object get(Object record) {
            return ((Record) record).get(name);
        }
    }
}
