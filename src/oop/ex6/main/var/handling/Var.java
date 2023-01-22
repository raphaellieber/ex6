package oop.ex6.main.var.handling;

public class Var {

    private final boolean finalOrNot;
    private final String type;
    private String value;
    private boolean isAfFuncParam; // true if assigned by func param or is a func param

    /**
     * A constructor for a var object
     * @param finalOrNot a boolean represents if it is final or not
     * @param type String that represents its type
     * @param value Stains the represents the vars value, can be null!
     */
    public Var(boolean finalOrNot, String type, String value, boolean funcParam) {

        this.finalOrNot = finalOrNot;
        this.type = type;
        this.value = value;
        this.isAfFuncParam = funcParam;
    }

    /**
     * A getter for the information if the var is final or not
     * @return true if final, false otherwise
     */
    public boolean finalOrNot() { return this.finalOrNot; }

    /**
     * A getter for the type of the var
     * @return the vars type
     */
    public String getType() { return this.type; }

    /**
     * a getter for the value of the var
     * @return the value of the var
     */
    public String getValue() {return this.value; }

    /**
     * A setter for the value of the var
     * The method allows to change the value even if the var is final!!
     * users responsibility to check if the var is final or not!!
     * @param value represents the given value
     */
    public void setValue(String value) { this.value = value;}

    public void setAFuncParam(boolean afFuncParam) { this.isAfFuncParam = afFuncParam;}

    public boolean isAFuncParam() {return this.isAfFuncParam;}
}
