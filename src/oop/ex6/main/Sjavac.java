package oop.ex6.main;

import oop.ex6.main.compiler.CompilationEngine;
import oop.ex6.main.function_handling.FunctionTable;
import oop.ex6.main.validity_checker.CorrectnessChecker;
import oop.ex6.main.var_handling.VarTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sjavac {
    static final int SUCCESS = 0;
    static final int FAILURE = 1;
    static final int IO_ERROR = 2;

    static final String FILE_NOT_FOUND = "File was not found or can't be open";
    static final String NO_PATH = "No file path given";

    /**
     * A function the compiles the file at a given path
     * @param filePath represents the given path
     * @return 0 for success, 1 for failure and 2 for IO error
     */
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
            System.err.println(FILE_NOT_FOUND);
            return IO_ERROR;
        }

        catch (IDENTIFIERException | SYNTAXException | SCOPEException |VALUEException e) {
            System.err.println(e.getMessage());
            return FAILURE;
        }
        return SUCCESS;
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println(NO_PATH);
            System.out.println(IO_ERROR);;
        }

        else {
            String filePath = args[0];
            System.out.println(compileFile(filePath));
        }
    }
}
