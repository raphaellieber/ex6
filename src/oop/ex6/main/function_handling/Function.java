package oop.ex6.main.function_handling;

import oop.ex6.main.var_handling.Var;

import java.util.*;

public class Function {
//    public static enum ReturnType {VOID, PRIMITIVE_VARIABLE, REFERENCE_VARIABLE}
//    private final String methodContent;

    private final String name;
    private final String returnType;
    private final HashMap<String, Var> varsMap;  // will map the function arguments to its types
    private final List<String> typesList;

    public Function(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
        this.varsMap = new HashMap<>();
        this.typesList = new ArrayList<>();
    }

    public String getName() { return name; }

    public String getReturnType() { return returnType; }

    public boolean addArgument(String name, String type, boolean finalOrNot) {

        // check if the var is already given in the param list
        if (this.varsMap.containsKey(name)) { return false;}

        // adding the var
        Var var = new Var(finalOrNot, type, null, true);
        this.varsMap.put(name, var);
        this.typesList.add(type);

        return true;
    }

    public boolean isVarAFuncArgument(String name) { return this.varsMap.containsKey(name); }

    public String getVarType(String name) { return this.varsMap.get(name).getType(); }

    public List<String> getTypesList() {return this.typesList;}

    public Set<Map.Entry<String, Var>> getVarsSet() {return this.varsMap.entrySet();}


//    public String getMethodContent() {
//        return methodContent;
//    }

//    public boolean isVoidMethod() {return this.returnType == ReturnType.VOID;}



}
