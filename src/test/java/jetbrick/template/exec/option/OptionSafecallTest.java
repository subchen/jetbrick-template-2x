package jetbrick.template.exec.option;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.runtime.InterpretException;
import org.junit.Test;

public class OptionSafecallTest extends AbstractJetxSourceTest {

    @Test
    public void testOk() {
        eval("#options(safecall=true)${a.toString()}");
    }

    @Test(expected = InterpretException.class)
    public void testFail() {
        eval("#options(safecall=false)${a.toString()}");
    }

}
