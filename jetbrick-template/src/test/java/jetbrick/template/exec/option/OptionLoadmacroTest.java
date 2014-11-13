package jetbrick.template.exec.option;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class OptionLoadmacroTest extends AbstractJetxTest {

    @Test
    public void testInclude() {
        StringBuilder s = new StringBuilder();
        s.append("#options(loadmacro='/macros.jetx')");
        s.append("#call size('abc')");
        s.append("#call isOdd(123)");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("#macro size(String s)");
        s.append("${s.length()}");
        s.append("#end");
        s.append("#macro isOdd(int n)");
        s.append("${n % 2 == 1}");
        s.append("#end");
        engine.set("/macros.jetx", s.toString());

        Assert.assertEquals("3true", eval());
    }

}
