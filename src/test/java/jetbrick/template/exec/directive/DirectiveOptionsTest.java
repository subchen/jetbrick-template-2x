package jetbrick.template.exec.directive;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.parser.SyntaxException;
import jetbrick.template.runtime.InterpretException;
import org.junit.Test;

public class DirectiveOptionsTest extends AbstractJetxSourceTest {

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

    @Test()
    public void testStrict_ok() {
        eval("#options(strict=false)${a}");
    }

    @Test(expected = SyntaxException.class)
    public void testStrict_fail() {
        eval("#options(strict=true)${a}");
    }

    @Test
    public void testSafecall_ok() {
        eval("#options(safecall=true)${a.toString()}");
    }

    @Test(expected = InterpretException.class)
    public void testSafecall_fail() {
        eval("#options(safecall=false)${a.toString()}");
    }
}
