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

import java.util.List;
import java.util.Map;
import jetbrick.collection.ListMap;
import jetbrick.template.runtime.InterpretContext;

public final class AstConstantMap extends AstExpression {
    private static final AstConstantMapEntry[] EMPTY_ARRAY = new AstConstantMapEntry[0];
    private final AstConstantMapEntry[] entries;

    public AstConstantMap(List<AstConstantMapEntry> entries, Position position) {
        super(position);
        this.entries = entries.toArray(EMPTY_ARRAY);
    }

    @Override
    public Object execute(InterpretContext ctx) {
        Map<String, Object> map = new ListMap<String, Object>(entries.length);
        for (AstConstantMapEntry entry : entries) {
            Map.Entry<String, Object> pair = entry.execute(ctx);
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }
}
