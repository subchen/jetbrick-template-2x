package jetbrick.template.exec.directive;

import jetbrick.template.Errors;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.parser.SyntaxException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class DirectiveDefineTest extends AbstractJetxTest {

    @Test
    public void test() {
        eval("#define(int a)");
        eval("#define(int a, Map b)");
    }

    @Test
    public void testRedefine() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.VARIABLE_REDEFINE)));

        eval("#define(int a)#define(int a)");
    }

    @Test
    public void testDefineAfterUse() {
        thrown.expect(SyntaxException.class);
        thrown.expectMessage(CoreMatchers.containsString(err(Errors.VARIABLE_DEFAINE_AFTER_USE)));

        eval("${a}, #define(int a)");
    }
}
