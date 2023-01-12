package oop.ex6.main;

// TYPES = ["int", "double", "String", "boolean", "char"]
// var name - "^(?!\d)[_]{1,}\w+|[A-Za-z]+\w*"
// int value - "^[-+]{0,1}\d+"
// double value = "^[-+]{0,1}\d+.{0,1}\d*|[-+]{0,1}\d*.{0,1}\d+"
// boolean value = "true|false|^[-+]{0,1}\d+.{0,1}\d*|[-+]{0,1}\d*.{0,1}\d+"
// string value = "^"[\w\W]*""
// char Value = "^'[\w\W]{1}'"

public class CorrectnessChecker {

    private static final String VAR_NAME= "^(?!\\d)_+\\w+|[A-Za-z]+\\w*";
    private static final String INT_VALUE = "^(?!\\d)_+\\w+|[A-Za-z]+\\w*";
    private static final String DOUBLE_VALUE = "^[-+]?\\d+.?\\d*|[-+]?\\d*.?\\d+";
    private static final String BOOLEAN_VALUE = "true|false|^[-+]?\\d+.?\\d*|[-+]?\\d*.?\\d+";
    private static final String STRING_VALUE = "^\"[\\w\\W]*\"";
    private static final String CHAR_VALUE = "^'[\\w\\W]'";

    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "String";
    private static final String CHAR = "char";




    /**
     * A function that checks the var name correctness of a given name
     * @param name represents the given name
     * @return ture if correct, false otherwise
     */
    public boolean checkVarName(String name) {return name.matches(VAR_NAME); }

    /**
     * A function that checks the value correctness depends on a type
     * @param type represents the type, can get: int, double, boolean, String, char
     * @param value represents the value
     * @return true if correct, false otherwise (as well if the given type don't match)
     */
    public boolean checkValue(String type, String value) {

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




}
