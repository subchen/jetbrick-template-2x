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
lexer grammar JetTemplateLexer;

/*
@header {
    package jetbrick.template.parser.grammer;
}
*/

// *******************************************************************
// ------- DEFAULT mode for Plain Text -------------------------------
COMMENT_LINE            : ('##'|'#//') ~[\r\n]* NEWLINE    -> skip ;
COMMENT_BLOCK           : '#--' .*? '--#'                  -> skip ;
fragment NEWLINE        : ('\r'? '\n' | EOF)               ;

// Texts
TEXT_PLAIN              : ~('$'|'#'|'\\')+                 ;
TEXT_CDATA              : '#[[' .*? ']]#'                  ;
TEXT_CHAR_ESCAPED       : ('\\#'|'\\$'|'\\\\')             ;
TEXT_CHAR_SINGLE        : ('#'|'$'|'\\')                   ;

// Values
VALUE_OPEN              : '${'                             -> pushMode(INSIDE) ;
VALUE_OPEN_ESCAPED      : '$!{'                            -> pushMode(INSIDE) ;

// Directives
DIRECTIVE_OPEN_OPTIONS  : '#options'     ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_DEFINE   : '#define'      ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_SET      : '#set'         ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_IF       : '#if'          ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_ELSEIF   : '#elseif'      ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_FOR      : '#for'         ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_BREAK    : '#break'       ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_CONTINUE : '#continue'    ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_STOP     : '#stop'        ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_RETURN   : '#return'      ARG_START         -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_INCLUDE  : '#include'     ARG_START         -> pushMode(INSIDE) ;

DIRECTIVE_OPEN_TAG      : '#tag'         NAME_ARG_START    -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_CALL     : '#call'        NAME_ARG_START    -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_MACRO    : '#macro'       NAME_ARG_START    -> pushMode(INSIDE) ;

DIRECTIVE_ELSE          : '#else'        EMPTY_ARG?        ;
DIRECTIVE_END           : '#end'         EMPTY_ARG?        ;

DIRECTIVE_BREAK         : '#break'                         ;
DIRECTIVE_CONTINUE      : '#continue'                      ;
DIRECTIVE_STOP          : '#stop'                          ;
DIRECTIVE_RETURN        : '#return'                        ;

//fragment IF_ARG_START : [ \t]+ 'if'    ARG_START         ;
fragment NAME_ARG_START : [ \t]+ ID      ARG_START         ;
fragment ARG_START      : [ \t]* '('                       ;
fragment EMPTY_ARG      : '()'                             ;
fragment ID             : [_a-zA-Z][_a-zA-Z0-9]*           ;


// Following is invalid directives
DIRECTIVE_OPTIONS       : '#options'                       ;
DIRECTIVE_DEFINE        : '#define'                        ;
DIRECTIVE_SET           : '#set'                           ;
DIRECTIVE_IF            : '#if'                            ;
DIRECTIVE_ELSEIF        : '#elseif'                        ;
DIRECTIVE_FOR           : '#for'                           ;
DIRECTIVE_INCLUDE       : '#include'                       ;
DIRECTIVE_TAG           : '#tag'                           ;
DIRECTIVE_CALL          : '#call'                          ;
DIRECTIVE_MACRO         : '#macro'                         ;


// It is a text which like a directive.
// It must be put after directive definition to avoid conflict.
TEXT_DIRECTIVE_LIKE     : '#' [a-zA-Z0-9]+                 ;


// *******************************************************************
// -------- INSIDE mode for directive --------------------------------
mode INSIDE;

WHITESPACE              : [ \t\r\n]+                       -> skip ;

// Separators
LPAREN                  : '('                              -> pushMode(INSIDE) ;
RPAREN                  : ')'                              -> popMode ;
LBRACK                  : '['                              ;
RBRACK                  : ']'                              ;
LBRACE                  : '{'                              -> pushMode(INSIDE) ;
RBRACE                  : '}'                              -> popMode ;

//
COMMA                   : ','                              ;
QUESTION                : '?'                              ;
COLON                   : ':'                              ;
COLON2                  : '::'                             ;
DOT                     : '.'                              ;
ASSIGN                  : '='                              ;
NULL_AS_DEFAULT         : '?!'                             ;

// Equals and Compares
IDENTICALLY_EQUAL       : '==='                            ;
IDENTICALLY_EQUAL_NOT   : '!=='                            ;
EQ                      : '=='                             ;
NE                      : '!='                             ;
GT                      : '>'                              ;
LT                      : '<'                              ;
GE                      : '>='                             ;
LE                      : '<='                             ;

// Condition
AND                     : '&&'                             ;
OR                      : '||'                             ;
NOT                     : '!'                              ;

// Unary and Binary Operators
PLUS                    : '+'                              ;
MINUS                   : '-'                              ;
MUL                     : '*'                              ;
DIV                     : '/'                              ;
MOD                     : '%'                              ;

// Bitwise Operators
BIT_AND                 : '&'                              ;
BIT_OR                  : '|'                              ;
BIT_NOT                 : '~'                              ;
BIT_XOR                 : '^'                              ;
BIT_SHL                 : '<<'                             ;
BIT_SHR                 : '>>'                             ;
BIT_USHR                : '>>>'                            ;

// Keywords
INSTANCEOF              : 'instanceof'                     ;
NEW                     : 'new'                            ;
TRUE                    : 'true'                           ;
FALSE                   : 'false'                          ;
NULL                    : 'null'                           ;
//THIS                  : 'this'                           ;
//SUPER                 : 'super'                          ;

// Id
IDENTIFIER              : [_a-zA-Z][_a-zA-Z0-9]*           ;

// Numbers
INTEGER                 : INT [lLfFdD]?                    ;
INTEGER_HEX             : '0x' HEX+ [lL]?                  ;
FLOATING_POINT          : INT ('.' FRAC)? EXP? [fFdD]?     ;
fragment INT            : '0' | [1-9] [0-9]*               ;
fragment FRAC           : [0-9]+                           ;
fragment EXP            : [Ee] [+\-]? INT                  ;

// Strings
STRING_DOUBLE           : '"'  (ESC|OTHERS)*? '"'          ;
STRING_SINGLE           : '\'' (ESC|OTHERS)*? '\''         ;
fragment OTHERS         : ~('\\' | '\r' | '\n')            ;
fragment ESC            : '\\' ([btnfr"'\\]|UNICODE)       ;
fragment UNICODE        : 'u' HEX HEX HEX HEX              ;
fragment HEX            : [0-9a-fA-F]                      ;

