package jetbrick.template.exec.invoker;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.runtime.InterpretException;
import org.junit.Assert;
import org.junit.Test;

public class InvokeFieldTest extends AbstractJetxSourceTest {

    @Test
    public void test() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("model", new Model());
        Assert.assertEquals("10", eval("${model.VALUE}", context));
        Assert.assertEquals("true", eval("${model.good}", context));
        Assert.assertEquals("model", eval("${model.name}", context));
    }

    @Test(expected = InterpretException.class)
    public void testNotFound() {
        eval("${''.xxx}");
    }

    @Test
    public void testMap() {
        Assert.assertEquals("jetbrick", eval("${{name:'jetbrick'}.name}"));
        Assert.assertEquals("", eval("${{name:'jetbrick'}.xx}"));
    }

    @Test
    public void testArrayLength() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("model1", new int[10]);
        context.put("model2", new String[20]);
        Assert.assertEquals("10", eval("${model1.length}", context));
        Assert.assertEquals("20", eval("${model2.length}", context));
    }

    @Test
    public void testClass() {
        Assert.assertEquals("class java.lang.String", eval("${'a'.class}"));
        Assert.assertEquals("class java.lang.String", eval("${String::class}"));
    }

    @Test
    public void testStatic() {
        Assert.assertEquals("2147483647", eval("${java.lang.Integer::MAX_VALUE}"));
        Assert.assertEquals("2147483647", eval("${Integer::MAX_VALUE}"));
    }

    @Test(expected = InterpretException.class)
    public void testStaticNotFound() {
        eval("${Integer::XXX}");
    }

    static class Model {
        public int VALUE = 10;
        public static int S_VAL = 20;

        public boolean isGood() {
            return true;
        }

        public String getName() {
            return "model";
        }
    }
}
