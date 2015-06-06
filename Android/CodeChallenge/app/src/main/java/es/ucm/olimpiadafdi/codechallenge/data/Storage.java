package es.ucm.olimpiadafdi.codechallenge.data;

import java.util.ArrayList;

public class Storage {

    private static final String VERSION = "1.2";

    private static Storage ourInstance = new Storage();

    public static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
    }

    // -------------------------------------------------------------------------------

    private String registerURL = "http://www.google.es";
    private String nick;
    private int countLines = 0;
    private int countLinesCorrect = 0;
    private int numFallos = 0;
    private String resultErrorLogin;
    private ArrayList<Badge> listBadges = new ArrayList<Badge>();

    public String getVersion(){
        return VERSION;
    }

    public String getRegisterURL() {
        return registerURL;
    }

    public void setNick(String s){
        this.nick = s;
    }

    public String getNick(){
        return this.nick;
    }


    public void setResultErrorLogin(String s){
        this.resultErrorLogin = s;
    }

    public String getResultErrorLogin(){
        return resultErrorLogin;
    }

    public void selectLine(){
        countLines++;
    }
    public void deselectLine(){
        countLines--;
    }
    public int getCountLines(){
        return countLines;
    }

    public void selectLineCorrect(){
        countLinesCorrect++;
    }
    public void deselectLineCorrect(){
        countLinesCorrect--;
    }
    public int getCountLinesCorrect(){
        return countLinesCorrect;
    }

    public void unFallo(){
        numFallos++;
    }
    public int getFallos(){
        return numFallos;
    }

    public ArrayList<Badge> getListBadges() {
        return listBadges;
    }

    public void setListBadges(ArrayList<Badge> listBadges) {
        this.listBadges = listBadges;
    }
}
