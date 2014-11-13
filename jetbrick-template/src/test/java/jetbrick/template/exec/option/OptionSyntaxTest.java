package jetbrick.template.exec.option;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.parser.SyntaxException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class OptionSyntaxTest extends AbstractJetxTest {

    @Test
    public void testSyntax() {
        eval("#options(import='java.io.*')");
        eval("#options(strict=true, safecall=false, import='java.io.*', import='java.net.*')");
    }

    @Test
    public void testInvalidName() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.OPTION_NAME_INVALID)));

        eval("#options(unknown=true)");
    }

    @Test
    public void testInvalidValue() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.OPTION_VALUE_INVALID)));

        eval("#options(strict=123)");
    }

}
