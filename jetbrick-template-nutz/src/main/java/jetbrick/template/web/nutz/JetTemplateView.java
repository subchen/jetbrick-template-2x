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
package jetbrick.template.web.nutz;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngine;
import org.nutz.lang.Lang;
import org.nutz.mvc.view.AbstractPathView;

/**
 * nutz与JetTemplate集成
 */
public final class JetTemplateView extends AbstractPathView {

    public JetTemplateView(String value) {
        super(value);
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
        JetEngine engine = JetWebEngine.getEngine();

        String charsetEncoding = engine.getConfig().getOutputEncoding().name();
        resp.setCharacterEncoding(charsetEncoding);
        if (resp.getContentType() == null) {
            resp.setContentType("text/html; charset=" + charsetEncoding);
        }

        try {
            JetTemplate template = engine.getTemplate(evalPath(req, obj));
            JetWebContext context = new JetWebContext(req, resp);
            template.render(context, resp.getOutputStream());
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }
}
