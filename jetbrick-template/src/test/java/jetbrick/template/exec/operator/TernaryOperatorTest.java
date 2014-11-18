package jetbrick.template.exec.operator;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class TernaryOperatorTest extends AbstractJetxTest {

    @Test
    public void testBasic() {
        Assert.assertEquals("1", eval("${true?1:0}"));
        Assert.assertEquals("0", eval("${false?1:0}"));
        Assert.assertEquals("2", eval("${false?1:true?2:3}"));
        Assert.assertEquals("2", eval("${true?false?1:2:3}"));
    }

    @Test
    public void testSimplify() {
        Assert.assertEquals("1", eval("${null?:1}"));
        Assert.assertEquals("A", eval("${'A'?:2}"));
        Assert.assertEquals("9", eval("${a?:b?:9}"));
    }
}
