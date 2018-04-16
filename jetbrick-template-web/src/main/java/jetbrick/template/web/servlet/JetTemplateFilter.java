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
package jetbrick.template.web.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngine;
import jetbrick.web.servlet.RequestUtils;

/**
 * 直接作为 Filter 使用.
 */
public final class JetTemplateFilter implements Filter {
    private JetEngine engine;
    private String charsetEncoding;

    @Override
    public void init(FilterConfig fc) throws ServletException {
        engine = JetWebEngine.create(fc.getServletContext());
        charsetEncoding = engine.getConfig().getOutputEncoding().name();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        response.setCharacterEncoding(charsetEncoding);
        if (response.getContentType() == null) {
            response.setContentType("text/html; charset=" + charsetEncoding);
        }

        String path = RequestUtils.getPathInfo(request);
        try {
            JetTemplate template = engine.getTemplate(path);
            JetWebContext context = new JetWebContext(request, response);
            template.render(context, response.getOutputStream());
        } catch (ResourceNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Template not found: " + path);
        }
    }

    @Override
    public void destroy() {
    }
}
