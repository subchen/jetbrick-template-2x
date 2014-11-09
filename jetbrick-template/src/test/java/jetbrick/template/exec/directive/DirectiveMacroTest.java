package jetbrick.template.exec.directive;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.parser.SyntaxException;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveMacroTest extends AbstractJetxSourceTest {

    @Test
    public void testDefination() {
        StringBuilder sb = new StringBuilder();
        sb.append("#macro size(String s)");
        sb.append("${s}, ${x}");
        sb.append("#end");
        eval(sb.toString());
    }

    @Test(expected = SyntaxException.class)
    public void testRedefination() {
        StringBuilder sb = new StringBuilder();
        sb.append("#macro size()#end");
        sb.append("#macro size(int a)#end");
        eval(sb.toString());
    }

    @Test
    public void testEmbed() {
        StringBuilder sb = new StringBuilder();
        sb.append("#macro size(String s)");
        sb.append("size=${s.length()}");
        sb.append("#end");
        sb.append("${size('abc')}");
        Assert.assertEquals("size=3", eval(sb.toString()));
    }

}
