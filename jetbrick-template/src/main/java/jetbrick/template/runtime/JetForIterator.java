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
package jetbrick.template.runtime;

import java.lang.reflect.Array;
import java.util.*;
import jetbrick.collection.iterator.*;

/**
 * 提供给 #for指令用的内部 Iterator 包装器
 */
public final class JetForIterator implements Iterator<Object> {
    protected final Iterator<?> iterator;
    protected final int size;
    protected int index; // index start by 1
    protected JetForIterator outer;

    public JetForIterator(Object items) {
        if (items == null) {
            iterator = EmptyIterator.INSTANCE;
            size = 0;
        } else if (items instanceof Iterator) {
            if (items instanceof LoopIterator) {
                iterator = (Iterator<?>) items;
                size = ((LoopIterator) items).size();
            } else {
                List<?> list = asList((Iterator<?>) items);
                iterator = list.iterator();
                size = list.size();
            }
        } else if (items instanceof Iterable) {
            if (items instanceof Collection) {
                iterator = ((Iterable<?>) items).iterator();
                size = ((Collection<?>) items).size();
            } else {
                List<?> list = asList(((Iterable<?>) items).iterator());
                iterator = list.iterator();
                size = list.size();
            }
        } else if (items instanceof Map) {
            iterator = ((Map<?, ?>) items).entrySet().iterator();
            size = ((Map<?, ?>) items).size();
        } else if (items instanceof Enumeration) {
            ArrayList<?> list = Collections.list((Enumeration<?>) items);
            iterator = list.iterator();
            size = list.size();
        } else if (items.getClass().isArray()) {
            iterator = new ArrayIterator(items);
            size = Array.getLength(items);
        } else if ((items instanceof Class) && ((Class<?>) items).isEnum()) {
            List<?> list = Arrays.asList(((Class<?>) items).getEnumConstants());
            iterator = list.iterator();
            size = list.size();
        } else {
            iterator = Collections.singleton(items).iterator();
            size = 1;
        }

        this.index = 0;
    }

    private List<?> asList(Iterator<?> it) {
        List<Object> list = new ArrayList<Object>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        Object value = iterator.next();
        index++;
        return value;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean empty() {
        return size == 0;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }

    public boolean isFirst() {
        return index == 1;
    }

    public boolean isLast() {
        return !iterator.hasNext();
    }

    public boolean isOdd() {
        return index % 2 != 0;
    }

    public boolean isEven() {
        return index % 2 == 0;
    }

    public void setOuter(JetForIterator outer) {
        this.outer = outer;
    }

    public JetForIterator getOuter() {
        return outer;
    }
}
