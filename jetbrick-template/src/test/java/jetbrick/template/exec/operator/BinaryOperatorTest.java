package jetbrick.template.exec.operator;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class BinaryOperatorTest extends AbstractJetxTest {

    @Test
    public void testPlus() {
        Assert.assertEquals("3", eval("${1+2}"));
        Assert.assertEquals("3", eval("${1+2L}"));
        Assert.assertEquals("3.1", eval("${1+2.1f}"));
        Assert.assertEquals("3.1", eval("${1+2.1d}"));
    }

    @Test
    public void testMinus() {
        Assert.assertEquals("1", eval("${2-1}"));
        Assert.assertEquals("1", eval("${2L-1}"));
        Assert.assertEquals("0.9", eval("${2-1.1f}"));
        Assert.assertEquals("0.9", eval("${2-1.1f}"));
    }

    @Test
    public void testMul() {
        Assert.assertEquals("6", eval("${2*3}"));
        Assert.assertEquals("6", eval("${2*3L}"));
        Assert.assertEquals("6.2", eval("${2*3.1f}"));
        Assert.assertEquals("6.2", eval("${2*3.1d}"));
    }

    @Test
    public void testDiv() {
        Assert.assertEquals("1", eval("${6/4}"));
        Assert.assertEquals("1", eval("${6/4L}"));
        Assert.assertEquals("1.5", eval("${6/4f}"));
        Assert.assertEquals("1.5", eval("${6/4d}"));
    }

    @Test
    public void testMod() {
        Assert.assertEquals("1", eval("${3%2}"));
        Assert.assertEquals("1", eval("${3%2L}"));
        Assert.assertEquals("1.0", eval("${3%2f}"));
        Assert.assertEquals("1.0", eval("${3%2d}"));
    }

    @Test
    public void testArithmetic() {
        Assert.assertEquals("7.2", eval("${1+2*3.1}"));
        Assert.assertEquals("2.2", eval("${1.1+1.1}"));
        Assert.assertEquals("-10.2", eval("${1+2*(3-4)*5.6}"));
    }

    @Test
    public void testStringAdd() {
        Assert.assertEquals("a1", eval("${'a'+1}"));
        Assert.assertEquals("1a", eval("${1+'a'}"));
        Assert.assertEquals("12", eval("${'1'+'2'}"));
        Assert.assertEquals("a", eval("${'a'+null}"));
        Assert.assertEquals("a", eval("${null+'a'}"));
    }

    @Test
    public void testInstanceOf() {
        Assert.assertEquals("true", eval("${'a' instanceof String}"));
        Assert.assertEquals("true", eval("${1 instanceof Number}"));
        Assert.assertEquals("false", eval("${'a' instanceof Number}"));
    }

    @Test
    public void testNullAsDefault() {
        Assert.assertEquals("0", eval("${a ?! 0}"));
        Assert.assertEquals("0", eval("${a.b.c ?! 0}"));
        Assert.assertEquals("0", eval("${a.b() ?! 0}"));
        Assert.assertEquals("0", eval("${a[0] ?! 0}"));
    }
}
