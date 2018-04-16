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

import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import jetbrick.io.resource.Resource;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.parser.*;
import jetbrick.template.parser.ast.AstDirectiveMacro;
import jetbrick.template.parser.ast.AstTemplate;
import jetbrick.template.resolver.macro.MacroResolver;
import jetbrick.template.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代表一个模板文件.
 */
final class JetTemplateImpl implements JetTemplate {
    private static final Logger log = LoggerFactory.getLogger(JetTemplateImpl.class);

    private final JetEngine engine;
    private final Resource resource;
    private final JetConfig config;
    private final JetSecurityManager securityManager;

    private final boolean reloadable;
    private volatile long lastModified;

    private Source source;
    private AstTemplate astNode;
    private JetTemplateOption option;

    private final MacroResolver macroResolver;

    public JetTemplateImpl(JetEngine engine, Resource resource, boolean reloadable, JetSecurityManager securityManager) {
        this.engine = engine;
        this.resource = resource;
        this.securityManager = securityManager;
        this.config = engine.getConfig();
        this.reloadable = reloadable;
        this.lastModified = 0;
        this.macroResolver = new MacroResolver();
    }

    // 检测模板是否已更新/删除
    @Override
    public void reload() throws ResourceNotFoundException {
        if (reloadable == false && lastModified > 0) {
            return;
        }

        long ts = resource.lastModified();
        if (ts <= 0) {
            throw new ResourceNotFoundException(resource.getRelativePathName());
        }

        if (lastModified != ts) {
            synchronized (this) {
                // double check
                ts = resource.lastModified();
                if (lastModified != ts) {
                    // 重建 AST & Config
                    rebuildAstNodeAndConfig();

                    // 保存最后修改时间
                    lastModified = ts;
                }
            }
        }
    }

    // 重建 AST & Config
    private void rebuildAstNodeAndConfig() {
        // create source
        String filename = resource.getRelativePathName();
        char[] contents = resource.toCharArray(config.getInputEncoding());
        source = new Source(filename, contents);

        log.info("Loading template: {}", filename);

        // create ctx
        ParserContext ctx = new ParserContext(engine.getGlobalResolver(), engine.getGlobalContext());
        ctx.setStrict(config.isSyntaxStrict());
        ctx.setSafecall(config.isSyntaxSafecall());
        ctx.setTrimLeadingWhitespaces(config.isTrimLeadingWhitespaces());
        ctx.setTrimDirectiveWhitespaces(config.isTrimDirectiveWhitespaces());
        ctx.setTrimDirectiveComments(config.isTrimDirectiveComments());
        ctx.setTrimDirectiveCommentsPrefix(config.getTrimDirectiveCommentsPrefix());
        ctx.setTrimDirectiveCommentsSuffix(config.getTrimDirectiveCommentsSuffix());

        // 解析模板，然后生成 AST
        astNode = AstBuilder.create(source, ctx);

        // 重置配置
        option = new JetTemplateOption(ctx);

        // 重建 MacroResolver
        macroResolver.clear();

        // add declared macros
        List<AstDirectiveMacro> macros = ctx.getDeclaredMacros();
        if (macros != null && macros.size() > 0) {
            for (AstDirectiveMacro macro : macros) {
                macroResolver.register(new JetTemplateMacro(this, macro));
            }
        }
        // add included macros
        List<String> files = ctx.getLoadMacroFiles();
        if (files != null && files.size() > 0) {
            for (String file : files) {
                JetTemplate t = engine.getTemplate(file);
                macroResolver.register(t);
            }
        }
    }

    @Override
    public void render(Map<String, Object> context, Writer out) {
        JetWriter writer = JetWriter.create(out, config.getOutputEncoding(), option.isTrimLeadingWhitespaces(), config.isIoSkiperrors());
        doInterpret(context, writer);
    }

    @Override
    public void render(Map<String, Object> context, OutputStream out) {
        JetWriter writer = JetWriter.create(out, config.getOutputEncoding(), option.isTrimLeadingWhitespaces(), config.isIoSkiperrors());
        doInterpret(context, writer);
    }

    private void doInterpret(Map<String, Object> context, JetWriter writer) {
        // 如果在扩展函数，方法，tag 等里面，再次调用 getTemplate().render()，需要保持当前环境，在运行完之后，进行现场恢复
        // see: https://github.com/subchen/jetbrick-template-2x/issues/6
        InterpretContextImpl last = (InterpretContextImpl) InterpretContext.current();
        try {
            InterpretContextImpl ctx = new InterpretContextImpl(engine, writer, context, securityManager);
            try {
                ctx.getTemplateStack().push(this);
                ctx.getValueStack().push(option.getSymbols(), null, true);

                astNode.execute(ctx);

                ctx.getValueStack().pop();
                ctx.getTemplateStack().pop();
            } catch (InterpretException e) {
                throw e.set(ctx.getSource());
            } finally {
                ctx.freeThreadLocal();
            }
        } finally {
            // 进行现场恢复
            if (last != null) {
                last.setThreadLocal();
            }
        }
    }

    @Override
    public JetTemplateMacro resolveMacro(String name, Class<?>[] argumentTypes, boolean root) {
        JetTemplateMacro macro = macroResolver.resolve(name, argumentTypes, reloadable);
        if (macro == null && root) {
            macro = engine.getGlobalResolver().resolveMacro(name, argumentTypes, reloadable);
        }
        return macro;
    }

    @Override
    public String getName() {
        return resource.getRelativePathName();
    }

    @Override
    public Source getSource() {
        return source;
    }

    @Override
    public boolean isReloadable() {
        return reloadable;
    }

    @Override
    public long getLastModified() {
        return lastModified;
    }

    @Override
    public JetTemplateOption getOption() {
        return option;
    }

    @Override
    public AstTemplate getAstNode() {
        return astNode;
    }

    @Override
    public JetEngine getEngine() {
        return engine;
    }

    @Override
    public JetSecurityManager getSecurityManager() {
        return securityManager;
    }

    @Override
    public String toString() {
        return getName();
    }
}
