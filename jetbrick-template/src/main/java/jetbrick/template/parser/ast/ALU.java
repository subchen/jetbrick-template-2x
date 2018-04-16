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
package jetbrick.template.parser.ast;

import java.lang.reflect.Array;
import java.util.*;
import jetbrick.template.Errors;

/**
 * Arithmetic Logical Unit
 */
public final class ALU {
    // VOID object
    public static final Object VOID = new Object();

    // number type
    //@formatter:off
    public static final int NaN     = (1 << 8) - 1;
    public static final int DOUBLE  = (1 << 6) - 1;
    public static final int FLOAT   = (1 << 5) - 1;
    public static final int LONG    = (1 << 4) - 1;
    public static final int INTEGER = (1 << 3) - 1;
    public static final int SHORT   = (1 << 2) - 1;
    public static final int BYTE    = (1 << 1) - 1;
    //@formatter:on

    public static int getNumberType(Class<?> cls) {
        if (cls == Integer.class) {
            return INTEGER;
        } else if (cls == Long.class) {
            return LONG;
        } else if (cls == Double.class) {
            return DOUBLE;
        } else if (cls == Float.class) {
            return FLOAT;
        } else if (cls == Short.class) {
            return SHORT;
        } else if (cls == Byte.class) {
            return BYTE;
        }
        return NaN;
    }

    public static int getNumberType(Class<?> c1, Class<?> c2) {
        return getNumberType(c1) | getNumberType(c2);
    }

