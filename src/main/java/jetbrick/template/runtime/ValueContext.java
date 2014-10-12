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
package jetbrick.template.runtime;

import java.util.HashMap;
import java.util.Map;

public class ValueContext {
    protected final Map<String, Class<?>> symbols; // 变量定义
    protected final Map<String, Object> privateContext; // 私有变量(不允许被修改), 主要是include/macro 参数
    protected Map<String, Object> localContext; // 本地变量

    public ValueContext(Map<String, Class<?>> symbols, Map<String, Object> privateContext) {
        this.symbols = symbols;
        this.privateContext = privateContext;
        this.localContext = null;
    }

    public Class<?> getType(String name) {
        if (symbols == null) {
            return null;
        }
        return symbols.get(name);
    }

    public Object getLocal(String name) {
        if (localContext == null) {
            return null;
        }
        return localContext.get(name);
    }

    public Object getPrivate(String name) {
        if (privateContext == null) {
            return null;
        }
        return privateContext.get(name);
    }

    public void setLocal(String name, Object value) {
        if (localContext == null) {
            localContext = new HashMap<String, Object>();
        }
        localContext.put(name, value);
    }
}
