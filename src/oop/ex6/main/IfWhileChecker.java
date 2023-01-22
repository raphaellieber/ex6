package oop.ex6.main;

import java.util.function.Predicate;

public class IfWhileChecker {
    private final Predicate<String> isInitializedVar;

    private static final String GENERIC_IF_WHILE_PATTERN = "^\\s*(if|while)\\s*\\(.*\\)\\s*\\{\\n";
    private static final String TRUE_FALSE_PATTERN = "\\(\\s*true|false\\s*\\)";
    private static final String INITIALIZED_VAR_PATTERN = "\\(\\s*\\w+\\s*\\)";
    private static final String VALUE_PATTERN = "\\(\\s*-?((\\d+.?\\d*)|(\\d*.?\\d+))\\s*\\)";

    public IfWhileChecker(Predicate<String> isInitializedVar) {
        this.isInitializedVar = isInitializedVar;
    }


    private boolean isLegalCondition(String condition) {
        if(isTrueFalseCondition(condition) || isValueCondition(condition) || isInitializedVarCondition(condition))
            return true;
        return false;
    }

    public boolean hasLegalIfWhilePattern(String line) {
        if(line.matches(GENERIC_IF_WHILE_PATTERN)) {
            int openingBracketIndex = line.indexOf("(");
            int closingBracketIndex = line.lastIndexOf(")");
            String condition = line.substring(openingBracketIndex, closingBracketIndex + 1);
            if(isLegalCondition(condition))
                return true;
        }
        return false;
    }

    private boolean isTrueFalseCondition(String condition) {
        return condition.matches(TRUE_FALSE_PATTERN);
    }

    private boolean isInitializedVarCondition(String condition) {
        if(condition.matches(INITIALIZED_VAR_PATTERN)) {
            if(isInitializedVar.test(condition))
                return true;
        }
        return false;
    }

    private boolean isValueCondition(String condition) {
        return condition.matches(VALUE_PATTERN);
    }

}
