package jetbrick.template.exec.buildin;

import jetbrick.template.exec.AbstractJetxSourceTest;
import org.junit.Assert;
import org.junit.Test;

public class BuildinFunctionTest extends AbstractJetxSourceTest {

    @Test
    public void testRange() {
        Assert.assertEquals("12345678910", eval("#for(int i:range(1,10))${i}#end"));
        Assert.assertEquals("13579", eval("#for(int i:range(1,10,2))${i}#end"));
    }

    @Test
    public void testDebug() {
        eval("${debug('123')}");
    }

}
