package jetbrick.template.exec.invoker;

import java.util.*;
import jetbrick.template.*;
import jetbrick.template.exec.AbstractJetxFileTest;
import jetbrick.template.runtime.InterpretException;
import jetbrick.template.loader.AbstractResourceLoader;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.hamcrest.CoreMatchers;

public class SecurityManagerTest extends AbstractJetxFileTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

        AbstractResourceLoader loader = (AbstractResourceLoader) engine.getConfig().getTemplateLoaders().get(0);
        loader.setSecurityManager(securityManager);
    }

    @Test
    public void pkgAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        sourceMap.put("/1.jetx", "${java.io.File::separator}");
        eval("/1.jetx");
    }

    @Test
    public void classAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        sourceMap.put("/1.jetx", "${System::gc()}");
        eval("/1.jetx");
    }

    @Test
    public void constructorAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        sourceMap.put("/1.jetx", "${new Date()}");
        eval("/1.jetx");
    }

    @Test
    public void methodAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        sourceMap.put("/1.jetx", "${'a'.length()}");
        eval("/1.jetx");
    }

    @Test
    public void fieldAccess() throws Exception {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.startsWith("java.security.AccessControlException"));

        sourceMap.put("/1.jetx", "${Integer::MAX_VALUE}");
        eval("/1.jetx");
    }
}
