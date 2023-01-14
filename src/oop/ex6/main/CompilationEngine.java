package oop.ex6.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CompilationEngine {

    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "String";
    private static final String CHAR = "char";
    private static final String FINAL = "final";

    private static final String VOID = "void";

    private static final String WHILE = "while";
    private static final String IF = "if";



    private static final ArrayList<String> DECLARATION_TYPES = new ArrayList<String>() {
        {
            add(INT);
            add(DOUBLE);
            add(BOOLEAN);
            add(STRING);
            add(CHAR);
            add(FINAL);
        }
    };

    private static final ArrayList<String> DECLARATION_SYMBOLS = new ArrayList<String>() {
        {
            add("=");
            add(";");
            add(",");
        }
    };

    private final CorrectnessChecker correctnessChecker;
    private final VarTable varTable;
    private final FunctionTable functionTable;
    private final BufferedReader reader;
    private Tokenizer tokenizer;

    CompilationEngine (CorrectnessChecker correctnessChecker, VarTable varTable, FunctionTable functionTable,
                       BufferedReader reader){
        this.correctnessChecker = correctnessChecker;
        this.varTable = varTable;
        this.functionTable = functionTable;
        this.reader = reader;
        this.tokenizer = null;

    }

    public void compile() throws IOException {

        initiateFuncTable();

        String line;

        while ((line = this.reader.readLine()) != null) {
            // checking if we can ignore the given line
            if (!(this.correctnessChecker.lineToIgnore(line))) {compileLine(line);}
        }
    }

    private void initiateFuncTable() throws IOException {
        String line;

        while ((line = this.reader.readLine()) != null) {
            if (line.startsWith(VOID + " ")){

            }

        }

    }

    private void compileLine(String line){

        // todo need to change the tokenizer
        this.tokenizer = new Tokenizer(line);

        while (tokenizer.hasMoreTokens()) {

            String curToken = tokenizer.getCurToken();

            // compiling declaration line
            if (DECLARATION_TYPES.contains(curToken)) { compileDeclaration(); }

            // compiling writing of a function
            if (curToken.equals(VOID)) { compileFunction(); }

            // if/while statement
            if(curToken.equals(IF) | curToken.equals(WHILE)) { compileIfWhileStatement(); }


        }


    }

    private void compileDeclaration() {
        boolean finalOrNot = false;

        if (this.tokenizer.getCurToken().equals(FINAL)) {
            finalOrNot = true;
            if (!advanceTokenizer()) {} // todo illegal declaration line, no var name
        }

        String type = this.tokenizer.getCurToken();

        compileDeclarationHelper(finalOrNot, type);

    }

    private void compileDeclarationHelper(boolean finalOrNot, String type) {
        if (!advanceTokenizer()) {} // todo illegal declaration line, no var name

        String name = this.tokenizer.getCurToken();
        if (!this.correctnessChecker.isLegalVarName(name)){} // todo illegal name

        if (!advanceTokenizer()) {} // todo illegal declaration line, no var name

        if (!DECLARATION_SYMBOLS.contains(this.tokenizer.getCurToken())) {} // todo raise exception -> illegal declaration line, illegal end of line

        // adding the var into the table:
        if (!this.varTable.addVar(name, finalOrNot, type, null)) {} // todo raise exception already declared

        while (!this.tokenizer.getCurToken().equals(";")) {

            if (this.tokenizer.getCurToken().equals(",")) { compileDeclarationHelper(finalOrNot, type);}

            // if next token is "="
            else {
                if (!advanceTokenizer()) {} // todo illegal declaration line, no var name

                String value = this.tokenizer.getCurToken();

                if (!this.correctnessChecker.isLegalValue(type, value)) {} // todo illegal value

                this.varTable.setValue(name,value);
            }
        }
        // has more tokens in this stage -> illegal line
        if (!advanceTokenizer()) {} // todo illegal line
    }

    private void compileFunction() {}

    private void compileIfWhileStatement() {}

    private boolean advanceTokenizer(){

        // checking if can advance tokenizer
        if (this.tokenizer.hasMoreTokens()) {
            this.tokenizer.advance();
            return true;
        }

        return false;

    }

}
