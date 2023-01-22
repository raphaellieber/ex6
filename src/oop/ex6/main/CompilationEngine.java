package oop.ex6.main;

import oop.ex6.main.function.handling.Function;
import oop.ex6.main.function.handling.FunctionTable;
import oop.ex6.main.var.handling.VarTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private static final String ROUND_OPEN_BRACE = "(";
    private static final String ROUND_CLOSE_BRACE = ")";
    //    private static final String CURLY_OPEN_BRACE = "{";
    //    private static final String CURLY_CLOSE_BRACE = "}";
    private static final String SPACE = " ";
    private static final String SEMICOLON = ";";
    private static final String COMMA = ",";
    private static final String EQUAL = "=";
    private static final String FUNC_PARAM_LST_SPLIT_REGEX = "\\s*,\\s*";
    private static final String ALL_SPACES_REGEX = "\\s";

    private static final String FUNC_DECLARATION_START = "void ";
    private static final String RETURN_REGEX = "^\\s*return\\s*;\\s*$";
    private static final String END_OF_SCOPE_REGEX = "^\\s*}\\s*$";
    private static final String ASSIGNMENT_REGEX = "^.*=.*";
    private static final String FUNCTION_CALL_REGEX = "^.*\\(.*\\).*";

    private static final String CONDITION_DELIMITER = "(\\|\\|)|(&&)";

    // Exceptions messages:
    private static final String ILLEGAL_FUNC_NAME = "Illegal function name";
    private static final String ILLEGAL_VAR_NAME = "Illegal var name";
    private static final String ILLEGAL_VALUE = "Illegal value";
    private static final String NO_VAR_NAME = "No var name";
    private static final String ILLEGAL_TYPE = "Illegal var type";
    private static final String ILLEGAL_END_OF_LINE = "Illegal end of line";
    private static final String ILLEGAL_DECLARATION_LINE = "Illegal declaration line";
    private static final String ILLEGAL_FUNCTION_DECLARATION = "Illegal in function - function declaration";
    private static final String IF_WHILE_OUT_OF_METHOD = "If/While declared out of method";
    private static final String IF_WHILE_EXCEEDING_DEPTH = "If/While statement exceeding max depth";
    private static final String ILLEGAL_IF_WHILE = "If/While declared out of function";
