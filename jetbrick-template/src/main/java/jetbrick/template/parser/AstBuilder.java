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

import jetbrick.template.parser.ast.AstTemplate;
import jetbrick.template.parser.ast.Position;
import jetbrick.template.runtime.parser.grammer.JetTemplateLexer;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser;
import org.antlr.v4.runtime.*;

/**
 * 将模板源文件转成 AST
 */
public final class AstBuilder {

    public static AstTemplate create(Source source, ParserContext ctx) {
        char[] contents = source.getContents();
        ANTLRInputStream is = new ANTLRInputStream(contents, contents.length);
        is.name = source.getFilename(); // set source file name, it will be displayed in error report.

        JetTemplateLexer lexer = new JetTemplateLexer(is);
        lexer.removeErrorListeners(); // remove default ConsoleErrorListener
        lexer.addErrorListener(AstErrorListener.instance);

        JetTemplateParser parser = new JetTemplateParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners(); // remove default ConsoleErrorListener
        parser.addErrorListener(AstErrorListener.instance);
        parser.setErrorHandler(new AstErrorStrategy());

        try {
            AstCodeVisitor visitor = new AstCodeVisitor(ctx);
            return visitor.visitTemplate(parser.template());
        } catch (SyntaxException e) {
            // 绑定模板源代码
            throw e.set(source);
        }
    }

    static class AstErrorListener extends BaseErrorListener {
        private static final AstErrorListener instance = new AstErrorListener();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            /*
            CharStream stream;
            if (recognizer instanceof Parser) {
                stream = ((Parser) recognizer).getInputStream().getTokenSource().getInputStream();
            } else if (recognizer instanceof Lexer) {
                stream = ((Lexer) recognizer).getInputStream();
            } else {
                throw new IllegalStateException();
            }

            String input = stream.toString();
            String filename = stream.getSourceName();
            Source source = new Source(filename, input);
            */

            Position position = new Position(line, charPositionInLine);
            throw new SyntaxException(msg).set(position);
        }
    }

    static class AstErrorStrategy extends DefaultErrorStrategy {
        @Override
        public void recover(Parser recognizer, RecognitionException e) {
            /*
            CharStream stream = recognizer.getInputStream().getTokenSource().getInputStream();
            String input = stream.toString();
            String filename = stream.getSourceName();
            Source source = new Source(filename, input);
            */

            int line = e.getOffendingToken().getLine();
            int column = e.getOffendingToken().getCharPositionInLine();
            Position position = new Position(line, column);

            throw new SyntaxException(e).set(position);
        }

        @Override
        public Token recoverInline(Parser recognizer) throws RecognitionException {
            reportMissingToken(recognizer);
            return null;
        }
    }
}
