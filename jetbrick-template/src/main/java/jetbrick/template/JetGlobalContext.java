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
package jetbrick.template;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局变量
 */
public final class JetGlobalContext {
    private final Map<String, Class<?>> symbols; // 变量定义
    private final Map<String, Object> context; // 本地变量

    public JetGlobalContext() {
        this.symbols = new HashMap<String, Class<?>>();
        this.context = new HashMap<String, Object>();
    }

    public void define(Class<?> type, String name) {
        Class<?> old = symbols.put(name, type);
        if (old != null) {
            throw new IllegalStateException(Errors.format(Errors.VARIABLE_REDEFINE, name));
        }
    }

    public void set(Class<?> type, String name, Object value) {
        define(type, name);
        set(name, value);
    }

    public void set(String name, Object value) {
        Class<?> type = symbols.get(name);
        if (type != null && value != null) {
            if (!type.isInstance(value)) {
                throw new IllegalStateException(Errors.format(Errors.VARIABLE_TYPE_INCONSISTENT, name, type.getName(), value.getClass().getName()));
            }
        }
        context.put(name, value);
    }

    public Class<?> getType(String name) {
        return symbols.get(name);
    }

    public Object getValue(String name) {
        return context.get(name);
    }
}
