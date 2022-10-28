package com.company;

import data.SymbolTable;

public class Main {

    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();

        symbolTable.add("key1", 2);
        symbolTable.add("key2", 5);

        for (int i = 0; i < 16; ++i) {
            symbolTable.add(String.valueOf(i), i);
        }

        symbolTable.add("key1", 3);

        assert symbolTable.get("key1") == 3;
        assert symbolTable.get("key2") == 5;
    }
}
