package jetbrick.template.exec.directive;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveStopTest extends AbstractJetxTest {

    @Test
    public void testForBreak() {
        Assert.assertEquals("1a2", eval("#for(i:[1,2,3])${i}#break(i>1)a#end"));
    }

    @Test
    public void testForContinue() {
        Assert.assertEquals("1a23", eval("#for(i:[1,2,3])${i}#continue(i>1)a#end"));
    }

    @Test
    public void testForStop() {
        Assert.assertEquals("123", eval("123#stop()abc"));
        Assert.assertEquals("1a2", eval("#for(i:[1,2,3])${i}#stop(i>1)a#end()123"));
    }

}
