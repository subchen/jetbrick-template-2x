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
parser grammar JetTemplateParser;

options {
    tokenVocab = JetTemplateLexer; // use tokens from JetTemplateLexer.g4
}

/*
@header {
    package jetbrick.template.parser.grammer;
}
*/

// -------- rule ---------------------------------------
template    :   block EOF
            ;

block       :   (text | value | directive)*
            ;

text        :   TEXT_PLAIN
            |   TEXT_CDATA
            |   TEXT_CHAR_SINGLE
            |   TEXT_CHAR_ESCAPED
            |   TEXT_DIRECTIVE_LIKE
            ;

value       :   VALUE_OPEN         expression '}'
            |   VALUE_OPEN_ESCAPED expression '}'
            ;

directive   :   directive_options
            |   directive_define
            |   directive_set
            |   directive_if
            |   directive_for
            |   directive_break
            |   directive_continue
            |   directive_stop
            |   directive_include
            |   directive_return
            |   directive_tag
            |   directive_call
            |   directive_macro
            |   directive_invalid
            ;

directive_options
            :   DIRECTIVE_OPEN_OPTIONS directive_options_expression (',' directive_options_expression)* ')'
            ;
directive_options_expression
            :   IDENTIFIER '=' constant
            ;

directive_define
            :   DIRECTIVE_OPEN_DEFINE directive_define_expression (',' directive_define_expression)* ')'
            ;
directive_define_expression
            :   type IDENTIFIER
            ;

directive_set
            :   DIRECTIVE_OPEN_SET directive_set_expression (',' directive_set_expression)* ')'
            ;
directive_set_expression
            :   type? IDENTIFIER '=' expression
            ;

directive_if
            :   DIRECTIVE_OPEN_IF expression ')' block (directive_elseif | directive_else)? DIRECTIVE_END
            ;
directive_elseif
            :   DIRECTIVE_OPEN_ELSEIF expression ')' block (directive_elseif | directive_else)?
            ;
directive_else
            :   DIRECTIVE_ELSE block
            ;

directive_for
            :   DIRECTIVE_OPEN_FOR type? IDENTIFIER ':' expression ')' block directive_else? DIRECTIVE_END
            ;

directive_break
            :   DIRECTIVE_OPEN_BREAK expression? ')'
            |   DIRECTIVE_BREAK
            ;
directive_continue
            :   DIRECTIVE_OPEN_CONTINUE expression? ')'
            |   DIRECTIVE_CONTINUE
            ;
directive_stop
            :   DIRECTIVE_OPEN_STOP expression? ')'
            |   DIRECTIVE_STOP
            ;

directive_return
            :   DIRECTIVE_OPEN_RETURN expression ')'
            |   DIRECTIVE_RETURN
            ;

directive_include
            :   DIRECTIVE_OPEN_INCLUDE expression (',' expression)* ')'
            ;

directive_tag
            :   DIRECTIVE_OPEN_TAG expression_list? ')' block DIRECTIVE_END
            ;

directive_call
            :   DIRECTIVE_OPEN_CALL expression_list? ')'
            ;

directive_macro
            :   DIRECTIVE_OPEN_MACRO directive_macro_arguments? ')' block DIRECTIVE_END
            ;
directive_macro_arguments
            :   type? IDENTIFIER (',' type? IDENTIFIER)*
            ;

directive_invalid
            :   DIRECTIVE_OPTIONS
            |   DIRECTIVE_DEFINE
            |   DIRECTIVE_SET
            |   DIRECTIVE_IF
            |   DIRECTIVE_ELSEIF
            |   DIRECTIVE_FOR
            |   DIRECTIVE_INCLUDE
            |   DIRECTIVE_TAG
            |   DIRECTIVE_CALL
            |   DIRECTIVE_MACRO
            ;

expression  :   '(' expression ')'                                           #expression_primary
            |   constant                                                     #expression_constant
            |   identifier                                                   #expression_identifier
            |   '[' expression_list? ']'                                     #expression_array_list
            |   '{' (hash_map_entry (',' hash_map_entry)* )? '}'             #expression_hash_map
            |   expression '.'  IDENTIFIER                                   #expression_field
            |   expression '.'  IDENTIFIER '(' expression_list? ')'          #expression_method
            |   type       '::' IDENTIFIER                                   #expression_field_static
            |   type       '::' IDENTIFIER '(' expression_list? ')'          #expression_method_static
            |   expression '[' expression ']'                                #expression_index_get
            |   IDENTIFIER '(' expression_list? ')'                          #expression_function
            |   ('+' |'-' )  expression                                      #expression_unary_operator
            |   '~'          expression                                      #expression_unary_operator
            |   '!'          expression                                      #expression_unary_operator
            |   'new' type '(' expression_list? ')'                          #expression_new_object
            |   'new' type ('[' expression ']')+                             #expression_new_array
            |   expression '?!' expression                                   #expression_nullsafe_operator
            |   expression ('*'|'/'|'%')  expression                         #expression_binary_operator
            |   expression ('+'|'-')      expression                         #expression_binary_operator
            |   expression ('<<'|'>>'|'>>>')   expression                    #expression_binary_operator
            |   expression ('>='|'<='|'>'|'<') expression                    #expression_binary_operator
            |   expression 'instanceof' type                                 #expression_instanceof
            |   expression ('=='|'!='|'==='|'!==') expression                #expression_binary_operator
            |   expression '&'  expression                                   #expression_binary_operator
            |   <assoc=right>   expression '^' expression                    #expression_binary_operator
            |   expression '|'  expression                                   #expression_binary_operator
            |   expression '&&' expression                                   #expression_binary_operator
            |   expression '||' expression                                   #expression_binary_operator
            |   <assoc=right>   expression '?' expression? ':' expression    #expression_ternary_operator
            ;

identifier  :   IDENTIFIER
            ;

constant    :   STRING_DOUBLE
            |   STRING_SINGLE
            |   INTEGER
            |   INTEGER_HEX
            |   FLOATING_POINT
            |   TRUE
            |   FALSE
            |   NULL
            ;

expression_list
            :   expression (',' expression)*
            ;

hash_map_entry
            :   (IDENTIFIER | STRING_DOUBLE | STRING_SINGLE) ':' expression
            ;

type        :   IDENTIFIER ('.' IDENTIFIER)* ('[' ']')*
            ;


