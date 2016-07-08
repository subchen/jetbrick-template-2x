package jetbrick.template.web.springboot;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public final class EnvHolder implements EnvironmentAware {

    public static final EnvHolder INSTANCE = new EnvHolder();
    private static Environment environment;

    private EnvHolder() {
        super();
    }

    @Override
    public void setEnvironment(Environment environment) {
        EnvHolder.environment = environment;
    }

    public static Environment getEnvironment() {
        return environment;
    }

}
