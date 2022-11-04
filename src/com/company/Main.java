package com.company;

import com.company.parser.LexicalScanner;
import com.company.parser.TokensBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            var tokensBuilder = new TokensBuilder("data/input/tokens.in");
            tokensBuilder.buildTokens();
            var lexicalScanner = new LexicalScanner(tokensBuilder);
            var data = lexicalScanner.analyze("data/input/p1.txt");
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
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
