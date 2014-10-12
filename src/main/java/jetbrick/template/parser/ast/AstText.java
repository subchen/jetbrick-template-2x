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
package jetbrick.template.parser.ast;

import java.io.IOException;
import java.nio.charset.Charset;
import jetbrick.bean.FieldInfo;
import jetbrick.bean.KlassInfo;
import jetbrick.template.runtime.*;
import jetbrick.util.JdkUtils;

public final class AstText extends AstStatement {
    // 原始文本
    private final String text;
    private volatile TextEncoder encoder;

    public AstText(String text) {
        this.text = text;
    }

    @Override
    public void execute(InterpretContext ctx) throws InterpretException {
        JetWriter os = ctx.getWriter();

        if (encoder == null) {
            if (os.isStreaming()) {
                encoder = new ByteArrayEncoder(text, os.getCharset());
            } else {
                if (JdkUtils.IS_AT_LEAST_JAVA_7) {
                    encoder = new Jdk7CharArrayEncoder(text);
                } else {
                    encoder = new Jdk7CharArrayEncoder(text);
                }
            }
        }

        try {
            encoder.writeTo(os);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

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
