package oop.ex6.main.function.handling;

import oop.ex6.main.var.handling.Var;

import java.util.*;

public class FunctionTable {
    private final HashMap<String, Function> functions;

    public FunctionTable(){
        this.functions = new HashMap<>();
    }

    public boolean addFunction(String name, Function function){
        // checks if function already declared before
        if (this.functions.containsKey(name)) {return false;}

        // adds the new func to the table
        this.functions.put(name, function);
        return true;
    }

    public boolean isFuncDeclared (String name){ return this.functions.containsKey(name); }

//    public Function getFunction(String name) {return this.functions.get(name);}

    public boolean isAFuncVar(String funcName, String varName){
        return this.functions.get(funcName).isVarAFuncArgument(varName);
    }

    public String getFuncVarsType(String funcName, String varName) {
        return this.functions.get(funcName).getVarType(varName);
    }

    public List<String> getTypeList(String funcName) {
        Function function = this.functions.get(funcName);
        return function.getTypesList();
    }

    public Set<Map.Entry<String, Var>> varsMap(String name) { return this.functions.get(name).getVarsSet(); }
}
