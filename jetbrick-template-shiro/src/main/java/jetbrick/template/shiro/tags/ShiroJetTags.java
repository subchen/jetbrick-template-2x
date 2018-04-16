/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.shiro.tags;

import jetbrick.template.JetAnnotations;
import jetbrick.template.runtime.JetTagContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@JetAnnotations.Tags
public class ShiroJetTags {

    private static Subject getSubject() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            throw new IllegalStateException("Cannot get subject.");
        }
        return subject;
    }

    private static void printTagBody(JetTagContext ctx) throws IOException {
        String body = ctx.getBodyContent();
        ctx.getWriter().print(body);
    }

    /**
     * Displays body content only if the current Subject (user) 'has' (implies)
     * the specified permission (i.e the user has the specified ability).
     */
    public static void has_permission(JetTagContext ctx, String permission) throws IOException {
        final Subject subject = getSubject();

        if (subject.isPermitted(permission)) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current Subject (user) does NOT have
     * (not imply) the specified permission (i.e. the user lacks the specified
     * ability)
     */
    public static void lacks_permission(JetTagContext ctx, String permission) throws IOException {
        final Subject subject = getSubject();

        if (!subject.isPermitted(permission)) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current user has one of the specified
     * roles from a list of role names.
     */
    public static void has_any_role(JetTagContext ctx, String... roles) throws IOException {
        final Subject subject = getSubject();

        boolean show = false;
        for (String role : roles) {
            if (subject.hasRole(role)) {
                show = true;
                break;
            }
        }

        if (show) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current user does NOT have the
     * specified role (i.e. they explicitly lack the specified role)
     */
    public static void lacks_role(JetTagContext ctx, String role) throws IOException {
        final Subject subject = getSubject();

        boolean show = !subject.hasRole(role);
        if (show) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current user has successfully
     * authenticated _during their current session_. It is more restrictive than
     * the 'user' tag. It is logically opposite to the 'notAuthenticated' tag.
     */
    public static void authenticated(JetTagContext ctx) throws IOException {
        final Subject subject = getSubject();

        boolean show = subject.isAuthenticated();
        if (show) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current user has NOT succesfully
     * authenticated _during their current session_. It is logically opposite to
     * the 'authenticated' tag.
     */
    public static void not_authenticated(JetTagContext ctx) throws IOException {
        final Subject subject = getSubject();

        boolean show = !subject.isAuthenticated();
        if (show) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current user has remembered.
     */
    public static void remembered(JetTagContext ctx) throws IOException {
        final Subject subject = getSubject();

        boolean show = subject.isRemembered();
        if (show) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current user has remembered.
     */
    public static void not_remembered(JetTagContext ctx) throws IOException {
        final Subject subject = getSubject();

        boolean show = !subject.isRemembered();
        if (show) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current Subject IS NOT known to the
     * system, either because they have not logged in or they have no
     * corresponding 'RememberMe' identity. It is logically opposite to the
     * 'user' tag.
     */
    public static void guest(JetTagContext ctx) throws IOException {
        final Subject subject = getSubject();

        boolean show = subject.getPrincipal() == null;
        if (show) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays body content only if the current Subject has a known identity,
     * either from a previous login or from 'RememberMe' services. Note that
     * this is semantically different from the 'authenticated' tag, which is
     * more restrictive. It is logically opposite to the 'guest' tag.
     */
    public static void user(JetTagContext ctx) throws IOException {
        final Subject subject = getSubject();

        boolean show = (subject.getPrincipal() != null);
        if (show) {
            printTagBody(ctx);
        }
    }

    /**
     * Displays the user's principal (toString() method will be called).
     */
    public static void principal(JetTagContext ctx) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        principal(ctx, null);
    }

    /**
     * Displays the user's principal or a property of the user's principal.
     */
    public static void principal(JetTagContext ctx, String property) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Subject subject = getSubject();

        String val = null;
        Object principal = subject.getPrincipal();
        if (principal != null) {
            if (property == null) {
                val = principal.toString();
            } else {
                Object propertyValue = PropertyUtils.getProperty(principal, property);
                val = propertyValue != null ? propertyValue.toString() : "null";
            }
        }

        if (val != null) {
            ctx.getWriter().print(val);
        }
    }
}
