package jetbrick.template.exec.directive;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.runtime.InterpretException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveSetTest extends AbstractJetxTest {

    @Test
    public void test() {
        Assert.assertEquals("1", eval("#set(i=1)${i}"));
        Assert.assertEquals("1true", eval("#set(i=1,x=true)${i}${x}"));
        Assert.assertEquals("1a", eval("#set(i=1)${i}#set(i='a')${i}"));
        Assert.assertEquals("7", eval("#set(i=1+2*3)${i}"));
    }

    @Test
    public void testOk() {
        eval("#set(long i=1L)#set(i=1)");
        eval("#set(int i=1)#set(int i=1)");
    }

    @Test
    public void testFailed() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.containsString(errbefore(Errors.VARIABLE_TYPE_INCONSISTENT)));

        eval("#set(int i=1)#set(i=1L)");
    }
}
