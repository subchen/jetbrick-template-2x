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
package jetbrick.template.parser.ast;

import java.util.HashSet;
import java.util.Set;

public final class Keywords {
    private static final Set<String> keywords;

    static {
        keywords = new HashSet<String>(64);
        keywords.add("abstract");
        keywords.add("continue");
        keywords.add("for");
        keywords.add("new");
        keywords.add("switch");
        keywords.add("assert");
        keywords.add("default");
        keywords.add("if");
        keywords.add("package");
        keywords.add("synchronized");
        keywords.add("boolean");
        keywords.add("do");
        keywords.add("goto");
        keywords.add("private");
        keywords.add("this");
        keywords.add("break");
        keywords.add("double");
        keywords.add("implements");
        keywords.add("protected");
        keywords.add("throw");
        keywords.add("byte");
        keywords.add("else");
        keywords.add("import");
        keywords.add("public");
        keywords.add("throws");
        keywords.add("case");
        keywords.add("enum");
        keywords.add("instanceof");
        keywords.add("return");
        keywords.add("transient");
        keywords.add("catch");
        keywords.add("extends");
        keywords.add("int");
        keywords.add("short");
        keywords.add("try");
        keywords.add("char");
        keywords.add("final");
        keywords.add("interface");
        keywords.add("static");
        keywords.add("void");
        keywords.add("class");
        keywords.add("finally");
        keywords.add("long");
        keywords.add("strictfp");
        keywords.add("volatile");
        keywords.add("const");
        keywords.add("float");
        keywords.add("native");
        keywords.add("super");
        keywords.add("while");
        keywords.add("null");
        keywords.add("true");
        keywords.add("false");
    }

    public static boolean isKeyword(String identifier) {
        return keywords.contains(identifier);
    }
}
