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
package jetbrick.template.resolver.clazz;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import jetbrick.io.finder.FileFinder;
import jetbrick.util.ClassLoaderUtils;
import jetbrick.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于查找全局 import 的 class.
 */
public final class ClassResolver {
    private static final Logger log = LoggerFactory.getLogger(ClassResolver.class);

    private final List<String> importedPackageList;
    private final Map<String, Class<?>> importedClassMap;
    private final boolean enableLogger;

    public ClassResolver() {
        this(false);
    }

    public ClassResolver(boolean enableLogger) {
        this.importedPackageList = new ArrayList<String>(8);
        this.importedClassMap = new ConcurrentHashMap<String, Class<?>>(16);
        this.enableLogger = enableLogger;
    }

    public void importClass(String name) {
        // import packages and children
        if (name.endsWith(".**")) {
            if (enableLogger) {
                log.info("import package: {}", name);
            }
            name = name.substring(0, name.length() - 3);
            for (String pkg : getPackages(name)) {
                if (!importedPackageList.contains(pkg)) {
                    importedPackageList.add(pkg);
                    if (enableLogger) {
                        log.info("found package: {}.*", pkg);
                    }
                }
            }
            return;
        }

        // import package
        if (name.endsWith(".*")) {
            name = name.substring(0, name.length() - 2);
            if (!importedPackageList.contains(name)) {
                importedPackageList.add(name);
                if (enableLogger) {
                    log.info("import package: {}.*", name);
                }
            }
            return;
        }

        // import class
        try {
            Class<?> cls = ClassLoaderUtils.loadClassEx(name);
            if (importedClassMap.put(cls.getSimpleName(), cls) == null) {
                if (enableLogger) {
                    log.info("import class: " + cls.getName());
                }
            }
        } catch (ClassNotFoundException e) {
            if (Package.getPackage(name) != null) {
                throw new IllegalStateException("This is a package name, not a class name. You should use `" + name + ".*` for import classes");
            }
            throw ExceptionUtils.unchecked(e);
        }
    }

    public Class<?> resolveClass(final String className) {
        Class<?> cls = importedClassMap.get(className);
        if (cls != null) {
            return cls;
        }

        cls = ClassLoaderUtils.loadClass(className);
        if (cls != null) {
            importedClassMap.put(className, cls);
            return cls;
        }

        int lpos = className.indexOf('.');
        if (lpos < 0 || lpos == className.lastIndexOf('.')) {
            String name = className;
            if (lpos > 0) {
                name = className.replace('.', '$'); // 内部类
            }
            for (String pkg : importedPackageList) {
                cls = ClassLoaderUtils.loadClass(pkg + '.' + name);
                if (cls != null) {
                    importedClassMap.put(className, cls);
                    return cls;
                }
            }
        }

        return null;
    }

    private static List<String> getPackages(String packageName) {
        final List<String> results = new ArrayList<String>();
        results.add(packageName);

        FileFinder finder = new FileFinder() {
            @Override
            protected boolean visitDirectory(ResourceEntry dir) {
                results.add(dir.getQualifiedJavaName());
                return true;
            }
        };
        finder.lookupClasspath(Arrays.asList(packageName), true);

        return results;
    }

}
