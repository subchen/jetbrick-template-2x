package jetbrick.template.exec.invoker;

import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class InvokeFunctionTest extends AbstractJetxTest {

    @Override
    public void initializeEngine() {
        engine.getGlobalResolver().registerFunctions(StringUtils.class);
    }

    @Test
    public void test() {
        Assert.assertEquals("123", eval("${trim(' 123 ')}"));
        Assert.assertEquals("000", eval("${repeat('0', 3)}"));
        Assert.assertEquals("?,?,?", eval("${repeat('?', ',', 3)}"));
    }

}
