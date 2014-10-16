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
package jetbrick.template.web.jfinal;

import com.jfinal.plugin.activerecord.Model;
import jetbrick.bean.Getter;
import jetbrick.template.resolver.property.GetterResolver;

/**
 * 访问 model.name
 */
final class JFinalModelGetterResolver implements GetterResolver {

    @Override
    public Getter resolve(Class<?> clazz, String name) {
        if (Model.class.isAssignableFrom(clazz)) {
            return new JFinalModelGetter(name);
        }
        return null;
    }

    /**
     * 访问 model.name
     */
    static final class JFinalModelGetter implements Getter {
        private final String name;

        public JFinalModelGetter(String name) {
            this.name = name;
        }

        @Override
        public Object get(Object model) {
            return ((Model<?>) model).get(name);
        }
    }

}
