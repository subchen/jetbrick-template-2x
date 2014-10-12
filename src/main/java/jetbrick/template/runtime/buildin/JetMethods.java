/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.template.runtime.buildin;

import java.text.DecimalFormat;
import java.util.Date;
import jetbrick.util.*;

public final class JetMethods {

    //---- json ----------------------------------------------------------

    public static String toJson(Object object) {
        return JSONUtils.toJSONString(object);
    }

    //---- format -------------------------------------------------------

    public static String format(Number value) {
        return format(value, "###,##0.00");
    }

    public static String format(Number value, String format) {
        return new DecimalFormat(format).format(value);
    }

    public static String format(Date value) {
        return DateUtils.format(value, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date value, String format) {
        return DateUtils.format(value, format);
    }

    //---- String -------------------------------------------------------
    public static String capitalize(String s) {
        return IdentifiedNameUtils.capitalize(s);
    }

    public static String toUnderlineName(String s) {
        return IdentifiedNameUtils.toUnderlineName(s);
    }

    public static String toCamelCase(String s) {
        return IdentifiedNameUtils.toCamelCase(s);
    }

    public static String toCapitalizeCamelCase(String s) {
        return IdentifiedNameUtils.toCapitalizeCamelCase(s);
    }

    //---- String escape -------------------------------------------------------

    public static String escapeJava(String value) {
        return StringEscapeUtils.escapeJava(value);
    }

    public static String unescapeJava(String value) {
        return StringEscapeUtils.unescapeJava(value);
    }

    public static String escapeJavaScript(String value) {
        return StringEscapeUtils.escapeJavaScript(value);
    }

    public static String unescapeJavaScript(String value) {
        return StringEscapeUtils.unescapeJavaScript(value);
    }

    public static String escapeXml(String value) {
        return StringEscapeUtils.escapeXml(value);
    }

    public static String unescapeXml(String value) {
        return StringEscapeUtils.unescapeXml(value);
    }

    public static String escapeUrl(String value) {
        return StringEscapeUtils.escapeUrl(value);
    }

    public static String escapeUrl(String value, String encoding) {
        return StringEscapeUtils.escapeUrl(value, encoding);
    }

    public static String unescapeUrl(String value) {
        return StringEscapeUtils.unescapeUrl(value);
    }

    public static String unescapeUrl(String value, String encoding) {
        return StringEscapeUtils.unescapeUrl(value, encoding);
    }

}
