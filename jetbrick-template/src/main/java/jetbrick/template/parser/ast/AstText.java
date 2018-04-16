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

import java.io.IOException;
import java.nio.charset.Charset;
import jetbrick.bean.FieldInfo;
import jetbrick.bean.KlassInfo;
import jetbrick.template.runtime.*;
import jetbrick.util.JdkUtils;

public final class AstText extends AstStatement {
    private String text;
    private volatile TextEncoder encoder;
    private int line;

    public AstText(String text, int line) {
        this.text = text;
        this.line = line;
    }

    public String getText() {
        return text;
    }

    public int getLine() {
        return line;
    }

    // --------------------------------------------------------------------
    protected boolean isEmpty() {
        return text == null || text.length() == 0;
    }

    protected void trimDirectiveWhitespaces(boolean trimLeft, boolean trimRight, boolean keepLeftNewLine) {
        if (text == null || text.length() == 0) {
            return;
        }

        int len = text.length();

        int lpos = 0;
        boolean trimedNewLine = false; // 是否删除了一个 new line
        if (trimLeft) {
            for (int i = 0; i < len; i++) {
                char c = text.charAt(i);
                if (c == ' ' || c == '\t') {
                    continue;
                } else if (c == '\r') {
                    if (keepLeftNewLine) {
                        lpos = i;
                        break;
                    } else {
                        trimedNewLine = true;
                        int n = i + 1;
                        if (n < len && text.charAt(n) == '\n') {
                            lpos = n + 1; // window: \r\n
                        } else {
                            lpos = n; // mac: \r
                        }
                        break;
                    }
                } else if (c == '\n') {
                    if (keepLeftNewLine) {
                        lpos = i;
                    } else {
                        trimedNewLine = true;
                        lpos = i + 1; // linux: \n
                    }
                    break;
                } else {
                    break;
                }
            }
        }

        int rpos = len;
        if (trimRight) {
            for (int i = len - 1; i >= 0; i--) {
                char c = text.charAt(i);
                if (c == ' ' || c == '\t') {
                    continue;
                } else if (c == '\n' || c == '\r') {
                    rpos = i + 1;
                    break;
                } else {
                    break;
                }
            }
        }

        if (lpos < rpos) {
            text = text.substring(lpos, rpos);
        } else {
            text = null;
        }

        if (trimedNewLine) {
            line++;
        }
    }

    protected void trimDirectiveComments(boolean trimLeft, boolean trimRight, String prefix, String suffix) {
        if (text == null || text.length() == 0) {
            return;
        }

        int text_len = text.length();
        int prefix_len = prefix.length();
        int suffix_len = suffix.length();

        int lpos = 0;
        if (trimLeft) {
            for (int i = 0; i < text_len; i++) {
                char c = text.charAt(i);
                if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                    continue;
                } else {
                    boolean matched = true;
                    int j = 0;
                    while (i < text_len && j < suffix_len) {
                        if (text.charAt(i) != suffix.charAt(j)) {
                            matched = false;
                            break;
                        }
                        i++;
                        j++;
                    }
                    if (matched) {
                        lpos = i;
                    }
                    break;
                }
            }
        }

        int rpos = text_len;
        if (trimRight) {
            for (int i = text_len - 1; i >= 0; i--) {
                char c = text.charAt(i);
                if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                    continue;
                } else {
                    boolean matched = true;
                    int j = prefix_len - 1;
                    while (i >= 0 && j >= 0) {
                        if (text.charAt(i) != prefix.charAt(j)) {
                            matched = false;
                            break;
                        }
                        i--;
                        j--;
                    }
                    if (matched) {
                        rpos = i + 1;
                    }
                    break;
                }
            }
        }

        if (lpos < rpos) {
            text = text.substring(lpos, rpos);
        } else {
            text = null;
        }
    }

    protected void trimLastNewLine() {
        if (text == null || text.length() == 0) {
            return;
        }

        int length = text.length();
        if (text.charAt(length - 1) == '\n') {
            length--;
            if (length > 0 && text.charAt(length - 1) == '\r') {
                length--;
            }
        }
        text = text.substring(0, length);
    }

    // --------------------------------------------------------------------

    @Override
    public void execute(InterpretContext ctx) throws InterpretException {
        JetWriter os = ctx.getWriter();

        if (encoder == null) {
            synchronized (this) {
                // double check
                if (encoder == null) {
                    if (os.isStreaming()) {
                        encoder = new ByteArrayEncoder(text, os.getCharset());
                    } else {
                        if (JdkUtils.IS_AT_LEAST_JAVA_7) {
                            encoder = new Jdk7CharArrayEncoder(text);
                        } else {
                            encoder = new Jdk6CharArrayEncoder(text);
                        }
                    }
                    text = null; // free
                }
            }
        }

        try {
            encoder.writeTo(os);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return text;
    }

    // --------------------------------------------------------------------

    static interface TextEncoder {
        public void writeTo(JetWriter os) throws IOException;
    }

    static final class ByteArrayEncoder implements TextEncoder {
        private final byte[] bytes;

        public ByteArrayEncoder(String text, Charset charset) {
            this.bytes = text.getBytes(charset);
        }

        @Override
        public void writeTo(JetWriter os) throws IOException {
            os.print(bytes, 0, bytes.length);
        }
    }

    // 从 JDK7 开始，String.class 不在提供 offset 和 count
    static final KlassInfo KLASS_STRING = KlassInfo.create(String.class);
    static final FieldInfo FIELD_STRING_VALUE = KLASS_STRING.getDeclaredField("value");
    static final FieldInfo FIELD_STRING_OFFSET = KLASS_STRING.getDeclaredField("offset");
    static final FieldInfo FIELD_STRING_COUNT = KLASS_STRING.getDeclaredField("count");

    static final class Jdk7CharArrayEncoder implements TextEncoder {
        final char[] chars;

        public Jdk7CharArrayEncoder(String text) {
            this.chars = (char[]) FIELD_STRING_VALUE.get(text);
        }

        @Override
        public void writeTo(JetWriter os) throws IOException {
            os.print(chars, 0, chars.length);
        }
    }

    static final class Jdk6CharArrayEncoder implements TextEncoder {
        final char[] chars;
        final int offset;
        final int count;

        public Jdk6CharArrayEncoder(String text) {
            this.chars = (char[]) FIELD_STRING_VALUE.get(text);
            this.offset = (Integer) FIELD_STRING_OFFSET.get(text);
            this.count = (Integer) FIELD_STRING_COUNT.get(text);
        }

        @Override
        public void writeTo(JetWriter os) throws IOException {
            os.print(chars, offset, count);
        }
    }

}
