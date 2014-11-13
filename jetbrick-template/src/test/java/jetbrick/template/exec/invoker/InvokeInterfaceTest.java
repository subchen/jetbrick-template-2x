package jetbrick.template.exec.invoker;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.JetTemplate;
import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class InvokeInterfaceTest extends AbstractJetxTest {
    private static Map<String, Object> ctx = new HashMap<String, Object>();

    @Test
    public void test() {
        engine.set(DEFAULT_MAIN_FILE, "${x.intValue()}");
        JetTemplate template = engine.getTemplate(DEFAULT_MAIN_FILE);

        ctx.put("x", new Integer(1));
        Assert.assertEquals("1", eval(template, ctx));

        ctx.put("x", new Long(1));
        Assert.assertEquals("1", eval(template, ctx));

        ctx.put("x", new Byte((byte) 1));
        Assert.assertEquals("1", eval(template, ctx));

        ctx.put("x", new Short((short) 1));
        Assert.assertEquals("1", eval(template, ctx));

        ctx.put("x", new Float(1));
        Assert.assertEquals("1", eval(template, ctx));

        ctx.put("x", new Double(1));
        Assert.assertEquals("1", eval(template, ctx));
    }

}
