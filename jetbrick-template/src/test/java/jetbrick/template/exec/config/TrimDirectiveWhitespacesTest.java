package jetbrick.template.exec.config;

import jetbrick.template.exec.AbstractJetxSourceTest;
import org.junit.Assert;
import org.junit.Test;

public class TrimDirectiveWhitespacesTest extends AbstractJetxSourceTest {

    @Test
    public void testBasic1() {
        StringBuilder sb = new StringBuilder();
        sb.append("#for(int i:range(1,10))\n");
        sb.append("    #if(for.odd)  \n");
        sb.append("${i}\n");
        sb.append("    #end\n");
        sb.append("#end");
        Assert.assertEquals("1\n3\n5\n7\n9\n", eval(sb.toString()));
    }

    @Test
    public void testBasic2() {
        StringBuilder sb = new StringBuilder();
        sb.append("#for(int i:range(1,10))\n");
        sb.append("    #if(for.odd)  \n");
        sb.append(" ${i}\n");
        sb.append("    #end\n");
        sb.append("#end");
        Assert.assertEquals(" 1\n 3\n 5\n 7\n 9\n", eval(sb.toString()));
    }

    @Test
    public void testInline1() {
        StringBuilder sb = new StringBuilder();
        sb.append("===\n");
        sb.append("OK=#if(true)OK#end  \n");
        sb.append("===");
        Assert.assertEquals("===\nOK=OK\n===", eval(sb.toString()));
    }

    @Test
    public void testInline2() {
        StringBuilder sb = new StringBuilder();
        sb.append("===\n");
        sb.append("#if(true)OK#end  \n");
        sb.append("===");
        Assert.assertEquals("===\nOK\n===", eval(sb.toString()));
    }

    @Test
    public void testInline3() {
        StringBuilder sb = new StringBuilder();
        sb.append("===\n");
        sb.append("#if(true)OK#end X \n");
        sb.append("===");
        Assert.assertEquals("===\nOK X \n===", eval(sb.toString()));
    }
}
