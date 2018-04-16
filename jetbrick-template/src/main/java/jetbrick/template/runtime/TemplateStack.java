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

import java.util.Arrays;
import java.util.EmptyStackException;
import jetbrick.template.JetTemplate;

/**
 * 模板对象执行栈.
 */
public final class TemplateStack {
    private static final int DEFAULT_CAPACITY = 8;
    private static final int UNUSED_INDEX = 0;

    private JetTemplate[] templates;
    private int index;
    private JetTemplate current;

    public TemplateStack() {
        this.templates = new JetTemplate[DEFAULT_CAPACITY];
        this.index = UNUSED_INDEX;
        this.current = null;
    }

    /**
     * 进入一个子模板.
     */
    public void push(JetTemplate template) {
        if (++index == templates.length) {
            templates = Arrays.copyOf(templates, index + DEFAULT_CAPACITY);
        }
        current = templates[index] = template;
    }

    /**
     * 退出一个子模板.
     */
    public void pop() {
        if (index == UNUSED_INDEX) {
            throw new EmptyStackException();
        }
        templates[index--] = null;
        current = (index == UNUSED_INDEX) ? null : templates[index];
    }

    /**
     * 返回当前模板
     */
    public JetTemplate current() {
        return current;
    }
}
