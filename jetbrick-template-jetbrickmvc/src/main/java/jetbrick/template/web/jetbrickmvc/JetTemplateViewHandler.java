/**
 * Copyright 2013-2019 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.template.web.jetbrickmvc;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import jetbrick.ioc.annotation.Config;
import jetbrick.ioc.annotation.IocInit;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngine;
import jetbrick.web.mvc.*;
import jetbrick.web.mvc.result.view.AbstractTemplateViewHandler;

@Managed
public final class JetTemplateViewHandler extends AbstractTemplateViewHandler {
    @Config(value = "web.view.jetx.prefix", required = false)
    private String prefix;

    @Config(value = "web.view.jetx.suffix", defaultValue = ".jetx")
    private String suffix;

    private JetEngine engine = null;

    @Override
    public String getType() {
        return "jetx";
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @IocInit
    private void initialize() {
        engine = JetWebEngine.create(WebConfig.getServletContext());
        suffix = engine.getConfig().getTemplateSuffix();
    }

    @Override
    protected void doRender(RequestContext ctx, String viewPathName) throws IOException {
        HttpServletResponse response = ctx.getResponse();
        response.setContentType("text/html; charset=" + response.getCharacterEncoding());

        JetWebContext context = new JetWebContext(ctx.getRequest(), response, ctx.getModel());
        OutputStream out = response.getOutputStream();

        JetTemplate template = engine.getTemplate(viewPathName);
        template.render(context, out);

        out.flush();
    }
}
