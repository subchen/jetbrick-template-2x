package jetbrick.template.exec.value;

import jetbrick.template.exec.AbstractJetxSourceTest;
import org.junit.Assert;
import org.junit.Test;

public class CommentTest extends AbstractJetxSourceTest {

    @Test
    public void testLine1() {
        Assert.assertEquals("abc", eval("abc##def##123"));
        Assert.assertEquals("", eval("##1234567890"));
        Assert.assertEquals("", eval("##"));
    }

    @Test
    public void testLine2() {
        Assert.assertEquals("abc", eval("abc#//def##123"));
        Assert.assertEquals("", eval("#//1234567890"));
        Assert.assertEquals("", eval("#//"));
    }

    @Test
    public void testBlock() {
        Assert.assertEquals("", eval("#-- aaa \n bbb --#"));
        Assert.assertEquals("", eval("#-- #if() --#"));
        Assert.assertEquals("aa", eval("a#----#a"));
    }
}
