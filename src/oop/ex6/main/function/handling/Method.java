package oop.ex6.main.function.handling;

public class Method {
    public static enum ReturnType {VOID, PRIMITIVE_VARIABLE, REFERENCE_VARIABLE}
    private final String name;
    private final ReturnType returnType;
    private final String methodContent;

    public Method(String name, ReturnType returnType, String methodContent) {
        this.name = name;
        this.returnType = returnType;
        this.methodContent = methodContent;
    }

    public String getName() {
        return name;
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public String getMethodContent() {
        return methodContent;
    }

    public boolean isVoidMethod() {return this.returnType == ReturnType.VOID;}
}
