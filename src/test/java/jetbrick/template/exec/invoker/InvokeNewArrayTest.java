package jetbrick.template.exec.invoker;

import jetbrick.template.exec.AbstractJetxSourceTest;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class InvokeNewArrayTest extends AbstractJetxSourceTest {

    @Test
    public void test() {
        Assert.assertThat(eval("${new int[1]}"), CoreMatchers.startsWith("[I"));
        Assert.assertThat(eval("${new String[1][2]}"), CoreMatchers.startsWith("[[Ljava.lang.String;"));
    }
}
