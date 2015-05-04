package jetbrick.template.exec.operator;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class EqualsOperatorTest extends AbstractJetxTest {

    @Test
    public void testTrue() {
        Assert.assertEquals("1", eval("${(true)?1:0}"));
        Assert.assertEquals("0", eval("${(false)?1:0}"));
        Assert.assertEquals("0", eval("${(null)?1:0}"));
        Assert.assertEquals("1", eval("${(1)?1:0}"));
        Assert.assertEquals("0", eval("${(0)?1:0}"));
        Assert.assertEquals("1", eval("${(1.0D)?1:0}"));
        Assert.assertEquals("0", eval("${(0.0F)?1:0}"));
        Assert.assertEquals("1", eval("${('a')?1:0}"));
        Assert.assertEquals("0", eval("${([])?1:0}"));
        Assert.assertEquals("0", eval("${({})?1:0}"));
        Assert.assertEquals("0", eval("${(a)?1:0}"));
    }

    @Test
    public void testCompare() {
        Assert.assertEquals("1", eval("${(0==0)?1:0}"));
        Assert.assertEquals("1", eval("${(0D==0.0F)?1:0}"));
        Assert.assertEquals("1", eval("${(0D==0L)?1:0}"));
        Assert.assertEquals("1", eval("${(1!=0)?1:0}"));
        Assert.assertEquals("1", eval("${(1>0d)?1:0}"));
        Assert.assertEquals("1", eval("${(1>=1)?1:0}"));
        Assert.assertEquals("1", eval("${(1f>=0)?1:0}"));
        Assert.assertEquals("1", eval("${(0<1)?1:0}"));
        Assert.assertEquals("1", eval("${(0<=1)?1:0}"));
        Assert.assertEquals("1", eval("${(0<=0)?1:0}"));
    }

    @Test
    public void testComparable() {
        Assert.assertEquals("true", eval("${'b' > 'a'}"));
        Assert.assertEquals("true", eval("${'a' < 'b'}"));
        Assert.assertEquals("true", eval("${'b' >= 'a'}"));
        Assert.assertEquals("true", eval("${'a' >= 'a'}"));
        Assert.assertEquals("true", eval("${'a' <= 'b'}"));
        Assert.assertEquals("true", eval("${'a' <= 'a'}"));
    }

    @Test
    public void testEquals() {
        Assert.assertEquals("true", eval("${null==null}"));
        Assert.assertEquals("false", eval("${1==null}"));
        Assert.assertEquals("false", eval("${null==1}"));
    }

    @Test
    public void testIdenticallyEquals() {
        Assert.assertEquals("true", eval("${null===null}"));
        Assert.assertEquals("false", eval("${'a'==='a'}"));
        Assert.assertEquals("false", eval("${1===1L}"));
    }

    @Test
    public void testAndOrNot() {
        Assert.assertEquals("false", eval("${!true}"));
        Assert.assertEquals("true", eval("${!false}"));
        Assert.assertEquals("true", eval("${true && true}"));
        Assert.assertEquals("false", eval("${true && false}"));
        Assert.assertEquals("false", eval("${false && false}"));
        Assert.assertEquals("true", eval("${true || true}"));
        Assert.assertEquals("true", eval("${true || false}"));
        Assert.assertEquals("false", eval("${false || false}"));
    }

    @Test
    public void testAndOrQuickPath() {
        Assert.assertEquals("false", eval("${false && [].get(0)}"));
        Assert.assertEquals("true", eval("${true || [].get(0)}"));
    }
}
