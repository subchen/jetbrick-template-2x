package jetbrick.template.exec.option;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.parser.SyntaxException;
import org.junit.Test;

public class OptionSyntaxTest extends AbstractJetxSourceTest {

    @Test
    public void testSyntax() {
        eval("#options(import='java.io.*')");
        eval("#options(strict=true, safecall=false, import='java.io.*', import='java.net.*')");
    }

    @Test(expected = SyntaxException.class)
    public void testInvalidName() {
        eval("#options(unknown=true)");
    }

    @Test(expected = SyntaxException.class)
    public void testInvalidValue() {
        eval("#options(strict=x)");
    }

}
