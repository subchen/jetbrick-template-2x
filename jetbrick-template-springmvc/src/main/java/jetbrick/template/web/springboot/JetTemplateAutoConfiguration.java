/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
 * <p>
 * Author: Guoqiang Chen
 * Email: subchen@gmail.com
 * WebURL: https://github.com/subchen
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.web.springboot;

import jetbrick.template.JetEngine;
import jetbrick.template.web.springmvc.JetTemplateViewResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(JetEngine.class)
@ConditionalOnProperty(value = "spring.jetbrick.template.enabled", havingValue = "true")
@EnableConfigurationProperties(JetTemplateProperties.class)
public class JetTemplateAutoConfiguration {

    private static final Map<String, String> DEFAULT_CONFIGS;

    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("jetx.template.loaders", SpringClasspathResourceLoader.class.getName());        // use SpringClassPathResourceLoader by default
        DEFAULT_CONFIGS = Collections.unmodifiableMap(map);
    }


    @Bean
    @ConditionalOnMissingBean(JetTemplateViewResolver.class)
    public JetTemplateViewResolver jetTemplateViewResolver(JetTemplateProperties properties) {
        mergeConfigs(properties);
        JetTemplateViewResolver resolver = new JetTemplateViewResolver();
        resolver.setPrefix(properties.getPrefix());
        resolver.setSuffix(properties.getSuffix());
        resolver.setCache(properties.isCache());
        resolver.setViewNames(properties.getViewNames());
        resolver.setContentType(properties.getContentType().toString());
        resolver.setOrder(properties.getOrder());
        resolver.setConfigProperties(properties.getConfig());
        resolver.setConfigLocation(properties.getConfigLocation());
        return resolver;
    }

    private void mergeConfigs(JetTemplateProperties properties) {
        if (properties.getConfig() == null) {
            properties.setConfig(new Properties());
        }

        for (String key : DEFAULT_CONFIGS.keySet()) {
            if (properties.getConfig().containsKey(key) == false) {
                String value = DEFAULT_CONFIGS.get(key);
                properties.getConfig().put(key, value);
            }
        }
    }
}
