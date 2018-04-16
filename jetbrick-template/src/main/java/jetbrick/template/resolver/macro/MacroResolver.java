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
package jetbrick.template.resolver.macro;

import java.util.*;
import jetbrick.template.*;

public final class MacroResolver {
    private List<JetTemplate> loadedTemplates;
    private Map<String, JetTemplateMacro> decleardMacros;
    private long lastCheckTimestamp = 0;

    public void clear() {
        loadedTemplates = null;
        decleardMacros = null;
    }

    public void register(JetTemplate template) {
        if (loadedTemplates == null) {
            loadedTemplates = new ArrayList<JetTemplate>(4);
        }
        loadedTemplates.add(template);
    }

    public void register(JetTemplateMacro macro) {
        if (decleardMacros == null) {
            decleardMacros = new HashMap<String, JetTemplateMacro>();
        }
        JetTemplateMacro old = decleardMacros.put(macro.getName(), macro);
        if (old != null) {
            throw new IllegalStateException(Errors.format(Errors.DIRECTIVE_MACRO_NAME_DUPLICATED, macro.getName()));
        }
    }

    public JetTemplateMacro resolve(String name, Class<?>[] argumentTypes, boolean reloadable) {
        JetTemplateMacro macro;

        if (decleardMacros != null) {
            macro = decleardMacros.get(name);
            if (macro != null) {
                return macro;
            }
        }

        if (loadedTemplates != null) {
            if (reloadable) {
                checkImportedMacrosReload();
            }
            for (JetTemplate template : loadedTemplates) {
                macro = template.resolveMacro(name, argumentTypes, false);
                if (macro != null) {
                    return macro;
                }
            }
        }

        return null;
    }

    // 如果外部 macro 文件被修改，尝试 reload
    private void checkImportedMacrosReload() {
        // 每 5 sec 检测一次
        long now = System.currentTimeMillis();
        if (now - lastCheckTimestamp > 5 * 1000) {
            for (JetTemplate template : loadedTemplates) {
                template.reload();
            }
            lastCheckTimestamp = now;
        }
    }
}
