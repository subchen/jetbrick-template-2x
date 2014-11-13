package jetbrick.template.exec.config;

import jetbrick.template.JetConfig;
import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class TrimDirectiveCommentsCustomTest extends AbstractJetxTest {

    @Override
    protected void initializeConfig() {
        config.setProperty(JetConfig.TRIM_DIRECTIVE_COMMENTS, "true");
        config.setProperty(JetConfig.TRIM_DIRECTIVE_COMMENTS_PREFIX, "<%--");
        config.setProperty(JetConfig.TRIM_DIRECTIVE_COMMENTS_SUFFIX, "--%>");
    }

    @Test
    public void test() {
        Assert.assertEquals("", eval("<%-- #stop --%>"));
    }

}
