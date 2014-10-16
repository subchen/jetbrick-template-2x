package jetbrick.template.exec.directive;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.parser.SyntaxException;
import org.junit.Test;

public class DirectiveInvalidTest extends AbstractJetxSourceTest {

    @Test(expected = SyntaxException.class)
    public void testInvalid_define() {
        eval("#define");
    }

    @Test(expected = SyntaxException.class)
    public void testInvalid_if() {
        eval("#if");
    }

    @Test(expected = SyntaxException.class)
    public void testInvalid_missing_end() {
        eval("#if(true)123");
    }

    @Test(expected = SyntaxException.class)
    public void testInvalid_break() {
        eval("#break");
    }

    @Test(expected = SyntaxException.class)
    public void testInvalid_continue() {
        eval("#continue");
    }

}
