%{
#include <stdio.h>
#include <stdlib.h>
#define YYDEBUG 1

%}


%token PROGRAM
%token BEGINN
%token END
%token CONST
%token DO
%token IF
%token ELSE
%token THEN
%token READ
%token WRITE
%token VAR
%token WHILE
%token STRUCT
%token OF
%token TYPE_1
%token TRUE
%token FALSE
%token UNSIGN_NR

%token ID
%token CONST_INT
%token CONST_REAL
%token CONST_CHAR
%token CONST_STR

%token CHAR
%token INTEGER
%token BOOLEAN
%token ARRAY

%token EQ
%token NE
%token LE
%token GE

%left '+' '-'
%left '%' '*' '/'
%left OR
%left AND

%%
src_program: PROGRAM ID ';' cmpd_stmt 
        | PROGRAM ID ';' VAR decl_list ';' cmpd_stmt 
        ;

decl_list: declaration 
        | declaration decl_list
        ;

declaration: id_list ':' all_type
        ;

id_list: ID 
        | id_list ',' ID 
        ;

simple_type: CHAR
        | INTEGER
        | BOOLEAN
        ;

array_type: ARRAY '[' CONST_INT ']' OF simple_type
        ;

all_type: simple_type
        | array_type
        ;

cmpd_stmt: BEGINN stmt_list ';' END
        ;

stmt_list: stmt 
        | stmt_list ';' stmt
        ;

stmt: simple_stmt
        | struct_stmt
        ;

simple_stmt: assign_stmt
        | io_stmt
        ;

assign_stmt: ID '=' expression
        ;

constant:	CONST_INT
		| TRUE
		| FALSE	
		| CONST_CHAR
		;
expression:	factor
		| expression '+' expression
		| expression '-' expression
		| expression '*' expression
		| expression '/' expression
		| expression '%' expression
		;
factor:		ID
		| constant
		| '(' expression ')'
		;

io_stmt: read_stmt
        | write_stmt
        ;

read_stmt: READ ID
        ;

write_stmt: WRITE expression
        ;

struct_stmt: if_else_stmt
        | while_stmt
        ;

if_else_stmt: if_stmt
        | if_stmt ';' else_stmt
        ;

if_stmt: IF condition THEN stmt
        | IF condition THEN cmpd_stmt
        ;

else_stmt: ELSE stmt
        | ELSE cmpd_stmt
        ;

while_stmt: WHILE condition DO stmt
        | WHILE condition DO cmpd_stmt
        ;

condition: expression relation expression
        ;

relation: EQ
		| '<'
		| '>'
		| LE
		| GE
        | NE
		;
%%

yyerror(char *s)
{
  printf("%s\n", s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
  if(argc>1) yyin = fopen(argv[1], "r");
  if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  if(!yyparse()) fprintf(stderr,"\tO.K.\n");
}

