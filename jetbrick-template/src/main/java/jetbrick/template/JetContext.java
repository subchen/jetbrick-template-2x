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

public final class JetContext extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public JetContext() {
        super();
    }

    public JetContext(int initialCapacity) {
        super(initialCapacity);
    }

    // 获取变量值
    public Object get(String name) {
        return super.get(name);
    }

    // 本地设置一个变量值
    public void set(String name, Object value) {
        super.put(name, value);
    }

}
