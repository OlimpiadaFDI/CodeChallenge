package es.ucm.olimpiadafdi.codechallenge.connection;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.ucm.olimpiadafdi.codechallenge.data.Badge;
import es.ucm.olimpiadafdi.codechallenge.data.Storage;

public class HtmlParser {

    public static String CODE = "code";
    public static String MESSAGE = "message";
    public static String RESULT = "result";

    public static boolean checkLogin(String html){
        Boolean b = false;
        try{
            JSONObject jObj = new JSONObject(html);
            int code = jObj.getInt(CODE);
            String message = jObj.getString(MESSAGE);
            String result = jObj.getString(RESULT);

            if (code==0){
                b = true;
            }
            else{
                Storage.getInstance().setResultErrorLogin(result);
            }
            Log.i("QuickTest", result);
        } catch (JSONException e) {
            Log.e("QuickTest", "There was an error parsing the JSON", e);
        }
    return b;
    }

    public static boolean parseListBadges(String html){
        ArrayList<Badge> l = new ArrayList<Badge>();
        boolean b = false;
        try {
            JSONObject jObj = new JSONObject(html);
            int code = jObj.getInt(CODE);
            String message = jObj.getString(MESSAGE);
            JSONObject result = jObj.getJSONObject(RESULT);

            if (code==0){
                String usuario = result.getString("usuario");
                JSONArray insignias = result.getJSONArray("insignias");
                for (int i=0; i<insignias.length(); i++){
                    JSONObject insignia = insignias.getJSONObject(i);
                    int idInsignia = insignia.getInt("idInsignia");
                    String descCorta = insignia.getString("descCorta");
                    String descLarga = insignia.getString("descLarga");
                    int puntuacion = insignia.getInt("puntuacion");

                    if (idInsignia==81 || idInsignia==82 || idInsignia==83 || idInsignia==84 || idInsignia==85)
                        l.add(new Badge(idInsignia, descCorta, descLarga, puntuacion));
                }
                Storage.getInstance().setListBadges(l);
                b = true;
            }
        } catch (JSONException e) {
            Log.e("QuickTest", "There was an error parsing the JSON parseListBadges", e);
        }
        return b;
    }

    public static boolean checkBadge(String html){
        Boolean b = false;
        try{
            JSONObject jObj = new JSONObject(html);
            int code = jObj.getInt(CODE);
            String message = jObj.getString(MESSAGE);
            String result = jObj.getString(RESULT);

            if (code==0){
                b = true;
                result = "Se ha desbloqueado una insignia";
            }
            else if (code==2){
                b=false;
                result = "Ya se disponía de la insignia";
            }
            Log.i("QuickTest", result);
        } catch (JSONException e) {
            Log.e("QuickTest", "There was an error parsing the JSON checkBadge", e);
        }
        return b;
    }
}
