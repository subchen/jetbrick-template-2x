package jetbrick.template.web.tag;

import jetbrick.template.JetAnnotations;
import jetbrick.template.runtime.JetTagContext;
import jetbrick.template.web.springboot.EnvHolder;

import java.io.IOException;

@JetAnnotations.Tags
public class SpringedTags {

    @JetAnnotations.Name("actived_profile")
    public static void activedProfile(JetTagContext ctx, String profile) throws IOException {
        if (EnvHolder.getEnvironment().acceptsProfiles(profile)) {
            String content = ctx.getBodyContent();
            ctx.getWriter().print(content);
        }
    }

    @JetAnnotations.Name("actived_profile")
    public static void activedProfile(JetTagContext ctx, String... profiles) throws IOException {
        boolean ok = true;

        // Environment#acceptsProfiles(String...) 是或关系
        // 本方法是与关系
        for (String profile : profiles) {
            if (!EnvHolder.getEnvironment().acceptsProfiles(profile)) {
                ok = false;
                break;
            }
        }

        if (ok) {
            String content = ctx.getBodyContent();
            ctx.getWriter().print(content);
        }
    }
}
