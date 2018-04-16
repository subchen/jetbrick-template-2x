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
package jetbrick.template;

import java.io.File;
import java.lang.reflect.*;
import java.security.AccessControlException;
import java.util.*;
import jetbrick.io.IoUtils;
import jetbrick.util.StringUtils;

/*
 * 默认的安全管理器。
 *
 * @author Guoqiang Chen
 */
public final class JetSecurityManagerImpl implements JetSecurityManager {
    private final Set<String> blackList;
    private final Set<String> whiteList;
    private boolean disallowStaticMethod;
    private boolean disallowStaticField;
    private boolean disallowConstructor;

    public JetSecurityManagerImpl() {
        blackList = new HashSet<String>();
        whiteList = new HashSet<String>();
        disallowStaticMethod = false;
        disallowStaticField = false;
        disallowConstructor = false;
    }

    public void setConfigFile(String configFile) {
        String fileContent = IoUtils.toString(new File(configFile), "utf-8");
        String[] lines = StringUtils.split(fileContent, '\n');
        for (String line : lines) {
            addList(line);
        }
    }

    public void setNameList(List<String> nameList) {
        for (String line : nameList) {
            addList(line);
        }
    }

    public void setDisallowConstructor(boolean disallowConstructor) {
        this.disallowConstructor = disallowConstructor;
    }

    public void setDisallowStaticMethod(boolean disallowStaticMethod) {
        this.disallowStaticMethod = disallowStaticMethod;
    }

    public void setDisallowStaticField(boolean disallowStaticField) {
        this.disallowStaticField = disallowStaticField;
    }

    private void addList(String line) {
        line = line.trim();
        if (line.length() == 0) {
            return;
        }
        char c = line.charAt(0);
        if (c == '+') {
            whiteList.add(line.substring(1));
        } else if (c == '-') {
            blackList.add(line.substring(1));
        } else {
            whiteList.add(line);
        }
    }

    /**
     * 是否允许进行访问.
     *
     * <p>
     * 其中参数 name 的格式.
     * <ul>
     * <li>java.lang：整个 package</li>
     * <li>java.lang.String：整个 Class</li>
     * <li>java.lang.String.&lt;init&gt;：构造函数</li>
     * <li>java.lang.Integer.MAX_VALUE ：字段</li>
     * <li>java.lang.Integer.valueOf：方法</li>
     * </ul>
     * </p>
     * <p>默认是白名单，前面加 + 代表白名单，- 代表黑名单</p>
     */
    private void checkAccess(String fullName) throws AccessControlException {
        String name = fullName;
        do {
            if (whiteList.contains(name)) {
                return;
            }
            if (blackList.contains(name)) {
                throw new AccessControlException("access denied for \'" + fullName + "\' because of \'" + name + "\' is in blacklist.");
            }

            int ipos = name.lastIndexOf('.');
            if (ipos == -1) break;
            name = name.substring(0, ipos);
        } while (true);
    }

    @Override
    public void checkAccess(Class<?> cls) {
        checkAccess(cls.getName());
    }

    @Override
    public void checkAccess(Constructor<?> constructor) throws AccessControlException {
        String fullName = constructor.getDeclaringClass().getName() + ".<init>";
        if (disallowConstructor) {
            throw new AccessControlException("access denied for constructor: " + fullName);
        }
        checkAccess(fullName);
    }

    @Override
    public void checkAccess(Method method) throws AccessControlException {
        String fullName = method.getDeclaringClass().getName() + '.' + method.getName();
        if (disallowStaticMethod && Modifier.isStatic(method.getModifiers())) {
            throw new AccessControlException("access denied for static method: " + fullName);
        }
        checkAccess(fullName);
    }

    @Override
    public void checkAccess(Field field) throws AccessControlException {
        String fullName = field.getDeclaringClass().getName() + '.' + field.getName();
        if (disallowStaticField && Modifier.isStatic(field.getModifiers())) {
            throw new AccessControlException("access denied for static field: " + fullName);
        }
        checkAccess(fullName);
    }

}
