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

import jetbrick.template.JetEngine;
import jetbrick.template.web.JetWebEngine;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.*;

/**
 * nutz与JetTemplate集成.
 * 
 * @since 1.1.0
 * @author wendal(wendal1985@gmail.com)
 */
public final class JetTemplateViewMaker implements ViewMaker {
    private final String suffix;

    public JetTemplateViewMaker() {
        JetEngine engine = JetWebEngine.create(Mvcs.getServletContext());
        suffix = engine.getConfig().getTemplateSuffix().substring(1);
    }

    @Override
    public View make(Ioc ioc, String type, final String value) {
        if (suffix.equals(type)) {
            return new JetTemplateView(value);
        }
        return null;
    }
}
