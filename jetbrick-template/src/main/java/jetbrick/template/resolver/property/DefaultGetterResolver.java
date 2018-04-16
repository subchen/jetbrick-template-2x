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
package jetbrick.template.resolver.property;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jetbrick.bean.*;
import jetbrick.template.resolver.SignatureUtils;

/**
 * 全局用于查找 obj.name 访问
 */
public final class DefaultGetterResolver implements GetterResolver {
    private final ConcurrentMap<String, Getter> cache = new ConcurrentHashMap<String, Getter>(256);
    private List<GetterResolver> resolvers;

    /**
     * 注册自定义的 resolver
     */
    public void register(GetterResolver resolver) {
        if (resolvers == null) {
            resolvers = new ArrayList<GetterResolver>(4);
        }
        resolvers.add(resolver);
    }

    /**
     * 查找属性访问
     */
    @Override
    public Getter resolve(Class<?> clazz, String name) {
        String signature = SignatureUtils.getFieldSignature(clazz, name);
        Getter found = cache.get(signature);
        if (found != null) {
            return found;
        }

        Getter getter = doGetGetter(clazz, name);

        if (getter != null) {
            cache.put(signature, getter);
            return getter;
        }

        return null;
    }

    /**
     * 查找属性访问
     */
    private Getter doGetGetter(Class<?> clazz, String name) {
        // array.length
        if ("length".equals(name) && clazz.isArray()) {
            return ArrayLengthGetter.INSTANCE;
        }

        KlassInfo klass = KlassInfo.create(clazz);

        // getXXX() or isXXX()
        PropertyInfo property = klass.getProperty(name);
        if (property != null && property.readable()) {
            return new PropertyGetter(property);
        }

        // object.field (only public)
        FieldInfo field = klass.getField(name);
        if (field != null && field.isPublic()) {
            return new FieldGetter(field);
        }

        // map.key
        if (Map.class.isAssignableFrom(clazz)) {
            return new MapGetter(name);
        }

        // get from custom resolver
        if (resolvers != null) {
            for (GetterResolver resolver : resolvers) {
                Getter getter = resolver.resolve(clazz, name);
                if (getter != null) {
                    return getter;
                }
            }
        }

        return null;
    }

}
