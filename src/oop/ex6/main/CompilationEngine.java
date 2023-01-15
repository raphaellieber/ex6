package oop.ex6.main;

import oop.ex6.main.function.handling.FunctionTable;
import oop.ex6.main.var.handling.VarTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CompilationEngine {

    // saved keywords:
    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "String";
    private static final String CHAR = "char";
    private static final String FINAL = "final";
    private static final String VOID = "void";
    private static final String WHILE = "while";
    private static final String IF = "if";

    private static final String FUNC_DECLARATION_START = "void ";
    private static final String ROUND_OPEN_BRACE = "(";
    private static final String ROUND_CLOSE_BRACE = ")";
    private static final String CURLY_OPEN_BRACE = "{";
    private static final String CURLY_CLOSE_BRACE = "}";
    private static final String SPACE = " ";
    private static final String FUNC_PARAM_LST_SPLIT_REGEX = "\\s*,\\s*";

    // Exceptions messages:
    private static final String WRONG_FUNC_NAME = "Wrong function name";
    private static final String WRONG_AVR_NAME = "Wrong var name";
    private static final String WRONG_TYPE = "Wrong var type";
    private static final String WRONG_END_OF_LINE = "Wrong end of line";

    private static final ArrayList<String> DECLARATION_TYPES = new ArrayList<>() {
        {
            add(INT);
            add(DOUBLE);
            add(BOOLEAN);
            add(STRING);
            add(CHAR);
            add(FINAL);
        }
    };

    private static final ArrayList<String> DECLARATION_SYMBOLS = new ArrayList<>() {
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

    public CompilationEngine (CorrectnessChecker correctnessChecker, VarTable varTable, FunctionTable functionTable,
                       BufferedReader reader) throws IDENTIFIERException, IOException, SYNTAXException {
        this.correctnessChecker = correctnessChecker;
        this.varTable = varTable;
        this.functionTable = functionTable;
        this.reader = reader;
        this.tokenizer = null;

        // Initializing the function table:
        initiateFuncTable();
        this.reader.reset();

    }

    public void compile() throws IOException {

        String line;

        while ((line = this.reader.readLine()) != null) {
            // checking if we can ignore the given line
            if (!(this.correctnessChecker.lineToIgnore(line))) {compileLine(line);}
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

    private void initiateFuncTable() throws IOException, IDENTIFIERException, SYNTAXException {
        String line;

        while ((line = this.reader.readLine()) != null) {
            if (line.startsWith(FUNC_DECLARATION_START)){
                int braceStartLoc = line.indexOf(ROUND_OPEN_BRACE);
                int braceFinishLoc = line.indexOf(ROUND_CLOSE_BRACE);
                int spaceLoc = line.indexOf(SPACE);

                // dividing into 3 sections: func name, params, end of line
                String funcName = line.substring(spaceLoc,braceStartLoc).trim();
                String declaration = line.substring(braceStartLoc+1, braceFinishLoc);
                String endOfLine = line.substring(braceFinishLoc+1).trim();

                // exception for the func name
                if (!this.correctnessChecker.isLegalMethodName(funcName)) {
                    throw new IDENTIFIERException(WRONG_FUNC_NAME);
                }

                // exception for wrong end of line
                if (!endOfLine.equals(CURLY_OPEN_BRACE)) {throw new SYNTAXException(WRONG_END_OF_LINE);}

                // dealing with param declaration:
                ArrayList<String> paramType = declarationHelper(declaration);
                this.functionTable.addFunction(funcName, paramType);
            }
        }
    }

    private ArrayList<String> declarationHelper(String declaration) throws IDENTIFIERException {
        // the result will be: type name
        String[] pramTypeTmp = declaration.split(FUNC_PARAM_LST_SPLIT_REGEX);

        // todo: need to update this section according to function class!!
        ArrayList<String> paramType = new ArrayList<>();
        for (String s: pramTypeTmp){

            String type = s.split(SPACE)[0];
            String name = s.split(SPACE)[1];

            // throwing exceptions for var type
            if (!this.correctnessChecker.isLegalVarType(type)) {
                throw new IDENTIFIERException(WRONG_TYPE);
            }

            // throwing exceptions for var name
            if (!this.correctnessChecker.isLegalVarName(name)) {
                throw new IDENTIFIERException(WRONG_AVR_NAME);
            }
            paramType.add(s);
        }
        return paramType;
    }

}
