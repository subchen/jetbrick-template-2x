package jetbrick.template.exec.invoker;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.runtime.InterpretException;
import org.junit.Assert;
import org.junit.Test;

public class VoidTest extends AbstractJetxTest {

    @Test
    public void testValue() {
        Assert.assertEquals("", eval("${Thread::yield()}"));
        Assert.assertEquals("", eval("${new StringBuilder().trimToSize()}"));
        Assert.assertEquals("", eval("$!{Thread::yield()}"));
    }

    @Test
    public void testArgumentIsVoid() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.EXPRESSION_ARGUMENT_IS_VOID);

        eval("${System::identityHashCode(Thread::yield())}");
    }

    @Test
    public void testMethodInvoke() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.EXPRESSION_OBJECT_IS_VOID);

        eval("${Thread::yield().toString()}");
    }

    @Test
    public void testListGet1() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.EXPRESSION_OBJECT_IS_VOID);

        eval("${Thread::yield()[0]}");
    }

    @Test
    public void testListGet2() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.EXPRESSION_OBJECT_IS_VOID);

        eval("${new StringBuilder().trimToSize()[0]}");
    }

    @Test
    public void testListGet3() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.EXPRESSION_INDEX_IS_VOID);

        eval("${[1,2,3][Thread::yield()]}");
    }
}
