package jetbrick.template.exec.invoker;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.exec.AbstractJetxFileTest;
import org.junit.Assert;
import org.junit.Test;

public class InvokeInterfaceTest extends AbstractJetxFileTest {
    private static Map<String, Object> ctx = new HashMap<String, Object>();

    static {
        sourceMap.put("/1.jetx", "${x.intValue()}");
    }

    @Test
    public void test() {
        ctx.put("x", new Integer(1));
        Assert.assertEquals("1", eval("/1.jetx", ctx));
        ctx.put("x", new Long(1));
        Assert.assertEquals("1", eval("/1.jetx", ctx));
        ctx.put("x", new Byte((byte) 1));
        Assert.assertEquals("1", eval("/1.jetx", ctx));
        ctx.put("x", new Short((short) 1));
        Assert.assertEquals("1", eval("/1.jetx", ctx));
        ctx.put("x", new Float(1));
        Assert.assertEquals("1", eval("/1.jetx", ctx));
        ctx.put("x", new Double(1));
        Assert.assertEquals("1", eval("/1.jetx", ctx));
    }

}
