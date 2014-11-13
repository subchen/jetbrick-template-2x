package jetbrick.template.exec.invoker;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.parser.SyntaxException;
import jetbrick.template.runtime.InterpretException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class InvokeNewObjectTest extends AbstractJetxTest {

    @Override
    public void initializeEngine() {
        engine.getGlobalResolver().importClass(Model.class.getPackage().getName() + ".*");
        engine.getGlobalResolver().importClass(Model.class.getName());
    }

    @Test
    public void test1() {
        Assert.assertEquals("1", eval("${new Integer(1)}"));
        Assert.assertEquals("1", eval("${new java.lang.Integer(1)}"));
        Assert.assertEquals("1", eval("${new java.lang.Long(1)}"));
    }

    @Test
    public void test2() {
        Assert.assertEquals("model", eval("${new jetbrick.template.exec.invoker.InvokeNewObjectTest.Model()}"));
        Assert.assertEquals("model", eval("${new jetbrick.template.exec.invoker.InvokeNewObjectTest.Model(1)}"));
        Assert.assertEquals("model", eval("${new jetbrick.template.exec.invoker.InvokeNewObjectTest.Model(1L)}"));
    }

    @Test
    public void test3() {
        Assert.assertEquals("model", eval("${new Model()}"));
        Assert.assertEquals("model", eval("${new InvokeNewObjectTest.Model()}"));
    }

    @Test
    public void testClassNotFound() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.CLASS_NOT_FOUND)));

        eval("${new Model12345()}");
    }

    @Test
    public void testCtorNotFound() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.CONSTRUCTOR_NOT_FOUND)));

        eval("${new Model(false)}");
    }

    static class Model {
        public Model() {
        }

        public Model(long n) {
        }

        @Override
        public String toString() {
            return "model";
        }
    }
}
