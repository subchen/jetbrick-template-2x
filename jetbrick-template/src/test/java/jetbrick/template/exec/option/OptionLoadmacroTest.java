package jetbrick.template.exec.option;

import jetbrick.template.exec.AbstractJetxFileTest;
import org.junit.Assert;
import org.junit.Test;

public class OptionLoadmacroTest extends AbstractJetxFileTest {

    static {
        StringBuilder s = new StringBuilder();
        s.append("#options(loadmacro='/s2.jetx')");
        s.append("#call size('abc')");
        s.append("#call isOdd(123)");
        sourceMap.put("/s1.jetx", s.toString());

        s = new StringBuilder();
        s.append("#macro size(String s)");
        s.append("${s.length()}");
        s.append("#end");
        s.append("#macro isOdd(int n)");
        s.append("${n % 2 == 1}");
        s.append("#end");
        sourceMap.put("/s2.jetx", s.toString());
    }

    @Test
    public void testInclude() {
        Assert.assertEquals("3true", eval("/s1.jetx"));
    }

}
