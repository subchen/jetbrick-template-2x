package jetbrick.template.exec;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import jetbrick.bean.asm.AsmFactory;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.util.ExceptionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractJetxSourceTest {
    protected static JetEngine engine;

    static {
        //System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
    }

    @BeforeClass
    public static void initialize() {
        AsmFactory.setThreshold(Integer.MAX_VALUE);
        engine = JetEngine.create();
    }

    @AfterClass
    public static void destory() {
        engine = null;
    }

    protected String eval(String source) {
        return eval(source, null);
    }

    protected String eval(String source, Map<String, Object> context) {
        JetTemplate template = engine.createTemplate(source);

        Writer out = new StringWriter();
        try {
            template.render(context, out);
            return out.toString();
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    protected static String str(Object value) {
        return (value == null) ? "" : value.toString();
    }
}
