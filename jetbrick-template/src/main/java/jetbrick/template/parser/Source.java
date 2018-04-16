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
package jetbrick.template.parser;

/**
 * 表示一个模板源代码，主要给 Exception 的时候，显示详细的错误信息.
 */
public final class Source {
    private static final String REGEX_NEWLINE_SPLIT = "\\r?\\n|\\r";

    private final String filename;
    private final String[] lines;
    private char[] contents;

    public Source(String filename, char[] contents) {
        this.filename = filename;
        this.lines = new String(contents).split(REGEX_NEWLINE_SPLIT);
        this.contents = contents;
    }

    public String getFilename() {
        return filename;
    }

    // 只能使用一次哦
    public char[] getContents() {
        char[] c = contents;
        if (c != null) {
            contents = null; // clear it
            return c;
        }
        throw new UnsupportedOperationException();
    }

    public String[] getLines() {
        return lines;
    }
}
