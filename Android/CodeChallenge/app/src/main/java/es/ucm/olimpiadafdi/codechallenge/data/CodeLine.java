package es.ucm.olimpiadafdi.codechallenge.data;

import java.util.ArrayList;

public class CodeLine {
    private String line;
    private Boolean isError;
    private String result;
    private boolean isSelected;

    public CodeLine(String line){
        this.line = line;
        this.isError = false;
        this.result = null;
        this.isSelected = false;
    }

    public CodeLine(String line, String result){
        this.line = line;
        this.isError = true;
        this.result = result;
        this.isSelected = false;
    }

    public String getLine() {
        return line;
    }

    public Boolean getIsError() {
        return isError;
    }

    public String getResult() {
        return result;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
