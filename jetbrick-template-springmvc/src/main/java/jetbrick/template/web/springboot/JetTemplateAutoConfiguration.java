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
package jetbrick.template.web.springboot;

import jetbrick.template.JetEngine;
import jetbrick.template.JetConfig;
import jetbrick.template.loader.ClasspathResourceLoader;
import jetbrick.template.web.springmvc.JetTemplateViewResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(JetEngine.class)
@ConditionalOnProperty(value = "spring.jetbrick.template.enabled", havingValue = "true")
@EnableConfigurationProperties(JetTemplateProperties.class)
public class JetTemplateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(JetTemplateViewResolver.class)
    public JetTemplateViewResolver jetTemplateViewResolver(JetTemplateProperties properties) {
        Properties config = properties.getConfig();
        if (config == null) {
            config = new Properties();
        }
        if (!config.containsKey(JetConfig.TEMPLATE_LOADERS)) {
            config.put(JetConfig.TEMPLATE_LOADERS, ClasspathResourceLoader.class.getName());
        }

        JetTemplateViewResolver resolver = new JetTemplateViewResolver();
        resolver.setPrefix(properties.getPrefix());
        resolver.setSuffix(properties.getSuffix());
        resolver.setCache(properties.isCache());
        resolver.setViewNames(properties.getViewNames());
        resolver.setContentType(properties.getContentType().toString());
        resolver.setOrder(properties.getOrder());
        resolver.setConfigProperties(config);
        resolver.setConfigLocation(properties.getConfigLocation());
        return resolver;
    }
}
