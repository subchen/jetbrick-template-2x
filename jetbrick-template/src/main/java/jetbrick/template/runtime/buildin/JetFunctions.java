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

import java.security.SecureRandom;
import java.util.*;
import jetbrick.collection.iterator.LoopIterator;
import jetbrick.io.IoUtils;
import jetbrick.io.resource.Resource;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.io.stream.UnsafeCharArrayWriter;
import jetbrick.template.Errors;
import jetbrick.template.JetTemplateMacro;
import jetbrick.template.resolver.ParameterUtils;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.JetWriter;
import jetbrick.util.PathUtils;

public final class JetFunctions {
    private static final SecureRandom RANDOM = new SecureRandom();

    // ------------------------------------------------------------

    public static Date now() {
        return new Date();
    }

    public static int random() {
        return RANDOM.nextInt();
    }

    public static UUID uuid() {
        return UUID.randomUUID();
    }

    // ------------------------------------------------------------

    public static Iterator<Integer> range(int start, int stop) {
        return new LoopIterator(start, stop);
    }

    public static Iterator<Integer> range(int start, int stop, int step) {
        return new LoopIterator(start, stop, step);
    }

    // ------------------------------------------------------------

    // 读取子模板内容
    public static String includeGet(String relativeName) throws ResourceNotFoundException {
        return includeGet(relativeName, null);
    }

    // 读取子模板内容
    public static String includeGet(String relativeName, Map<String, Object> parameters) throws ResourceNotFoundException {
        InterpretContext ctx = InterpretContext.current();

        String name = PathUtils.getRelativePath(ctx.getTemplate().getName(), relativeName);

        JetWriter originWriter = ctx.getWriter();
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter(1024);
        JetWriter writer = JetWriter.create(out, originWriter.getCharset(), false, false);

        ctx.setWriter(writer);
        ctx.doIncludeCall(name, parameters, null);
        ctx.setWriter(originWriter); // reset

        return out.toString();
    }

    // 读取纯文本内容
    public static String fileGet(String relativeName) throws ResourceNotFoundException {
        InterpretContext ctx = InterpretContext.current();
        String encoding = ctx.getEngine().getConfig().getInputEncoding().name();
        return fileGet(relativeName, encoding);
    }

    // 读取纯文本内容
    public static String fileGet(String relativeName, String encoding) {
        InterpretContext ctx = InterpretContext.current();

        String name = PathUtils.getRelativePath(ctx.getTemplate().getName(), relativeName);
        Resource resource = ctx.getEngine().getResource(name);

        return IoUtils.toString(resource.openStream(), encoding);
    }

    // ------------------------------------------------------------

    // 调用一个 macro，并获取生成的内容
    public static String macroGet(String name, Object... arguments) {
        InterpretContext ctx = InterpretContext.current();

        Class<?>[] argumentTypes = ParameterUtils.getParameterTypes(arguments);
        JetTemplateMacro macro = ctx.getTemplate().resolveMacro(name, argumentTypes, true);
        if (macro == null) {
            throw new IllegalStateException(Errors.format(Errors.MACRO_NOT_FOUND, name));
        }

        JetWriter originWriter = ctx.getWriter();

        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter(128);
        ctx.setWriter(JetWriter.create(out, originWriter.getCharset(), false, false));

        ctx.doMacroCall(macro, arguments);

        ctx.setWriter(originWriter);

        return out.toString();
    }

    // ------------------------------------------------------------

    public static void debug(String format, Object... args) {
        org.slf4j.LoggerFactory.getLogger("jetx.debug").info(format, args);
    }

}
