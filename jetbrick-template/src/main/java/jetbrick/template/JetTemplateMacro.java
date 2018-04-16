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

import java.util.List;
import java.util.Map;
import jetbrick.template.parser.ast.AstDirectiveMacro;
import jetbrick.template.resolver.function.FunctionInvoker;
import jetbrick.template.runtime.InterpretContext;

/**
 * 代表一个模板宏定义.
 */
public final class JetTemplateMacro implements FunctionInvoker {
    private final JetTemplate template;
    private final AstDirectiveMacro astNode;

    public JetTemplateMacro(JetTemplate template, AstDirectiveMacro astNode) {
        this.template = template;
        this.astNode = astNode;
    }

    public JetTemplate getTemplate() {
        return template;
    }

    public AstDirectiveMacro getAstNode() {
        return astNode;
    }

    public String getName() {
        return astNode.getName();
    }

    public List<String> getArgumentNames() {
        return astNode.getArgumentNames();
    }

    public Map<String, Class<?>> getSymbols() {
        return astNode.getSymbols();
    }

    @Override
    public void checkAccess(JetSecurityManager securityManager) {
    }

    @Override
    public Object invoke(Object[] arguments) {
        InterpretContext ctx = InterpretContext.current();
        ctx.doMacroCall(this, arguments);
        return null;
    }

    @Override
    public String getSignature() {
        return "macro#" + getName();
    }

}
