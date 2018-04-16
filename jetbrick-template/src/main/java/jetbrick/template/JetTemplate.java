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

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.parser.Source;
import jetbrick.template.parser.ast.AstTemplate;

/**
 * 代表一个模板文件.
 */
public interface JetTemplate {

    public void reload() throws ResourceNotFoundException;

    public void render(Map<String, Object> context, Writer out);

    public void render(Map<String, Object> context, OutputStream out);

    public JetTemplateMacro resolveMacro(String name, Class<?>[] argumentTypes, boolean root);

    public String getName();

    public Source getSource();

    public boolean isReloadable();

    public long getLastModified();

    public JetTemplateOption getOption();

    public AstTemplate getAstNode();

    public JetEngine getEngine();

    public JetSecurityManager getSecurityManager();

}
