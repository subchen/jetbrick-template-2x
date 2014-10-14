package jetbrick.template.exec.config;

import jetbrick.template.JetConfig;
import jetbrick.template.exec.AbstractJetxSourceTest;
import org.junit.*;

public class TrimDirectiveCommentsTest extends AbstractJetxSourceTest {

    @Override
    protected void initializeConfig() {
        config.setProperty(JetConfig.TRIM_DIRECTIVE_COMMENTS, "true");
    }

    @Test
    public void testBasic() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!-- #for(int i:range(1,3)) -->\n");
        sb.append("${i}\n");
        sb.append("<!--#end-->");
        Assert.assertEquals("1\n2\n3\n", eval(sb.toString()));
    }

    @Test
    public void testMultiline() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!-- \n");
        sb.append("    #for(int i:range(1,3))  \n");
        sb.append(" -->  \n");
        sb.append("${i}\n");
        sb.append("<!--#end-->");
        Assert.assertEquals("1\n2\n3\n", eval(sb.toString()));
    }
}
