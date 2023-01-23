package oop.ex6.main.function_handling;

import oop.ex6.main.var_handling.Var;

import java.util.*;

public class FunctionTable {
    private final HashMap<String, Function> functions;

    /**
     * Constructor
     */
    public FunctionTable(){
        this.functions = new HashMap<>();
    }

    /**
     * Method that adds a function into the function table
     * @param name represents the function name
     * @param function represents the function
     * @return true ups success, false otherwise
     */
    public boolean addFunction(String name, Function function){
        // checks if function already declared before
        if (this.functions.containsKey(name)) {return false;}

        // adds the new func to the table
        this.functions.put(name, function);
        return true;
    }

    /**
     * A checker if a function with a given name was already declared
     * @param name represents the given name
     * @return true if is, false otherwise
     */
    public boolean isFuncDeclared (String name){ return this.functions.containsKey(name); }

    /**
     * A method that checks if a given var name is a function argument
     * @param funcName represents the function name
     * @param varName represents the vars name
     * @return true if it is a function argument, false otherwise
     */
    public boolean isAFuncVar(String funcName, String varName){
        return this.functions.get(funcName).isVarAFuncArgument(varName);
    }

    /**
     * A getter for a argument's type
     * @param funcName represents the function name
     * @param varName represents the vars name
     * @return vars type
     */
    public String getFuncVarsType(String funcName, String varName) {
        return this.functions.get(funcName).getVarType(varName);
    }

    /**
     * returns a list of the function arguments list
     * @param funcName represents the function name
     * @return list of types
     */
    public List<String> getTypeList(String funcName) {
        Function function = this.functions.get(funcName);
        return function.getTypesList();
    }

    /**
     * returns a set of function vars
     * @param name represents the function name
     * @return set of vars
     */
    public Set<Map.Entry<String, Var>> varsMap(String name) { return this.functions.get(name).getVarsSet(); }
}
