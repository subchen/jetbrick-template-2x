package jetbrick.template.exec;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import jetbrick.template.*;
import jetbrick.util.ExceptionUtils;
import jetbrick.util.StringUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;

public abstract class AbstractJetxTest {
    protected static final String DEFAULT_MAIN_FILE = "/main.jetx";
    protected SandboxJetEngine engine;
    protected Properties config;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    static {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
        jetbrick.bean.asm.AsmFactory.setThreshold(0);
        //jetbrick.bean.asm.AsmFactory.setThreshold(Integer.MAX_VALUE);
    }

    @Before
    public void initialize() {
        config = new Properties();
        initializeConfig();
        engine = new SandboxJetEngine(JetEngine.create(config));
        initializeEngine();
    }

    @After
    public void destory() {
        config = null;
        engine = null;
    }

    protected void initializeConfig() {
    }

    protected void initializeEngine() {
    }

    protected String eval() {
        return eval((Map<String, Object>) null);
    }

    protected String eval(Map<String, Object> context) {
        JetTemplate template = engine.getTemplate(DEFAULT_MAIN_FILE);
        return eval(template, context);
    }

    protected String eval(String source) {
        return eval(source, null);
    }

    protected String eval(String source, Map<String, Object> context) {
        engine.set(DEFAULT_MAIN_FILE, source);
        return eval(context);
    }

    protected String eval(JetTemplate template, Map<String, Object> context) {
        Writer out = new StringWriter();
        try {
            template.render(context, out);
            return out.toString();
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    protected static String err(String value) {
        return value.replace("%s", "");
    }

    protected static String errbefore(String value) {
        return StringUtils.substringBefore(value, "%s");
    }

    protected static String str(Object value) {
        return (value == null) ? "" : value.toString();
    }
}
