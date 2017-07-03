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

import com.jfinal.core.JFinal;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;
import jetbrick.template.JetEngine;
import jetbrick.template.web.JetWebEngine;

public final class JetTemplateRenderFactory extends RenderFactory {
    private final JetEngine engine;

    public JetTemplateRenderFactory() {
        engine = JetWebEngine.create(JFinal.me().getServletContext());
        engine.getGlobalResolver().registerGetterResolver(new JFinalActiveRecordGetterResolver());
    }

    @Override
    public Render getRender(String view) {
        return new JetTemplateRender(view);
    }

    @Override
    public Render getDefaultRender(String view) {
        return getRender(view + engine.getConfig().getTemplateSuffix());
    }

}
