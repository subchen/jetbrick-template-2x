package jetbrick.template.exec.value;

import jetbrick.template.exec.AbstractJetxSourceTest;
import org.junit.Assert;
import org.junit.Test;

public class ValueTest extends AbstractJetxSourceTest {

    @Test
    public void testValue() {
        Assert.assertEquals("<>", eval("${'<>'}"));
    }

    @Test
    public void testValueEscape() {
        Assert.assertEquals("&lt;&gt;", eval("$!{'<>'}"));
    }
}
