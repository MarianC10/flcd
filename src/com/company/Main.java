package com.company;

import com.company.data.FiniteAutomaton;
import com.company.parser.LexicalScanner;
import com.company.parser.TokensBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Enter the run mode.");
            System.out.println("1. Analyze a program");
            System.out.println("2. Upload a finite automaton");
            var scanner = new Scanner(System.in);

            String runMode = scanner.nextLine().trim();
            while (!runMode.equals("1") && !runMode.equals("2")) {
                runMode = scanner.nextLine().trim();
            }

            if (runMode.equals("1")) {
                analyzeProgram();
            }
            else {
                uploadAutomaton();
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static void analyzeProgram() throws IOException {
        var tokensBuilder = new TokensBuilder("data/input/tokens.in");
        tokensBuilder.buildTokens();
        var lexicalScanner = new LexicalScanner(tokensBuilder);
        var data = lexicalScanner.analyze("data/input/p1err.txt");
        if (data.getLexicalErrors().size() == 0) {
            String result = "No lexical errors!";
            String symbolTableString = data.getSymbolTable().toString();
            String pifString = data.getProgramInternalForm().toString();
            System.out.println(result);
            System.out.println(symbolTableString);
            System.out.println(pifString);

            BufferedWriter symTableWriter = new BufferedWriter(new FileWriter("data/output/symbol_table.out"));
            BufferedWriter pifWriter = new BufferedWriter(new FileWriter("data/output/pif.out"));
            BufferedWriter resultWriter = new BufferedWriter(new FileWriter("data/output/result.out"));
            symTableWriter.write(symbolTableString);
            pifWriter.write(pifString);
            resultWriter.write(result);

            symTableWriter.close();
            pifWriter.close();
            resultWriter.close();
        }
        else {
            data.getLexicalErrors().forEach(System.out::println);
            BufferedWriter resultWriter = new BufferedWriter(new FileWriter("data/output/result.out", true));
            for (var err : data.getLexicalErrors())
                resultWriter.append(err).append(String.valueOf('\n'));
        }
    }

    public static void uploadAutomaton() throws IOException {
        System.out.println("Enter the automaton to be uploaded.");
        System.out.println("1. Identifier finite automaton");
        System.out.println("2. Integer constant finite automaton");
        var scanner = new Scanner(System.in);

        var automatonType = scanner.nextLine().trim();
        while (!automatonType.equals("1") && !automatonType.equals("2")) {
            automatonType = scanner.nextLine().trim();
        }

        var finiteAutomaton = automatonType.equals("1")
                ? FiniteAutomaton.provideFiniteAutomaton("data/input/identifier.csv")
                : FiniteAutomaton.provideFiniteAutomaton("data/input/integer_constant.csv");

        var done = false;
        while (!done) {
            System.out.println("1. Print automaton");
            System.out.println("2. Match string");
            System.out.println("0. Exit");

            var action = scanner.nextLine().trim();
            while (!action.equals("1") && !action.equals("2") && !action.equals("0")) {
                action = scanner.nextLine().trim();
            }

            if (action.equals("1")) {
                System.out.println(finiteAutomaton);
            }
            else if (action.equals("2")) {
                System.out.println("Enter string to be matched:");
                var toBeMatched = scanner.nextLine();
                System.out.println(finiteAutomaton.matches(toBeMatched) ? "It matches!" : "It does not match!");
            }
            else {
                done = true;
            }
        }
    }
}
