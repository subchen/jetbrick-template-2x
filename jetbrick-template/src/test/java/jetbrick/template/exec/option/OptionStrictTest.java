package jetbrick.template.exec.option;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.parser.SyntaxException;
import org.junit.Test;

public class OptionStrictTest extends AbstractJetxSourceTest {

    @Test()
    public void testOk() {
        eval("#options(strict=false)${a}");
    }

    @Test(expected = SyntaxException.class)
    public void testFail() {
        eval("#options(strict=true)${a}");
    }

}
