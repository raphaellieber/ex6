package oop.ex6.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

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


        } catch (IOException e) {
            System.out.println("File was not found or can't be open");
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

        String str = "a=5;";
        StringTokenizer tokenizer = new StringTokenizer(str);
        while (tokenizer.hasMoreTokens()) {
            System.out.println(tokenizer.nextToken());
        }
    }
}
