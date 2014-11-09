package jetbrick.template.exec.option;

import jetbrick.template.exec.AbstractJetxSourceTest;
import org.junit.Assert;
import org.junit.Test;

public class OptionImportTest extends AbstractJetxSourceTest {

    @Test
    public void testBasic() {
        Assert.assertEquals("class java.text.DateFormat", eval("#options(import='java.text.DateFormat')${DateFormat::class}"));
        Assert.assertEquals("class java.text.DateFormat", eval("#options(import='java.text.*')${DateFormat::class}"));
    }

    @Test
    public void testMultiPkgs() {
        Assert.assertEquals("class jetbrick.template.loader.ClasspathResourceLoader", eval("#options(import='jetbrick.template.**')${ClasspathResourceLoader::class}"));
    }
}
