package com.company.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Grammar {
    private final String start;
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final Map<String, Set<String>> productions;
    private final boolean isContextFree;

    public static Grammar provideGrammar(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        Scanner sc = new Scanner(fis);
        var nonTerminals = new HashSet<String>();
        var terminals = new HashSet<String>();
        var productions = new HashMap<String, Set<String>>();
        var isContextFree = true;

        if (!sc.hasNextLine()) {
            throw new RuntimeException("The format of the grammar file is not valid!");
        }

        var start = sc.nextLine();

        while (sc.hasNextLine()) {
            var tokens = sc.nextLine().split(" = ");

            if (tokens.length != 2) {
                throw new RuntimeException("The format of the grammar file is not valid!");
            }

            var nonTerminal = tokens[0];
            nonTerminals.add(nonTerminal);

            if (nonTerminal.split(" ").length != 1) {
                isContextFree = false;
            }

            nonTerminals.add(nonTerminal);

            var currentProductions = tokens[1].split("\\|");
            for (var production: currentProductions) {
                productions.computeIfAbsent(nonTerminal, k -> new HashSet<>());
                productions.get(nonTerminal).add(production);

                var elements = production.split(" ");
                for (var element: elements) {
                    var length = element.length();
                    if (length >= 3 && element.charAt(0) == '"' && element.charAt(length - 1) == '"') {
                        terminals.add(element);
                    }
                }
            }
        }

        return new Grammar(start, nonTerminals, terminals, productions, isContextFree);
    }

    private Grammar(String start,
                    Set<String> nonTerminals,
                    Set<String> terminals,
                    Map<String, Set<String>> productions,
                    boolean isContextFree) {
        this.start = start;
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.isContextFree = isContextFree;
    }

    public Set<String> getProductions(String nonTerminal) {
        return productions.get(nonTerminal);
    }

    public String getStart() {
        return start;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Map<String, Set<String>> getProductions() {
        return productions;
    }

    public boolean isContextFree() {
        return isContextFree;
    }
}