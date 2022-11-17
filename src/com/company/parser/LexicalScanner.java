package com.company.parser;

import com.company.common.AnalyzerData;
import com.company.common.PIFPair;
import com.company.data.FiniteAutomaton;
import com.company.data.ProgramInternalForm;
import com.company.data.SymbolTable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class LexicalScanner {
    private final TokensBuilder tokensBuilder;

    public LexicalScanner(TokensBuilder tokensBuilder) {
        this.tokensBuilder = tokensBuilder;
    }

    public AnalyzerData analyze(String programFilePath) throws IOException {
        var keywords = this.tokensBuilder.getKeywords();
        var operators = this.tokensBuilder.getOperators();
        var operatorStarts = this.tokensBuilder.getOperatorStarts();
        var opCharacters = this.tokensBuilder.getOpCharacters();

        var symbolTable = new SymbolTable();
        var programInternalForm = new ProgramInternalForm();
        var lexicalErrors = new ArrayList<String>();
        String integerConstantAutomatonFileName = "data/input/integer_constant.csv";
        var isIntegerConstantAutomaton =
                FiniteAutomaton.provideFiniteAutomaton(integerConstantAutomatonFileName);
        String identifierAutomatonFileName = "data/input/identifier.csv";
        var isIdentifierAutomaton =
                FiniteAutomaton.provideFiniteAutomaton(identifierAutomatonFileName);

        FileInputStream fis = new FileInputStream(programFilePath);
        Scanner sc = new Scanner(fis);

        String delim = opCharacters.stream().map(Object::toString).reduce(" \t", (a, b) -> a + b);
        int line = 1;

        while (sc.hasNextLine()) {
            var stringTokenizer = new StringTokenizer(sc.nextLine(), delim, true);
            while (stringTokenizer.hasMoreTokens()) {
                StringBuilder token = new StringBuilder(stringTokenizer.nextToken());
                String nextToken = null;
                if (token.length() == 1 && opCharacters.contains(token.charAt(0))) {
                    if (operatorStarts.contains(token.charAt(0))) {
                        while (stringTokenizer.hasMoreTokens()) {
                            nextToken = stringTokenizer.nextToken();
                            if (nextToken.length() != 1 || !opCharacters.contains(nextToken.charAt(0)))
                                break;
                            token.append(nextToken);
                        }
                    }
                    processOperator(token, operators, programInternalForm, lexicalErrors, line);

                    if (nextToken != null)
                        processNonOperator(nextToken, keywords, symbolTable, programInternalForm, lexicalErrors, line,
                                isIntegerConstantAutomaton, isIdentifierAutomaton);
                }
                else {
                    processNonOperator(token.toString(), keywords, symbolTable, programInternalForm, lexicalErrors,
                            line, isIntegerConstantAutomaton, isIdentifierAutomaton);
                }
            }

            line++;
        }

        return new AnalyzerData(symbolTable, programInternalForm, lexicalErrors);
    }

    private boolean isStringConstant(String token) {
        return token.startsWith("\"") && token.endsWith("\"");
    }

    private boolean isCharConstant(String token) {
        return token.length() == 3 && token.startsWith("'") && token.endsWith("'");
    }

    private void processOperator(StringBuilder operatorBuilder,
                                TreeSet<String> operators,
                                ProgramInternalForm programInternalForm,
                                ArrayList<String> lexicalErrors,
                                int line) {
        var operator = operatorBuilder.toString();
        if (operators.contains(operator)) {
            programInternalForm.add(new PIFPair(operator));
        }
        else {
            lexicalErrors.add("Lexical error at line " + line + ": invalid token " + operator + "!");
        }
    }

    private void processNonOperator(String token,
                                    TreeSet<String> keywords,
                                    SymbolTable symbolTable,
                                    ProgramInternalForm programInternalForm,
                                    ArrayList<String> lexicalErrors,
                                    int line,
                                    FiniteAutomaton isIntegerConstant,
                                    FiniteAutomaton isIdentifier) {
//        Matcher isIntegerConstant = Pattern.compile("(([+\\-])?[1-9][0-9]*)|0").matcher("");
//        Matcher isIdentifier = Pattern.compile("_?[a-zA-Z][a-zA-Z0-9]*").matcher("");

        if (keywords.contains(token)) {
            programInternalForm.add(new PIFPair(token));
        }
        else if (!token.equals(" ") && !token.equals("\t")) {
//            isIntegerConstant.reset(token);
//            isIdentifier.reset(token);

            if (isIntegerConstant.matches(token) || isStringConstant(token) || isCharConstant(token)) {
                int position;
                if (symbolTable.contains(token)) {
                    position = symbolTable.get(token);
                }
                else {
                    position = symbolTable.add(token);
                }
                programInternalForm.add(new PIFPair("const", position));
            }
            else if (isIdentifier.matches(token)) {
                int position;
                if (symbolTable.contains(token)) {
                    position = symbolTable.get(token);
                }
                else {
                    position = symbolTable.add(token);
                }
                programInternalForm.add(new PIFPair("id", position));
            }
            else {
                lexicalErrors.add("Lexical error at line " + line + ": invalid token " + token + "!");
            }
        }
    }
}
