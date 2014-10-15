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

import java.util.List;
import jetbrick.template.parser.ParserContext;
import jetbrick.template.runtime.InterpretContext;

public final class AstStatementList extends AstStatement {
    private static final AstStatement[] EMPTY_ARRAY = new AstStatement[0];
    private final AstStatement[] statements;

    public AstStatementList(List<AstStatement> statements, int block, ParserContext ctx) {
        if (statements == null || statements.isEmpty()) {
            this.statements = EMPTY_ARRAY;
        } else {
            if (statements.size() > 0 && block != Tokens.AST_BLOCK_SET) {
                // 注意： 这里直接修改的是 statements 本身
                ListIterator<AstStatement> it = new ListIterator<AstStatement>(statements);
                splitStatementListChilldren(it);
                combinedContinuousTexts(it);
                trimDirectiveWhitespacesAndComments(it, block, ctx);
            }
            this.statements = statements.toArray(EMPTY_ARRAY);
        }
    }

    // 优化 - 分解 AstStatementList 子节点 (主要是 #set 产生的)
    private void splitStatementListChilldren(ListIterator<AstStatement> it) {
        it.reset();
        while (it.has()) {
            AstStatement statment = it.peek();
            if (statment instanceof AstStatementList) {
                it.remove();
                it.addAll(((AstStatementList) statment).statements);
            } else {
                it.move();
            }
        }
    }

    // 优化 - 合并连续的 AstText 节点
    private void combinedContinuousTexts(ListIterator<AstStatement> it) {
        StringBuilder sb = null;

        it.reset();
        while (it.has()) {
            AstStatement stmt = it.peek();
            if (!(stmt instanceof AstText)) {
                it.move();
                continue;
            }
            if (it.hasNext()) {
                AstStatement next = it.peek(1);
                if (!(next instanceof AstText)) {
                    it.move(2);
                    continue;
                }

                if (sb == null) {
                    sb = new StringBuilder(256);
                } else {
                    sb.setLength(0);
                }

                sb.append(((AstText) stmt).getText());
                it.remove();

                sb.append(((AstText) next).getText());
                it.remove();

                while (it.has()) {
                    next = it.peek();
                    if (next instanceof AstText) {
                        sb.append(((AstText) next).getText());
                        it.remove();
                    } else {
                        break;
                    }
                }

                it.add(new AstText(sb.toString(), ((AstText) stmt).getLineStart()));
            }

            it.move();
        }
    }

    // trimDirectiveWhitespaces & trimDirectiveComments
    private void trimDirectiveWhitespacesAndComments(ListIterator<AstStatement> it, int block, ParserContext ctx) {
        boolean trimDirectiveWhitespaces = ctx.isTrimDirectiveWhitespaces();
        boolean trimDirectiveComments = ctx.isTrimDirectiveComments();
        String trimDirectiveCommentsPrefix = ctx.getTrimDirectiveCommentsPrefix();
        String trimDirectiveCommentsSuffix = ctx.getTrimDirectiveCommentsSuffix();

        if (!trimDirectiveWhitespaces && !trimDirectiveComments) {
            return;
        }

        it.reset();
        while (it.has()) {
            AstStatement stmt = it.peek();
            if (!(stmt instanceof AstText)) {
                it.move();
                continue;
            }

            AstText text = (AstText) stmt;

            boolean trimLeft;
            boolean keepLeftNewLine = false;
            if (it.hasPrevious()) {
                trimLeft = (it.peek(-1) instanceof AstDirective);
                if (trimLeft) {
                    // inline directive, 对于一个内联的 #if, #for, 等指令，后面有要求保留一个 NewLine
                    // @see https://github.com/subchen/jetbrick-template/issues/25
                    int offset = -1;
                    while (it.has(offset)) {
                        AstStatement prev = it.peek(offset);
                        if (prev instanceof AstText) {
                            keepLeftNewLine = ((AstText) prev).getLineStop() == text.getLineStart();
                            break;
                        }
                        offset--;
                    }
                }
            } else {
                trimLeft = (block != Tokens.AST_BLOCK_TEMPLATE);
            }

            boolean trimRight;
            if (it.hasNext()) {
                trimRight = (it.peek(1) instanceof AstDirective);
            } else {
                trimRight = (block != Tokens.AST_BLOCK_TEMPLATE);
            }

            // trim 指令两边的注释
            if (trimDirectiveComments) {
                text.trimDirectiveComments(trimLeft, trimRight, trimDirectiveCommentsPrefix, trimDirectiveCommentsSuffix);
            }
            // trim 指令两边的空白内容
            if (trimDirectiveWhitespaces) {
                text.trimDirectiveWhitespaces(trimLeft, trimRight, keepLeftNewLine);
            }

            // trim 掉 #tag 和 #macro 指令最后一个多余的 '\n'
            if (!it.hasNext()) {
                if (block == Tokens.AST_BLOCK_TAG || block == Tokens.AST_BLOCK_MACRO) {
                    text.trimLastNewLine();
                }
            }

            if (text.isEmpty()) {
                it.remove();
            } else {
                it.move();
            }
        }
    }

    @Override
    public void execute(InterpretContext ctx) {
        for (AstStatement stmt : statements) {
            stmt.execute(ctx);

            // 处理 break, continue, return, stop 语句
            if (ctx.getSignal() != InterpretContext.SINGAL_NONE) {
                return;
            }
        }
    }

    // --------------------------------------------------------------------

    static final class ListIterator<T> {
        private final List<T> list;
        private int index;
        private int size;

        public ListIterator(List<T> list) {
            this.list = list;
            this.index = 0;
            this.size = list.size();
        }

        // 当前位置是否存在对象
        public boolean has() {
            return index >= 0 && index < size;
        }

        public boolean hasNext() {
            return index + 1 < size;
        }

        public boolean hasPrevious() {
            return index > 0;
        }

        public boolean has(int offset) {
            int p = index + offset;
            return p >= 0 && p < size;
        }

        // 向下移动一个位置
        public void move() {
            index += 1;
        }

        public void move(int offset) {
            index += offset;
        }

        public void reset() {
            this.index = 0;
            this.size = list.size();
        }

        // 获取当前位置对象
        public T peek() {
            return list.get(index);
        }

        public T peek(int offset) {
            return list.get(index + offset);
        }

        // 删除当前位置，并 move 到下一个
        public void remove() {
            list.remove(index);
            size = list.size();
        }

        // 在当前位置加入，同时指针还是指向原来的那个对象
        public void add(T item) {
            list.add(index++, item);
            size = list.size();
        }

        public void addAll(T[] items) {
            for (T item : items) {
                list.add(index++, item);
            }
            size = list.size();
        }
    }

}
