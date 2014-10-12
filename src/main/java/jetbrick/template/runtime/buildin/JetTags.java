package jetbrick.template.runtime.buildin;

import java.io.IOException;
import jetbrick.template.runtime.JetTagContext;

/**
 * 系统自带的 Tag
 */
public final class JetTags {

    /**
     * 将一个 layout_block 的内容保存到一个 JetContext 变量中。
     *
     * @param ctx Tag 上下文对象
     * @param name 保存到 JetContext 的变量名
     */
    public static void layout_block(JetTagContext ctx, String name) {
        String bodyContent = ctx.getBodyContent();
        ctx.getValueStack().setLocal(name, bodyContent);
    }
    
    /**
     * 如果不存在指定的 JetContext 变量，那么输出 layout_block_default 块内容，否则输出指定的 JetContext 变量。
     *
     * @param ctx Tag 上下文对象
     * @param name JetContext 的变量名
     */
    public static void layout_block_default(JetTagContext ctx, String name) throws IOException {
        Object value = ctx.getValueStack().getValue(name);
        if (value == null) {
            ctx.invoke();
        } else {
            ctx.getWriter().print(value);
        }
    }

}
