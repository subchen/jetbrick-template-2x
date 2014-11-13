package jetbrick.template.exec.option;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.parser.SyntaxException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class OptionStrictTest extends AbstractJetxTest {

    @Test()
    public void testOk() {
        eval("#options(strict=false)${a}");
    }

    @Test
    public void testFail() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.VARIABLE_UNDEFINED)));

        eval("#options(strict=true)${a}");
    }

}
