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

import java.lang.reflect.*;
import java.security.AccessControlException;

/*
 * 用于实现安全管理器。
 *
 * @author Guoqiang Chen
 */
public interface JetSecurityManager {

    public void checkAccess(Class<?> cls) throws AccessControlException;

    public void checkAccess(Constructor<?> constructor) throws AccessControlException;

    public void checkAccess(Method method) throws AccessControlException;

    public void checkAccess(Field field) throws AccessControlException;

}
