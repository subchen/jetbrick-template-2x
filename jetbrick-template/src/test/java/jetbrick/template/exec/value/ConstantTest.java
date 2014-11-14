package jetbrick.template.exec.value;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class ConstantTest extends AbstractJetxTest {

    @Test
    public void testNumber() {
        Assert.assertEquals("123", eval("${123}"));
        Assert.assertEquals("123", eval("${123L}"));
        Assert.assertEquals("99.99", eval("${99.99}"));
        Assert.assertEquals("99.99", eval("${99.99F}"));
        Assert.assertEquals("99.99", eval("${99.99d}"));
        Assert.assertEquals("9.999E100", eval("${9.999e100}"));
        Assert.assertEquals("65280", eval("${0xFF00}"));
        Assert.assertEquals("65280", eval("${0xFF00L}"));

        Assert.assertEquals("-123", eval("${-123}"));
        Assert.assertEquals("-99.99", eval("${-99.99F}"));
        Assert.assertEquals("-9.999E-9", eval("${-99.99E-10d}"));

        Assert.assertEquals("0.01", eval("${0.01}"));
        Assert.assertEquals("1.1", eval("${1.10D}"));
    }

    @Test
    public void testString() {
        Assert.assertEquals("abc", eval("${\"abc\"}"));
        Assert.assertEquals("abc", eval("${'abc'}"));
        Assert.assertEquals("abc\n000", eval("${'abc\\n000'}"));
        Assert.assertEquals("\u004a", eval("${'\\u004a'}"));
        Assert.assertEquals("\t\n\'\"", eval("${'\\t\\n\\'\\\"'}"));
    }

    @Test
    public void testOthers() {
        Assert.assertEquals("true", eval("${true}"));
        Assert.assertEquals("false", eval("${false}"));
        Assert.assertEquals("", eval("${null}"));
    }

    @Test
    public void testList() {
        Assert.assertEquals("[]", eval("${[]}"));
        Assert.assertEquals("[1, 2, 3]", eval("${[1,2,3]}"));
        Assert.assertEquals("[1, a, true]", eval("${[1,'a',true]}"));
    }

    @Test
    public void testMap() {
        Assert.assertEquals("{}", eval("${{}}"));
        Assert.assertEquals("{name=jetbrick}", eval("${{name:'jetbrick'}}"));
        Assert.assertEquals("{name=jetbrick}", eval("${{'name':'jetbrick'}}"));
        Assert.assertEquals("{name=jetbrick, lang=java}", eval("${{name:'jetbrick', lang: 'java'}}"));
    }
}
