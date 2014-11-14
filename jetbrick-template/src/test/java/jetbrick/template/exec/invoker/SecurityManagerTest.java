package jetbrick.template.exec.invoker;

import java.util.ArrayList;
import java.util.List;
import jetbrick.template.JetSecurityManagerImpl;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.runtime.InterpretException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class SecurityManagerTest extends AbstractJetxTest {

    @Override
    protected void initializeEngine() {
        List<String> nameList = new ArrayList<String>();
        nameList.add("-java.io");
        nameList.add("-java.lang.System");
        nameList.add("-java.util.Date.<init>");
        nameList.add("-java.lang.Integer.MAX_VALUE");
        nameList.add("-java.lang.String.length");
        nameList.add("-java.lang.CharSequence.length");

        JetSecurityManagerImpl securityManager = new JetSecurityManagerImpl();
        securityManager.setNameList(nameList);

        engine.setSecurityManager(securityManager);
    }

    @Test
    public void pkgAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        eval("${java.io.File::separator}");
    }

    @Test
    public void classAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        eval("${System::gc()}");
    }

    @Test
    public void constructorAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        eval("${new Date()}");
    }

    @Test
    public void methodAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        eval("${'a'.length()}");
    }

    @Test
    public void fieldAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        eval("${Integer::MAX_VALUE}");
    }
}
