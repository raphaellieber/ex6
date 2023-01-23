package oop.ex6.main.validity_checker;

import java.util.ArrayList;
import java.util.List;

public class CorrectnessChecker {

    // names regex
    private static final String VAR_NAME = "^(?!(_(?!\\w)|\\d))\\w+";
    private static final String METHOD_NAME = "^(?!(_|\\d))\\w+";

    // if/while regex
    private static final String GENERIC_IF_WHILE_PATTERN = "^\\s*(if|while)\\s*\\(.*\\)\\s*\\{";
    private static final String GENERIC_CONDITION_PATTERN = ".+((\\|\\|)?|(&&))*";
    private static final String TRUE_FALSE_PATTERN = "\\s*true|false\\s*";

    // values regex
    private static final String INT_VALUE = "^[-+]?\\d+";
    private static final String DOUBLE_VALUE = "^[-+]?\\d+.?\\d*|[-+]?\\d*.?\\d+";
    private static final String BOOLEAN_VALUE = "true|false|^[-+]?\\d+.?\\d*|[-+]?\\d*.?\\d+|[-+]?\\d+";
    private static final String STRING_VALUE = "^\"[\\w\\W]*\"";
    private static final String CHAR_VALUE = "^'[\\w\\W]'";

    // line correctness regex
    private static final String COMMENT_START = "//";
    private static final String SPACES = "^\\s+";
    private static final String ONLY_END_SEMICOLON_REGEX = "^[^;]*;\\s*$";
    private static final String FUNCTION_DEC_LINE_REGEX = "^.*\\(.*\\)\\s*\\{\\s*$";
    private static final String FUNCTION_CALL_REGEX = "\\w+.*\\(.*\\)\\s*;\\s*$";

    // Keywords
    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "String";
    private static final String CHAR = "char";
    private static final String VOID = "void";
    private static final String WHILE = "while";
    private static final String IF = "if";
    private static final String FINAL = "final";

    // array of s-javac keywords
    private static final ArrayList<String> DECLARATION_KEYWORDS = new ArrayList<>() {
        {
            add(INT);
            add(DOUBLE);
            add(BOOLEAN);
            add(STRING);
            add(CHAR);
            add(FINAL);
            add (VOID);
            add(WHILE);
            add(IF);
        }
    };

    /**
     * A method that verifies whether the argument corresponds to a legal variable name (i.e. starting with letters
     * or with an underscore followed by any character).
     * @param name the variable name to verify.
     * @return ture if correct, false otherwise.
     */
    public boolean isLegalVarName(String name) {
        if (DECLARATION_KEYWORDS.contains(name)) {return false;}
        return name.matches(VAR_NAME);
    }

    /**
     * A function that checks the value correctness depends on a type
     * @param type represents the type, can get: int, double, boolean, String, char
     * @param value represents the value
     * @return true if correct, false otherwise (as well if the given type don't match)
     */
    public  boolean isLegalValue(String type, String value) {

        switch (type){
            case INT: return value.matches(INT_VALUE);
            case DOUBLE: return value.matches(DOUBLE_VALUE);
            case BOOLEAN: return value.matches(BOOLEAN_VALUE);
            case STRING: return value.matches(STRING_VALUE);
            case CHAR: return value.matches(CHAR_VALUE);
        }

        // if the type didn't match
        return false;
    }

    /**
     * A method that checks the validity of a given varType
     * @param varType represents the given var type
     * @return return true is valid, false otherwise
     */
    public boolean isLegalVarType(String varType) {
        return varType.equals(INT) | varType.equals(DOUBLE) | varType.equals(CHAR) | varType.equals(STRING) |
                varType.equals(BOOLEAN);
    }

    /**
     * A method that checks if the given line can be ignored
     * @param line represents the given line
     * @return true if it can be ignored, false otherwise
     */
    public boolean lineToIgnore(String line){
        return line.startsWith(COMMENT_START) | line.matches(SPACES);
    }

    /**
     * A method that verifies whether the argument corresponds to a legal method name (i.e. starting with letters).
     * @param name the method name to verify.
     * @return true if correct, false otherwise.
     */
    public boolean legalMethodName(String name) {
        if (DECLARATION_KEYWORDS.contains(name)) {return false;}
        return name.matches(METHOD_NAME);
    }

    /**
     * A method that checks if the line ends legally - with semicolon (;) and has only one semicolon
     * @param line represents the given line
     * @return true if legal, false otherwise
     */
    public boolean legalEndOfLine(String line) { return line.matches(ONLY_END_SEMICOLON_REGEX); }

    /**
     * checks if a given line is a legal function declaration line
     * @param line represents the line
     * @return true if is, false otherwise
     */
    public boolean legalFunctionDeclarationLine(String line) {
        return line.matches(FUNCTION_DEC_LINE_REGEX);
    }

    /**
     * checks if a given line is a legal function call line
     * @param line represents the line
     * @return true if is, false otherwise
     */
    public boolean legalFunctionCall(String line) { return line.matches(FUNCTION_CALL_REGEX); }

    /**
     * checks if the line has legal if while line pattern
     * @param line represents the given line
     * @return true if is, false otherwise
     */
    public boolean hasLegalIfWhilePattern(String line) {return line.matches(GENERIC_IF_WHILE_PATTERN);}

    /**
     * checks if a given condition is a legal pattern condition
     * @param condition represents the condition
     * @return true if is, false otherwise
     */
    public boolean hasLegalConditionPattern(String condition) {return condition.matches(GENERIC_CONDITION_PATTERN);}

    /**
     * Checks if a given condition is a value condition
     * @param condition represents the condition
     * @return true if is, false otherwise
     */
    public boolean hasValueCondition(String condition) {return condition.matches(DOUBLE_VALUE);}

    /**
     * Checks if a given condition is a true or false condition
     * @param condition represents the condition
     * @return true if is, false otherwise
     */
    public boolean hasTrueFalseCondition(String condition) {return condition.matches(TRUE_FALSE_PATTERN);}

    /**
     * The method checks if receiver type can be assigned by assigner type
     * @param receiver represents the receiver type
     * @param assigner represents the assigner type
     * @return true if is, false otherwise
     */
    public boolean checkEqualTypes(String receiver, String assigner){
        if (receiver.equals(BOOLEAN) & (assigner.equals(INT) | (assigner.equals(DOUBLE)))) { return true; }
        else { if (receiver.equals(DOUBLE) & (assigner.equals(INT))) {return true;}
        else { return receiver.equals(assigner); }}
    }

    /**
     * Checks if all the types in the receiver list can be assigned by values from assigners list by order
     * @param receiverTypeList represents the receiver list
     * @param assignersTypeList represents the assigners list
     * @return true if is, false otherwise
     */
    public boolean checkEqualTypesAll (List<String> receiverTypeList, List<String> assignersTypeList) {

        // checking sizes
        if (receiverTypeList.size() != assignersTypeList.size()) {return false;}

        // checking each type
        for (int i = 0; i < receiverTypeList.size(); i++) {
            if (!checkEqualTypes(receiverTypeList.get(i),assignersTypeList.get(i))) {return false;}
        }
        return true;


    }

}
