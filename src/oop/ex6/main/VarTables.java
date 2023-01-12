package oop.ex6.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class VarTables {
    private static final String FINAL = "FINAL";
    private static final String NOT_FINAL = "NOT_FINAL";

    private static final int FINAL_LOC = 0;
    private static final int TYPE_LOC = 1;
    private static final int VALUE_LOC = 2;
    private static final int REGEX_LOC = 3;

    private static final String TYPE = "TYPE";
    private static final String FINAL_OR_NOT = "FINAL_OR_NOT";
    private static final String VALUE = "VALUE";
    private static final String REGEX = "REGEX";

    // name - [FINAL/NOT, Type, Regex, Value]
    private final HashMap<String, String[]> globalMemberVars;
    private final LinkedList<HashMap<String, String[]>> scopeVars;

    private int scopeInd;

    VarTables(){
        this.globalMemberVars = new HashMap<>();
        this.scopeVars = new LinkedList<>();
        this.scopeInd = 0;
    }

    /**
     * A method that checks if a var got correct type and if it is adds a var to the var tables
     * @param name represents the name of the var
     * @param globOrLoc a boolean that represents if a var is global/member var (true) or local (false)
     * @param finalOrNot a boolean that represents if a var is final var (true) or not (false)
     * @param type String that represents vars type
     * @param value String that represents vars value
     * @return true upon success, false otherwise (if the var got incorrect value)
     */
    public boolean addVar(String name, boolean globOrLoc, boolean finalOrNot, String type, String value){
        // todo need to add regex check to the value

        String[] varArray = new String[4];

        // Final or not:
        if (finalOrNot) { varArray[0] = FINAL;}
        else { varArray[FINAL_LOC] = NOT_FINAL;}

        // updating type and value
        varArray[TYPE_LOC] = type;
        varArray[VALUE_LOC] = value;

        // if global or member var
        if (globOrLoc) { this.globalMemberVars.put(name, varArray);}

        // if local var
        else {

            // if we are in a new scope adding new map
            if (this.scopeVars.size() < this.scopeInd ){
                HashMap<String, String[]> newMap = new HashMap<>();
                this.scopeVars.addLast(newMap);
            }
            // inserting the varArray into the map
            this.scopeVars.getLast().put(name, varArray);
        }

        return true;
    }

    /**
     * A method that returns the wanted info about the var
     * @param name a String that represents the vars name
     * @param infoType represents the wanted info, can get: "TYPE" "FINAL_OR_NOT" "VALUE" "REGEX"
     *                 if none of them, returns false
     * @return returns the wanted info if the car is in the tables and if the wanted info was correct
     *         false otherwise
     */
    public String getVarInfo (String name, String infoType) {

        int inVarLocation = -1;
        if (infoType.equals(TYPE)) { inVarLocation = TYPE_LOC;}
        if (infoType.equals(FINAL_OR_NOT)) { inVarLocation = FINAL_LOC;}
        if (infoType.equals(VALUE)) { inVarLocation = VALUE_LOC;}
        if (infoType.equals(REGEX)) { inVarLocation = REGEX_LOC;}
        if (inVarLocation == -1) {return null;}

        // first checking in the local scopes
        for (HashMap<String, String[]> map :this.scopeVars) {
            for (Map.Entry<String, String[]> entry: map.entrySet()){
                if (entry.getKey().equals(name)) { return entry.getValue()[VALUE_LOC]; }
            }
        }

        // checking in the global / members table
        for (Map.Entry<String, String[]> entry: this.globalMemberVars.entrySet()){
            if (entry.getKey().equals(name)) { return entry.getValue()[VALUE_LOC]; }
        }

        // if not found
        return null;
    }

    /**
     * A method that updates the scope counter
     */
    public void newScope() {this.scopeInd ++;}

    /**
     * A method that updates the scope counter
     */
    public void outOfScope() {
        this.scopeInd --;
        this.scopeVars.removeLast();
    }
}
