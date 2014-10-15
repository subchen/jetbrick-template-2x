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
import jetbrick.template.*;
import jetbrick.template.web.buildin.JetWebTags;
import jetbrick.web.servlet.map.ServletContextAttributeMap;

/**
 * 负责初始化 Web 环境下的 JetEngine
 */
public final class JetWebEngine {
    private static final String JET_ENGINE_KEY_NAME = JetEngine.class.getName();
    private static final String CONFIG_LOCATION_PARAMETER = "jetbrick-template-config-location";

    private static ServletContext sc;
    private static JetEngine engine;

    public static JetEngine create(ServletContext sc) {
        return create(sc, null, null);
    }

    public static JetEngine create(ServletContext sc, Properties config, String configLocation) {
        if (engine != null) {
            throw new IllegalStateException("JetEngine has been created");
        }

        JetWebEngine.sc = sc;
        JetWebEngine.engine = doCreateWebEngine(sc, config, configLocation);

        sc.setAttribute(JET_ENGINE_KEY_NAME, engine);
        return engine;
    }

    public static boolean unavailable() {
        return engine == null;
    }

    public static ServletContext getServletContext() {
        return sc;
    }

    public static JetEngine getEngine() {
        return engine;
    }

    private static JetEngine doCreateWebEngine(ServletContext sc, Properties config, String configLocation) {
        // Web 环境下的默认配置
        Properties options = new Properties();
        options.setProperty(JetConfig.IO_SKIPERRORS, "true");
        options.setProperty(JetConfig.TEMPLATE_LOADER, ServletResourceLoader.class.getName());

        if (config != null) {
            options.putAll(config);
        }

        // 用户配置文件
        if (configLocation == null) {
            configLocation = sc.getInitParameter(CONFIG_LOCATION_PARAMETER);
            if (configLocation != null && configLocation.length() > 0) {
                configLocation = JetConfig.DEFAULT_CONFIG_FILE;
            }
        }

        // create engine
        JetEngine engine = JetEngine.create(options, configLocation);
        JetGlobalContext ctx = engine.getGlobalContext();

        // 加入默认的全局变量
        ctx.define(javax.servlet.ServletContext.class, JetWebContext.APPLICATION);
        ctx.define(javax.servlet.http.HttpSession.class, JetWebContext.SESSION);
        ctx.define(javax.servlet.http.HttpServletRequest.class, JetWebContext.REQUEST);
        ctx.define(javax.servlet.http.HttpServletResponse.class, JetWebContext.RESPONSE);
        ctx.define(java.util.Map.class, JetWebContext.APPLICATION_SCOPE);
        ctx.define(java.util.Map.class, JetWebContext.SESSION_SCOPE);
        ctx.define(java.util.Map.class, JetWebContext.REQUEST_SCOPE);
        ctx.define(java.util.Map.class, JetWebContext.PARAMETER);
        ctx.define(java.util.Map.class, JetWebContext.PARAMETER_VALUES);

        ctx.set(JetWebContext.APPLICATION, sc);
        ctx.set(JetWebContext.APPLICATION_SCOPE, new ServletContextAttributeMap(sc));

        // 内置 tag
        engine.getGlobalResolver().registerTags(JetWebTags.class);

        // 返回
        return engine;
    }

}
