package es.ucm.olimpiadafdi.codechallenge.data;

import java.util.ArrayList;

public class Code {
    private ArrayList<CodeLine> code;
    private int totalErrors;

    public Code(ArrayList<CodeLine> code, int totalErrors){
        this.code = code;
        this.totalErrors = totalErrors;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public ArrayList<CodeLine> getCode() {
        return code;
    }
}
