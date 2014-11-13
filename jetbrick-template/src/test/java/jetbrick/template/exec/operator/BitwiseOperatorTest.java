package jetbrick.template.exec.operator;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class BitwiseOperatorTest extends AbstractJetxTest {

    @Test
    public void testBasic() {
        Assert.assertEquals(str(0xF & 0x8), eval("${0xF & 0x8}"));
        Assert.assertEquals(str(0xF | 0x8), eval("${0xF | 0x8}"));
        Assert.assertEquals(str(0xF ^ 0x8), eval("${0xF ^ 0x8}"));
        Assert.assertEquals(str(~0xF), eval("${~0xF}"));
        Assert.assertEquals(str(~1 ^ 2 & 3 | 4), eval("${~1 ^ 2 & 3 | 4}"));
    }

    @Test
    public void testShift() {
        Assert.assertEquals(str(0xFF << 4), eval("${0xFF << 4}"));
        Assert.assertEquals(str(0xFF >> 4), eval("${0xFF >> 4}"));
        Assert.assertEquals(str(0xFF >>> 4), eval("${0xFF >>> 4}"));
    }
}
