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
package jetbrick.template;

import jetbrick.template.parser.Source;
import jetbrick.template.parser.ast.Position;
import jetbrick.util.StringUtils;

public final class Errors {
    public static final String JETX_ERROR_SOURCE_DISABLED = "JETX_ERROR_SOURCE_DISABLED";

    // syntax error
    public static final String OPTION_NAME_INVALID = "#option name is unknown: %s";
    public static final String OPTION_VALUE_INVALID = "#option value is invalid: %s";
    public static final String VAR_UNDEFINED = "Undefined variable found in strict mode: %s";
    public static final String VAR_REDEFINE = "Cannot redefine variable: %s";
    public static final String PARAM_TYPE_MISSING = "Missing argument type in strict mode: %s";
    public static final String INCLUDE_PARAM_MORE = "#include cannot accept more than 3 arguments";
    public static final String RETURN_NAME_INVALID = "Parameter `returnName` is invalid";
    public static final String DIRECTIVE_PARAM_MISSING = "Missing arguments for directive %s";
    public static final String UNKNOWN_OP_UNARY = "Unknown unary operator: %s";
    public static final String UNKNOWN_OP_BINARY = "Unknown binary operator: %s";
    public static final String UNKNOWN_TOKEN = "Unexpected token type: %s";
    public static final String ID_IS_KEYWORD = "Syntax error on token `%s`, It is a reserved/keyword identifier";
    public static final String ID_IS_RESERVED = "Local variable `%s` cannot start with '$', it is a reserved identifier";
    public static final String DIRECTIVE_OUTSIDE_FOR = "`%s` cannot be used outside of `#for(...)`";
    public static final String INVALID_UNICODE_STRING = "Invalid unicode in String";
    public static final String DUPLICATED_MACRO_NAME = "Duplicated macro name: %s";

    // interpret error
    public static final String CLASS_NOT_FOUND = "Cannot resolve class: %s";
    public static final String CTOR_NOT_FOUND = "Cannot resolve constructor: %s";
    public static final String METHOD_NOT_FOUND = "Cannot resolve method: %s";
    public static final String METHOD_STATIC_NOT_FOUND = "Cannot resolve static method: %s";
    public static final String FIELD_STATIC_NOT_FOUND = "Cannot resolve static field: %s";
    public static final String PROPERTY_NOT_FOUND = "Cannot resolve property: %s";
    public static final String FUNCTION_NOT_FOUND = "Cannot resolve function: %s";
    public static final String TAG_NOT_FOUND = "Cannot resolve tag: %s";
    public static final String OBJECT_IS_NULL = "object is null: %s";
    public static final String INDEX_IS_NULL = "index is null: %s";
    public static final String TYPE_INCONSISTENT = "Inconsistent class for variable: %s";
    public static final String OP_UNARY_UNDEFINED = "The operator `%s` is undefined for the argument type: %s";
    public static final String OP_BINARY_UNDEFINED = "The operator `%s` is undefined for the argument type(s): %s, %s";
    public static final String OP_LHS_IS_NULL = "Left operand is null";
    public static final String OP_RHS_IS_NULL = "Right operand is null";
    public static final String ARRAY_LEN_NULL = "Array length is null";
    public static final String PARAM_NOT_INT = "Type mismatch: the arguments[%s] is not an Integer";
    public static final String PARAM_IS_NULL = "The %s argument is null";
    public static final String TYPE_MISMATCH = "Type mismatch: the %s argument cannot convert from %s to %s";
    public static final String METHOD_INVOKE_ERROR = "Method invoke error: %s";
    public static final String STATIC_METHOD_INVOKE_ERROR = "Method invoke error: %s#%s";
    public static final String FUNCTION_INVOKE_ERROR = "Function invoke error: %s";
    public static final String NEW_OBJECT_ERROR = "New object error: %s";
    public static final String STATIC_FIELD_GET_ERROR = "Static field get error: %s#%s";
    public static final String PROPERTY_GET_ERROR = "Property get error: %s";

    // 判断是否是因为参数不匹配导致的错误
    public static boolean isReflectArgumentNotMatch(IllegalArgumentException e) {
        String className = e.getStackTrace()[0].getClassName();
        //@formatter:off
        return "sun.reflect.NativeMethodAccessorImpl".equals(className)
            || "sun.reflect.GeneratedMethodAccessor1".equals(className)
            || className.startsWith("jetbrick.bean.asm.delegate.");
        //@formatter:on
    }

    public static String typeName(Object object) {
        if (object == null) {
            return "<null>";
        } else {
            return StringUtils.removeStart(object.getClass().getName(), "java.lang.");
        }
    }

    public static String format(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        return String.format(message, args);
    }

    public static String format(String message, Source source, Position position) {
        if (source == null || position == null) {
            return message;
        }

        // 允许用户禁止输出模板源码
        if ("true".equals(System.getProperty(JETX_ERROR_SOURCE_DISABLED))) {
            return message;
        }

        StringBuilder sb = new StringBuilder(256);
        sb.append(message);
        sb.append("\n\n");
        sb.append("template: ");
        sb.append(source.getFilename());
        sb.append(": ");
        sb.append(position.getLine());
        sb.append(',');
        sb.append(position.getColumn());
        sb.append('\n');
        sb.append("-------------------------------------------------------------------------------\n");
        markSourceLines(sb, source.getLines(), position.getLine(), position.getColumn());
        sb.append("-------------------------------------------------------------------------------\n");
        return sb.toString();
    }

    // 输出错误对应的模板源码
    static void markSourceLines(StringBuilder sb, String[] sourceLines, int line, int column) {
        int lineStart = Math.max(line - 3, 0);
        int lineEnd = Math.min(line + 2, sourceLines.length);

        for (int i = lineStart; i < lineEnd; i++) {
            String sourceLine = sourceLines[i];

            if (i == line - 1) {
                // 修正 column （可能存在宽字符）
                int origin_column = Math.min(column, sourceLine.length() - 1);
                for (int j = 0; j < origin_column; j++) {
                    char c = sourceLine.charAt(j);
                    if (c == '\t') {
                        column += 3; // 1 个 Tab 变成 4 个空格
                    } else if (c >= '\u2E80' && c <= '\uFE4F') {
                        column++; // 中日韩统一表意文字（CJK Unified Ideographs）
                    }
                }
            }

            sourceLine = sourceLine.replace("\t", "    ");
            sb.append(String.format("%4d: %s%n", Integer.valueOf(i + 1), sourceLine));

            // 插入错误提示符
            if (i == line - 1) {
                sb.append("      ");
                for (int j = 0; j < column; j++) {
                    sb.append(' ');
                }
                sb.append("^ -- here\n");
            }
        }
    }

}
