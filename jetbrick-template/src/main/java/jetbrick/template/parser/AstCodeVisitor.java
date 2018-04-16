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

import java.util.*;
import jetbrick.template.Errors;
import jetbrick.template.parser.ast.*;
import jetbrick.template.runtime.parser.grammer.*;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.BlockContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.ConstantContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.DirectiveContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_breakContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_callContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_continueContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_defineContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_define_expressionContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_elseContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_elseifContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_forContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_ifContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_includeContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_invalidContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_macroContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_macro_argumentsContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_optionsContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_options_expressionContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_returnContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_setContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_set_expressionContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_stopContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Directive_tagContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_array_listContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_binary_operatorContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_constantContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_fieldContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_field_staticContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_functionContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_hash_mapContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_identifierContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_index_getContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_instanceofContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_listContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_methodContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_method_staticContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_new_arrayContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_new_objectContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_nullsafe_operatorContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_primaryContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_ternary_operatorContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Expression_unary_operatorContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.Hash_map_entryContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.IdentifierContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.TemplateContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.TextContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.TypeContext;
import jetbrick.template.runtime.parser.grammer.JetTemplateParser.ValueContext;
import jetbrick.util.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;

/**
 * ANTLR 4 的 parse tree 的 visitor. (根据 parse tree 生成 AST)
 */
public final class AstCodeVisitor extends AbstractParseTreeVisitor<AstNode> implements JetTemplateParserVisitor<AstNode> {
    private final ParserContext parseCtx;

    public AstCodeVisitor(ParserContext parseCtx) {
        this.parseCtx = parseCtx;
    }

    @Override
    public AstTemplate visitTemplate(TemplateContext ctx) {
        AstStatementList statements = accept(ctx.getChild(0));
        return new AstTemplate(statements);
    }

    @Override
    public AstStatementList visitBlock(BlockContext ctx) {
        List<AstStatement> statements = accept(ctx.children);

        int block;
        ParseTree parent = ctx.getParent();
        if (parent instanceof TemplateContext) {
            block = Tokens.AST_BLOCK_TEMPLATE;
        } else if (parent instanceof Directive_forContext) {
            block = Tokens.AST_BLOCK_FOR;
        } else if (parent instanceof Directive_ifContext) {
            block = Tokens.AST_BLOCK_IF;
        } else if (parent instanceof Directive_elseifContext) {
            block = Tokens.AST_BLOCK_ELSEIF;
        } else if (parent instanceof Directive_elseContext) {
            block = Tokens.AST_BLOCK_ELSE;
        } else if (parent instanceof Directive_macroContext) {
            block = Tokens.AST_BLOCK_MACRO;
        } else if (parent instanceof Directive_tagContext) {
            block = Tokens.AST_BLOCK_TAG;
        } else {
            throw new UnsupportedOperationException();
        }

        return new AstStatementList(statements, block, parseCtx);
    }

    @Override
    public AstText visitText(TextContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        String text = token.getText();
        switch (token.getType()) {
        case JetTemplateLexer.TEXT_CDATA:
            text = text.substring(3, text.length() - 3);
            break;
        case JetTemplateLexer.TEXT_CHAR_ESCAPED:
            text = text.substring(1);
            break;
        }
        return new AstText(text, token.getLine());
    }

    @Override
    public AstNode visitValue(ValueContext ctx) {
        AstExpression expression = accept(ctx.expression());

        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        if (token.getType() == JetTemplateLexer.VALUE_OPEN_ESCAPED) {
            return new AstValueEscaped(expression);
        } else {
            return new AstValue(expression);
        }
    }

