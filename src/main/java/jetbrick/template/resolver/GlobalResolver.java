/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.resolver;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import jetbrick.bean.Getter;
import jetbrick.bean.MethodInfo;
import jetbrick.io.finder.ClassFinder;
import jetbrick.template.*;
import jetbrick.template.resolver.clazz.ClassResolver;
import jetbrick.template.resolver.function.FunctionInvoker;
import jetbrick.template.resolver.function.FunctionResolver;
import jetbrick.template.resolver.macro.MacroResolver;
import jetbrick.template.resolver.method.MethodInvoker;
import jetbrick.template.resolver.method.MethodInvokerResolver;
import jetbrick.template.resolver.property.GetterResolver;
import jetbrick.template.resolver.tag.TagInvoker;
import jetbrick.template.resolver.tag.TagResolver;
import jetbrick.util.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负责解析模板中出现的 class, method, function, tag, macro 等等.
 */
public final class GlobalResolver {
    private static final Logger log = LoggerFactory.getLogger(GlobalResolver.class);

    private final ClassResolver classResolver;
    private final MethodInvokerResolver methodInvokerResolver;
    private final FunctionResolver functionResolver;
    private final TagResolver tagResolver;
    private final MacroResolver macroResolver;

    public GlobalResolver() {
        classResolver = new ClassResolver(true);
        methodInvokerResolver = new MethodInvokerResolver();
        functionResolver = new FunctionResolver();
        tagResolver = new TagResolver();
        macroResolver = new MacroResolver();
    }

    /**
     * 自动扫描 annotation
     */
    public void scan(List<String> packageNames, boolean skipErrors) {
        //@formatter:off
        @SuppressWarnings("unchecked")
        Class<? extends Annotation>[] annoClasses = (Class<? extends Annotation>[]) new Class<?>[] {
            JetAnnotations.Methods.class,
            JetAnnotations.Functions.class,
            JetAnnotations.Tags.class,
        };
        //@formatter:on

        log.info("Scanning @JetMethods, @JetFunctions, @JetTags implements from " + packageNames + " ...");

        long ts = System.currentTimeMillis();
        String[] pkgs = packageNames.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        Set<Class<?>> classes = ClassFinder.getClasses(pkgs, true, annoClasses, skipErrors);

        log.info("Found {} annotated classes, time elapsed {} ms.", classes.size(), System.currentTimeMillis() - ts);

        for (Class<?> cls : classes) {
            for (Annotation anno : cls.getAnnotations()) {
                if (anno instanceof JetAnnotations.Methods) {
                    registerMethods(cls);
                } else if (anno instanceof JetAnnotations.Functions) {
                    registerFunctions(cls);
                } else if (anno instanceof JetAnnotations.Tags) {
                    registerTags(cls);
                }
            }
        }
    }

    /**
     * 注册一个 Import
     */
    public void importClass(String name) {
        classResolver.importClass(name);
    }

    /**
     * 注册 method 扩展
     */
    public void registerMethods(String className) {
        Class<?> cls = resolveClass(className);
        if (cls == null) {
            throw new TemplateException("@JetMethods class not found: " + className);
        }
        methodInvokerResolver.register(cls);
    }

    /**
     * 注册 method 扩展
     */
    public void registerMethods(Class<?> cls) {
        methodInvokerResolver.register(cls);
    }

    /**
     * 注册 method 扩展
     */
    public void registerMethod(MethodInfo method) {
        methodInvokerResolver.register(method);
    }

    /**
     * 注册 function 扩展
     */
    public void registerFunctions(String className) {
        Class<?> cls = resolveClass(className);
        if (cls == null) {
            throw new TemplateException("@JetFunctions class not found: " + className);
        }
        functionResolver.register(cls);
    }

    /**
     * 注册 function 扩展
     */
    public void registerFunctions(Class<?> cls) {
        functionResolver.register(cls);
    }

    /**
     * 注册 function 扩展
     */
    public void registerFunction(MethodInfo method) {
        functionResolver.register(method);
    }

    /**
     * 注册 tag 扩展
     */
    public void registerTags(String className) {
        Class<?> cls = resolveClass(className);
        if (cls == null) {
            throw new TemplateException("@JetTags class not found: " + className);
        }
        tagResolver.register(cls);
    }

    /**
     * 注册 tag 扩展
     */
    public void registerTags(Class<?> cls) {
        tagResolver.register(cls);
    }

    /**
     * 注册 tag 扩展
     */
    public void registerTag(MethodInfo method) {
        tagResolver.register(method);
    }

    /**
     * 注册全局宏 loadmacro
     */
    public void registerMacros(JetTemplate template) {
        macroResolver.register(template);
    }

    //-----------------------------------------------------------------

    /**
     * 根据名称，查找 class 定义
     */
    public Class<?> resolveClass(String name) {
        return classResolver.resolveClass(name);
    }

    /**
     * 根据参数类型，查找一个匹配的方法
     */
    public MethodInvoker resolveMethod(Class<?> clazz, String name, Class<?>[] argumentTypes, boolean isStatic) {
        return methodInvokerResolver.resolve(clazz, name, argumentTypes, isStatic);
    }

    /**
     * 根据参数类型，查找一个匹配的函数
     */
    public FunctionInvoker resolveFunction(String name, Class<?>[] argumentTypes) {
        return functionResolver.resolve(name, argumentTypes);
    }

    /**
     * 根据参数类型，查找一个匹配的 Tag
     */
    public TagInvoker resolveTag(String name, Class<?>[] argumentTypes) {
        return tagResolver.resolve(name, argumentTypes);
    }

    /**
     * 根据参数类型，查找一个匹配的 macro
     */
    public JetTemplateMacro resolveMacro(String name, Class<?>[] argumentTypes) {
        return macroResolver.resolve(name, argumentTypes);
    }

    public Getter resolveGetter(Class<?> objectClass, String name) {
        return GetterResolver.resolve(objectClass, name);
    }
}
