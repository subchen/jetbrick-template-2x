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

import jetbrick.io.stream.UnsafeCharArrayWriter;
import jetbrick.template.JetEngine;
import jetbrick.template.parser.ast.AstStatementList;

public final class JetTagContext {
    private final AstStatementList statements;

    public JetTagContext(AstStatementList statements) {
        this.statements = statements;
    }

    /**
     * 执行 Tag，并获取 Tag 内容的输出
     */
    public String getBodyContent() {
        if (statements == null) {
            return "";
        }

        InterpretContext ctx = getInterpretContext();
        JetWriter originWriter = ctx.getWriter();

        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter(128);
        JetWriter writer = JetWriter.create(out, originWriter.getCharset(), false, false);
        ctx.setWriter(writer);

        statements.execute(ctx);

        ctx.setWriter(originWriter);

        // 获取输出
        return out.toString();
    }

    /**
     * 执行 Tag
     */
    public void invoke() {
        if (statements != null) {
            InterpretContext ctx = getInterpretContext();
            statements.execute(ctx);
        }
    }

    public InterpretContext getInterpretContext() {
        return InterpretContext.current();
    }

    public JetEngine getEngine() {
        return getInterpretContext().getEngine();
    }

    public JetWriter getWriter() {
        return getInterpretContext().getWriter();
    }

    public ValueStack getValueStack() {
        return getInterpretContext().getValueStack();
    }

}
