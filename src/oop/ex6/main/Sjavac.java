package oop.ex6.main;

import oop.ex6.main.function.handling.FunctionTable;
import oop.ex6.main.var.handling.VarTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sjavac {
    static final int SUCCESS = 0;
    static final int FAILURE = 1;
    static final int IO_ERROR = 2;

    public static int compileFile(String filePath) {
        try (
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            CorrectnessChecker correctnessChecker = new CorrectnessChecker();
            VarTable varTable = new VarTable();
            FunctionTable functionTable = new FunctionTable();

            CompilationEngine compilationEngine = new CompilationEngine (correctnessChecker, varTable,
                    functionTable  ,bufferedReader);

            compilationEngine.compile();
        }

        catch (IOException e) {
            System.out.println("File was not found or can't be open");
            return IO_ERROR;
        }

        catch (IDENTIFIERException | SYNTAXException | VALUEException e) {
            System.out.println(e.getMessage());
            return IO_ERROR;
        }

        return SUCCESS;
    }

    public static void main(String[] args) {
//
//        if (args.length == 0) {
//            System.out.println("No file path given");
//            return FAILURE;
//        }
//
//        String filePath = args[0];
//        return compileFile(filePath);

        String line = "a    ,b  =  5,c=d;       ";
//        String[] split= str.split("\\s+|\\s+,\\s+|\\(|\\)");
//        String[] split= str.split("\\s*(void|\\w|\\w|\\w|\\w)\\s*/g");
//        System.out.println(split);\
//        int braceStartLoc = line.indexOf("(");
//        int braceFinishLoc = line.indexOf(")");
//        int spaceLoc = line.indexOf(" ");

//        String funcName = line.substring(spaceLoc,braceStartLoc).trim();
//        String declaration = line.substring(braceStartLoc+1, braceFinishLoc);
//        String endOfLine = line.substring(braceFinishLoc+1);

//        System.out.println(funcName);
//        System.out.println(declaration);
//        System.out.println(endOfLine);

        line = line.replaceAll("\\s", "");
        String[] split = line.split(",");
//        System.out.println(!line.matches(".*;$"));
        int a = line.indexOf("k");
        System.out.println(a);
        for(String s:split) {
            System.out.println(s);
        }
    }
}
