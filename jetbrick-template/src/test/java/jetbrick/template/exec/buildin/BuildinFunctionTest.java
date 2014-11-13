package jetbrick.template.exec.buildin;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class BuildinFunctionTest extends AbstractJetxTest {

    @Test
    public void testRange() {
        Assert.assertEquals("12345678910", eval("#for(int i:range(1,10))${i}#end"));
        Assert.assertEquals("13579", eval("#for(int i:range(1,10,2))${i}#end"));
    }

    @Test
    public void testMacroGet() {
        Assert.assertEquals("2", eval("#macro inc(int x)${x+1}#end${macroGet('inc', 1)}"));
    }

    @Test
    public void testDebug() {
        eval("${debug('123')}");
    }

}
