package oop.ex6.main;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class MethodChecker {
    private final Predicate<String> isLegalVarType;
    private final Predicate<String> isLegalVarName;
    private final Supplier<String> variableNamePattern;
    private static final String METHOD_DECLARATION_RETURN_TYPE_PATTERN = "void";
    private static final String METHOD_NAME_PATTERN = "^(?!(_|\\d))\\w+";
    private static final String METHOD_DECLARATION_PARAMETERS_PATTERN = "\\((\\s*\\w+\\s\\w+)(,\\s\\w+\\s\\w+)*\\)";
    private static final String METHOD_DECLARATION_INIT_PATTERN =
            "^\\s*" + METHOD_DECLARATION_RETURN_TYPE_PATTERN + "\\s+" + METHOD_NAME_PATTERN +
                    "\\s*" + METHOD_DECLARATION_PARAMETERS_PATTERN + "\\s*\\{";
    private static final String SINGLE_PARAMETER_DECLARATION_DELIMITER = "\\s";
    private static final String PARAMETERS_DECLARATION_DELIMITER = "\\(|,\\s|\\)";
    private static final String METHOD_CALLING_PATTERN = "\\b" + METHOD_NAME_PATTERN + "\\s+";
    private final String methodCallingParametersPattern;

    public MethodChecker(Supplier<String> variableNamePattern, Predicate<String> isLegalVarType,
                         Predicate<String> isLegalVarName) {
        this.variableNamePattern = variableNamePattern;
        this.isLegalVarType = isLegalVarType;
        this.isLegalVarName = isLegalVarName;
        methodCallingParametersPattern = "\\(\\s*" + variableNamePattern.get() + "(\\s*,)";
        String temp = "\\(\\s*(" + variableNamePattern.get() + "\\s*(,\\s*" +
                variableNamePattern.get() + "\\s*)*\\s*)??\\)";
    }

    /**
     * A method that verifies whether a single line contains a method declaration pattern of the form:
     *      "returnType methodName (parameters) {".
     * @param line the line to evaluate.
     * @return true if correct, false otherwise.
     */
    public boolean hasMethodDeclarationPattern(String line) {return line.matches(METHOD_DECLARATION_INIT_PATTERN);}

    /**
     * A method that verifies whether the method returns a legal type.
     * In the case of the current exercise, the method shouldn't return anything and therefore should have
     * void as its returnType.
     * @param returnType the method return type.
     * @return true if correct, false otherwise.
     */
    public boolean isLegalMethodDeclarationReturnType(String returnType) {
        return returnType.matches(METHOD_DECLARATION_RETURN_TYPE_PATTERN);
    }

    /**
     * A method that verifies whether the argument corresponds to a legal method name (i.e. starting with letters).
     * @param name the method name to verify.
     * @return true if correct, false otherwise.
     */
    public boolean isLegalMethodDeclarationName(String name) {return name.matches(METHOD_NAME_PATTERN);}


    public boolean hasLegalMethodDeclarationParameters(String parameters) {
        if(parameters.matches(METHOD_DECLARATION_PARAMETERS_PATTERN)) {
            String[] singleParameters = parameters.split(PARAMETERS_DECLARATION_DELIMITER);
            for(String singleParameter : singleParameters) {
                if(!isLegalMethodDeclarationParameter(singleParameter))
                    return false;
            }
            return true;
        }
        return false;
    }

    public boolean isLegalMethodDeclarationParameter(String parameter) {
        String[] delimitedParameter = parameter.split(SINGLE_PARAMETER_DECLARATION_DELIMITER);
        String type = delimitedParameter[0];
        String name = delimitedParameter[1];
        return isLegalVarType.test(type) && isLegalVarName.test(name);
    }
}
