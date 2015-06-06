package es.ucm.olimpiadafdi.codechallenge.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import es.ucm.olimpiadafdi.codechallenge.R;
import es.ucm.olimpiadafdi.codechallenge.connection.JsonRequest;
import es.ucm.olimpiadafdi.codechallenge.data.CodeLine;
import es.ucm.olimpiadafdi.codechallenge.data.Storage;

public class CustomTouchListener implements View.OnTouchListener {

    private static String UNLOCKBADGE = "unlockBadge";

    private Activity activity;
    private TextView text1;
    private TextView text2;
    private TextView textView_lineCounter;
    private CodeLine codeLine;
    private int total;
    private String text;
    private CountDownTimer countDown;
    private long timestamp;

    public CustomTouchListener(Activity activity, TextView textView_lineCounter, CodeLine codeLine, TextView text1, TextView text2, int total) {
        this.activity = activity;
        this.textView_lineCounter = textView_lineCounter;
        this.codeLine = codeLine;
        this.text1 = text1;
        this.text2 = text2;
        this.total = total;
        countDown = new CountDownTimer(60000, 1) {
            public void onTick(long millisUntilFinished) {
                timestamp = 60000 - millisUntilFinished;
            }
            public void onFinish() {
            }
        }.start();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                String textToast = "";
                if (text1.getCurrentTextColor() == Color.DKGRAY) {
                    if (Storage.getInstance().getCountLines() <= total) {
                        if (codeLine.getIsError()) {
                            text1.setTextColor(Color.RED);
                            text2.setTextColor(Color.GREEN);
                            text2.setText(codeLine.getResult());
                            codeLine.setIsSelected(true);
                            textToast = "¡Has encontrado un error!";
                            Storage.getInstance().selectLine();
                            Storage.getInstance().selectLineCorrect();

                            triggerInsignia_81();
                            if (timestamp<=2000)
                                triggerInsignia_85();


                        } else {
                            text1.setTextColor(Color.BLACK);
                            codeLine.setIsSelected(true);
                            textToast = "¡Te has equivocado!";
                            Storage.getInstance().selectLine();

                            Storage.getInstance().unFallo();
                            if (Storage.getInstance().getFallos() == 30)
                                triggerInsignia_83();
                        }
                        textToast = "¡No puedes seleccionar más líneas!";
                        Toast toast = Toast.makeText(activity.getApplicationContext(), textToast, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        textToast = "¡No puedes seleccionar más líneas!";
                        Toast toast = Toast.makeText(activity.getApplicationContext(), textToast, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    text1.setTextColor(Color.DKGRAY);
                    codeLine.setIsSelected(false);
                    Storage.getInstance().deselectLine();
                    Storage.getInstance().deselectLineCorrect();
                }
                int n = Storage.getInstance().getCountLines();
                String text = activity.getString(R.string.gameExposition, total - n, total - n == 0 ? "" : "s");
                textView_lineCounter.setText(text);
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                text1.setTextColor(Color.CYAN);
                break;
        }
        return false;
    }

    public void triggerInsignia_81(){
        // Insignia 81:

        String s[] = {Storage.getInstance().getNick(), Integer.toString(81)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, activity.getApplicationContext(), updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'NEWBIE'!";
    }
    public void triggerInsignia_83(){
        // Insignia 81:

        String s[] = {Storage.getInstance().getNick(), Integer.toString(83)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, activity.getApplicationContext(), updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'CRYSIS’ LEAD PROGRAMMER'!";
    }

    public void triggerInsignia_85(){
        // Insignia 81:

        String s[] = {Storage.getInstance().getNick(), Integer.toString(85)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, activity.getApplicationContext(), updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'PRESS ‘N’ PRAY'!";
    }



    private Runnable updateDataSuccess = new Runnable() {
        public void run() {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
            toast.show();

            Log.d("CodeChallenge", "Badge unlocked" + text);
        }
    };

    private Runnable updateDataError = new Runnable() {
        public void run() {
            Log.d("CodeChallenge", "Error unlocking Badge");
        }
    };
}
