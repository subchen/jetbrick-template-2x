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

public final class Tokens {

    // binary operator
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int MUL = 3;
    public static final int DIV = 4;
    public static final int MOD = 5;
    public static final int BIT_AND = 6;
    public static final int BIT_OR = 7;
    public static final int BIT_XOR = 8;
    public static final int BIT_SHL = 9;
    public static final int BIT_SHR = 10;
    public static final int BIT_USHR = 11;
    public static final int LT = 12;
    public static final int LE = 13;
    public static final int GT = 14;
    public static final int GE = 15;
    public static final int EQ = 16;
    public static final int NE = 17;
    public static final int IDENTICALLY_EQUAL = 18;
    public static final int IDENTICALLY_EQUAL_NOT = 19;
    public static final int AND = 20;
    public static final int OR = 21;

    // unary operator
    public static final int BIT_NOT = 22;
    public static final int NOT = 23;

    //@formatter:off
    public static final String[] NAMES = new String[] {
        "<unknown>",
        "+", "-", "*", "/", "%",
        "&", "|", "^", "<<", ">>", ">>>",
        "<", "<=", ">", ">=",
        "==", "!=", "===", "!==",
        "&&", "||",
        "~", "!"
    };
    //@formatter:on

    // block node
    public static final int AST_BLOCK_TEMPLATE = 1;
    public static final int AST_BLOCK_FOR = 2;
    public static final int AST_BLOCK_IF = 3;
    public static final int AST_BLOCK_ELSEIF = 4;
    public static final int AST_BLOCK_ELSE = 5;
    public static final int AST_BLOCK_MACRO = 6;
    public static final int AST_BLOCK_TAG = 7;
    public static final int AST_BLOCK_SET = 8;

}
