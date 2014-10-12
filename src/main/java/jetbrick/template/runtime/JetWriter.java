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
package jetbrick.template.runtime;

import java.io.*;
import java.nio.charset.Charset;
import jetbrick.template.runtime.writer.*;

/**
 * 负责模板输出.
 */
public abstract class JetWriter implements Closeable, Flushable {

    public static JetWriter create(Writer os, Charset charset, boolean trimLeadingWhitespaces, boolean skipErrors) {
        if (skipErrors || os instanceof PrintWriter) {
            if (trimLeadingWhitespaces) {
                os = new TrimLeadingWhitespacesWriter(os);
            }
            return new JetWriterPrinter(os, charset);
        } else {
            if (trimLeadingWhitespaces) {
                os = new TrimLeadingWhitespacesWriter(os);
            }
            return new JetWriterPrinterEx(os, charset);
        }
    }

    public static JetWriter create(OutputStream os, Charset charset, boolean trimLeadingWhitespaces, boolean skipErrors) {
        if (skipErrors || os instanceof PrintStream) {
            if (trimLeadingWhitespaces) {
                os = new TrimLeadingWhitespacesOutputStream(os);
            }
            return new JetOutputStreamPrinter(os, charset);
        } else {
            if (trimLeadingWhitespaces) {
                os = new TrimLeadingWhitespacesOutputStream(os);
            }
            return new JetOutputStreamPrinterEx(os, charset);
        }
    }

    /**
     * 是 OutputStream 还是 Writer
     */
    public abstract boolean isStreaming();

    public abstract Charset getCharset();

    public abstract void print(int x) throws IOException;

    public abstract void print(byte x[]) throws IOException;

    public abstract void print(byte x[], int offset, int length) throws IOException;

    public abstract void print(char x[]) throws IOException;

    public abstract void print(char x[], int offset, int length) throws IOException;

    public abstract void print(String x) throws IOException;

    public final void print(Object x) throws IOException {
        if (x != null) {
            if (x instanceof byte[]) {
                print((byte[]) x);
            } else if (x instanceof char[]) {
                print((char[]) x);
            } else {
                print(x.toString());
            }
        }
    }

    @Override
    public abstract void flush() throws IOException;

    @Override
    public abstract void close() throws IOException;

}