    // a + b
    public static Object plus(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (c1 == String.class || c2 == String.class) {
            return String.valueOf(o1).concat(String.valueOf(o2));
        }
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return Integer.valueOf(n1.intValue() + n2.intValue());
            case LONG:
                return Long.valueOf(n1.longValue() + n2.longValue());
            case FLOAT:
                return Float.valueOf(n1.floatValue() + n2.floatValue());
            case DOUBLE:
                return Double.valueOf(n1.doubleValue() + n2.doubleValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "+", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a - b
    public static Object minus(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return Integer.valueOf(n1.intValue() - n2.intValue());
            case LONG:
                return Long.valueOf(n1.longValue() - n2.longValue());
            case FLOAT:
                return Float.valueOf(n1.floatValue() - n2.floatValue());
            case DOUBLE:
                return Double.valueOf(n1.doubleValue() - n2.doubleValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "-", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a * b
    public static Object mul(Object o1, Object o2) throws IllegalStateException {
        if (o1 == null || o2 == null) return null;

        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return Integer.valueOf(n1.intValue() * n2.intValue());
            case LONG:
                return Long.valueOf(n1.longValue() * n2.longValue());
            case FLOAT:
                return Float.valueOf(n1.floatValue() * n2.floatValue());
            case DOUBLE:
                return Double.valueOf(n1.doubleValue() * n2.doubleValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "*", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a / b
    public static Object div(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return Integer.valueOf(n1.intValue() / n2.intValue());
            case LONG:
                return Long.valueOf(n1.longValue() / n2.longValue());
            case FLOAT:
                return Float.valueOf(n1.floatValue() / n2.floatValue());
            case DOUBLE:
                return Double.valueOf(n1.doubleValue() / n2.doubleValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "/", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a % b
    public static Object mod(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return Integer.valueOf(n1.intValue() % n2.intValue());
            case LONG:
                return Long.valueOf(n1.longValue() % n2.longValue());
            case FLOAT:
                return Float.valueOf(n1.floatValue() % n2.floatValue());
            case DOUBLE:
                return Double.valueOf(n1.doubleValue() % n2.doubleValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "%", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // +a
    public static Object positive(Object o) throws IllegalStateException {
        if (o instanceof Number) {
            return o;
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_UNARY_UNDEFINED, "+", Errors.typeName(o)));
    }

    // -a
    public static Object negative(Object o) throws IllegalStateException {
        Class<?> cls = o.getClass();
        if (Number.class.isAssignableFrom(cls)) {
            Number n = (Number) o;
            if (cls == Integer.class) {
                return Integer.valueOf(-n.intValue());
            } else if (cls == Long.class) {
                return Long.valueOf(-n.longValue());
            } else if (cls == Double.class) {
                return Double.valueOf(-n.doubleValue());
            } else if (cls == Float.class) {
                return Float.valueOf(-n.floatValue());
            } else if (cls == Short.class) {
                return Integer.valueOf(-n.shortValue());
            } else if (cls == Byte.class) {
                return Integer.valueOf(-n.byteValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_UNARY_UNDEFINED, "-", Errors.typeName(o)));
    }

    //-----------------------------------------------------------------------
    // a & b
    public static Object bitAnd(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return Integer.valueOf(n1.intValue() & n2.intValue());
            case LONG:
                return Long.valueOf(n1.longValue() & n2.longValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "&", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a | b
    public static Object bitOr(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return Integer.valueOf(n1.intValue() | n2.intValue());
            case LONG:
                return Long.valueOf(n1.longValue() | n2.longValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "|", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a ^ b
    public static Object bitXor(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return Integer.valueOf(n1.intValue() ^ n2.intValue());
            case LONG:
                return Long.valueOf(n1.longValue() ^ n2.longValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "^", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // ~a
    public static Object bitNot(Object o) throws IllegalStateException {
        Class<?> cls = o.getClass();
        if (Number.class.isAssignableFrom(cls)) {
            Number n = (Number) o;
            if (cls == Integer.class) {
                return Integer.valueOf(~n.intValue());
            } else if (cls == Long.class) {
                return Long.valueOf(~n.longValue());
            } else if (cls == Short.class) {
                return Integer.valueOf(~n.shortValue());
            } else if (cls == Byte.class) {
                return Integer.valueOf(~n.byteValue());
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_UNARY_UNDEFINED, "~", Errors.typeName(o)));
    }

    //-----------------------------------------------------------------------
    // a << b
    public static Object shl(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            if (c1 == Long.class) {
                if (c2 == Long.class) {
                    return Long.valueOf(n1.longValue() << n2.longValue());
                }
                if (c2 == Integer.class || c2 == Short.class || c2 == Byte.class) {
                    return Long.valueOf(n1.longValue() << n2.intValue());
                }
            } else if (c1 == Integer.class || c1 == Short.class || c1 == Byte.class) {
                if (c2 == Long.class) {
                    return Integer.valueOf(n1.intValue() << n2.longValue());
                }
                if (c2 == Integer.class || c2 == Short.class || c2 == Byte.class) {
                    return Integer.valueOf(n1.intValue() << n2.intValue());
                }
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "<<", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a >> b
    public static Object shr(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            if (c1 == Long.class) {
                if (c2 == Long.class) {
                    return Long.valueOf(n1.longValue() >> n2.longValue());
                }
                if (c2 == Integer.class || c2 == Short.class || c2 == Byte.class) {
                    return Long.valueOf(n1.longValue() >> n2.intValue());
                }
            } else if (c1 == Integer.class || c1 == Short.class || c1 == Byte.class) {
                if (c2 == Long.class) {
                    return Integer.valueOf(n1.intValue() >> n2.longValue());
                }
                if (c2 == Integer.class || c2 == Short.class || c2 == Byte.class) {
                    return Integer.valueOf(n1.intValue() >> n2.intValue());
                }
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, ">>", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a >>> b
    public static Object ushr(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            if (c1 == Long.class) {
                if (c2 == Long.class) {
                    return Long.valueOf(n1.longValue() >>> n2.longValue());
                }
                if (c2 == Integer.class || c2 == Short.class || c2 == Byte.class) {
                    return Long.valueOf(n1.longValue() >>> n2.intValue());
                }
            } else if (c1 == Integer.class || c1 == Short.class || c1 == Byte.class) {
                if (c2 == Long.class) {
                    return Integer.valueOf(n1.intValue() >>> n2.longValue());
                }
                if (c2 == Integer.class || c2 == Short.class || c2 == Byte.class) {
                    return Integer.valueOf(n1.intValue() >>> n2.intValue());
                }
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, ">>>", Errors.typeName(o1), Errors.typeName(o2)));
    }

    //-----------------------------------------------------------------------
    // a == b
    public static Boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return Boolean.TRUE;
        }
        if (o1 == null || o2 == null) {
            return Boolean.FALSE;
        }
        if (o1.equals(o2)) {
            return Boolean.TRUE;
        }
        if (o1 instanceof Number && o2 instanceof Number) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(o1.getClass(), o2.getClass())) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return n1.intValue() == n2.intValue() ? Boolean.TRUE : Boolean.FALSE;
            case LONG:
                return n1.longValue() == n2.longValue() ? Boolean.TRUE : Boolean.FALSE;
            case FLOAT:
                return n1.floatValue() == n2.floatValue() ? Boolean.TRUE : Boolean.FALSE;
            case DOUBLE:
                return n1.doubleValue() == n2.doubleValue() ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    // a > b
    @SuppressWarnings("unchecked")
    public static Object gt(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();

        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return n1.intValue() > n2.intValue() ? Boolean.TRUE : Boolean.FALSE;
            case LONG:
                return n1.longValue() > n2.longValue() ? Boolean.TRUE : Boolean.FALSE;
            case FLOAT:
                return n1.floatValue() > n2.floatValue() ? Boolean.TRUE : Boolean.FALSE;
            case DOUBLE:
                return n1.doubleValue() > n2.doubleValue() ? Boolean.TRUE : Boolean.FALSE;
            }
        }

        if (c1 == c2 && Comparable.class.isAssignableFrom(c1)) {
            return ((Comparable<Object>) o1).compareTo(o2) > 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, ">", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a >= b
    @SuppressWarnings("unchecked")
    public static Object ge(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();

        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return n1.intValue() >= n2.intValue() ? Boolean.TRUE : Boolean.FALSE;
            case LONG:
                return n1.longValue() >= n2.longValue() ? Boolean.TRUE : Boolean.FALSE;
            case FLOAT:
                return n1.floatValue() >= n2.floatValue() ? Boolean.TRUE : Boolean.FALSE;
            case DOUBLE:
                return n1.doubleValue() >= n2.doubleValue() ? Boolean.TRUE : Boolean.FALSE;
            }
        }

        if (c1 == c2 && Comparable.class.isAssignableFrom(c1)) {
            if (c2.isAssignableFrom(c1)) {
                return ((Comparable<Object>) o1).compareTo(o2) >= 0 ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, ">=", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a < b
    @SuppressWarnings("unchecked")
    public static Object lt(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();

        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return n1.intValue() < n2.intValue() ? Boolean.TRUE : Boolean.FALSE;
            case LONG:
                return n1.longValue() < n2.longValue() ? Boolean.TRUE : Boolean.FALSE;
            case FLOAT:
                return n1.floatValue() < n2.floatValue() ? Boolean.TRUE : Boolean.FALSE;
            case DOUBLE:
                return n1.doubleValue() < n2.doubleValue() ? Boolean.TRUE : Boolean.FALSE;
            }
        }

        if (c1 == c2 && Comparable.class.isAssignableFrom(c1)) {
            return ((Comparable<Object>) o1).compareTo(o2) <= 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "<", Errors.typeName(o1), Errors.typeName(o2)));
    }

    // a <= b
    @SuppressWarnings("unchecked")
    public static Object le(Object o1, Object o2) throws IllegalStateException {
        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();

        if (Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            switch (getNumberType(c1, c2)) {
            case BYTE:
            case SHORT:
            case INTEGER:
                return n1.intValue() <= n2.intValue() ? Boolean.TRUE : Boolean.FALSE;
            case LONG:
                return n1.longValue() <= n2.longValue() ? Boolean.TRUE : Boolean.FALSE;
            case FLOAT:
                return n1.floatValue() <= n2.floatValue() ? Boolean.TRUE : Boolean.FALSE;
            case DOUBLE:
                return n1.doubleValue() <= n2.doubleValue() ? Boolean.TRUE : Boolean.FALSE;
            }
        }

        if (c1 == c2 && Comparable.class.isAssignableFrom(c1)) {
            return ((Comparable<Object>) o1).compareTo(o2) <= 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        throw new IllegalStateException(Errors.format(Errors.OPERATION_BINARY_UNDEFINED, "<=", Errors.typeName(o1), Errors.typeName(o2)));
    }

    public static boolean isTrue(Object o) {
        if (o == null) return false;

        Class<?> cls = o.getClass();
        if (cls == Boolean.class) return (Boolean) o;
        if (o instanceof Collection) return !((Collection<?>) o).isEmpty();
        if (o instanceof Map) return !((Map<?, ?>) o).isEmpty();
        if (o instanceof CharSequence) return ((CharSequence) o).length() > 0;
        if (o instanceof Number) return ((Number) o).intValue() != 0;
        if (cls.isArray()) return Array.getLength(o) > 0;
        if (cls == Character.class) return ((Character) o) != '\0';
        if (o instanceof Enumeration) return ((Enumeration<?>) o).hasMoreElements();
        if (o instanceof Iterator) return ((Iterator<?>) o).hasNext();
        if (o instanceof Iterable) return ((Iterable<?>) o).iterator().hasNext();

        return true;
    }
}
