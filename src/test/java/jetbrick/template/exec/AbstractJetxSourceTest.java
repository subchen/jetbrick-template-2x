package jetbrick.template.exec;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.util.ExceptionUtils;
import org.junit.*;

public abstract class AbstractJetxSourceTest {
    protected JetEngine engine;
    protected Properties config;

    static {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
        jetbrick.bean.asm.AsmFactory.setThreshold(0);
        //jetbrick.bean.asm.AsmFactory.setThreshold(Integer.MAX_VALUE);
    }

    @Before
    public void initialize() {
        config = new Properties();
        initializeConfig();
        engine = JetEngine.create(config);
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
    
    protected String eval(String source) {
        return eval(source, null);
    }

    protected String eval(String source, Map<String, Object> context) {
        JetTemplate template = getTemplate(source);

        Writer out = new StringWriter();
        try {
            template.render(context, out);
            return out.toString();
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }
    
    protected JetTemplate getTemplate(String source) {
        return engine.createTemplate(source);
    }

    protected static String str(Object value) {
        return (value == null) ? "" : value.toString();
    }
}
