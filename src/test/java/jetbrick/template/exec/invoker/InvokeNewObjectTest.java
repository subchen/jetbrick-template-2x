package jetbrick.template.exec.invoker;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.parser.SyntaxException;
import jetbrick.template.runtime.InterpretException;
import org.junit.*;

public class InvokeNewObjectTest extends AbstractJetxSourceTest {

    @BeforeClass
    public static void beforeClass() {
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

    @Test(expected = SyntaxException.class)
    public void testClassNotFound() {
        eval("${new Model12345()}");
    }

    @Test(expected = InterpretException.class)
    public void testCtorNotFound() {
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
