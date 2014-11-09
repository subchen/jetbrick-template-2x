package jetbrick.template.exec;

import java.util.HashMap;
import java.util.Map;
import jetbrick.io.resource.Resource;
import jetbrick.template.JetConfig;
import jetbrick.template.JetTemplate;
import jetbrick.template.loader.AbstractResourceLoader;
import jetbrick.template.loader.resource.SourceResource;

public abstract class AbstractJetxFileTest extends AbstractJetxSourceTest {
    protected static final Map<String, String> sourceMap = new HashMap<String, String>();

    @Override
    public void initializeConfig() {
        config.put(JetConfig.TEMPLATE_LOADERS, SourceResourceLoader.class.getName());
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
