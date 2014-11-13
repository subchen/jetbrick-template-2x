package jetbrick.template.exec.directive;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class DirectiveCallTest extends AbstractJetxTest {

    @Test
    public void test() {
        Assert.assertEquals("2", eval("#macro inc(int x)${x+1}#end#call inc(1)"));
    }
}
