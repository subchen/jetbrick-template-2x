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
package jetbrick.template;

import java.util.Properties;
import jetbrick.io.resource.Resource;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.resolver.GlobalResolver;
import jetbrick.util.VersionUtils;

/**
 * 模板引擎
 */
public abstract class JetEngine {

    /**
     * 模板引擎当前版本号
     */
    public static final String VERSION = VersionUtils.getVersion(JetEngine.class);

    /**
     * 使用默认的配置文件(classpath:/jetbrick-template.properties)，创建 #{link JetEngine} 对象.
     *
     * @return 模板引擎对象
     */
    public static JetEngine create() {
        return new JetEngineImpl(new JetConfig(null, JetConfig.DEFAULT_CONFIG_FILE));
    }

    /**
     * 使用用户指定的配置文件，创建 #{link JetEngine} 对象.
     *
     * @param configLocation    配置文件路径
     * @return                  模板引擎对象
     */
    public static JetEngine create(String configLocation) {
        return new JetEngineImpl(new JetConfig(null, configLocation));
    }

    /**
     * 使用用户指定的配置信息，创建 #{link JetEngine} 对象.
     *
     * @param config    配置信息
     * @return          模板引擎对象
     */
    public static JetEngine create(Properties config) {
        return new JetEngineImpl(new JetConfig(config, null));
    }

    /**
     * 使用用户指定的配置文件，创建 #{link JetEngine} 对象.
     *
     * @param defaultConfig     默认的配置信息(优先级低)
     * @param configLocation    配置文件路径(优先级高)
     * @return                  模板引擎对象
     */
    public static JetEngine create(Properties defaultConfig, String configLocation) {
        return new JetEngineImpl(new JetConfig(defaultConfig, configLocation));
    }

    /**
     * 获取全局配置信息.
     *
     * @return 配置信息
     */
    public abstract JetConfig getConfig();

    /**
     * 获取全局变量.
     *
     * @return 变量信息
     */
    public abstract JetGlobalContext getGlobalContext();

    /**
     * 获取全局 Resolver.
     *
     * @return 变量信息
     */
    public abstract GlobalResolver getGlobalResolver();

    /**
     * 判断模板是否存在.
     *
     * @param name  模板名称
     * @return      模板是否存在
     */
    public abstract boolean checkTemplate(String name);

    /**
     * 获取模板对象.
     *
     * @param name  模板名称
     * @return      模板对象
     *
     * @throws ResourceNotFoundException 如果模板不存在，抛出该异常
     */
    public abstract JetTemplate getTemplate(String name) throws ResourceNotFoundException;

    /**
     * 以模板源代码方式，创建一个模板(无缓存).
     *
     * @param source 模板源代码
     * @return       模板对象
     */
    public abstract JetTemplate createTemplate(String source);

    /**
     * 以模板源代码方式，创建一个模板(无缓存).
     *
     * @param name   模板名称，用于标识
     * @param source 模板源代码
     * @return       模板对象
     */
    public abstract JetTemplate createTemplate(String name, String source);

    /**
     * 获取资源.
     *
     * @param name  资源名称
     * @return      资源对象
     *
     * @throws ResourceNotFoundException 如果资源不存在，抛出该异常
     */
    public abstract Resource getResource(String name) throws ResourceNotFoundException;

}