    @Override
    public AstNode visitDirective(DirectiveContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public AstNode visitDirective_options(Directive_optionsContext ctx) {
        accept(ctx.directive_options_expression());
        return AstDirectiveNoop.INSTANCE;
    }

    @Override
    public AstNode visitDirective_options_expression(Directive_options_expressionContext ctx) {
        ParseTree nameNode = ctx.getChild(0);
        ParseTree valueNode = ctx.getChild(2);

        String name = nameNode.getText();
        Object value = ((AstConstant) accept(valueNode)).getValue();

        boolean invalidName = false;
        if (Symbols.OPTION_IMPORT.equals(name)) {
            if (value instanceof String) {
                parseCtx.importClass((String) value);
                return null;
            }
        } else if (Symbols.OPTION_LOAD_MACRO.equals(name)) {
            if (value instanceof String) {
                parseCtx.loadMacroFile((String) value);
                return null;
            }
        } else if (Symbols.OPTION_STRICT.equals(name)) {
            if (value instanceof Boolean) {
                parseCtx.setStrict((Boolean) value);
                return null;
            }
        } else if (Symbols.OPTION_SAFECALL.equals(name)) {
            if (value instanceof Boolean) {
                parseCtx.setSafecall((Boolean) value);
                return null;
            }
        } else if (Symbols.OPTION_TRIM_LEADING_WHITESPACES.equals(name)) {
            if (value instanceof Boolean) {
                parseCtx.setTrimLeadingWhitespaces((Boolean) value);
                return null;
            }
        } else {
            invalidName = true;
        }

        if (invalidName) {
            throw new SyntaxException(Errors.OPTION_NAME_INVALID, name).set(pos(nameNode));
        } else {
            throw new SyntaxException(Errors.OPTION_VALUE_INVALID, name).set(pos(valueNode));
        }
    }

    @Override
    public AstNode visitDirective_define(Directive_defineContext ctx) {
        accept(ctx.directive_define_expression());
        return AstDirectiveNoop.INSTANCE;
    }

    @Override
    public AstNode visitDirective_define_expression(Directive_define_expressionContext ctx) {
        AstType type = accept(ctx.type());
        String identifier = ctx.IDENTIFIER().getText();

        validateIdentifier(identifier, true, ctx.IDENTIFIER());

        // resolve class
        Class<?> cls = resolveClass(type);

        // define
        try {
            parseCtx.defineSymbol(identifier, cls);
        } catch (IllegalStateException e) {
            throw new SyntaxException(e).set(pos(ctx));
        }

        return null;
    }

    @Override
    public AstStatementList visitDirective_set(Directive_setContext ctx) {
        List<AstStatement> statements = accept(ctx.directive_set_expression());
        return new AstStatementList(statements, Tokens.AST_BLOCK_SET, parseCtx);
    }

    @Override
    public AstStatement visitDirective_set_expression(Directive_set_expressionContext ctx) {
        AstType type = accept(ctx.type());
        String identifier = ctx.IDENTIFIER().getText();
        AstExpression expression = accept(ctx.expression());

        validateIdentifier(identifier, true, ctx.IDENTIFIER());

        if (type != null) {
            // resolve class
            Class<?> cls = resolveClass(type);

            // define
            try {
                parseCtx.defineSymbol(identifier, cls, true);
            } catch (IllegalStateException e) {
                throw new SyntaxException(e).set(pos(ctx));
            }

        } else {
            try {
                parseCtx.useSymbol(identifier);
            } catch (IllegalStateException e) {
                throw new SyntaxException(e).set(pos(ctx));
            }
        }

        return new AstDirectiveSet(identifier, expression, pos(ctx));
    }

    @Override
    public AstDirectiveIf visitDirective_if(Directive_ifContext ctx) {
        AstExpression conditionExpression = accept(ctx.getChild(1));
        AstStatementList thenStatement = accept(ctx.getChild(3));

        AstStatement elseStatement = accept(ctx.directive_else());
        if (elseStatement == null) {
            elseStatement = accept(ctx.directive_elseif());
        }
        return new AstDirectiveIf(conditionExpression, thenStatement, elseStatement, pos(ctx));
    }

    @Override
    public AstDirectiveIf visitDirective_elseif(Directive_elseifContext ctx) {
        AstExpression conditionExpression = accept(ctx.getChild(1));
        AstStatementList thenStatement = accept(ctx.getChild(3));

        AstStatement elseStatement = accept(ctx.directive_else());
        if (elseStatement == null) {
            elseStatement = accept(ctx.directive_elseif());
        }
        return new AstDirectiveIf(conditionExpression, thenStatement, elseStatement, pos(ctx));
    }

    @Override
    public AstNode visitDirective_else(Directive_elseContext ctx) {
        return ctx.getChild(1).accept(this);
    }

    @Override
    public AstDirectiveFor visitDirective_for(Directive_forContext ctx) {
        AstType type = accept(ctx.type());
        String identifier = ctx.IDENTIFIER().getText();
        validateIdentifier(identifier, true, ctx.IDENTIFIER());

        Class<?> cls = null;
        if (type != null) {
            cls = resolveClass(type);
        }

        // define
        try {
            parseCtx.defineSymbol(identifier, cls, true);
        } catch (IllegalStateException e) {
            throw new SyntaxException(e).set(pos(ctx));
        }

        AstExpression expression = accept(ctx.expression());
        AstStatementList statement = accept(ctx.block());
        AstStatementList elseStatement = accept(ctx.directive_else());

        return new AstDirectiveFor(identifier, expression, statement, elseStatement, pos(ctx));
    }

    @Override
    public AstDirectiveBreak visitDirective_break(Directive_breakContext ctx) {
        validateInsideOfDirectiveFor(ctx, "#break");
        AstExpression expression = accept(ctx.expression());
        return new AstDirectiveBreak(expression, pos(ctx));
    }

    @Override
    public AstDirectiveContinue visitDirective_continue(Directive_continueContext ctx) {
        validateInsideOfDirectiveFor(ctx, "#continue");
        AstExpression expression = accept(ctx.expression());
        return new AstDirectiveContinue(expression, pos(ctx));
    }

    @Override
    public AstNode visitDirective_stop(Directive_stopContext ctx) {
        AstExpression expression = accept(ctx.expression());
        return new AstDirectiveStop(expression, pos(ctx));
    }

    @Override
    public AstNode visitDirective_return(Directive_returnContext ctx) {
        AstExpression expression = accept(ctx.getChild(1));
        return new AstDirectiveReturn(expression, pos(ctx));
    }

    @Override
    public AstNode visitDirective_include(Directive_includeContext ctx) {
        List<AstExpression> expressions = accept(ctx.expression());

        AstExpression fileExpression = expressions.get(0);
        AstExpression parametersExpression = null;
        String returnName = null;

        switch (expressions.size()) {
        case 1:
            break;
        case 2: {
            AstExpression expr = expressions.get(1);
            if (expr instanceof AstConstantMap) {
                parametersExpression = expr;
            } else if (expr instanceof AstConstant) {
                Object value = ((AstConstant) expr).getValue();
                if (value instanceof String) {
                    returnName = (String) value;
                } else {
                    throw new SyntaxException(Errors.VARIABLE_TYPE_MISMATCH, "2nd", value.getClass(), "String").set(pos(expr));
                }
            } else {
                parametersExpression = expr;
            }
            break;
        }
        case 3: {
            parametersExpression = expressions.get(1);
            AstExpression expr = expressions.get(2);

            Object value = ((AstConstant) expr).getValue();
            if (value instanceof String) {
                returnName = (String) value;
            } else {
                throw new SyntaxException(Errors.VARIABLE_TYPE_MISMATCH, "3rd", value.getClass(), "String").set(pos(expr));
            }
            break;
        }
        default:
            throw new SyntaxException(Errors.ARGUMENTS_NOT_MATCH).set(pos(expressions.get(3)));
        }

        return new AstDirectiveInclude(fileExpression, parametersExpression, returnName, pos(ctx));
    }

    @Override
    public AstNode visitDirective_tag(Directive_tagContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        Position position = new Position(token.getLine(), token.getCharPositionInLine() + 5);

        String name = ctx.getChild(0).getText();
        name = StringUtils.substringBetween(name, " ", "(").trim();

        AstExpressionList expressionList = accept(ctx.expression_list());
        AstStatementList block = accept(ctx.block());
        return new AstDirectiveTag(name, expressionList, block, position);
    }

    @Override
    public AstNode visitDirective_call(Directive_callContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        Position position = new Position(token.getLine(), token.getCharPositionInLine() + 6);

        String name = ctx.getChild(0).getText();
        name = StringUtils.substringBetween(name, " ", "(").trim();

        AstExpressionList expressionList = accept(ctx.expression_list());
        return new AstDirectiveCall(name, expressionList, position);
    }

    @Override
    public AstNode visitDirective_macro(Directive_macroContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        Position position = new Position(token.getLine(), token.getCharPositionInLine() + 7);

        String name = token.getText();
        name = StringUtils.substringBetween(name, " ", "(").trim();

        parseCtx.enterMacros();

        // 处理参数
        accept(ctx.directive_macro_arguments());

        // 立即获取参数列表(在 body 处理之前)
        List<String> argumentNames = parseCtx.getMacroArgumentNames();
        AstStatementList block = accept(ctx.block());

        AstDirectiveMacro macro = new AstDirectiveMacro(name, argumentNames, parseCtx.getSymbols(), block, position);
        try {
            parseCtx.defineMacro(macro);
        } catch (IllegalStateException e) {
            throw new SyntaxException(e).set(position);
        }

        parseCtx.exitMacros();

        return AstDirectiveNoop.INSTANCE;
    }

    @Override
    public AstNode visitDirective_macro_arguments(Directive_macro_argumentsContext ctx) {
        int count = ctx.getChildCount();
        int i = 0;
        while (i < count) {
            ParseTree node = ctx.getChild(i++);

            // get type and resolve
            Class<?> cls = null;
            if (node instanceof TypeContext) {
                AstType type = accept(node);
                cls = resolveClass(type);

                // get next node as name
                node = ctx.getChild(i++);
            }

            // get name
            String name = node.getText();

            // define arguments
            try {
                parseCtx.defineSymbol(name, cls);
            } catch (IllegalStateException e) {
                throw new SyntaxException(e).set(pos(node));
            }

            // skip next "," if exists
            i++;
        }

        return null;
    }

    @Override
    public AstNode visitDirective_invalid(Directive_invalidContext ctx) {
        throw new SyntaxException(Errors.ARGUMENTS_MISSING, ctx.getText()).set(pos(ctx));
    }

    @Override
    public AstNode visitExpression_primary(Expression_primaryContext ctx) {
        return ctx.getChild(1).accept(this);
    }

    @Override
    public AstNode visitExpression_constant(Expression_constantContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public AstExpression visitExpression_array_list(Expression_array_listContext ctx) {
        AstExpressionList expressionList = accept(ctx.getChild(1));
        if (expressionList == null) {
            return new AstConstant(Collections.emptyList(), pos(ctx));
        } else {
            return new AstConstantList(expressionList, pos(ctx));
        }
    }

    @Override
    public AstExpression visitExpression_hash_map(Expression_hash_mapContext ctx) {
        List<AstConstantMapEntry> entries = accept(ctx.hash_map_entry());
        if (entries == null || entries.isEmpty()) {
            return new AstConstant(Collections.emptyMap(), pos(ctx));
        } else {
            return new AstConstantMap(entries, pos(ctx));
        }
    }

    @Override
    public AstConstantMapEntry visitHash_map_entry(Hash_map_entryContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();

        String name = token.getText();
        switch (token.getType()) {
        case JetTemplateLexer.STRING_DOUBLE:
        case JetTemplateLexer.STRING_SINGLE:
            name = getJavaString(token.getText(), ctx);
            break;
        }
        AstExpression valueExpression = accept(ctx.getChild(2));
        return new AstConstantMapEntry(name, valueExpression, pos(ctx));
    }

    @Override
    public AstNode visitExpression_unary_operator(Expression_unary_operatorContext ctx) {
        Position position = pos(ctx);

        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        AstExpression expression = accept(ctx.getChild(1));
        switch (token.getType()) {
        case JetTemplateLexer.PLUS:
            return new AstOperatorUnary(Tokens.PLUS, expression, position);
        case JetTemplateLexer.MINUS:
            return new AstOperatorUnary(Tokens.MINUS, expression, position);
        case JetTemplateLexer.NOT:
            return new AstOperatorEquals(Tokens.NOT, expression, null, position);
        case JetTemplateLexer.BIT_NOT:
            return new AstOperatorUnary(Tokens.BIT_NOT, expression, position);
        }
        throw new SyntaxException(Errors.UNREACHABLE_CODE).set(position);
    }

    @Override
    public AstNode visitExpression_binary_operator(Expression_binary_operatorContext ctx) {
        Position position = pos(ctx, 1);
        Token token = ((TerminalNode) ctx.getChild(1)).getSymbol();

        AstExpression lhs = accept(ctx.getChild(0)); // first
        AstExpression rhs = accept(ctx.getChild(ctx.getChildCount() - 1)); // last
        switch (token.getType()) {
        case JetTemplateLexer.PLUS:
            return new AstOperatorBinary(Tokens.PLUS, lhs, rhs, position);
        case JetTemplateLexer.MINUS:
            return new AstOperatorBinary(Tokens.MINUS, lhs, rhs, position);
        case JetTemplateLexer.MUL:
            return new AstOperatorBinary(Tokens.MUL, lhs, rhs, position);
        case JetTemplateLexer.DIV:
            return new AstOperatorBinary(Tokens.DIV, lhs, rhs, position);
        case JetTemplateLexer.MOD:
            return new AstOperatorBinary(Tokens.MOD, lhs, rhs, position);
        case JetTemplateLexer.BIT_SHL:
            return new AstOperatorBinary(Tokens.BIT_SHL, lhs, rhs, position);
        case JetTemplateLexer.BIT_SHR:
            return new AstOperatorBinary(Tokens.BIT_SHR, lhs, rhs, position);
        case JetTemplateLexer.BIT_USHR:
            return new AstOperatorBinary(Tokens.BIT_USHR, lhs, rhs, position);
        case JetTemplateLexer.BIT_AND:
            return new AstOperatorBinary(Tokens.BIT_AND, lhs, rhs, position);
        case JetTemplateLexer.BIT_OR:
            return new AstOperatorBinary(Tokens.BIT_OR, lhs, rhs, position);
        case JetTemplateLexer.BIT_XOR:
            return new AstOperatorBinary(Tokens.BIT_XOR, lhs, rhs, position);
        case JetTemplateLexer.LT:
            return new AstOperatorBinary(Tokens.LT, lhs, rhs, position);
        case JetTemplateLexer.LE:
            return new AstOperatorBinary(Tokens.LE, lhs, rhs, position);
        case JetTemplateLexer.GT:
            return new AstOperatorBinary(Tokens.GT, lhs, rhs, position);
        case JetTemplateLexer.IDENTICALLY_EQUAL:
            return new AstOperatorEquals(Tokens.IDENTICALLY_EQUAL, lhs, rhs, position);
        case JetTemplateLexer.IDENTICALLY_EQUAL_NOT:
            return new AstOperatorEquals(Tokens.IDENTICALLY_EQUAL_NOT, lhs, rhs, position);
        case JetTemplateLexer.GE:
            return new AstOperatorBinary(Tokens.GE, lhs, rhs, position);
        case JetTemplateLexer.EQ:
            return new AstOperatorEquals(Tokens.EQ, lhs, rhs, position);
        case JetTemplateLexer.NE:
            return new AstOperatorEquals(Tokens.NE, lhs, rhs, position);
        case JetTemplateLexer.AND:
            return new AstOperatorEquals(Tokens.AND, lhs, rhs, position);
        case JetTemplateLexer.OR:
            return new AstOperatorEquals(Tokens.OR, lhs, rhs, position);
        }
        throw new SyntaxException(Errors.UNREACHABLE_CODE).set(position);
    }

    @Override
    public AstNode visitExpression_nullsafe_operator(Expression_nullsafe_operatorContext ctx) {
        parseCtx.setNullSafe(true);
        AstExpression objectExpression = accept(ctx.getChild(0));
        parseCtx.setNullSafe(false);

        AstExpression defaultExpression = accept(ctx.getChild(2));
        return new AstOperatorNullAsDefault(objectExpression, defaultExpression, pos(ctx, 1));
    }

    @Override
    public AstNode visitExpression_ternary_operator(Expression_ternary_operatorContext ctx) {
        if (ctx.getChildCount() == 5) {
            // a ? b : c
            AstExpression conditionExpression = accept(ctx.getChild(0));
            AstExpression trueExpression = accept(ctx.getChild(2));
            AstExpression falseExpression = accept(ctx.getChild(4));
            return new AstTernaryOperator(conditionExpression, trueExpression, falseExpression, pos(ctx, 1));
        } else {
            // a ?: b
            AstExpression objectExpression = accept(ctx.getChild(0));
            AstExpression defaultExpression = accept(ctx.getChild(3));
            return new AstTernarySimplifyOperator(objectExpression, defaultExpression, pos(ctx, 1));
        }
    }

    @Override
    public AstNode visitExpression_instanceof(Expression_instanceofContext ctx) {
        AstExpression expression = accept(ctx.getChild(0));
        AstType type = accept(ctx.getChild(2));

        Class<?> cls = resolveClass(type);
        return new AstOperatorInstanceof(expression, cls, pos(ctx));
    }

    @Override
    public AstNode visitExpression_new_object(Expression_new_objectContext ctx) {
        AstType type = accept(ctx.getChild(1));
        AstExpressionList argumentList = accept(ctx.expression_list());

        Class<?> cls = resolveClass(type);
        return new AstInvokeNewObject(cls, argumentList, pos(ctx));
    }

    @Override
    public AstNode visitExpression_new_array(Expression_new_arrayContext ctx) {
        AstType type = accept(ctx.getChild(1));
        List<AstExpression> expressions = accept(ctx.expression());

        Class<?> cls = resolveClass(type);
        return new AstInvokeNewArray(cls, expressions, pos(ctx));
    }

    @Override
    public AstNode visitExpression_field(Expression_fieldContext ctx) {
        AstExpression objectExpression = accept(ctx.getChild(0));
        String name = ctx.getChild(2).getText();
        return new AstInvokeField(objectExpression, name, parseCtx.isNullSafe(), pos(ctx, 1));
    }

    @Override
    public AstNode visitExpression_field_static(Expression_field_staticContext ctx) {
        AstType type = accept(ctx.getChild(0));
        String name = ctx.getChild(2).getText();

        Class<?> cls = resolveClass(type);
        return new AstInvokeFieldStatic(cls, name, pos(ctx, 1));
    }

    @Override
    public AstNode visitExpression_method(Expression_methodContext ctx) {
        AstExpression objectExpression = accept(ctx.getChild(0));
        String name = ctx.getChild(2).getText();
        AstExpressionList argumentList = accept(ctx.expression_list());
        return new AstInvokeMethod(objectExpression, name, argumentList, parseCtx.isNullSafe(), pos(ctx, 1));
    }

    @Override
    public AstNode visitExpression_method_static(Expression_method_staticContext ctx) {
        AstType type = accept(ctx.getChild(0));
        String name = ctx.getChild(2).getText();
        AstExpressionList argumentList = accept(ctx.expression_list());

        Class<?> cls = resolveClass(type);
        return new AstInvokeMethodStatic(cls, name, argumentList, pos(ctx, 1));
    }

    @Override
    public AstNode visitExpression_index_get(Expression_index_getContext ctx) {
        AstExpression objectExpression = accept(ctx.getChild(0));
        AstExpression indexExpression = accept(ctx.getChild(2));
        return new AstInvokeIndexGet(objectExpression, indexExpression, parseCtx.isNullSafe(), pos(ctx, 1));
    }

    @Override
    public AstNode visitExpression_function(Expression_functionContext ctx) {
        String name = ctx.getChild(0).getText();
        AstExpressionList argumentList = accept(ctx.expression_list());
        return new AstInvokeFunction(name, argumentList, pos(ctx));
    }

    @Override
    public AstNode visitExpression_identifier(Expression_identifierContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public AstIdentifier visitIdentifier(IdentifierContext ctx) {
        String name = ctx.getChild(0).getText();
        validateIdentifier(name, false, ctx);
        if (Symbols.FOR.equals(name)) {
            validateInsideOfDirectiveFor(ctx, "Local variable `" + Symbols.FOR + "`");
        }
        return new AstIdentifier(name, pos(ctx));
    }

    @Override
    public AstConstant visitConstant(ConstantContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        String text = token.getText();
        int length = text.length();
        int type = token.getType();
        switch (type) {
        case JetTemplateLexer.STRING_DOUBLE:
        case JetTemplateLexer.STRING_SINGLE: {
            String value = getJavaString(text, ctx);
            return new AstConstant(value, pos(ctx));
        }
        case JetTemplateLexer.INTEGER:
        case JetTemplateLexer.INTEGER_HEX:
        case JetTemplateLexer.FLOATING_POINT: {
            Object value;
            int radix;
            char suffix = text.charAt(length - 1);
            if (type == JetTemplateLexer.INTEGER_HEX) {
                radix = 16;
                if (suffix == 'l' || suffix == 'L') {
                    text = text.substring(2, length - 1);
                } else {
                    text = text.substring(2);
                    suffix = 0; // clear
                }
            } else {
                radix = 10;
                if (suffix > '9') {
                    text = text.substring(0, length - 1);
                }
            }
            switch (suffix) {
            case 'l':
            case 'L':
                value = Long.valueOf(text, radix);
                break;
            case 'f':
            case 'F':
                value = Float.valueOf(text);
                break;
            case 'd':
            case 'D':
                value = Double.valueOf(text);
                break;
            default:
                if (type == JetTemplateLexer.FLOATING_POINT) {
                    value = Double.valueOf(text);
                } else {
                    value = Integer.valueOf(text, radix);
                }
            }
            return new AstConstant(value, pos(ctx));
        }
        case JetTemplateLexer.TRUE:
            return new AstConstant(Boolean.TRUE, pos(ctx));
        case JetTemplateLexer.FALSE:
            return new AstConstant(Boolean.FALSE, pos(ctx));
        case JetTemplateLexer.NULL:
            return new AstConstant(null, pos(ctx));
        }

        throw new SyntaxException(Errors.UNREACHABLE_CODE).set(pos(ctx));
    }

    @Override
    public AstExpressionList visitExpression_list(Expression_listContext ctx) {
        List<AstExpression> expression_list = accept(ctx.expression());
        return new AstExpressionList(expression_list, pos(ctx));
    }

    @Override
    public AstType visitType(TypeContext ctx) {
        String className = StringUtils.deleteWhitespace(ctx.getText());
        return new AstType(className, pos(ctx));
    }

    private Class<?> resolveClass(AstType type) {
        Class<?> cls = parseCtx.resolveClass(type.getClassName());
        if (cls == null) {
            throw new SyntaxException(Errors.CLASS_NOT_FOUND, type.getClassName()).set(pos(type));
        }
        return cls;
    }

    @SuppressWarnings("unchecked")
    private <T extends AstNode> T accept(ParseTree node) {
        if (node != null) {
            return (T) node.accept(this);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T extends AstNode> List<T> accept(List<? extends ParseTree> nodes) {
        if (nodes != null) {
            int size = nodes.size();
            if (size == 0) {
                return Collections.emptyList();
            } else {
                List<T> results = new ArrayList<T>(size);
                for (ParseTree node : nodes) {
                    T ast = (T) node.accept(this);
                    if (ast != null) {
                        results.add(ast);
                    }
                }
                return results;
            }
        }
        return null;
    }

    private void validateIdentifier(String name, boolean defining, ParseTree node) {
        if (Symbols.FOR.equals(name)) {
            if (defining) {
                throw new SyntaxException(Errors.VARIABLE_IS_KEYWORD, name).set(pos(node));
            }
        } else if (JavaKeywordsUtils.isKeyword(name)) {
            throw new SyntaxException(Errors.VARIABLE_IS_KEYWORD, name).set(pos(node));
        } else if (name.startsWith("$")) {
            throw new SyntaxException(Errors.VARIABLE_IS_RESERVED, name).set(pos(node));
        }

        if (!defining) {
            try {
                parseCtx.useSymbol(name);
            } catch (IllegalStateException e) {
                throw new SyntaxException(e).set(pos(node));
            }
        }
    }

    private void validateInsideOfDirectiveFor(ParserRuleContext ctx, String name) {
        ParserRuleContext p = ctx.getParent();
        while (p != null) {
            if (p instanceof Directive_forContext) {
                return;
            }
            if (p instanceof Directive_elseContext) {
                p = p.getParent();
            }
            p = p.getParent();
        }
        throw new SyntaxException(Errors.DIRECTIVE_OUTSIDE_OF_FOR, name).set(pos(ctx));
    }

    private String getJavaString(String text, ParserRuleContext ctx) {
        String value = text;
        value = value.substring(1, value.length() - 1);
        try {
            value = StringEscapeUtils.unescapeJava(value);
        } catch (StringIndexOutOfBoundsException e) {
            throw new SyntaxException(Errors.UNICODE_STRING_INVALID).set(pos(ctx));
        }
        return value;
    }

    // 获取一个 Position 信息
    private Position pos(ParserRuleContext ctx, int childIndex) {
        ParseTree node = ctx.getChild(childIndex);
        if (node instanceof TerminalNode) {
            Token token = ((TerminalNode) node).getSymbol();
            return new Position(token.getLine(), token.getCharPositionInLine());
        } else if (node instanceof ParserRuleContext) {
            Token token = ctx.getStart();
            return new Position(token.getLine(), token.getCharPositionInLine());
        }
        throw new UnsupportedOperationException();
    }

    // 获取一个 Position 信息
    private Position pos(Object ctx) {
        Token token = null;
        if (ctx instanceof ParserRuleContext) {
            token = ((ParserRuleContext) ctx).getStart();
        } else if (ctx instanceof TerminalNode) {
            token = ((TerminalNode) ctx).getSymbol();
        } else if (ctx instanceof Token) {
            token = (Token) ctx;
        } else if (ctx instanceof AstExpression) {
            return ((AstExpression) ctx).getPosition();
        } else if (ctx instanceof AstType) {
            return ((AstType) ctx).getPosition();
        }
        if (token != null) {
            return new Position(token.getLine(), token.getCharPositionInLine());
        }
        throw new UnsupportedOperationException();
    }

}
