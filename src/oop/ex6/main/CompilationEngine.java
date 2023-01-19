package oop.ex6.main;

import oop.ex6.main.function.handling.FunctionTable;
import oop.ex6.main.var.handling.VarTable;

import java.io.BufferedReader;
import java.io.FileReader;
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


    private static final String RETURN_REGEX = "^\\s*return\\s*;\\s*$";
    private static final String END_OF_SCOPE_REGEX = "^\\s*}\\s*$";
    private static final String ASSIGNMENT_REGEX = "^.*=.*";
    private static final String FUNCTION_CALL_REGEX = "^.*\\(.*\\).*";

    // Exceptions messages:
    private static final String WRONG_FUNC_NAME = "Illegal function name";
    private static final String WRONG_VAR_NAME = "Illegal var name";
    private static final String WRONG_VALUE = "Illegal value";
    private static final String NO_AVR_NAME = "No var name";
    private static final String Illegal_TYPE = "Illegal var type";
    private static final String Illegal_END_OF_LINE = "Illegal end of line";
    private static final String ILLEGAL_DECLARATION_LINE = "Illegal declaration line";
    private static final String ILLEGAL_FUNCTION_DECLARATION = "Illegal in function - function declaration";
    private static final String ILLEGAL_IF_WHILE = "If/While declared out of function";
    private static final String ASSIGNING_FINAL_VAR = "Illegal assignment of final var";
    private static final String ILLEGAL_FUNCTION_CALL = "Illegal function call - probably out of function";
    private static final String VAR_ALREADY_DECLARED = "Illegal declaration - the var with same name was" +
            " already declared";

    private static final ArrayList<String> DECLARATION_KEYWORDS = new ArrayList<>() {
        {
            add(INT);
            add(DOUBLE);
            add(BOOLEAN);
            add(STRING);
            add(CHAR);
            add(FINAL);
        }
    };

    private final CorrectnessChecker checker;
    private final VarTable varTable;
    private final FunctionTable functionTable;
    private final BufferedReader reader;
    private final BufferedReader firstRunReader;

    private boolean inFuncFlag;
    private int depthCounter;
