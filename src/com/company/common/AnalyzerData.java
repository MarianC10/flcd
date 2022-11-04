package com.company.common;

import com.company.data.ProgramInternalForm;
import com.company.data.SymbolTable;

import java.util.ArrayList;

public class AnalyzerData {
    private final SymbolTable symbolTable;
    private final ProgramInternalForm programInternalForm;
    private final ArrayList<String> lexicalErrors;

    public AnalyzerData(SymbolTable symbolTable,
                        ProgramInternalForm programInternalForm,
                        ArrayList<String> lexicalErrors) {
        this.symbolTable = symbolTable;
        this.programInternalForm = programInternalForm;
        this.lexicalErrors = lexicalErrors;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public ProgramInternalForm getProgramInternalForm() {
        return programInternalForm;
    }

    public ArrayList<String> getLexicalErrors() {
        return lexicalErrors;
    }
}
