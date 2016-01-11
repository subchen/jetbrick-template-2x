package jetbrick.template.exec.directive;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveForTest extends AbstractJetxTest {

    @Test
    public void testForList() {
        Assert.assertEquals("", eval("#for(i:[])${i}#end"));
        Assert.assertEquals("123", eval("#for(i:[1,2,3])${i}#end"));
    }

    @Test
    public void testForMap() {
        Assert.assertEquals("", eval("#for(i:{})${i}#end"));
        Assert.assertEquals("a-1,b-2,", eval("#for(e:{a:1,b:2})${e.key}-${e.value},#end"));
    }

    @Test
    public void testForElse() {
        Assert.assertEquals("XX", eval("#for(i:[])${i}#else()XX#end"));
        Assert.assertEquals("123", eval("#for(i:[1,2,3])${i}#else()XX#end"));
    }

    @Test
    public void testForStatus() {
        Assert.assertEquals("1-true,2-false,3-true,", eval("#for(i:[10,11,12])${for.index}-${for.odd},#end"));
    }

    @Test
    public void testForOuter() {
        Assert.assertEquals("1.1,1.2,1.3,2.1,2.2,2.3,", eval("#for(x:[1,2])#for(y:[1,2,3])${for.outer.index}.${for.index},#end#end"));
    }

    @Test
    public void testForComplex() {
        StringBuilder sb = new StringBuilder();
        sb.append("#for(i:[1,2])");
        sb.append("${for.index}-${i},");
        sb.append("#for(j:['a','b'])");
        sb.append("${for.index}-${j},");
        sb.append("#end");
        sb.append("#end");
        Assert.assertEquals("1-1,1-a,2-b,2-2,1-a,2-b,", eval(sb.toString()));
    }
}
