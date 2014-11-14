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
package jetbrick.template;

import java.util.HashMap;
import java.util.Map;
import jetbrick.io.resource.Resource;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.loader.resource.SourceResource;
import jetbrick.template.resolver.GlobalResolver;

public final class SandboxJetEngine extends JetEngine {
    private final JetEngine engine;
    private final Map<String, String> files = new HashMap<String, String>();
    private JetSecurityManager securityManager;

    public SandboxJetEngine(JetEngine engine) {
        this.engine = engine;
        this.securityManager = null;
    }

    public void setSecurityManager(JetSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void set(String name, String source) {
        files.put(name, source);
    }

    @Override
    public JetConfig getConfig() {
        return engine.getConfig();
    }

    @Override
    public JetGlobalContext getGlobalContext() {
        return engine.getGlobalContext();
    }

    @Override
    public GlobalResolver getGlobalResolver() {
        return engine.getGlobalResolver();
    }

    @Override
    public boolean checkTemplate(String name) {
        return engine.checkTemplate(name);
    }

    @Override
    public JetTemplate getTemplate(String name) throws ResourceNotFoundException {
        String source = files.get(name);
        Resource resource = new SourceResource(name, source);
        JetTemplate template = new JetTemplateImpl(this, resource, false, securityManager);
        template.reload();
        return template;
    }

    @Override
    public JetTemplate createTemplate(String source) {
        return engine.createTemplate(source);
    }

    @Override
    public JetTemplate createTemplate(String name, String source) {
        return engine.createTemplate(name, source);
    }

    @Override
    public Resource getResource(String name) throws ResourceNotFoundException {
        return engine.getResource(name);
    }
}
