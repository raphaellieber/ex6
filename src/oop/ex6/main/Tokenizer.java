package oop.ex6.main;

public class Tokenizer {

    private int wordInd;
    private int inWordInd;
    private String[] split;
    private int len;
    private String curToken;

    public Tokenizer(String line){
        this.split = line.split("\\s");
        this.inWordInd = 0;
        this.wordInd = 0;
        this.len = split.length;
    }

    public boolean hasMoreTokens() {
        return true;
//        return !(this.wordInd == this.len - 1 & this.inWordInd == this.split[this.wordInd].length());
    }



    public void advance(){}

    public String getCurToken(){ return "";}

    /**
     * identifier, symbol etc
     * @return
     */
    public String getType() { return "";}
}
