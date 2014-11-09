package jetbrick.template.exec.value;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.parser.SyntaxException;
import org.junit.Assert;
import org.junit.Test;

public class IdentifierTest extends AbstractJetxSourceTest {

    @Test
    public void test() {
        Assert.assertEquals("", eval("${a}"));
    }

    @Test(expected = SyntaxException.class)
    public void testInvalid_for_status() {
        eval("${for.index}");
    }

    @Test(expected = SyntaxException.class)
    public void testInvalid_keyword_var() {
        eval("${public}");
    }
}
