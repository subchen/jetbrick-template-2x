package jetbrick.template.exec.operator;

import jetbrick.template.exec.AbstractJetxSourceTest;
import org.junit.Assert;
import org.junit.Test;

public class UnaryOperatorTest extends AbstractJetxSourceTest {

    @Test
    public void testBasic() {
        Assert.assertEquals("2", eval("${+2}"));
        Assert.assertEquals("-2", eval("${-2}"));
        Assert.assertEquals("2.1", eval("${+2.1}"));
        Assert.assertEquals("-2.1", eval("${-2.1}"));
    }

}
