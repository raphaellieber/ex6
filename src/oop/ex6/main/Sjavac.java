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
        try (  FileReader fileReader = new FileReader(filePath);
               FileReader firstRunFileReader = new FileReader(filePath);
               BufferedReader bufferedReader = new BufferedReader(fileReader);
               BufferedReader firstRunReader = new BufferedReader(firstRunFileReader)) {

            CorrectnessChecker correctnessChecker = new CorrectnessChecker();
            VarTable varTable = new VarTable();
            FunctionTable functionTable = new FunctionTable();

            CompilationEngine compilationEngine = new CompilationEngine (correctnessChecker, functionTable,
                    varTable, bufferedReader, firstRunReader);

            compilationEngine.compile();
        }

        catch (IOException e) {
            System.err.println("File was not found or can't be open");
            return FAILURE;
        }

        catch (IDENTIFIERException | SYNTAXException | VALUEException e) {
            System.err.println(e.getMessage());
            return IO_ERROR;
        }

        return SUCCESS;
    }

    public static void main(String[] args) {
//
//        if (args.length == 0) {
//            System.err.println("No file path given");
//            return FAILURE;
//        }
//
//        String filePath = args[0];
//        return compileFile(filePath);

        String line = "agdgad    fg a   { {  ";
        System.out.println(line.matches("^[^{]*\\{\\s*$"));
    }
}
