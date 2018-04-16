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
package jetbrick.template.web.jodd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.*;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngine;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.result.AbstractTemplateViewResult;

/**
 * 与 jodd madvoc 的集成。 (只支持 Jodd 3.5.1+, 3.5 之前的版本，可以用 jetx 1.2.4 版本)
 *
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public final class JetTemplateResult extends AbstractTemplateViewResult {
    private String contentType;

    public JetTemplateResult() {
        super("jetx");
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    protected String locateTarget(ActionRequest actionRequest, String path) {
        JetEngine engine = JetWebEngine.getEngine();
        if (engine == null) {
            engine = JetWebEngine.create(actionRequest.getHttpServletRequest().getServletContext());
        }

        String suffix = engine.getConfig().getTemplateSuffix();
        if (!path.endsWith(suffix)) {
            path = path + suffix;
        }
        if (engine.checkTemplate(path)) {
            return path;
        }
        return null;
    }

    @Override
    protected void renderView(ActionRequest actionRequest, String path) throws Exception {
        final HttpServletRequest request = actionRequest.getHttpServletRequest();
        final HttpServletResponse response = actionRequest.getHttpServletResponse();

        JetEngine engine = JetWebEngine.getEngine();
        response.setCharacterEncoding(engine.getConfig().getOutputEncoding().name());
        if (contentType != null) {
            response.setContentType(contentType);
        }

        JetTemplate template = engine.getTemplate(path);
        JetWebContext context = new JetWebContext(request, response);
        template.render(context, response.getOutputStream());
    }
}
