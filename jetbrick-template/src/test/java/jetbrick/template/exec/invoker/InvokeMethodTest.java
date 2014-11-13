package jetbrick.template.exec.invoker;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class InvokeMethodTest extends AbstractJetxTest {

    @Test
    public void test() {
        Assert.assertEquals("6", eval("${'123456'.length()}"));
        Assert.assertEquals("2", eval("${'123456'.charAt(1)}"));
    }

    @Test
    public void testOverload() {
        Assert.assertEquals("456", eval("${'123456'.substring(3)}"));
        Assert.assertEquals("45", eval("${'123456'.substring(3,5)}"));
    }

    @Test
    public void testVarargs() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("model", new Model());
        Assert.assertEquals("", eval("${model.format()}", context));
        Assert.assertEquals("1", eval("${model.format(1)}", context));
        Assert.assertEquals("12", eval("${model.format(1,2)}", context));
        Assert.assertEquals("123", eval("${model.format(1,2,3)}", context));
    }

    @Test
    public void testStatic() {
        Assert.assertEquals("12", eval("${String::valueOf(12)}"));
        Assert.assertEquals("12", eval("${java.lang.String::valueOf(12)}"));
    }

    @Test
    public void testStaticOverload() {
        Assert.assertEquals("12", eval("${String::valueOf(12)}"));
        Assert.assertEquals("12.99", eval("${String::valueOf(12.99d)}"));
        Assert.assertEquals("true", eval("${java.lang.String::valueOf(true)}"));
    }

    @Test
    public void testStaticVarargs() {
        Assert.assertEquals("aa", eval("${String::format('aa')}"));
        Assert.assertEquals("aa", eval("${String::format('%s', 'aa')}"));
        Assert.assertEquals("aa12", eval("${java.lang.String::format('%s%d', 'aa', 12)}"));
    }

    static class Model {
        public String format(Object... args) {
            if (args.length == 0) {
                return "";
            } else if (args.length == 1) {
                return String.format("%s", args);
            } else if (args.length == 2) {
                return String.format("%s%s", args);
            } else {
                return String.format("%s%s%s", args);
            }
        }
    }
}
