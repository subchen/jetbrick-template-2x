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

import java.util.Map;
import jetbrick.template.*;
import jetbrick.template.parser.Source;
import jetbrick.template.resolver.GlobalResolver;

/**
 * 解释引擎执行上下文（AST 执行相关）
 */
public abstract class InterpretContext {
    protected final static ThreadLocal<InterpretContext> threadLocal = new InheritableThreadLocal<InterpretContext>();

    // signal
    public static final int SIGNAL_NONE = 0;
    public static final int SIGNAL_BREAK = 1;
    public static final int SIGNAL_CONTINUE = 2;
    public static final int SIGNAL_RETURN = 3;
    public static final int SIGNAL_STOP = 4;

    public static InterpretContext current() {
        return threadLocal.get();
    }

    public abstract JetEngine getEngine();

    public abstract GlobalResolver getGlobalResolver();

    public abstract JetSecurityManager getSecurityManager();

    public abstract TemplateStack getTemplateStack();

    public abstract ValueStack getValueStack();

    public abstract JetTemplate getTemplate();

    public abstract Source getSource();

    public abstract JetWriter getWriter();

    public abstract void setWriter(JetWriter writer);

    public abstract int getSignal();

    public abstract void setSignal(int signal);

    public abstract String getReturnName();

    public abstract void doIncludeCall(String file, Map<String, Object> arguments, String returnName);

    public abstract void doMacroCall(JetTemplateMacro macro, Object[] arguments);
}
