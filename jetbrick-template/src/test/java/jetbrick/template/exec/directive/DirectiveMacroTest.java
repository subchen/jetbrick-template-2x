package jetbrick.template.exec.directive;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.parser.SyntaxException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveMacroTest extends AbstractJetxTest {

    @Test
    public void testDefination() {
        StringBuilder sb = new StringBuilder();
        sb.append("#macro size(String s)");
        sb.append("${s}, ${x}");
        sb.append("#end");
        eval(sb.toString());
    }

    @Test
    public void testRedefination() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.DIRECTIVE_MACRO_NAME_DUPLICATED)));

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
        sb.append("#call size('abc')");
        Assert.assertEquals("size=3", eval(sb.toString()));
    }

}
