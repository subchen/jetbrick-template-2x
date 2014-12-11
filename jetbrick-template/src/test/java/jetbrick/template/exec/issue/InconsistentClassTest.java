package jetbrick.template.exec.issue;

import jetbrick.template.exec.AbstractJetxTest;
import org.junit.Assert;
import org.junit.Test;

public class InconsistentClassTest extends AbstractJetxTest {

    @Test
    public void testInclude() {
        StringBuilder s = new StringBuilder();
        s.append("#for(int i:[0,1,2])${i}#end");
        s.append("#include('/sub.jetx')");
        engine.set(DEFAULT_MAIN_FILE, s.toString());

        s = new StringBuilder();
        s.append("#for(int i:[0,1,2])${i}#end");
        engine.set("/sub.jetx", s.toString());

        Assert.assertEquals("012012", eval());
    }

}
