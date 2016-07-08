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
}
