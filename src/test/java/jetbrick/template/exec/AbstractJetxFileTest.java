package jetbrick.template.exec;

import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import jetbrick.io.resource.Resource;
import jetbrick.template.*;
import jetbrick.template.resource.SourceResource;
import jetbrick.template.resource.loader.AbstractResourceLoader;
import jetbrick.util.ExceptionUtils;
import org.junit.*;

public abstract class AbstractJetxFileTest extends AbstractJetxSourceTest {
    protected static final Map<String, String> sourceMap = new HashMap<String, String>();

    @Override
    public void initializeConfig() {
        config.put(JetConfig.TEMPLATE_LOADER, SourceResourceLoader.class.getName());
    }

    @Override
    protected JetTemplate getTemplate(String name) {
        return engine.getTemplate(name);
    }

    public static class SourceResourceLoader extends AbstractResourceLoader {
        @Override
        public Resource load(String name) {
            String source = sourceMap.get(name);
            if (source != null) {
                return new SourceResource(source);
            }
            return null;
        }
    }
}
