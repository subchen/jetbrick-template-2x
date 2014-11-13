package jetbrick.template.exec.invoker;

import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class InvokeMethodExtensionTest extends AbstractJetxTest {

    @Override
    public void initializeEngine() {
        engine.getGlobalResolver().registerMethods(StringUtils.class);
        engine.getGlobalResolver().registerMethods(String.class);
    }

    @Test
    public void test() {
        Assert.assertEquals("123", eval("${' 123 '.trim()}"));
        Assert.assertEquals("000", eval("${'0'.repeat(3)}"));
        Assert.assertEquals("?,?,?", eval("${'?'.repeat(',', 3)}"));
    }

    @Test
    public void testVarargs() {
        Assert.assertEquals("", eval("${''.format()}"));
        Assert.assertEquals("1", eval("${'%s'.format(1)}"));
        Assert.assertEquals("12", eval("${'%s%s'.format(1,2)}"));
        Assert.assertEquals("123", eval("${'%s%s%s'.format(1,2,3)}"));
    }

}
