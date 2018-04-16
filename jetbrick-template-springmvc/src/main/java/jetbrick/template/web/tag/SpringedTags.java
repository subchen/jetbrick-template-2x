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
package jetbrick.template.web.tag;

import jetbrick.template.JetAnnotations;
import jetbrick.template.runtime.JetTagContext;
import jetbrick.template.web.springboot.EnvHolder;
import java.io.IOException;

@JetAnnotations.Tags
public class SpringedTags {

    @JetAnnotations.Name("actived_profile")
    public static void activedProfile(JetTagContext ctx, String profile) throws IOException {
        if (EnvHolder.getEnvironment().acceptsProfiles(profile)) {
            String content = ctx.getBodyContent();
            ctx.getWriter().print(content);
        }
    }

    @JetAnnotations.Name("actived_profile")
    public static void activedProfile(JetTagContext ctx, String... profiles) throws IOException {
        boolean ok = true;

        // Environment#acceptsProfiles(String...) 是或关系
        // 本方法是与关系
        for (String profile : profiles) {
            if (!EnvHolder.getEnvironment().acceptsProfiles(profile)) {
                ok = false;
                break;
            }
        }

        if (ok) {
            String content = ctx.getBodyContent();
            ctx.getWriter().print(content);
        }
    }
}
