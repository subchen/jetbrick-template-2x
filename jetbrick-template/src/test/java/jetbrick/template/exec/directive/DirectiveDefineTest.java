package jetbrick.template.exec.directive;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.parser.SyntaxException;
import org.junit.Test;

public class DirectiveDefineTest extends AbstractJetxSourceTest {

    @Test
    public void test() {
        eval("#define(int a)");
        eval("#define(int a, Map b)");
    }

    @Test(expected = SyntaxException.class)
    public void testRedefine() {
        eval("#define(int a)#define(int a)");
    }

    @Test(expected = SyntaxException.class)
    public void testDefineAfterUse() {
        eval("${a}, #define(int a)");
    }
}
