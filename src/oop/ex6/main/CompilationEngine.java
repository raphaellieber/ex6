package oop.ex6.main;

import java.util.ArrayList;

public class CompilationEngine {

    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "String";
    private static final String CHAR = "char";
    private static final String VOID = "void";

    private static final ArrayList<String> DECLARATION = new ArrayList<String>() {
        {
            add(INT);
            add(DOUBLE);
            add(BOOLEAN);
            add(STRING);
            add(CHAR);
        }
    };

    private final CorrectnessChecker correctnessChecker;
    private final VarTable varTable;

    CompilationEngine (Tokenizer tokenizer, CorrectnessChecker correctnessChecker, VarTable varTable){
        this.tokenizer = tokenizer;
        this.correctnessChecker = correctnessChecker;
        this.varTable = varTable;
    }

    public void compile(){ // todo void?

        while (this.tokenizer.hasMoreTokens()) {

            // compiling declaration line
            if (DECLARATION.contains(this.tokenizer.curToken)) {compileDeclaration();}

            // compiling function
            if (this.tokenizer.curToken.equals(VOID)) { compileFunction();}

        }




    }

    private static void compileDeclaration() {}

    private static void compileFunction() {}

}
