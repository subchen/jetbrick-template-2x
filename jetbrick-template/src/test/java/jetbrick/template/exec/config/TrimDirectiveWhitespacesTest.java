package jetbrick.template.exec.config;

import java.io.IOException;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.runtime.JetTagContext;
import org.junit.Assert;
import org.junit.Test;

public class TrimDirectiveWhitespacesTest extends AbstractJetxTest {

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

    @Test
    public void testInline4() {
        StringBuilder sb = new StringBuilder();
        sb.append("#for(int i: range(0,3))${i}#end\n");
        sb.append("#for(int i: range(0,3))${i}#end\n");
        Assert.assertEquals("0123\n0123\n", eval(sb.toString()));
    }

    @Test
    public void testMacroCall() {
        StringBuilder sb = new StringBuilder();
        sb.append("#macro hello()\n");
        sb.append("hello\n");
        sb.append("#end\n");
        sb.append("#call hello()\n");
        sb.append("#call hello()\n");
        Assert.assertEquals("hello\nhello\n", eval(sb.toString()));
    }

    @Test
    public void testIncludeCall() {
        StringBuilder s = new StringBuilder();
        s.append("#include('/sub.jetx')\n");
        s.append("#include('/sub.jetx')\n");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("123");
        engine.set("/sub.jetx", s.toString());

        Assert.assertEquals("123\n123\n", eval());
    }

    @Test
    public void testTagCall() {
        engine.getGlobalResolver().registerTags(Tags.class);

        StringBuilder sb = new StringBuilder();
        sb.append("#tag body()\n");
        sb.append("123\n");
        sb.append("#end\n");
        sb.append("#tag body()\n");
        sb.append("123\n");
        sb.append("#end\n");
        Assert.assertEquals("123\n123\n", eval(sb.toString()));
    }

    static class Tags {
        public static void body(JetTagContext ctx) throws IOException {
            ctx.invoke();
        }
    }
}
