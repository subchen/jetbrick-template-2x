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

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import jetbrick.collection.TimedSizeCache;
import jetbrick.template.runtime.JetTagContext;
import jetbrick.template.web.JetWebContext;

public final class JetWebTags {

    public static final String CACHE_KEY = TimedSizeCache.class.getName();

    /**
     * Application 级别的 cache.
     *
     * @param ctx Tag 上下文对象
     * @param name cache 名称
     * @param timeout 超时时间，单位秒
     */
    public static void application_cache(JetTagContext ctx, String name, long timeout) throws IOException {
        ServletContext sc = (ServletContext) ctx.getValueStack().getValue(JetWebContext.APPLICATION);
        TimedSizeCache cache = (TimedSizeCache) sc.getAttribute(CACHE_KEY);
        if (cache == null) {
            cache = new TimedSizeCache(128);
            sc.setAttribute(CACHE_KEY, cache);
        }
        Object value = cache.get(name);
        if (value == null) {
            value = ctx.getBodyContent();
            cache.put(name, value, timeout * 1000);
        }
        ctx.getWriter().print(value.toString());
    }

    /**
     * Session 级别的 cache.
     *
     * @param ctx Tag 上下文对象
     * @param name cache 名称
     * @param timeout 超时时间，单位秒
     */
    public static void session_cache(JetTagContext ctx, String name, long timeout) throws IOException {
        HttpSession session = (HttpSession) ctx.getValueStack().getValue(JetWebContext.SESSION);
        TimedSizeCache cache = (TimedSizeCache) session.getAttribute(CACHE_KEY);
        if (cache == null) {
            cache = new TimedSizeCache(128);
            session.setAttribute(CACHE_KEY, cache);
        }
        Object value = cache.get(name);
        if (value == null) {
            value = ctx.getBodyContent();
            cache.put(name, value, timeout * 1000);
        }
        ctx.getWriter().print(value.toString());
    }
}
