package jetbrick.template.exec.value;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class TextTest extends AbstractJetxTest {

    @Test
    public void testText() {
        Assert.assertEquals("123", eval("123"));
        Assert.assertEquals("#", eval("#"));
        Assert.assertEquals("$", eval("$"));
        Assert.assertEquals("\\", eval("\\"));
        Assert.assertEquals("#ff0000", eval("#ff0000"));
        Assert.assertEquals("#$#", eval("#$#"));
        Assert.assertEquals("#{}", eval("#{}"));
        Assert.assertEquals("$()", eval("$()"));
        Assert.assertEquals("$abc()", eval("$abc()"));
    }

    @Test
    public void testTextEscape() {
        Assert.assertEquals("#", eval("\\#"));
        Assert.assertEquals("$", eval("\\$"));
        Assert.assertEquals("\\", eval("\\"));
        Assert.assertEquals("#if", eval("\\#if"));
        Assert.assertEquals("${a}", eval("\\${a}"));
    }

    @Test
    public void testCDATA() {
        Assert.assertEquals("#if", eval("#[[#if]]#"));
        Assert.assertEquals("\na\nb", eval("#[[\na\nb]]#"));
        Assert.assertEquals("##$$\\", eval("#[[##$$\\]]#"));
    }
}
