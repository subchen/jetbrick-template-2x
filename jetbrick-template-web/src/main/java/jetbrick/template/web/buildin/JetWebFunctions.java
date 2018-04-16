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
package jetbrick.template.web.buildin;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.ValueStack;
import jetbrick.template.web.JetWebContext;
import jetbrick.web.servlet.BufferedHttpServletResponse;

public final class JetWebFunctions {

    /**
     * 直接 include 一个 servlet 页面 (内部 forward).
     * 
     * @param url  相对于 request 的 url
     * @return 获取 url reponse 输出
     */
    public static String servletGet(String url) throws Exception {
        InterpretContext ctx = InterpretContext.current();
        ValueStack stack = ctx.getValueStack();

        ServletContext sc = (ServletContext) stack.getValue(JetWebContext.APPLICATION);
        HttpServletRequest request = (HttpServletRequest) stack.getValue(JetWebContext.REQUEST);
        HttpServletResponse response = (HttpServletResponse) stack.getValue(JetWebContext.RESPONSE);

        BufferedHttpServletResponse resp = new BufferedHttpServletResponse(response);
        sc.getRequestDispatcher(url).include(request, resp);
        return resp.toString();
    }

}
