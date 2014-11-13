package jetbrick.template.exec.directive;

import java.io.IOException;
import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.runtime.InterpretException;
import jetbrick.template.runtime.JetTagContext;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveTagTest extends AbstractJetxTest {

    @Override
    public void initializeEngine() {
        engine.getGlobalResolver().registerTags(Tags.class);
    }

    @Test
    public void test() {
        Assert.assertEquals("hello", eval("#tag hello()#end"));
        Assert.assertEquals("hello", eval("#tag hello()XXX#end"));
        Assert.assertEquals("hello:XXX", eval("#tag helloWithBody()XXX#end"));
        Assert.assertEquals("hello:jetbrick:XXX", eval("#tag helloWithParam('jetbrick')XXX#end"));
    }

    @Test
    public void testClosure() {
        Assert.assertEquals("hello:1", eval("#set(i=1)#tag helloWithBody()${i}#end"));
        Assert.assertEquals("hello:19", eval("#set(i=1)#tag helloWithBody()${i}#set(x=9)#end${x}"));
    }

    @Test
    public void testNotFound() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.TAG_NOT_FOUND)));

        Assert.assertEquals("hello", eval("#tag hello(1)#end"));
    }

    static class Tags {
        public static void hello(JetTagContext ctx) throws IOException {
            ctx.getWriter().print("hello");
        }

        public static void helloWithBody(JetTagContext ctx) throws IOException {
            ctx.getWriter().print("hello:" + ctx.getBodyContent());
        }

        public static void helloWithParam(JetTagContext ctx, String name) throws IOException {
            ctx.getWriter().print("hello:" + name + ":");
            ctx.invoke();
        }
    }
}
