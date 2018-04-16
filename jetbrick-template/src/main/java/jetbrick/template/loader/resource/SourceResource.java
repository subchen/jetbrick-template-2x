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
package jetbrick.template.loader.resource;

import java.io.InputStream;
import java.nio.charset.Charset;
import jetbrick.io.resource.AbstractResource;
import jetbrick.io.resource.ResourceNotFoundException;

/**
 * 以源码形式存在的资源.
 *
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public final class SourceResource extends AbstractResource {
    public static final String DEFAULT_NAME = "(source)";
    private final String source;

    public SourceResource(String source) {
        this.relativePathName = DEFAULT_NAME;
        this.source = source;
    }

    public SourceResource(String name, String source) {
        this.relativePathName = name;
        this.source = source;
    }

    @Override
    public InputStream openStream() throws ResourceNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public char[] toCharArray(Charset charset) {
        return source.toCharArray();
    }

    @Override
    public long lastModified() {
        return 1000;
    }
}
