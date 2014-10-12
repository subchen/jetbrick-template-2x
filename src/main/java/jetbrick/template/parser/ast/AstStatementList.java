/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.parser.ast;

import java.util.ArrayList;
import java.util.List;
import jetbrick.template.runtime.InterpretContext;

public class AstStatementList extends AstStatement {
    private static final AstStatement[] EMPTY_ARRAY = new AstStatement[0];
    private final AstStatement[] statements;

    public AstStatementList(List<AstStatement> statementList) {
        this.statements = optimize(statementList);
    }

    // 优化
    private AstStatement[] optimize(List<AstStatement> statementList) {
        if (statementList == null || statementList.isEmpty()) {
            return EMPTY_ARRAY;
        }

        // 快速查看是否需要优化
        // 1. 存在连续的 AstText 节点需要合并
        // 2. 存在 AstStatementList 需要分解
        boolean optimize = false;
        boolean prevIsText = false;
        for (AstStatement statement : statementList) {
            if (statement instanceof AstText) {
                if (prevIsText) {
                    optimize = true;
                    break;
                }
                prevIsText = true;
            } else if (statement instanceof AstStatementList) {
                optimize = true;
                break;
            } else {
                prevIsText = false;
            }
        }

        if (!optimize) {
            // 无需优化
            return statementList.toArray(EMPTY_ARRAY);
        }

        // 开始正式优化
        List<AstStatement> results = new ArrayList<AstStatement>(statementList.size() + 4);
        int i = 0, length = statementList.size();
        StringBuilder sb = null;
        while (i < length) {
            AstStatement statement = statementList.get(i++);
            if (statement instanceof AstText) {
                // 合并连续的 AstText 节点
                while (i < length) {
                    // look ahead
                    AstStatement next = statementList.get(i++);
                    if (next instanceof AstText) {
                        if (sb == null) {
                            sb = new StringBuilder(256);
                        }
                        if (sb.length() == 0) {
                            sb.append(((AstText) statement).getText());
                        }
                        sb.append(((AstText) next).getText());
                    } else {
                        i--; // push back
                        break;
                    }
                }
                if (sb == null || sb.length() == 0) {
                    if (statement instanceof AstStatementList) {
                        // 分解儿子 AstStatementList
                        for (AstStatement stmt : ((AstStatementList) statement).statements) {
                            results.add(stmt);
                        }
                    } else {
                        results.add(statement);
                    }
                } else {
                    // 正式合并连续的 AstText 节点
                    results.add(new AstText(sb.toString()));
                    sb.setLength(0);
                }
            } else if (statement instanceof AstStatementList) {
                // 分解儿子 AstStatementList
                for (AstStatement stmt : ((AstStatementList) statement).statements) {
                    results.add(stmt);
                }
            } else {
                results.add(statement);
            }
        }

        return results.toArray(EMPTY_ARRAY);
    }

    @Override
    public void execute(InterpretContext ctx) {
        for (AstStatement stmt : statements) {
            stmt.execute(ctx);

            // 处理 break, continue, return, stop 语句
            if (ctx.getSignal() != ALU.SINGAL_NONE) {
                return;
            }
        }
    }
}
