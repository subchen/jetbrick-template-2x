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

import jetbrick.template.Errors;
import jetbrick.template.TemplateException;
import jetbrick.template.parser.ast.Position;

/**
 * 模板语法错误.
 */
public final class SyntaxException extends TemplateException {
    private static final long serialVersionUID = 1L;

    private Source source;
    private Position position;

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(String message, Object... args) {
        super(Errors.format(message, args));
    }

    public SyntaxException(Throwable cause) {
        super(cause);
    }

    public SyntaxException cause(Throwable cause) {
        initCause(cause);
        return this;
    }

    public SyntaxException set(Source source) {
        this.source = source;
        return this;
    }

    public SyntaxException set(Position position) {
        this.position = position;
        return this;
    }

    @Override
    public String getMessage() {
        return Errors.format(super.getMessage(), source, position);
    }
}
