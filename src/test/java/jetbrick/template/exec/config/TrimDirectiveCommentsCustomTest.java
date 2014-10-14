package jetbrick.template.exec.config;

import jetbrick.template.JetConfig;
import jetbrick.template.exec.AbstractJetxSourceTest;
import org.junit.*;

public class TrimDirectiveCommentsCustomTest extends AbstractJetxSourceTest {

    @Override
    protected void initializeConfig() {
        config.setProperty(JetConfig.TRIM_DIRECTIVE_COMMENTS, "true");
        config.setProperty(JetConfig.TRIM_DIRECTIVE_COMMENTS_PREFIX, "<%--");
        config.setProperty(JetConfig.TRIM_DIRECTIVE_COMMENTS_SUFFIX, "--%>");
    }

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder();
        sb.append("<%-- #stop --%>");
        Assert.assertEquals("", eval(sb.toString()));
    }

}
