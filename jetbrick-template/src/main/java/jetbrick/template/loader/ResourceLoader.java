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
package jetbrick.template.loader;

import jetbrick.io.resource.Resource;
import jetbrick.template.JetSecurityManager;

public interface ResourceLoader {

    /**
     * 返回根路径.
     *
     * @return  根路径
     */
    public String getRoot();

    /**
     * 是否支持热加载.
     *
     * @return 是否支持热加载
     */
    public boolean isReloadable();

    /**
     * 获取一个代表模板的 Resource.
     *
     * @param name  模板路径名
     * @return 如果模板不存在，那么返回 {@code null}
     */
    public Resource load(String name);

    /**
     * 获取安全管理器.
     *
     * @return 如果不存在，那么返回 {@code null}
     */
    public JetSecurityManager getSecurityManager();

}
