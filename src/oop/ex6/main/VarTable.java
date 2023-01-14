package oop.ex6.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class VarTable {
    private static final String FINAL = "FINAL";
    private static final String NOT_FINAL = "NOT_FINAL";

    private static final int ARRAY_SIZE = 3;

    private static final int FINAL_LOC = 0;
    private static final int TYPE_LOC = 1;
    private static final int VALUE_LOC = 2;

    private static final String TYPE = "TYPE";
    private static final String FINAL_OR_NOT = "FINAL_OR_NOT";
    private static final String VALUE = "VALUE";


    private final LinkedList<HashMap<String, String[]>> vars;
//    private final HashMap<String, String[]> globalVars;

    private int scopeInd;

    VarTable(){
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
    public boolean addVar(String name, boolean finalOrNot, String type, String value){

        // check if the var is already in the table at the same scope:
        if (checkVarInCurrentScope(name)) { return false;}

        String[] varArray = new String[ARRAY_SIZE];

        // Final or not:
        if (finalOrNot) { varArray[0] = FINAL;}
        else { varArray[FINAL_LOC] = NOT_FINAL;}

        // updating type and value
        varArray[TYPE_LOC] = type;
        varArray[VALUE_LOC] = value;

        // inserting into the vars table at the last scope
        this.vars.getLast().put(name, varArray);

        return true;
    }

    /**
     * private method that checks if a var is already declared in current scope
     * @param name represents the name of the var
     * @return true if is already declared, false otherwise
     */
    private boolean checkVarInCurrentScope(String name) {
        return this.vars.getLast().containsKey(name);
    }

    /**
     * A method that returns the wanted info about the var
     * !!!method assumes the var is in the table and the infoType is correct!!!!
     * @param name a String that represents the vars name
     * @param infoType represents the wanted info, can get: "TYPE" "FINAL_OR_NOT" "VALUE" "REGEX"
     * @return returns the wanted info of the var from the table
     */
    public String getVarInfo (String name, String infoType) {

        int inVarLocation = -1;
        if (infoType.equals(TYPE)) { inVarLocation = TYPE_LOC;}
        if (infoType.equals(FINAL_OR_NOT)) { inVarLocation = FINAL_LOC;}
        if (infoType.equals(VALUE)) { inVarLocation = VALUE_LOC;}
        if (inVarLocation == -1) {return null;} // should not get here! // todo maybe to raise exception??

        // checking the table in reversed order
        Iterator<HashMap<String, String[]>> iterator = this.vars.descendingIterator();
        while( iterator.hasNext()) {
            for (Map.Entry<String, String[]> entry: iterator.next().entrySet()){
                if (entry.getKey().equals(name)) { return entry.getValue()[VALUE_LOC]; }
            }
        }

        // should not get here! // todo maybe to raise exception??
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
    public void outOfScope() {
        this.scopeInd --;
        this.vars.removeLast();
    }

    /**
     * A method that checks if a var is in the table
     * @param name represents the name of the var
     * @return true if is, false otherwise
     */
    public boolean varDeclared(String name) {

        // looking for the var in the table
        for (HashMap<String, String[]> map :this.vars) {
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
            // first checking in the local scopes
            Iterator<HashMap<String, String[]>> iterator = this.vars.descendingIterator();
            while (iterator.hasNext()) {
                for (Map.Entry<String, String[]> entry : iterator.next().entrySet()) {
                    if (entry.getKey().equals(name)) {

                        // if found the var and it is a final var
                        if (entry.getValue()[FINAL_LOC].equals(FINAL)) { return false;}

                        // otherwise, updating the value
                        entry.getValue()[VALUE_LOC] = value;
                        return true;
                    }
                }
            }
        }
        // if not found the var at all
        return false;

    }

}
