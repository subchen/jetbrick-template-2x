package jetbrick.template.exec.directive;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.exec.AbstractJetxFileTest;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveIncludeTest extends AbstractJetxFileTest {

    static {
        StringBuilder s = new StringBuilder();
        s.append("abc");
        s.append("#include('/s2.jetx')");
        s.append("123");
        sourceMap.put("/s1.jetx", s.toString());

        s = new StringBuilder();
        s.append("xxx");
        sourceMap.put("/s2.jetx", s.toString());

        //--------------------------------
        s = new StringBuilder();
        s.append("${a}");
        s.append("#include('/s4.jetx', {b:1})");
        s.append("${a}");
        sourceMap.put("/s3.jetx", s.toString());

        s = new StringBuilder();
        s.append("${a}-${b}");
        sourceMap.put("/s4.jetx", s.toString());
        
        //--------------------------------
        s = new StringBuilder();
        s.append("${X}");
        s.append("#include('/s6.jetx', 'X')");
        s.append("${X}");
        sourceMap.put("/s5.jetx", s.toString());

        s = new StringBuilder();
        s.append("#return(12345)");
        sourceMap.put("/s6.jetx", s.toString());
        
    }

    @Test
    public void testInclude() {
        Assert.assertEquals("abcxxx123", eval("/s1.jetx"));
        Assert.assertEquals("-1", eval("/s3.jetx"));
    }

    @Test
    public void testInclude_args() {
        Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put("a", "a");
        Assert.assertEquals("aa-1a", eval("/s3.jetx", ctx));
    }

    @Test
    public void testReturn() {
        Assert.assertEquals("12345", eval("/s5.jetx"));
    }

}
