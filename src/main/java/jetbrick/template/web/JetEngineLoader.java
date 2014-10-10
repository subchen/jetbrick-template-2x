/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.template.web;

import java.util.Properties;
import javax.servlet.ServletContext;
import jetbrick.config.ConfigLoader;
import jetbrick.template.*;
import jetbrick.web.servlet.map.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JetEngineLoader {
    private static final Logger log = LoggerFactory.getLogger(JetEngineLoader.class);

    private static final String CONFIG_LOCATION_PARAMETER = "jetbrick-template-config-location";
    private static ServletContext sc;
    private static JetEngine engine;

    public static boolean unavailable() {
        return engine == null;
    }

    public static ServletContext getServletContext() {
        return sc;
    }

    public static JetEngine getEngine() {
        return engine;
    }

    // 允许非 ServletContextListener 方式初始化
    public static void initialize(ServletContext sc) {
        if (JetEngineLoader.engine != null) {
            throw new IllegalStateException("JetEngine has been initialized");
        }

        JetEngineLoader.sc = sc;
        JetEngineLoader.engine = createWebEngine(sc);
    }

    public static void destory() {
        engine = null;
    }

    private static JetEngine createWebEngine(ServletContext sc) {
        // Web 环境下的默认配置
        Properties config = new Properties();
        config.setProperty(JetConfig.IO_SKIPERRORS, "true");
        config.setProperty(JetConfig.TEMPLATE_LOADER, WebServletResourceLoader.class.getName());

        // 用户配置文件
        String configLocation = sc.getInitParameter(CONFIG_LOCATION_PARAMETER);
        if (configLocation != null && configLocation.length() > 0) {
            configLocation = JetConfig.DEFAULT_CONFIG_FILE;
        }

        // create engine
        JetEngine engine = JetEngine.create(config, configLocation);
        JetGlobalContext ctx = engine.getGlobalContext();

        // 加入默认的全局变量
        ctx.define(javax.servlet.ServletContext.class, JetWebContext.SERVLET_CONTEXT);
        ctx.define(javax.servlet.http.HttpSession.class, JetWebContext.SESSION);
        ctx.define(javax.servlet.http.HttpServletRequest.class, JetWebContext.REQUEST);
        ctx.define(javax.servlet.http.HttpServletResponse.class, JetWebContext.RESPONSE);
        ctx.define(java.util.Map.class, JetWebContext.SERVLET_CONTEXT_SCOPE);
        ctx.define(java.util.Map.class, JetWebContext.SESSION_SCOPE);
        ctx.define(java.util.Map.class, JetWebContext.REQUEST_SCOPE);
        ctx.define(java.util.Map.class, JetWebContext.PARAMETER);
        ctx.define(java.util.Map.class, JetWebContext.PARAMETER_VALUES);

        ctx.set(JetWebContext.SERVLET_CONTEXT, sc);
        ctx.set(JetWebContext.SERVLET_CONTEXT_SCOPE, new ServletContextAttributeMap(sc));

        return engine;
    }

}
