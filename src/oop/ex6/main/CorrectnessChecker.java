package oop.ex6.main;

// todo should we use pattern and matcher or use the functionally of string.matches is okay??

//ValidityChecker
//CorrectnessChecker

// TYPES = ["int", "double", "String", "boolean", "char"]
// var name - "^(?!\d)[_]{1,}\w+|[A-Za-z]+\w*"
// int value - "^[-+]{0,1}\d+"
// double value = "^[-+]{0,1}\d+.{0,1}\d*|[-+]{0,1}\d*.{0,1}\d+"
// boolean value = "true|false|^[-+]{0,1}\d+.{0,1}\d*|[-+]{0,1}\d*.{0,1}\d+"
// string value = "^"[\w\W]*""
// char Value = "^'[\w\W]{1}'"

public class CorrectnessChecker {

    //    private static final String VAR_NAME = "^(?!\\d)_+\\w+|[A-Za-z]+\\w*";
    private static final String VAR_NAME = "^(?!(_(?!\\w)|\\d))\\w+";
    private static final String PARAMETERS_DELIMITER = "\\(|,\\s|\\)";
    private static final String METHOD_NAME = "^(?!(_|\\d))\\w+";
    private static final String METHOD_PARAMETERS = "\\((\\w+\\s\\w+)(,\\s\\w+\\s\\w+)*\\)";
    private static final String SINGLE_PARAMETER_DELIMITER = "\\s";

    // values regex
    private static final String INT_VALUE = "^[-+]?\\d+";
    private static final String DOUBLE_VALUE = "^[-+]?\\d+.?\\d*|[-+]?\\d*.?\\d+";
    private static final String BOOLEAN_VALUE = "true|false|^[-+]?\\d+.?\\d*|[-+]?\\d*.?\\d+";
    private static final String STRING_VALUE = "^\"[\\w\\W]*\"";
    private static final String CHAR_VALUE = "^'[\\w\\W]'";

    // line correctness regex
    private static final String COMMENT_START = "//";
    private static final String SPACES = "^\\s+";
    private static final String ONLY_END_SEMICOLON_REGEX = "^[^;]*;\\s*$";
    private static final String ONLY_END_CURLY_BRACES_REGEX = "^[^{]*\\{\\s*$";
    private static final String FUNCTION_DEC_LINE_REGEX = "^.*\\(.*\\)\\s*\\{\\s*$";

    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "String";
    private static final String CHAR = "char";

    private static final String VOID = "void";


    /**
     * A method that verifies whether the argument corresponds to a legal variable name (i.e. starting with letters
     * or with an underscore followed by any character).
     * @param name the variable name to verify.
     * @return ture if correct, false otherwise.
     */
    public boolean isLegalVarName(String name) {return name.matches(VAR_NAME); }

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
    public  boolean isLegalVarType(String varType) {
        return varType.equals(INT) | varType.equals(DOUBLE) | varType.equals(CHAR) | varType.equals(STRING) |
                varType.equals(BOOLEAN);
    }

    /**
     * A method that checks if the given line can be ignored
     * @param line represents the given line
     * @return true if it can be ignored, false otherwise
     */
    public  boolean lineToIgnore(String line){
        return line.startsWith(COMMENT_START) | line.matches(SPACES);
    }

    /**
     * A method that verifies whether the argument corresponds to a legal method name (i.e. starting with letters).
     * @param name the method name to verify.
     * @return true if correct, false otherwise.
     */
    public  boolean legalMethodName(String name) {return name.matches(METHOD_NAME);}

    /**
     * A method that verifies whether the method returns a legal type.
     * In the case of the current exercise, the method shouldn't return anything and therefore should have
     * void as its returType.
     * @param returnType the method return type.
     * @return true if correct, false otherwise.
     */
    public  boolean isLegalMethodReturnType(String returnType) {return returnType.equals(VOID);}

    public  boolean legalMethodParameter(String parameter) {
        String[] delimitedParameter = parameter.split(SINGLE_PARAMETER_DELIMITER);
        String type = delimitedParameter[0];
        String name = delimitedParameter[1];
        return isLegalVarType(type) && isLegalVarName(name);
    }

    public  boolean hasLegalMethodParameters(String parameters) {
        if(parameters.matches(METHOD_PARAMETERS)) {
            String[] singleParameters = parameters.split(PARAMETERS_DELIMITER);
            for(String singleParameter : singleParameters) {
                if(!legalMethodParameter(singleParameter))
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * A method that checks if the line ends legally - with semicolon (;) and has only one semicolon
     * @param line represents the given line
     * @return true if legal, false otherwise
     */
    public boolean legalEndOfLine(String line) {
        return line.matches(ONLY_END_SEMICOLON_REGEX);
    }

    /**
     * A method that checks if the line ends legally - with { and has only one { in it
     * @param line represents the given line to check
     * @return true upos success, false otherwise
     */
    public boolean legalEndOfIfWhileLine(String line) {return line.matches(ONLY_END_CURLY_BRACES_REGEX);}

    public boolean legalFunctionDeclarationLine(String line) {
        return line.matches(FUNCTION_DEC_LINE_REGEX);
    }




}
