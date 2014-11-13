package jetbrick.template.exec.option;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class OptionTrimLeadingWhitespacesTest extends AbstractJetxTest {

    @Test()
    public void test() {
        Assert.assertEquals("", eval("#options(trimLeadingWhitespaces=true)"));
        Assert.assertEquals("", eval("#options(trimLeadingWhitespaces=true)\r\n"));
        Assert.assertEquals("abc\n", eval("#options(trimLeadingWhitespaces=true)\r\nabc\n"));
    }

}
