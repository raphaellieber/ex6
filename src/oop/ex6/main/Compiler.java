package oop.ex6.main;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Compiler {

    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "String";
    private static final String CHAR = "char";
    private static final String VOID = "void";

    private static final ArrayList<String> DECLARATION = new ArrayList<String>() {
        {
            add(INT);
            add(DOUBLE);
            add(BOOLEAN);
            add(STRING);
            add(CHAR);
        }
    };

    public static boolean compileLie(String line){
        String[] splitLine = line.split(" "); // todo -> need to check if can be more than one space, \t, \n etc..
        int len = splitLine.length;


        if (DECLARATION.contains(splitLine[0])) {return compileDeclaration(line);}

        if (splitLine[0].equals(VOID)) { return compileFunction(line);}

    }

    private static boolean compileFunction(String line) {}

    private static boolean compileDeclaration(String line) {}

}
