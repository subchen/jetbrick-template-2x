package jetbrick.template.exec.buildin;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class BuildinTagTest extends AbstractJetxTest {

    @Test
    public void test() {
        StringBuilder s = new StringBuilder();
        s.append("#tag layout_block('a')");
        s.append("aaa");
        s.append("#end");
        s.append("#tag layout_block('b')");
        s.append("bbb");
        s.append("#end");
        s.append("#include('/layout.jetx')");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("===");
        s.append("${a}");
        s.append("---");
        s.append("${b}");
        s.append("~~~");
        s.append("${c}");
        engine.set("/layout.jetx", s.toString());

        Assert.assertEquals("===aaa---bbb~~~", eval());
    }

    @Test
    public void test_default_block() {
        StringBuilder s = new StringBuilder();
        s.append("#tag layout_block('a')");
        s.append("aaa");
        s.append("#end");
        s.append("#include('/layout.jetx', {c:'ccc'})");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("#tag layout_block_default('a')");
        s.append("111");
        s.append("#end");
        s.append("#tag layout_block_default('b')");
        s.append("222");
        s.append("#end");
        s.append("${c}");
        engine.set("/layout.jetx", s.toString());

        Assert.assertEquals("aaa222ccc", eval());
    }
}
