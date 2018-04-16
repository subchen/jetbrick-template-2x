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
package jetbrick.template.runtime.buildin;

import java.io.IOException;
import jetbrick.template.runtime.JetTagContext;

/**
 * 系统自带的 Tag
 */
public final class JetTags {

    /**
     * 将一个 layout_block 的内容保存到一个 Context 变量中.
     *
     * @param ctx Tag 上下文对象
     * @param name 保存到 Context 的变量名
     */
    public static void layout_block(JetTagContext ctx, String name) {
        String bodyContent = ctx.getBodyContent();
        ctx.getValueStack().setLocal(name, bodyContent);
    }

    /**
     * 如果不存在指定的 Context 变量，那么输出 layout_block_default 块内容，否则输出指定的 Context 变量.
     *
     * @param ctx Tag 上下文对象
     * @param name Context 的变量名
     */
    public static void layout_block_default(JetTagContext ctx, String name) throws IOException {
        Object value = ctx.getValueStack().getValue(name);
        if (value == null) {
            ctx.invoke();
        } else {
            ctx.getWriter().print(value.toString());
        }
    }

}
