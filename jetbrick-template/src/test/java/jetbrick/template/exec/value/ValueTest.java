package jetbrick.template.exec.value;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class ValueTest extends AbstractJetxTest {

    @Test
    public void testValue() {
        Assert.assertEquals("<>", eval("${'<>'}"));
    }

    @Test
    public void testValueEscape() {
        Assert.assertEquals("&lt;&gt;", eval("$!{'<>'}"));
    }
}