//    private static final String ASSIGNING_FINAL_VAR = "Illegal assignment of final var";
    private static final String ILLEGAL_CONDITION = "Illegal condition used in if/while statement.";
    private static final String ILLEGAL_FUNCTION_CALL = "Illegal function call";
    private static final String ILLEGAL_NUM_OF_PARAMS = "Given illegal number of params";
    private static final String ILLEGAL_ASSIGNMENT = "Illegal assignment of var, may be final or undeclared";
    private static final String UNDECLARED_PARAM = "Used undeclared param";
    private static final String VAR_ALREADY_DECLARED = "Var with same name was declared in this scope";
    private static final String FUNC_ALREADY_DECLARED = "Function with same name was declared in this scope";

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
    private long scopeDepthIfWhile;
    private String currentFuncName;

    public CompilationEngine (CorrectnessChecker checker, FunctionTable functionTable,
                              VarTable varTable,  BufferedReader reader, BufferedReader firstRunReader)
                            throws IDENTIFIERException, IOException, SYNTAXException {
        this.checker = checker;
        this.varTable = varTable;
        this.functionTable = functionTable;
        this.reader = reader;
        this.firstRunReader = firstRunReader;
        this.inFuncFlag = true;
        this.scopeDepthIfWhile = 0L;
        this.currentFuncName = null;

        // Initializing the function table:
        initiateFuncTable();

    }


    /**
     * A method that compiles the file
     * @throws IOException Exception for using the BufferReader - can't read from the file
     * @throws SYNTAXException Exception for wrong syntax
     * @throws VALUEException Exception for wrong value
     * @throws IDENTIFIERException Exception for wrong identifier (type, var name or func name)
     * @throws SCOPEException Exception for exceeding the allowed scope depth - Integer.MAX_VALUE
     */
    public void compile() throws IOException, SYNTAXException, VALUEException, IDENTIFIERException, SCOPEException {
        String line;

        while ((line = this.reader.readLine()) != null) {
            // checking if we can ignore the given line
            if (!(this.checker.lineToIgnore(line))) {compileLine(line.trim());}
        }
    }


    /**
     * A method that compiles one line
     * @param line represents the line
     * @throws SYNTAXException Exception for wrong syntax
     * @throws VALUEException Exception for wrong value
     * @throws IDENTIFIERException Exception for wrong identifier (type, var name or func name)
     * @throws SCOPEException Exception for exceeding the allowed scope depth - Integer.MAX_VALUE
     */
    private void compileLine(String line) throws SYNTAXException, VALUEException, IDENTIFIERException,
            SCOPEException {
        while (!line.equals("")) {

            int firstSpace = line.indexOf(SPACE);
            String firstWord = "";
            if (firstSpace != -1) { firstWord = line.substring(0, firstSpace);}

            // compiling declaration line
            if (DECLARATION_KEYWORDS.contains(firstWord)) { compileVarDeclaration(line); }

            // compiling declaration of a function
            else {if (firstWord.equals(VOID)) { compileFunctionDeclaration(line); }

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


    /**
     * A method that charge of compiling a function call
     * @param line represents the function call line
     * @throws SYNTAXException Exception for wrong syntax
     * @throws IDENTIFIERException Exception for wrong identifier (type, var name or func name)
     */
    private void compileFunctionCall(String line) throws SYNTAXException, IDENTIFIERException {
        // if function was called out of function
        if (!inFunctionScope()) { throw new SYNTAXException(ILLEGAL_FUNCTION_CALL);}

        // check of function call line correctness: checks for: existence of '(', ')', ';'
        if (!this.checker.legalFunctionCall(line)){ throw new SYNTAXException(ILLEGAL_FUNCTION_CALL);}

        // slicing name and params list:
        int openRoundBraces = line.indexOf(ROUND_OPEN_BRACE);
        int closeRoundBraces = line.indexOf(ROUND_CLOSE_BRACE);

        String funcName = line.substring(0, openRoundBraces).trim();
        String paramsList = line.substring(openRoundBraces + 1, closeRoundBraces);
        String[] splitParam = paramsList.split(ALL_SPACES_REGEX);

        // checking the name
        if (!this.functionTable.isFuncDeclared(funcName)) { throw new IDENTIFIERException(ILLEGAL_FUNC_NAME);}

        // checking the vars and getting their types
        List<String> paramsTypeList = checkFuncCallParamList(funcName, splitParam);

        // checking if number of types is sufficient and the types matches to the function params list
        if (!this.functionTable.checkParamListMatchesFunction(funcName,paramsTypeList)){
            throw new SYNTAXException(ILLEGAL_NUM_OF_PARAMS);
        }

    }


    /**
     * A helper method that deals with function call param list, the method checks that all the vars are
     * declared and makes a list of their types to compare with function param list types
     * @param funcName represents the function name
     * @param splitParam represents the list of params
     * @return A list of types of the params
     * @throws SYNTAXException Exception for wrong syntax
     */
    private List<String> checkFuncCallParamList(String funcName, String[] splitParam) throws SYNTAXException {

        List<String> paramsTypeList = new ArrayList<>();
        for (String name: splitParam){
            String type;

            // looking first in function params list
            if (this.functionTable.isAFuncVar(funcName,name)) {
                type = this.functionTable.getFuncVarsType(funcName,name);
            }

            // looking in the var table and checking if the param has value
            else { if (this.varTable.varDeclared(name) & this.varTable.getVarValue(name) != null) {
                type = this.varTable.getVarType(name);
            }

            // param not found
            else { throw new SYNTAXException(UNDECLARED_PARAM); }}

            paramsTypeList.add(type);
        }
        return paramsTypeList;
    }


    /**
     * A method that compiles return statement
     */
    private void compileReturnStatement() { }


    /**
     * A method that compiles assignment of var
     * @param line represents the assignment line
     * @throws VALUEException Exception for wrong value
     * @throws SYNTAXException Exception for wrong syntax
     */
    private void compileVarAssignment(String line) throws VALUEException, SYNTAXException {
        // checking of the validity of end of line
        if (!this.checker.legalEndOfLine(line)) { throw new SYNTAXException(ILLEGAL_END_OF_LINE);}

        line = line.replaceAll(ALL_SPACES_REGEX,"");
        line = line.substring(0,line.length()-1);  // slicing ';' from end
        String[] split = line.split(COMMA);

        for (String str: split) {
            String varName = str.split(EQUAL)[0];
            String value = str.split(EQUAL)[1];

            // checking if the vars value is legal, if not, the "value" may be another var
            String type = this.varTable.getVarType(varName);
            if (!this.checker.isLegalValue(type, value)) { value = setVarValueFromVarTable(type,value);}

            // setting the value of the var -> checks as well if the var declared and is not final
            if (!this.varTable.setValue(varName, value)){ throw new SYNTAXException(ILLEGAL_ASSIGNMENT); }

            // if the value is null and reached here means that it was assigned by legal value by funcParam
            if (value == null & !this.varTable.setAFuncParam(varName, true)) {
                throw new SYNTAXException(ILLEGAL_ASSIGNMENT);
            }

//            // checking if the var declared
//            if (!this.varTable.varDeclared(varName)) { throw new IDENTIFIERException(UNDECLARED_PARAM);}
//
//            // checking if trying to assign final var
//            boolean finalOrNot = this.varTable.getVarFinalOrNot(varName);
//            if (finalOrNot) {throw new IDENTIFIERException(ASSIGNING_FINAL_VAR); }


//            // if value isn't null it's a legal value, given in the beginning or from setVarValueFromVarTable
//            if (value != null) { this.varTable.setValue(varName, value); }

//            // the value is irrelevant from now on because was assigned by legal value by funcParam
//            else {
//                this.varTable.setValue(varName, value);
//                this.varTable.setAFuncParam(varName, true);
//            }
        }
    }

    
    /**
     * A method that compiles the end of the scope
     */
    private void compileEndOfScope() {

        this.varTable.outOfScope();

        // out of if/while scope
        if (inIfWhileScope()) { decreaseIfWhileScopeDepth();}

        // out of function scope
        else if(inFunctionScope()) {
            exitFunctionScope();
            this.currentFuncName = null;
        }
    }


    /**
     * A method that compiles var declaration line
     * @param line represents the declaration line
     * @throws SYNTAXException Exception for wrong syntax
     * @throws VALUEException Exception for wrong value
     * @throws IDENTIFIERException Exception for wrong identifier (type, var name or func name)
     */
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
        if (!this.checker.isLegalVarType(type)) {throw new SYNTAXException(ILLEGAL_TYPE);}

        line = line.substring(firstSpaceInd + 1, endOfLine);
        line = line.replaceAll(ALL_SPACES_REGEX, "");
        compileVarDeclarationParams(finalOrNot, type, line);
    }

    /**
     * A helper method to the compileVarDeclaration that deals with compiling the params part
     * @param finalOrNot represents if the params in the given line are final or not
     * @param type represents the type of the params in the line
     * @param line represents the params line
     * @throws SYNTAXException Exception for wrong syntax
     * @throws VALUEException Exception for wrong value
     * @throws IDENTIFIERException Exception for wrong identifier (type, var name or func name)
     */
    private void compileVarDeclarationParams(boolean finalOrNot, String type, String line)
            throws SYNTAXException, VALUEException, IDENTIFIERException {
        String[] split = line.split(COMMA);

        // length should be at least one, otherwise there is no var name in declaration line
        if (split.length < 1) { throw new SYNTAXException(NO_VAR_NAME); }

        for (String str : split) {
            boolean funcParam = false;
            String value = null;
            String name = str;

            // if there is an assignment inside the declaration
            if (name.contains(EQUAL)) {
                name = str.split(EQUAL)[0];
                value = str.split(EQUAL)[1];

                // if the value isn't valid, means it's another var, looking in the varTable
                if (!this.checker.isLegalValue(type, value)) { value = setVarValueFromVarTable(type, value); }

                // checking if a func param
                funcParam = this.varTable.isAFuncParam(name);
                }

            // checks var validity
            checkVarValidityHelper(finalOrNot, funcParam, value, name);

            if (!this.varTable.addVar(name, finalOrNot, type, value, funcParam)) {
                throw new SYNTAXException(VAR_ALREADY_DECLARED);
            }

        }
    }


    /**
     * A validity checker for a param
     * @param finalOrNot represents if the var is final or not
     * @param assignedByFuncParam represents if the var was assigned by a func parameter
     * @param value represents the vars name
     * @param name represents the vars name
     * @throws SYNTAXException Exception for wrong syntax
     * @throws IDENTIFIERException Exception for wrong identifier (type, var name or func name)
     */
    private void checkVarValidityHelper(boolean finalOrNot, boolean assignedByFuncParam, String value,
                                        String name) throws SYNTAXException, IDENTIFIERException {
        // checking if the var final -> a value should be assigned or should be assigned by func param
        if (finalOrNot & !assignedByFuncParam & value == null) {
            throw new SYNTAXException(ILLEGAL_DECLARATION_LINE);
        }

        // checking name and if the var with same name was already declared
        if (!this.checker.isLegalVarName(name)) { throw new IDENTIFIERException(ILLEGAL_VAR_NAME); }
//        if (!this.varTable.varDeclaredInCurScope(name)) { throw new SYNTAXException(VAR_ALREADY_DECLARED); }
    }


    /**
     * A method that checks the var table for a given var name and checks if the vars type matches given type
     * @param type represents the type we want to match to
     * @param varName represents the var name
     * @return A string that represents the vars value, may be null
     * @throws VALUEException Exception for wrong value
     */
    private String setVarValueFromVarTable(String type, String varName) throws VALUEException {

        // checking if the var is declared
        if (!this.varTable.varDeclared(varName)) { throw new VALUEException(ILLEGAL_VALUE);}

        // todo add a function in the checker that checks for type match
        // if the var is a func param, no meaning to its value, need to check match types
        if (this.varTable.isAFuncParam(varName) & this.varTable.getVarType(type).equals(type)) {
            return null;
        }

        // if vars value isn't null
        if ( this.varTable.getVarValue(varName) != null) {

            // checking validity of the vars value, if valid returns it
            if (this.checker.isLegalValue(type, this.varTable.getVarValue(varName))) {
                return this.varTable.getVarValue(varName);
            }
        }
        // otherwise throws exception - wrong value
        throw new VALUEException(ILLEGAL_VALUE);
    }


    /**
     * A method that is in charge of compiling function declaration
     * @param line represents the function declaration line
     * @throws SYNTAXException Exception for wrong syntax
     */
    private void compileFunctionDeclaration(String line) throws SYNTAXException {

        // checking if function declared inside another function
        if (inFuncFlag) { throw new SYNTAXException(ILLEGAL_FUNCTION_DECLARATION); }

        //trimming the name of the func
        int roundOpenBraceLoc = line.indexOf(ROUND_OPEN_BRACE);
        int firstSpaceLoc = line.indexOf(SPACE);
        this.currentFuncName = line.substring(firstSpaceLoc,roundOpenBraceLoc).trim();

        enterFunctionScope();
        this.varTable.newScope();

        // adding func params to the var table:
        this.varTable.addAll(this.functionTable.varsMap(this.currentFuncName));

    }


    /**
     * A checker if we are inside of if while scope
     * @return true if we are inside, false otherwise
     */
    private boolean inIfWhileScope() {return this.scopeDepthIfWhile > 0;}


    /**
     * Increases the scope counter
     */
    private void increaseScopeDepthIfWhile() {this.scopeDepthIfWhile++;}


    /**
     * decreases the scope counter
     */
    private void decreaseIfWhileScopeDepth() {this.scopeDepthIfWhile--;}


    /**
     * A checker if we are inside a method scope
     * @return true if we are in a function scope, false otherwise
     */
    private boolean inFunctionScope() {return this.inFuncFlag;}


    /**
     * sets the function flag to true
     */
    private void enterFunctionScope() {this.inFuncFlag = true;}


    /**
     * sets the function flag to false
     */
    private void exitFunctionScope() {this.inFuncFlag = false;}


    /**
     * Checker for exceeding the allowed scope depth - Integer.MAX_VALUE
     * @return true if exceeded, false otherwise
     */
    private boolean exceededMaxScopeDepth() {return this.scopeDepthIfWhile > Integer.MAX_VALUE;}

    private boolean hasValidIfWhileCondition(String condition) {
        String[] conditions = condition.split(CONDITION_DELIMITER);
        for (String singleCondition : conditions) {
            if (!isValidSingleCondition(singleCondition))
                return false;
        }
        return true;
    }

    private boolean isValidSingleCondition(String singleCondition) {
        return this.checker.hasTrueFalseCondition(singleCondition) || this.checker.hasValueCondition(singleCondition)
                || legalIfWhileInitializedVarCondition(singleCondition);
    }

    private boolean legalIfWhileInitializedVarCondition(String condition) {
        return this.checker.hasInitializedVarCondition(condition) && varTable.varDeclared(condition) && (
                varTable.getVarType(condition).equals(BOOLEAN) | varTable.getVarType(condition).equals(DOUBLE) ||
                        varTable.getVarType(condition).equals(INT));
    }


    /**
     * A method that in charge of compiling if while statement
     * * @param line
     * @throws SYNTAXException Exception for wrong syntax
     * @throws SCOPEException Exception for exceeding the allowed scope depth - Integer.MAX_VALUE
     */
    private void compileIfWhileStatement(String line) throws SYNTAXException, SCOPEException {
        // if / while out of function
        if (!inFunctionScope()) { throw new SYNTAXException(IF_WHILE_OUT_OF_METHOD); }

        // check if exceeded java.lang.Integer.MAX_VALUE:
        if (exceededMaxScopeDepth()) { throw new SCOPEException(IF_WHILE_EXCEEDING_DEPTH);}

        // todo need to add ( and ) to the check
        // basic line check (checks for only one '{' and at least one '(', ')':
        if (!this.checker.legalEndOfIfWhileLine(line)) {throw new SYNTAXException(ILLEGAL_END_OF_LINE);}

        // condition check
        if(this.checker.hasLegalIfWhilePattern(line)) {
            int openingBracketIndex = line.indexOf("(");
            int closingBracketIndex = line.lastIndexOf(")");
            String condition = line.substring(openingBracketIndex + 1, closingBracketIndex);
            if(checker.hasLegalConditionPattern(condition))
                if(this.hasValidIfWhileCondition(condition))
                    increaseScopeDepthIfWhile();
        }
        throw new SYNTAXException(ILLEGAL_CONDITION);
        //TODO: add possibility to have multiple conditions using || and &&
    }


    /**
     * A method that initializing the FuncTable by running on the file and compiling only function declaration
     * lines
     * @throws IOException Exception for using the BufferReader - can't read from the file
     * @throws IDENTIFIERException Exception for wrong identifier (type, var name or func name)
     * @throws SYNTAXException Exception for wrong syntax
     */
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

                // dividing into 2 sections: func name, params
                String funcName = line.substring(spaceLoc,braceStartLoc).trim();
                String paramList = line.substring(braceStartLoc+1, braceFinishLoc);

                // exception for the func name
                if (!this.checker.legalMethodName(funcName)) {
                    throw new IDENTIFIERException(ILLEGAL_FUNC_NAME);
                }

                // creating the function object and dealing with its params list:
                Function function = new Function(funcName, VOID);
                funcDeclarationParamsListCheck(paramList, function);

                if (!this.functionTable.addFunction(funcName, function)) {
                    throw new SYNTAXException(FUNC_ALREADY_DECLARED);
                }
            }
        }
    }


    /**
     * A helper function that deals with compiling the params list of compiling function line
     * @param declaration represents the param list part of the declaration
     * @param func represents the function object
     * @throws IDENTIFIERException Exception for wrong identifier (type, var name or func name)
     * @throws SYNTAXException Exception for wrong syntax
     */
    private void funcDeclarationParamsListCheck(String declaration, Function func) throws IDENTIFIERException,
            SYNTAXException {
        // todo general check for the line correctness!!
        // the result will be: type name
        String[] pramTypeTmp = declaration.split(FUNC_PARAM_LST_SPLIT_REGEX);

        for (String s: pramTypeTmp){
            boolean finalOrNot = false;

            String[] split = s.split(SPACE);

            // checking if final or not
            // todo -> no need of verifying the word is final because of the general check of the line
            if (split.length == 3) { finalOrNot = true; }

            String type = s.split(SPACE)[split.length - 2];
            String name = s.split(SPACE)[split.length - 1];

            // throwing exceptions for var type
            if (!this.checker.isLegalVarType(type)) { throw new IDENTIFIERException(ILLEGAL_TYPE); }

            // throwing exceptions for var name
            if (!this.checker.isLegalVarName(name)) { throw new IDENTIFIERException(ILLEGAL_VAR_NAME); }

            if (!func.addArgument(name, type, finalOrNot)) {throw new SYNTAXException(VAR_ALREADY_DECLARED);}
        }
    }

}
