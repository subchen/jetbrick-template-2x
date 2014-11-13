package jetbrick.template.exec.directive;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveIncludeTest extends AbstractJetxTest {

    @Test
    public void testInclude() {
        StringBuilder s = new StringBuilder();
        s.append("abc");
        s.append("#include('/sub.jetx')");
        s.append("123");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("xxx");
        engine.set("/sub.jetx", s.toString());

        Assert.assertEquals("abcxxx123", eval());
    }

    @Test
    public void testInclude_args() {
        StringBuilder s = new StringBuilder();
        s.append("#set(c='c')");
        s.append("${a}");
        s.append("#include('/sub.jetx', {b:'b'})");
        s.append("${c}");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("<${a}-${b}-${c}>");
        engine.set("/sub.jetx", s.toString());

        Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put("a", "a");
        Assert.assertEquals("a<a-b-c>c", eval(ctx));
    }

    @Test
    public void testReturn() {
        StringBuilder s = new StringBuilder();
        s.append("${X}");
        s.append("#include('/sub.jetx', 'X')");
        s.append("${X}");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("#return(12345)");
        engine.set("/sub.jetx", s.toString());

        Assert.assertEquals("12345", eval());
    }

}
