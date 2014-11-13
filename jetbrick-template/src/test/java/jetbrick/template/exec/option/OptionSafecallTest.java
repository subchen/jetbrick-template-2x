package jetbrick.template.exec.option;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.runtime.InterpretException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class OptionSafecallTest extends AbstractJetxTest {

    @Test
    public void testOk() {
        eval("#options(safecall=true)${a.toString()}");
    }

    @Test
    public void testFail() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.containsString(Errors.EXPRESSION_OBJECT_IS_NULL));

        eval("#options(safecall=false)${a.toString()}");
    }

}
