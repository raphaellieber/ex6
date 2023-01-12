package oop.ex6.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sjavac {
    static final int SUCCESS = 0;
    static final int FAILURE = 1;
    static final int IO_ERROR = 2;

    public static int compileFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);  // todo need to close the file some when
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // todo need to initialize a tokenizer
            CorrectnessChecker correctnessChecker = new CorrectnessChecker();
            VarTable varTable = new VarTable();

            CompilationEngine compilationEngine = new CompilationEngine(tokenizer, correctnessChecker,
                    varTable);


        } catch (IOException e) {
            System.out.println("File was not found or can't be open");
            return IO_ERROR;
        }

        return SUCCESS;
    }

    public static int main(String[] args) {

        if (args.length == 0) {
            System.out.println("No file path given");
            return FAILURE;
        }

        String filePath = args[0];
        return compileFile(filePath);
    }
}
