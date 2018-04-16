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
package jetbrick.template.web.struts;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngine;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import com.opensymphony.xwork2.ActionInvocation;

public class JetTemplateResult extends StrutsResultSupport {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doExecute(String location, ActionInvocation ai) throws Exception {
        Map<String, Object> model = ai.getStack().getContext();
        HttpServletRequest request = (HttpServletRequest) model.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) model.get(ServletActionContext.HTTP_RESPONSE);

        JetEngine engine = JetWebEngine.getEngine();
        if (engine == null) {
            ServletContext servletContext = (ServletContext) model.get(ServletActionContext.SERVLET_CONTEXT);
            engine = JetWebEngine.create(servletContext);
        }

        String charsetEncoding = engine.getConfig().getOutputEncoding().name();
        response.setCharacterEncoding(charsetEncoding);
        if (response.getContentType() == null) {
            response.setContentType("text/html; charset=" + charsetEncoding);
        }

        JetWebContext context = new JetWebContext(request, response, null);
        context.put("action", ai.getAction());
        context.put("valueStack", model);

        JetTemplate template = engine.getTemplate(location);
        template.render(context, response.getOutputStream());
    }
}
