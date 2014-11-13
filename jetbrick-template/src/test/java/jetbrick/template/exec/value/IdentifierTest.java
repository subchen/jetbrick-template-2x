package jetbrick.template.exec.value;

import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.parser.SyntaxException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class IdentifierTest extends AbstractJetxTest {

    @Test
    public void test() {
        Assert.assertEquals("", eval("${a}"));
    }

    @Test
    public void testInvalid_for_status() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString("cannot be used outside of"));

        eval("${for.index}");
    }

    @Test
    public void testInvalid_keyword_var() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString("It is a reserved/keyword identifier"));

        eval("${public}");
    }
}
