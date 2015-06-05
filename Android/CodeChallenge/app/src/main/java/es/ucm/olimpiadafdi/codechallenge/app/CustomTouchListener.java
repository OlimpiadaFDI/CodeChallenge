package es.ucm.olimpiadafdi.codechallenge.app;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import es.ucm.olimpiadafdi.codechallenge.R;
import es.ucm.olimpiadafdi.codechallenge.data.CodeLine;
import es.ucm.olimpiadafdi.codechallenge.data.Storage;

public class CustomTouchListener implements View.OnTouchListener {
    private Activity activity;
    private TextView text1;
    private TextView text2;
    private TextView textView_lineCounter;
    private CodeLine codeLine;
    private int total;

    public CustomTouchListener(Activity activity, TextView textView_lineCounter, CodeLine codeLine, TextView text1, TextView text2, int total) {
        this.activity = activity;
        this.textView_lineCounter = textView_lineCounter;
        this.codeLine = codeLine;
        this.text1 = text1;
        this.text2 = text2;
        this.total = total;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                String textToast = "";
                if (Storage.getInstance().getCountLines() == total)
                    if (text1.getCurrentTextColor() == Color.DKGRAY) {
                        if (codeLine.getIsError()){
                            text1.setTextColor(Color.GREEN);
                            text2.setTextColor(Color.RED);
                            text2.setText(codeLine.getResult());
                            codeLine.setIsSelected(true);
                            textToast = "¡Has encontrado un error!";
                            Storage.getInstance().selectLine();
                        }
                        else{
                            text1.setTextColor(Color.BLACK);
                            textToast = "¡Te has equivocado!";
                            Storage.getInstance().selectLine();
                        }
                    } else {
                        text1.setTextColor(Color.DKGRAY);
                        Storage.getInstance().deselectLine();
                    }
                textToast = "¡No puedes seleccionar más líneas!";
                int n = Storage.getInstance().getCountLines();
                String text = activity.getString(R.string.gameExposition, total - n,total-n==0? "" : "s" );
                textView_lineCounter.setText(text);

                Toast toast = Toast.makeText(activity.getApplicationContext(), textToast, Toast.LENGTH_SHORT);
                toast.show();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                text1.setTextColor(Color.CYAN);
                break;
        }
        return false;
    }
}
