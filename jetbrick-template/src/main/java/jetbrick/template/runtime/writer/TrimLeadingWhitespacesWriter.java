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
package jetbrick.template.runtime.writer;

import java.io.IOException;
import java.io.Writer;
import jetbrick.template.runtime.OriginalStream;

public final class TrimLeadingWhitespacesWriter extends Writer implements OriginalStream {
    private final Writer out;
    private boolean first;

    public TrimLeadingWhitespacesWriter(Writer out) {
        this.out = out;
        this.first = true;
    }

    @Override
    public Object getOriginStream() {
        return out;
    }

    @Override
    public void write(int c) throws IOException {
        if (first) {
            if (c <= 32) { // Character.isWhitespace(b)
                return;
            }
            first = false;
        }
        out.write(c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (first) {
            if ((off | len | (cbuf.length - (len + off)) | (off + len)) < 0) {
                throw new IndexOutOfBoundsException();
            }

            int max = off + len;
            while (off < max) {
                if (cbuf[off] <= 32) { // Character.isWhitespace(b)
                    off++;
                } else {
                    first = false;
                    break;
                }
            }

            len = max - off;
            if (len == 0) {
                return;
            }
        }

        out.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
