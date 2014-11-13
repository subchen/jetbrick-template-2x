package jetbrick.template.exec.directive;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveIfTest extends AbstractJetxTest {

    @Test
    public void testIf() {
        Assert.assertEquals("a", eval("#if(true)a#end"));
        Assert.assertEquals("", eval("#if(false)a#end"));
    }

    @Test
    public void testElseIf() {
        Assert.assertEquals("1", eval("#set(i=1)#if(i==0)0#elseif(i==1)1#elseif(i==2)2#end"));
        Assert.assertEquals("9", eval("#set(i=3)#if(i==0)0#elseif(i==1)1#else()9#end"));
    }

    @Test
    public void testElse() {
        Assert.assertEquals("a", eval("#if(true)a#else()b#end"));
        Assert.assertEquals("b", eval("#if(false)a#else()b#end"));
    }

}
