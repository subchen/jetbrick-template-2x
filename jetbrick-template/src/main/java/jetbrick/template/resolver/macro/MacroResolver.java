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
package jetbrick.template.resolver.macro;

import java.util.*;
import jetbrick.template.*;
import jetbrick.template.resolver.GlobalResolver;

public final class MacroResolver {
    private final GlobalResolver globalResolver;
    private List<JetTemplate> loadedTemplates;
    private Map<String, JetTemplateMacro> decleardMacros;

    public MacroResolver() {
        this.globalResolver = null;
    }

    public MacroResolver(GlobalResolver globalResolver) {
        this.globalResolver = globalResolver;
    }

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

    public JetTemplateMacro resolve(String name, Class<?>[] argumentTypes) {
        JetTemplateMacro macro;

        if (decleardMacros != null) {
            macro = decleardMacros.get(name);
            if (macro != null) {
                return macro;
            }
        }

        if (loadedTemplates != null) {
            for (JetTemplate template : loadedTemplates) {
                macro = template.resolveMacro(name, argumentTypes);
                if (macro != null) {
                    return macro;
                }
            }
        }

        if (globalResolver != null) {
            return globalResolver.resolveMacro(name, argumentTypes);
        }

        return null;
    }
}
