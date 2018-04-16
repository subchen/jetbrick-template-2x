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

import java.util.*;
import jetbrick.template.Errors;
import jetbrick.template.JetGlobalContext;
import jetbrick.util.ClassUtils;

/**
 * 模板变量执行栈
 */
public final class ValueStack {
    private static final Object NULL = new Object();

    private static final int DEFAULT_CAPACITY = 8;
    private static final int UNUSED_INDEX = 0;

    private final JetGlobalContext globalContext; // 全局变量 (不会被修改)
    private final Map<String, Object> userContext; // 用户变量 (不会被修改)

    private ValueContext[] contexts;
    private int index;
    private ValueContext current;

    public ValueStack(JetGlobalContext globalContext, Map<String, Object> userContext) {
        this.globalContext = globalContext;
        this.userContext = userContext;

        this.contexts = new ValueContext[DEFAULT_CAPACITY];
        this.index = UNUSED_INDEX;
        this.current = null;
    }

    /**
     * 进入一个变量作用域(模板或者宏).
     *
     * @param symbols           define过的所有变量类型
     * @param privateContext    私有变量(include/macro 参数)
     */
    public void push(Map<String, Class<?>> symbols, Map<String, Object> privateContext, boolean inherited) {
        if (++index == contexts.length) {
            contexts = Arrays.copyOf(contexts, index + DEFAULT_CAPACITY);
        }

        ValueContext parent = inherited ? current : null;
        current = contexts[index] = new ValueContext(parent, symbols, privateContext);
    }

    /**
     * 退出一个变量作用域.
     */
    public void pop() {
        if (index == UNUSED_INDEX) {
            throw new EmptyStackException();
        }
        contexts[index--] = null;
        current = (index == UNUSED_INDEX) ? null : contexts[index];
    }

    /**
     * 获取一个模板本地变量（当前模板找不到，向上查找父模板）
     */
    public Object getValue(String name) {
        if (index == UNUSED_INDEX) {
            throw new EmptyStackException();
        }

        // 先找当前作用域
        Object value = current.getLocal(name);
        if (value != null) {
            return value == NULL ? null : value;
        }

        // 查找私有作用域(include/macro 参数，集成过来的父模板参数)
        value = current.getPrivate(name);
        if (value != null) {
            current.setLocal(name, value); // cache
            return value == NULL ? null : value;
        }

        // 查找用户作用域
        if (userContext != null) {
            value = userContext.get(name);
            if (value != null) {
                current.setLocal(name, value); // cache
                return value; // 不会出现 NULL
            }
        }

        // 查找全局作用域
        if (globalContext != null) {
            value = globalContext.getValue(name);
            if (value != null) {
                current.setLocal(name, value); // cache
                return value; // 不会出现 NULL
            }
        }

        return null;
    }

    /**
     * 获取变量类型
     */
    public Class<?> getType(String name) {
        return doGetType(name, current, true);
    }

    private Class<?> doGetType(String name, ValueContext which, boolean fromInherited) {
        if (index == UNUSED_INDEX) {
            throw new EmptyStackException();
        }

        // 先找当前作用域
        Class<?> type = which.getType(name, fromInherited);
        if (type != null) {
            return type;
        }

        // 查找全局作用域
        if (fromInherited && globalContext != null) {
            return globalContext.getType(name);
        }

        return null;
    }

    /**
     * 在当前模板中设置变量
     */
    public void setLocal(String name, Object value) throws IllegalStateException {
        if (index == UNUSED_INDEX) {
            throw new EmptyStackException();
        }

        if (value == null) {
            value = NULL;
        } else {
            // 校验变量的类型是否匹配
            Class<?> type = doGetType(name, current, false);
            if (type != null) {
                if (!ClassUtils.isInstance(type, value)) {
                    throw new IllegalStateException(Errors.format(Errors.VARIABLE_TYPE_INCONSISTENT, name, type.getName(), value.getClass().getName()));
                }
            }
        }

        current.setLocal(name, value);
    }

    /**
     * 在父模板中设置变量
     */
    public void setSuper(String name, Object value) throws IllegalStateException {
        if (index == UNUSED_INDEX) {
            throw new EmptyStackException();
        }
        if (index == 1) {
            throw new IllegalStateException("no super");
        }

        ValueContext parent = contexts[index - 1];

        if (value == null) {
            value = NULL;
        } else {
            // 校验变量的类型是否匹配
            Class<?> type = doGetType(name, parent, false);
            if (type != null) {
                if (!ClassUtils.isInstance(type, value)) {
                    throw new IllegalStateException(Errors.format(Errors.VARIABLE_TYPE_INCONSISTENT, name, type.getName(), value.getClass().getName()));
                }
            }
        }

        parent.setLocal(name, value);
    }
}
