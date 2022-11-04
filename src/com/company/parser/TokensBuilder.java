package com.company.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokensBuilder {
    private final String tokensFilePath;
    private final TreeSet<String> keywords;
    private final TreeSet<String> operators;
    private final TreeSet<Character> opCharacters;
    private final TreeSet<Character> operatorStarts;

    public TokensBuilder(String tokensFilePath) {
        this.tokensFilePath = tokensFilePath;
        this.keywords = new TreeSet<>();
        this.operators = new TreeSet<>();
        this.opCharacters = new TreeSet<>();
        this.operatorStarts = new TreeSet<>();
    }

    public void buildTokens() throws IOException {
        FileInputStream fis = new FileInputStream(tokensFilePath);
        Scanner sc = new Scanner(fis);

        Matcher hasOnlyLetters = Pattern.compile("[a-z]+").matcher("");
        while (sc.hasNextLine()) {
            String token = sc.nextLine().trim();
            hasOnlyLetters.reset(token);
            if (hasOnlyLetters.matches()) {
                this.keywords.add(token);
            }
            else {
                if (token.length() > 1)
                    this.operatorStarts.add(token.charAt(0));
                for (var c : token.toCharArray()) {
                    this.opCharacters.add(c);
                }
                this.operators.add(token);
            }
        }
    }

    public TreeSet<String> getKeywords() {
        return this.keywords;
    }

    public TreeSet<String> getOperators() {
        return this.operators;
    }

    public TreeSet<Character> getOpCharacters() {
        return this.opCharacters;
    }

    public TreeSet<Character> getOperatorStarts() {
        return this.operatorStarts;
    }
}
