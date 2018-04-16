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
package jetbrick.template.web;

import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import jetbrick.config.ConfigLoader;
import jetbrick.template.*;
import jetbrick.template.loader.ServletResourceLoader;
import jetbrick.template.web.buildin.JetWebFunctions;
import jetbrick.template.web.buildin.JetWebTags;
import jetbrick.web.servlet.map.ServletContextAttributeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负责初始化 Web 环境下的 JetEngine
 */
public final class JetWebEngine {
    private static final Logger log = LoggerFactory.getLogger(JetWebEngine.class);

    private static final String JET_ENGINE_KEY_NAME = JetEngine.class.getName();
    private static final String CONFIG_LOCATION_PARAMETER = "jetbrick-template-config-location";

    private static ServletContext servletContext;
    private static JetEngine engine;

    public static JetEngine create(ServletContext sc) {
        return create(sc, null, null);
    }

    public static JetEngine create(ServletContext sc, Properties config, String configLocation) {
        if (engine != null) {
            if (sc.getAttribute(JET_ENGINE_KEY_NAME) == engine) {
                return engine;
            }
            log.warn("webapp reloading: recreating the JetEngine ...");
        }

        servletContext = sc;
        engine = doCreateWebEngine(sc, config, configLocation);

        servletContext.setAttribute(JET_ENGINE_KEY_NAME, engine);
        return engine;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static JetEngine getEngine() {
        return engine;
    }

    private static JetEngine doCreateWebEngine(ServletContext sc, Properties config, String configLocation) {
        ConfigLoader loader = new ConfigLoader();

        // Web 环境下的默认配置
        loader.load(JetConfig.IO_SKIPERRORS, "true");
        loader.load(JetConfig.TEMPLATE_LOADERS, ServletResourceLoader.class.getName());

        if (config != null) {
            loader.load(config);
        }

        // 用户配置文件
        if (configLocation == null) {
            configLocation = sc.getInitParameter(CONFIG_LOCATION_PARAMETER);
            if (configLocation == null || configLocation.length() == 0) {
                configLocation = JetConfig.DEFAULT_CONFIG_FILE;
            }
        }
        try {
            log.info("Loading config file: {}", configLocation);
            loader.load(configLocation, sc);
        } catch (IllegalStateException e) {
            // 默认配置文件允许不存在
            if (!JetConfig.DEFAULT_CONFIG_FILE.equals(configLocation)) {
                throw e;
            }
            log.warn("no default config file found: {}", JetConfig.DEFAULT_CONFIG_FILE);
        }

        // create engine
        JetEngine engine = JetEngine.create(loader.asProperties());
        JetGlobalContext ctx = engine.getGlobalContext();

        // 加入默认的全局变量
        ctx.define(ServletContext.class, JetWebContext.APPLICATION);
        ctx.define(HttpServletRequest.class, JetWebContext.REQUEST);
        ctx.define(HttpServletResponse.class, JetWebContext.RESPONSE);
        ctx.define(Map.class, JetWebContext.APPLICATION_SCOPE);
        ctx.define(Map.class, JetWebContext.REQUEST_SCOPE);
        ctx.define(Map.class, JetWebContext.PARAM);
        ctx.define(Map.class, JetWebContext.PARAM_VALUES);

        if (JetWebContext.SESSION_ENABLED) {
            ctx.define(HttpSession.class, JetWebContext.SESSION);
            ctx.define(Map.class, JetWebContext.SESSION_SCOPE);
        }

        ctx.define(String.class, JetWebContext.CONTEXT_PATH);
        ctx.define(String.class, JetWebContext.WEBROOT_PATH);
        ctx.define(String.class, JetWebContext.WEBROOT);
        ctx.define(String.class, JetWebContext.BASE_PATH);

        ctx.set(JetWebContext.APPLICATION, sc);
        ctx.set(JetWebContext.APPLICATION_SCOPE, new ServletContextAttributeMap(sc));

        // 内置 methods, functions, tags
        engine.getGlobalResolver().registerFunctions(JetWebFunctions.class);
        engine.getGlobalResolver().registerTags(JetWebTags.class);

        // 返回
        return engine;
    }

}
