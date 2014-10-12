package jetbrick.template.runtime.buildin;

import java.security.SecureRandom;
import java.util.*;
import jetbrick.collection.iterator.LoopIterator;
import jetbrick.io.IoUtils;
import jetbrick.io.resource.Resource;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.io.stream.UnsafeCharArrayWriter;
import jetbrick.template.TemplateNotFoundException;
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
    public static String include(String relativeName) throws TemplateNotFoundException  {
        return include(relativeName, null);
    }

    // 读取子模板内容
    public static String include(String relativeName, Map<String, Object> parameters) throws TemplateNotFoundException {
        InterpretContext ctx = InterpretContext.current();

        String name = PathUtils.getRelativePath(ctx.getTemplate().getName(), relativeName);

        JetWriter originWriter = ctx.getWriter();
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter(1024);
        JetWriter writer = JetWriter.create(out, originWriter.getCharset(), false, false);

        ctx.setWriter(writer);
        ctx.invokeInclude(name, parameters, null);
        ctx.setWriter(originWriter); // reset

        return out.toString();
    }

    // 读取纯文本内容
    public static String read(String relativeName) throws ResourceNotFoundException {
        InterpretContext ctx = InterpretContext.current();
        String encoding = ctx.getEngine().getConfig().getInputEncoding().name();
        return read(relativeName, encoding);
    }

    // 读取纯文本内容
    public static String read(String relativeName, String encoding) {
        InterpretContext ctx = InterpretContext.current();

        String name = PathUtils.getRelativePath(ctx.getTemplate().getName(), relativeName);
        Resource resource = ctx.getEngine().getResource(name);

        return IoUtils.toString(resource.openStream(), encoding);
    }

    // ------------------------------------------------------------

    public static void debug(String format, Object... args) {
        org.slf4j.LoggerFactory.getLogger("jetx::debug").warn(format, args);
    }

}