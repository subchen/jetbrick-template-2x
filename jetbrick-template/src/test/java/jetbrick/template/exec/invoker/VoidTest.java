package jetbrick.template.exec.invoker;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.runtime.InterpretException;
import org.junit.*;
import org.junit.rules.ExpectedException;

public class VoidTest extends AbstractJetxSourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValue() {
        Assert.assertEquals("", eval("${System::gc()}"));
        Assert.assertEquals("", eval("${new StringBuilder().trimToSize()}"));
        Assert.assertEquals("", eval("$!{System::gc()}"));
    }

    @Test
    public void testArgumentIsVoid() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.ARGUMENT_IS_VOID);
        eval("${System::identityHashCode(System::gc())}");
    }

    @Test
    public void testMethodInvoke() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.OBJECT_IS_VOID);
        eval("${System::gc().toString()}");
    }

    @Test
    public void testListGet1() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.OBJECT_IS_VOID);
        eval("${System::gc()[0]}");
    }

    @Test
    public void testListGet2() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.OBJECT_IS_VOID);
        eval("${new StringBuilder().trimToSize()[0]}");
    }

    @Test
    public void testListGet3() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(Errors.INDEX_IS_VOID);
        eval("${[1,2,3][System::gc()]}");
    }
}
