package jetbrick.template.exec.config;

import jetbrick.template.JetConfig;
import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class TrimDirectiveCommentsTest extends AbstractJetxTest {

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

    @Test
    public void testIssue10() {
        StringBuilder s = new StringBuilder();
        s.append("<!-- #include('/sub.jetx') -->");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("xxx");
        engine.set("/sub.jetx", s.toString());

        Assert.assertEquals("xxx", eval());
    }
}
