/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import jetbrick.io.resource.Resource;
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
    private final Charset outputEncoding;

    private final boolean reloadable;
    private volatile long lastModified;

    private Source source;
    private AstTemplate astNode;
    private JetTemplateConfig config;

    private final MacroResolver macroResolver;

    public JetTemplateImpl(JetEngine engine, Resource resource) {
        this.engine = engine;
        this.resource = resource;
        this.outputEncoding = engine.getConfig().getOutputEncoding();
        this.reloadable = engine.getConfig().isTemplateReload();
        this.lastModified = 0;
        this.macroResolver = new MacroResolver();
    }

    // 检测模板是否已更新/删除
    @Override
    public void reload() throws TemplateNotFoundException {
        if (reloadable == false && lastModified > 0) {
            return;
        }

        long ts = resource.lastModified();
        if (ts <= 0) {
            throw new TemplateNotFoundException(resource.getPath());
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
        String filename = resource.getPath();
        char[] contents = resource.toCharArray(engine.getConfig().getInputEncoding());
        source = new Source(filename, contents);

        log.info("Loading template: {}", filename);

        // create ctx
        ParserContext ctx = new ParserContext(engine.getGlobalResolver(), engine.getGlobalContext());
        ctx.setStrict(engine.getConfig().isSyntaxStrict());
        ctx.setSafecall(engine.getConfig().isSyntaxSafecall());
        ctx.setTrimLeadingWhitespaces(engine.getConfig().isTrimLeadingWhitespaces());

        // 解析模板，然后生成 AST
        astNode = AstBuilder.create(source, ctx);

        // 重置配置
        config = new JetTemplateConfig(ctx);

        // 重建 MacroResolver
        macroResolver.clear();

        // add declear macros
        List<AstDirectiveMacro> macros = ctx.getDeclaredMacros();
        if (macros != null && macros.size() > 0) {
            for (AstDirectiveMacro macro : macros) {
                macroResolver.register(new JetTemplateMacro(this, macro));
            }
        }
        // add include macros
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
        boolean skipErrors = engine.getConfig().isIoSkiperrors();
        JetWriter writer = JetWriter.create(out, outputEncoding, config.isTrimLeadingWhitespaces(), skipErrors);
        doInterpret(context, writer);
    }

    @Override
    public void render(Map<String, Object> context, OutputStream out) {
        boolean skipErrors = engine.getConfig().isIoSkiperrors();
        JetWriter writer = JetWriter.create(out, outputEncoding, config.isTrimLeadingWhitespaces(), skipErrors);
        doInterpret(context, writer);
    }

    private void doInterpret(Map<String, Object> context, JetWriter writer) {
        InterpretContextImpl ctx = new InterpretContextImpl(engine, writer, context);
        try {
            ctx.getTemplateStack().push(this);
            ctx.getValueStack().push(config.getSymbols(), null);

            astNode.execute(ctx);

            ctx.getValueStack().pop();
            ctx.getTemplateStack().pop();
        } catch (InterpretException e) {
            throw e.set(ctx.getSource());
        } finally {
            ctx.remove();
        }
    }

    @Override
    public JetTemplateMacro resolveMacro(String name, Class<?>[] argumentTypes) {
        return macroResolver.resolve(name, argumentTypes);
    }

    @Override
    public String getName() {
        return resource.getPath();
    }

    @Override
    public Source getSource() {
        return source;
    }

    @Override
    public long getLastModified() {
        return lastModified;
    }

    @Override
    public JetTemplateConfig getConfig() {
        return config;
    }

    @Override
    public AstTemplate getAstNode() {
        return astNode;
    }

    @Override
    public JetEngine getEngine() {
        return engine;
    }

}
