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
package jetbrick.template.web.springmvc;

import jetbrick.template.JetEngine;
import jetbrick.template.web.JetWebEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import java.util.Locale;
import java.util.Properties;

public final class JetTemplateViewResolver extends AbstractTemplateViewResolver implements InitializingBean {
    private String configLocation;
    private Properties configProperties;
    private JetEngine jetEngine;

    public JetTemplateViewResolver() {
        setViewClass(requiredViewClass());
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public void setConfigProperties(Properties configProperties) {
        this.configProperties = configProperties;
    }

    public JetEngine getJetEngine() {
        return jetEngine;
    }

    @Override
    protected Class<?> requiredViewClass() {
        return JetTemplateView.class;
    }

    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        View view = super.loadView(viewName, locale);
        if (view instanceof JetTemplateView) {
            ((JetTemplateView) view).setAllowRequestOverride(false);
            ((JetTemplateView) view).setAllowSessionOverride(false);
        }
        return view;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JetEngine engine = JetWebEngine.create(getServletContext(), configProperties, configLocation);
        if (getSuffix() == null || getSuffix().length() == 0) {
            setSuffix(engine.getConfig().getTemplateSuffix());
        }
        this.jetEngine = engine;
    }
}
