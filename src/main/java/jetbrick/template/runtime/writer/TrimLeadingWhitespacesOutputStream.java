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
package jetbrick.template.runtime.writer;

import java.io.*;

public final class TrimLeadingWhitespacesOutputStream extends FilterOutputStream {
    private boolean first;

    public TrimLeadingWhitespacesOutputStream(OutputStream out) {
        super(out);
        first = true;
    }

    @Override
    public void write(int b) throws IOException {
        if (first) {
            if (b <= 32) { // Character.isWhitespace(b)
                return;
            }
            first = false;
        }
        out.write(b);
    }
}
