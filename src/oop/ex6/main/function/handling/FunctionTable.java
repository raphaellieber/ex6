package oop.ex6.main.function.handling;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionTable {
    private final HashMap<String, ArrayList<String>> functions;

    public FunctionTable(){
        this.functions = new HashMap<>();
    }

    public boolean addFunction(String name, ArrayList<String> paramTypes){
        // checks if function already declared before
        if (this.functions.containsKey(name)) {return false;}

        // adds the new func to the table
        this.functions.put(name, paramTypes);
        return true;
    }

    /**
     * A getter for the function's param types array
     * the method considers that the function has already declared!!
     * @param name represents the name of the function
     * @return Arraylist of param types
     */
    public ArrayList<String> getFuncParamTypes(String name) {
        return this.functions.get(name);
    }

    public boolean isFuncDeclared (String name){ return this.functions.containsKey(name); }
}
