package oop.ex6.main.function_handling;

import oop.ex6.main.var_handling.Var;

import java.util.*;

public class Function {

    private final String name;
    private final String returnType;
    private final HashMap<String, Var> varsMap;  // will map the function arguments to its types
    private final List<String> typesList;

    /**
     * Constructor
     * @param name represents the function name
     * @param returnType represents the function type
     */
    public Function(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
        this.varsMap = new HashMap<>();
        this.typesList = new ArrayList<>();
    }

    /**
     * A getter for the function name
     * @return function name
     */
    public String getName() { return name; }

    /**
     * A getter for the function type
     * @return function type
     */
    public String getReturnType() { return returnType; }

    /**
     * A method that adds an argument to the functions' arguments
     * @param name represents the args name
     * @param type represents the args type
     * @param finalOrNot represents if the arg is final or not
     * @return true upon success, false otherwise
     */
    public boolean addArgument(String name, String type, boolean finalOrNot) {

        // check if the var is already given in the param list
        if (this.varsMap.containsKey(name)) { return false;}

        // adding the var
        Var var = new Var(finalOrNot, type, null, true);
        this.varsMap.put(name, var);
        this.typesList.add(type);

        return true;
    }

    /**
     * A method that checks if a given var name is a function argument
     * @param name represents the vars name
     * @return true if it is a function argument, false otherwise
     */
    public boolean isVarAFuncArgument(String name) { return this.varsMap.containsKey(name); }

    /**
     * A getter for a argument's type
     * @param name represents the argument's name
     * @return argument's type
     */
    public String getVarType(String name) { return this.varsMap.get(name).getType(); }

    /**
     * returns a list of the function arguments list
     * @return list of types
     */
    public List<String> getTypesList() {return this.typesList;}

    /**
     * returns a set of function vars
     * @return set of vars
     */
    public Set<Map.Entry<String, Var>> getVarsSet() {return this.varsMap.entrySet();}

}
