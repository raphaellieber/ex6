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
    private static final String SEMICOLON = ";";
    private static final String COMMA = ",";
    private static final String EQUAL = "=";
    private static final String FUNC_PARAM_LST_SPLIT_REGEX = "\\s*,\\s*";
    private static final String ALL_SPACES_REGEX = "\\s";

    // Exceptions messages:
    private static final String WRONG_FUNC_NAME = "Illegal function name";
    private static final String WRONG_VAR_NAME = "Illegal var name";
    private static final String WRONG_VALUE = "Illegal value";
    private static final String NO_AVR_NAME = "No var name";
    private static final String WRONG_TYPE = "Illegal var type";
    private static final String WRONG_END_OF_LINE = "Illegal end of line";
    private static final String ILLEGAL_DECLARATION_LINE = "Illegal declaration line";

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

    public void compile() throws IOException, SYNTAXException, VALUEException, IDENTIFIERException {

        String line;

        while ((line = this.reader.readLine()) != null) {
            // checking if we can ignore the given line
            if (!(this.correctnessChecker.lineToIgnore(line))) {compileLine(line);}
        }
    }

    private void compileLine(String line) throws SYNTAXException, VALUEException, IDENTIFIERException {
        while (!line.equals("")) {
            int firstSpace = line.indexOf(SPACE);
            String firstWord = line.substring(0, firstSpace);

            String curToken = tokenizer.getCurToken();

            // compiling declaration line
            if (DECLARATION_TYPES.contains(curToken)) { compileDeclaration(line); }

            // compiling writing of a function
            if (curToken.equals(VOID)) { compileFunction(); }

            // if/while statement
            if(curToken.equals(IF) | curToken.equals(WHILE)) { compileIfWhileStatement(); }


        }


    }

    private void compileDeclaration(String line) throws SYNTAXException, VALUEException, IDENTIFIERException {
        boolean finalOrNot = false;

        if (line.startsWith(FINAL)) {
            finalOrNot = true;

            // checking for valid line beginning (should start with final + " "+)
            int firstSpaceInd = line.indexOf(SPACE);
            if (firstSpaceInd == -1) {throw new SYNTAXException(ILLEGAL_DECLARATION_LINE);}

            // line now should start with the name of the type
            line = line.substring(firstSpaceInd + 1).trim();
        }

        // checking validity of line beginning and end
        int firstSpaceInd = line.indexOf(SPACE);
        int endOfLine = line.indexOf(SEMICOLON);

        if (firstSpaceInd == -1 | endOfLine == -1 | !this.correctnessChecker.isLegalEndOfLine(line)) {
            throw new SYNTAXException(ILLEGAL_DECLARATION_LINE);
        }

        // type check
        String type = line.substring(0, firstSpaceInd);
        if (!this.correctnessChecker.isLegalVarType(type)) {throw new SYNTAXException(WRONG_TYPE);}

        line = line.substring(firstSpaceInd + 1, endOfLine);
        line = line.replaceAll(ALL_SPACES_REGEX, "");
        compileDeclarationParams(finalOrNot, type, line);

    }

    private void compileDeclarationParams(boolean finalOrNot, String type, String line)
            throws SYNTAXException, VALUEException, IDENTIFIERException {

        String[] split = line.split(COMMA);

        // length should be at least one, otherwise there is no var name in declaration line
        if (split.length < 1) {throw new SYNTAXException(NO_AVR_NAME);}

        for (String str: split){
            int equal = str.indexOf(EQUAL);
            String value = null;
            String name = str;

            // str is the name of the var
            if (equal != -1){
                name = str.split(EQUAL)[0];
                value = str.split(EQUAL)[1];

                // checking value
                if (!this.correctnessChecker.isLegalValue(type, value)) {
                    throw new VALUEException(WRONG_VALUE);
                }
            }
            // checking name
            if (!this.correctnessChecker.isLegalVarName(name)) {
                throw new IDENTIFIERException(WRONG_VAR_NAME);
            }
            this.varTable.addVar(name, finalOrNot, type, value);
        }
    }

    private void compileFunction() {}

    private void compileIfWhileStatement() {}

//    private boolean advanceTokenizer(){
//
//        // checking if can advance tokenizer
//        if (this.tokenizer.hasMoreTokens()) {
//            this.tokenizer.advance();
//            return true;
//        }
//
//        return false;
//
//    }

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

    // todo: need to update this section according to Rephael's method class!!
    private ArrayList<String> declarationHelper(String declaration) throws IDENTIFIERException {
        // the result will be: type name
        String[] pramTypeTmp = declaration.split(FUNC_PARAM_LST_SPLIT_REGEX);

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
                throw new IDENTIFIERException(WRONG_VAR_NAME);
            }
            paramType.add(s);
        }
        return paramType;
    }

}
