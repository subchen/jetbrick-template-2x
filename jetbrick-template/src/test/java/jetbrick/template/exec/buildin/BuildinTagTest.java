package jetbrick.template.exec.buildin;

import jetbrick.template.exec.AbstractJetxFileTest;
import org.junit.Assert;
import org.junit.Test;

public class BuildinTagTest extends AbstractJetxFileTest {

    static {
        StringBuilder s = new StringBuilder();
        s.append("#tag layout_block('a')");
        s.append("aaa");
        s.append("#end");
        s.append("#tag layout_block('b')");
        s.append("bbb");
        s.append("#end");
        s.append("#include('/s2.jetx')");
        sourceMap.put("/s1.jetx", s.toString());

        s = new StringBuilder();
        s.append("===");
        s.append("${a}");
        s.append("---");
        s.append("${b}");
        s.append("~~~");
        s.append("${c}");
        sourceMap.put("/s2.jetx", s.toString());

        // --------------------------------------------
        s = new StringBuilder();
        s.append("#tag layout_block('a')");
        s.append("aaa");
        s.append("#end");
        s.append("#include('/s4.jetx', {c:'ccc'})");
        sourceMap.put("/s3.jetx", s.toString());

        s = new StringBuilder();
        s.append("#tag layout_block_default('a')");
        s.append("111");
        s.append("#end");
        s.append("#tag layout_block_default('b')");
        s.append("222");
        s.append("#end");
        s.append("${c}");
        sourceMap.put("/s4.jetx", s.toString());

    }

    @Test
    public void test() {
        Assert.assertEquals("===aaa---bbb~~~", eval("/s1.jetx"));
    }

    @Test
    public void test_default_block() {
        Assert.assertEquals("aaa222ccc", eval("/s3.jetx"));
    }
}
