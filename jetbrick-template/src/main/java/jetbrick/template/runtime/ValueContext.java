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
package jetbrick.template.runtime;

import java.util.HashMap;
import java.util.Map;

final class ValueContext {
    private final ValueContext inheritedContext; // 如果不为 NULL，则代表当前作用域自动继承父作用域(不允许被修改)
    private final Map<String, Class<?>> symbols; // 变量类型定义
    private final Map<String, Object> privateContext; // 私有变量(不允许被修改), 主要是include/macro 参数
    private Map<String, Object> localContext; // 本地变量

    public ValueContext(ValueContext inheritedContext, Map<String, Class<?>> symbols, Map<String, Object> privateContext) {
        this.inheritedContext = inheritedContext;
        this.symbols = symbols;
        this.privateContext = privateContext;
        this.localContext = null;
    }

    public Class<?> getType(String name, boolean fromInherited) {
        if (symbols != null) {
            Class<?> type = symbols.get(name);
            if (type != null) {
                return type;
            }
        }

        if (fromInherited && inheritedContext != null) {
            return inheritedContext.getType(name, true);
        }

        return null;
    }

    public Object getLocal(String name) {
        if (localContext != null) {
            return localContext.get(name);
        }
        return null;
    }

    public Object getPrivate(String name) {
        Object value;

        if (privateContext != null) {
            value = privateContext.get(name);
            if (value != null) {
                return value;
            }
        }

        // 获取继承过来的变量
        if (inheritedContext != null) {
            value = inheritedContext.getLocal(name);
            if (value != null) {
                return value; // 可能返回 ValueStack.NULL
            }

            value = inheritedContext.getPrivate(name);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    public void setLocal(String name, Object value) {
        if (localContext == null) {
            localContext = new HashMap<String, Object>();
        }

        localContext.put(name, value);
    }
}
