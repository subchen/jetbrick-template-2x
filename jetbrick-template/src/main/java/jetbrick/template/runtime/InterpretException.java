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
package jetbrick.template.runtime;

import jetbrick.template.Errors;
import jetbrick.template.TemplateException;
import jetbrick.template.parser.Source;
import jetbrick.template.parser.ast.Position;

/**
 * 模板运行时异常.
 */
public final class InterpretException extends TemplateException {
    private static final long serialVersionUID = 1L;

    private Source source;
    private Position position;

    public InterpretException(String message) {
        super(message);
    }

    public InterpretException(String message, Object... args) {
        super(Errors.format(message, args));
    }

    public InterpretException(Throwable cause) {
        super(cause);
    }

    public InterpretException cause(Throwable cause) {
        initCause(cause);
        return this;
    }

    public InterpretException set(Source source) {
        if (this.source == null) {
            this.source = source;
        }
        return this;
    }

    public InterpretException set(Position position) {
        this.position = position;
        return this;
    }

    @Override
    public String getMessage() {
        return Errors.format(super.getMessage(), source, position);
    }
}
