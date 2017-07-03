/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.template.web.jfinal;

import java.io.IOException;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngine;
import com.jfinal.render.Render;

public final class JetTemplateRender extends Render {
    private static final long serialVersionUID = 1L;

    public JetTemplateRender(String view) {
        this.view = view;
    }

    @Override
    public void render() {
        JetEngine engine = JetWebEngine.getEngine();

        String charsetEncoding = engine.getConfig().getOutputEncoding().name();
        response.setCharacterEncoding(charsetEncoding);
        if (response.getContentType() == null) {
            response.setContentType("text/html; charset=" + charsetEncoding);
        }

        try {
            JetTemplate template = engine.getTemplate(view);
            JetWebContext context = new JetWebContext(request, response);
            template.render(context, response.getOutputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
