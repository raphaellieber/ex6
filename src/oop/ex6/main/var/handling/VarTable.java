package oop.ex6.main.var.handling;

import java.util.*;

public class VarTable {

    private final LinkedList<HashMap<String, Var>> vars;

    private int scopeInd;

    public VarTable(){
//        this.globalVars = new HashMap<>();

        this.vars = new LinkedList<>();

        // initializing hashmap for first scope - global scope
        this.vars.addFirst(new HashMap<>());
        this.scopeInd = 0;
    }

    /**
     * A method that checks if a var is in the table, if not, ads it and returns true, false otherwise
     * @param name represents the name of the var
     * @param finalOrNot a boolean that represents if a var is final var (true) or not (false)
     * @param type String that represents vars type
     * @param value String that represents vars value, may be null
     * @return true upon success, false otherwise
     */
    public boolean addVar(String name, boolean finalOrNot, String type, String value,
                          boolean functionalParam){

        // check if the var is already in the table at the same scope:
        if (varDeclaredInCurScope(name)) { return false;}

        // creating the var according to the given params
        Var var = new Var(finalOrNot, type, value, functionalParam);

        // inserting into the vars table at the last scope
        this.vars.getLast().put(name, var);

        return true;
    }

    public void addAll(Set<Map.Entry<String, Var>> varSet) {
        for (Map.Entry<String, Var> v: varSet) { this.vars.getLast().put(v.getKey(), v.getValue());}
        }

    /**
     * A method that checks if a var is already declared in current scope
     * @param name represents the name of the var
     * @return true if is already declared, false otherwise
     */
    public boolean varDeclaredInCurScope(String name) {
        return this.vars.getLast().containsKey(name);
    }

    /**
     * A method that returns the wanted info about the var
     * !!!! method assumes the var is in the table !!!!
     * @param name a String that represents the vars name
     * @return returns the wanted info of the var from the table
     */
    public Boolean getVarFinalOrNot (String name) {

        // checking the table in reversed order
        Iterator<HashMap<String, Var>> iterator = this.vars.descendingIterator();
        while( iterator.hasNext()) {
            for (Map.Entry<String, Var> entry: iterator.next().entrySet()){
                if (entry.getKey().equals(name)) { return entry.getValue().finalOrNot();}
            }
        }
        return null;
    }

    /**
     * A method that returns the wanted info about the var
     * !!!! method assumes the var is in the table !!!!
     * @param name a String that represents the vars name
     * @return returns the wanted info of the var from the table
     */
    public String getVarType (String name) {

        // checking the table in reversed order
        Iterator<HashMap<String, Var>> iterator = this.vars.descendingIterator();
        while( iterator.hasNext()) {
            for (Map.Entry<String, Var> entry: iterator.next().entrySet()){
                if (entry.getKey().equals(name)) { return entry.getValue().getType();}
            }
        }
        return null;
    }

    /**
     * A method that returns the wanted info about the var
     * !!!! method assumes the var is in the table !!!!
     * @param name a String that represents the vars name
     * @return returns the wanted info of the var from the table
     */
    public String getVarValue (String name) {

        // checking the table in reversed order
        Iterator<HashMap<String, Var>> iterator = this.vars.descendingIterator();
        while( iterator.hasNext()) {
            for (Map.Entry<String, Var> entry: iterator.next().entrySet()){
                if (entry.getKey().equals(name)) { return entry.getValue().getValue();}
            }
        }
        return null;
    }

    /**
     * A method that updates the scope counter
     */
    public void newScope() {
        this.scopeInd ++;
        this.vars.addLast(new HashMap<>());
    }

    /**
     * A method that updates the scope counter
     */
    public boolean outOfScope() {
        if (this.scopeInd == 0) { return false;}

        this.scopeInd --;
        this.vars.removeLast();
        return true;
    }

    /**
     * A method that checks if a var is in the table
     * @param name represents the name of the var
     * @return true if is, false otherwise
     */
    public boolean varDeclared(String name) {

        // looking for the var in the table
        for (HashMap<String, Var> map :this.vars) {
            if (map.containsKey(name)) { return true;}
        }

        // if not found
        return false;
    }

    /**
     * A method that allows to set the value of the var
     * @param name represents the var name
     * @param value represents the given value
     * @return true upon success(if found the var and is not final) false otherwise
     */
    public boolean setValue(String name, String value){
        if (varDeclared(name)) {

            Iterator<HashMap<String, Var>> iterator = this.vars.descendingIterator();
            while (iterator.hasNext()) {
                for (Map.Entry<String, Var> entry : iterator.next().entrySet()) {
                    if (entry.getKey().equals(name)) {

                        // if found the var and it is a final var
                        if (entry.getValue().finalOrNot()) { return false;}

                        // otherwise, updating the value
                        entry.getValue().setValue(value);
                        return true;
                    }
                }
            }
        }
        // if not found the var at all
        return false;
    }

    /**
     * A getter for the information if the var was initialed by func param
     * The var should be inside the func!!!!
     * @param name represents the vars name
     * @return true if is, false otherwise
     */
    public boolean isAFuncParam(String name) {
        // checking the table in reversed order
        Iterator<HashMap<String, Var>> iterator = this.vars.descendingIterator();
        while( iterator.hasNext()) {
            for (Map.Entry<String, Var> entry: iterator.next().entrySet()){
                if (entry.getKey().equals(name)) { return entry.getValue().isAFuncParam();}
            }
        }
        return false;
    }

    /**
     * A setter for the information if the var was initialed by func param
     * the function checks if it is legal to set the new value (if the var is not final)
     * @param name represents the vars name
     * @param valueToChange represents the value to set
     * @return true if succeeded, false otherwise
     */
    public boolean setAFuncParam(String name, boolean valueToChange) {
        // checking the table in reversed order
        Iterator<HashMap<String, Var>> iterator = this.vars.descendingIterator();
        while( iterator.hasNext()) {
            for (Map.Entry<String, Var> entry: iterator.next().entrySet()){
                if (entry.getKey().equals(name)) {

                    if (entry.getValue().finalOrNot()) { return false;}

                    entry.getValue().setAFuncParam(valueToChange);
                    return true;
                }
            }
        }
        return false;
    }
}