//    private Tokenizer tokenizer;

    public CompilationEngine (CorrectnessChecker checker,FunctionTable functionTable,
                              VarTable varTable,  BufferedReader reader, BufferedReader firstRunReader)
                            throws IDENTIFIERException, IOException, SYNTAXException {
        this.checker = checker;
        this.varTable = varTable;
        this.functionTable = functionTable;
        this.reader = reader;
        this.firstRunReader = firstRunReader;
        this.inFuncFlag = true;
        this.depthCounter = 0;

        // Initializing the function table:
        initiateFuncTable();

    }

    public void compile() throws IOException, SYNTAXException, VALUEException, IDENTIFIERException {
        String line;

        while ((line = this.reader.readLine()) != null) {
            // checking if we can ignore the given line
            if (!(this.checker.lineToIgnore(line))) {compileLine(line.trim());}
        }
    }

    private void compileLine(String line) throws SYNTAXException, VALUEException, IDENTIFIERException,
            IOException {
        while (!line.equals("")) {

            int firstSpace = line.indexOf(SPACE);
            String firstWord = "";
            if (firstSpace != -1) { firstWord = line.substring(0, firstSpace);}

            // compiling declaration line
            if (DECLARATION_KEYWORDS.contains(firstWord)) { compileVarDeclaration(line); }

            // compiling declaration of a function
            else {if (firstWord.equals(VOID)) { compileFunctionDeclaration(); }

            // if/while statement
            else {if(line.startsWith(IF) | line.startsWith(WHILE)) { compileIfWhileStatement(line); }


            // end of scope: '}'
            else {if (line.matches(END_OF_SCOPE_REGEX)) {compileEndOfScope();}

            // return;
            else { if (line.matches(RETURN_REGEX)) { compileReturnStatement(); }

            // assignment line (not as declaration)
            else {if (line.matches(ASSIGNMENT_REGEX)) { compileVarAssignment(line); }

            // function call
            else {if (line.matches(FUNCTION_CALL_REGEX)) {compileFunctionCall(line);}}

            }}}}}
        }
    }

    private void compileFunctionCall(String line) throws SYNTAXException {
        // if function was called out of function
        if (!this.inFuncFlag) { throw new SYNTAXException(ILLEGAL_FUNCTION_CALL);}

        // TODO add more general check: check for only one '(', ')' and no more chars after ')' until ';' except spaces
        // end of line check:
        if (!this.checker.legalEndOfLine(line)){ throw new SYNTAXException(Illegal_END_OF_LINE);}

        // slicing name and params list:
        int openRoundBraces = line.indexOf(ROUND_OPEN_BRACE);
        int closeRoundBraces = line.indexOf(ROUND_CLOSE_BRACE);

        String funcName = line.substring(0, openRoundBraces).trim();
        String paramsList = line.substring(openRoundBraces + 1, closeRoundBraces);



        // calling to a function that checks the params:
        // todo

    }

    private void compileReturnStatement() { }

    private void compileVarAssignment(String line) throws IDENTIFIERException, VALUEException,
            SYNTAXException {

        // checking of the validity of end of line
        if (!this.checker.legalEndOfLine(line)) { throw new SYNTAXException(Illegal_END_OF_LINE);}

        line = line.replaceAll(ALL_SPACES_REGEX,"");
        line = line.substring(0,line.length()-1);
        String[] split = line.split(COMMA);

        for (String str: split) {
            String varName = str.split(EQUAL)[0];
            String value = str.split(EQUAL)[1];

            // checking if the var declared
            if (!this.varTable.varDeclared(varName)) { throw new IDENTIFIERException(WRONG_VAR_NAME);}

            // checking if trying to assign final var
            boolean finalOrNot = this.varTable.getVarFinalOrNot(varName);
            if (finalOrNot) {throw new IDENTIFIERException(ASSIGNING_FINAL_VAR); }

            // checking if the vars value is legal, if not, the "value" may be another var
            String type = this.varTable.getVarType(varName);
            if (!this.checker.isLegalValue(type, value)) { value = setVarValueFromTable(type,value);}

            //if reached here the value is legal, so we can assign it
            this.varTable.setValue(varName,value);
        }
    }

    /**
     * A method that compiles the end of the scope
     */
    private void compileEndOfScope() {

        this.varTable.outOfScope();

        // out of if/while scope
        if (this.depthCounter > 0) { this.depthCounter --;}

        // out of function scope
        else { this.inFuncFlag = false; }
    }

    private void compileVarDeclaration(String line) throws SYNTAXException, VALUEException,
            IDENTIFIERException {

        boolean finalOrNot = false;

        if (line.startsWith(FINAL)) {
            finalOrNot = true;

            // checking for valid line beginning (should start with final + " "+)
            int firstSpaceInd = line.indexOf(SPACE);
            if (firstSpaceInd == -1) {throw new SYNTAXException(ILLEGAL_DECLARATION_LINE);}

            line = line.substring(firstSpaceInd + 1).trim(); // trimming final from line
        }

        // checking validity of line beginning and end
        int firstSpaceInd = line.indexOf(SPACE);
        int endOfLine = line.indexOf(SEMICOLON);

        if (firstSpaceInd == -1 | endOfLine == -1 | !this.checker.legalEndOfLine(line)) {
            throw new SYNTAXException(ILLEGAL_DECLARATION_LINE);
        }

        // type check
        String type = line.substring(0, firstSpaceInd);
        if (!this.checker.isLegalVarType(type)) {throw new SYNTAXException(Illegal_TYPE);}

        line = line.substring(firstSpaceInd + 1, endOfLine);
        line = line.replaceAll(ALL_SPACES_REGEX, "");
        compileVarDeclarationParams(finalOrNot, type, line);
    }

    private void compileVarDeclarationParams(boolean finalOrNot, String type, String line)
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

                // checking value validity including looking in var table
                if (!this.checker.isLegalValue(type, value)) {
                    value =  setVarValueFromTable(type, value);
                }
            }
            // checking if the var final -> a value should not be null
            if (finalOrNot & value == null) { throw new SYNTAXException(ILLEGAL_DECLARATION_LINE);}

            // checking name and if the var with same name was already declared
            if (!this.checker.isLegalVarName(name)) { throw new IDENTIFIERException(WRONG_VAR_NAME); }
            if (!this.varTable.varDeclaredInCurScope(name)) {throw new SYNTAXException(VAR_ALREADY_DECLARED);}

            this.varTable.addVar(name, finalOrNot, type, value);
        }
    }

    private String setVarValueFromTable(String type, String varName) throws VALUEException {
        // checking if the var is declared, and its value isn't null
            if (this.varTable.varDeclared(varName) &  this.varTable.getVarValue(varName) != null){

                // checking validity of the vars value
                if (this.checker.isLegalValue(type, this.varTable.getVarValue(varName))){
                    return  this.varTable.getVarValue(varName);
                }
            }

            // otherwise throws exception - wrong value
            throw new VALUEException(WRONG_VALUE);
        }

    private void compileFunctionDeclaration() throws IDENTIFIERException, SYNTAXException,
            IOException, VALUEException {
        // checking if function declared inside another function
        if (inFuncFlag) { throw new SYNTAXException(ILLEGAL_FUNCTION_DECLARATION); }

        // already checked the validity of declaration line in first run
        // so only need to mark that we are inside a function and advance the scope in var table
        this.inFuncFlag = true;
        this.varTable.newScope();

        compile();
    }

    private void compileIfWhileStatement(String line) throws SYNTAXException {
        // if / while out of function
        if (!this.inFuncFlag) { throw new SYNTAXException(ILLEGAL_IF_WHILE); }

        // check if exceeded java.lang.Integer.MAX_VALUE:
        // todo

        // todo need to add ( and ) to the check
        // basic line check (checks for only one '{' and at least one '(', ')':
        if (!this.checker.legalEndOfIfWhileLine(line)) {throw new SYNTAXException(Illegal_END_OF_LINE);}

        // condition check
        // todo

        // advancing counter of depth
        // todo
    }

    private void initiateFuncTable() throws IOException, IDENTIFIERException, SYNTAXException {
        String line;

        while ((line = this.firstRunReader.readLine()) != null) {
            if (line.startsWith(FUNC_DECLARATION_START)){
                int braceStartLoc = line.indexOf(ROUND_OPEN_BRACE);
                int braceFinishLoc = line.indexOf(ROUND_CLOSE_BRACE);
                int spaceLoc = line.indexOf(SPACE);

                // checking validity of the given line
                if (!this.checker.legalFunctionDeclarationLine(line)) {
                    throw  new SYNTAXException(ILLEGAL_FUNCTION_DECLARATION);
                }

                // dividing into 3 sections: func name, params, end of line
                String funcName = line.substring(spaceLoc,braceStartLoc).trim();
                String declaration = line.substring(braceStartLoc+1, braceFinishLoc);
                String endOfLine = line.substring(braceFinishLoc+1).trim();

                // exception for the func name
                if (!this.checker.legalMethodName(funcName)) {
                    throw new IDENTIFIERException(WRONG_FUNC_NAME);
                }

                // exception for wrong end of line
//                if (!endOfLine.equals(CURLY_OPEN_BRACE)) {throw new SYNTAXException(Illegal_END_OF_LINE);}`

                // dealing with param declaration:
                ArrayList<String> paramType = funcParamsCheck(declaration);
                this.functionTable.addFunction(funcName, paramType);
            }
        }
    }

    // todo: need to update this section according to Rephael's method class!!
    // todo this section is already written in checker class? (hasLegalMethodParameters)
    // todo param may be final
    private ArrayList<String> funcParamsCheck(String declaration) throws IDENTIFIERException {
        // the result will be: type name
        String[] pramTypeTmp = declaration.split(FUNC_PARAM_LST_SPLIT_REGEX);

        ArrayList<String> paramType = new ArrayList<>();
        for (String s: pramTypeTmp){

            String type = s.split(SPACE)[0];
            String name = s.split(SPACE)[1];

            // throwing exceptions for var type
            if (!this.checker.isLegalVarType(type)) { throw new IDENTIFIERException(Illegal_TYPE); }

            // throwing exceptions for var name
            if (!this.checker.isLegalVarName(name)) { throw new IDENTIFIERException(WRONG_VAR_NAME); }
            paramType.add(s);
        }
        return paramType;
    }

}
