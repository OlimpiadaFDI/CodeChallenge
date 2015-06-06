package es.ucm.olimpiadafdi.codechallenge.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import es.ucm.olimpiadafdi.codechallenge.R;
import es.ucm.olimpiadafdi.codechallenge.connection.JsonRequest;
import es.ucm.olimpiadafdi.codechallenge.data.Code;
import es.ucm.olimpiadafdi.codechallenge.data.CodeInit;
import es.ucm.olimpiadafdi.codechallenge.data.CodeLine;
import es.ucm.olimpiadafdi.codechallenge.data.SharedPrefInfo;
import es.ucm.olimpiadafdi.codechallenge.data.Storage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static String UNLOCKBADGE = "unlockBadge";

    private Activity activity;
    private TextView textView_countDown;
    private TextView  textView_lineCounter;
    private Button button_check;
    private ListView lv;
    private CountDownTimer countDown;
    private static long timestamp;
    private ArrayAdapter<CodeLine> adapter;
    private int incorrectLinesFound;
    private Code code;
    String text;

    public MainFragment(){}

    public interface mainInterface{
        public void showScores();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Asociamos los widgets del layout a variables java
        this.textView_lineCounter = (TextView) rootView.findViewById(R.id.textView_lineCounter);
        this.textView_countDown = (TextView) rootView.findViewById(R.id.textView_countDown);
        this.button_check = (Button) rootView.findViewById(R.id.button_check);
        this.lv = (ListView) rootView.findViewById(R.id.listView_code);

        Context context = activity.getApplicationContext();
        String countDownString = activity.getString(R.string.countDown, "60");
        textView_countDown.setText(countDownString);

        countDown = new CountDownTimer(60000, 1) {
            public void onTick(long millisUntilFinished) {
                timestamp = 60000 - millisUntilFinished;
                Context context = activity.getApplicationContext();
                String countdown = activity.getString(R.string.countDown, Long.toString(millisUntilFinished / 1000));
                textView_countDown.setText(countdown);
            }
            public void onFinish() {
                Context context = activity.getApplicationContext();
                String text = activity.getString(R.string.timesUp);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                finishGame();
            }
        };

        Random r = new Random();
        int i1 = r.nextInt(2); //i1 vale 0 ó 1. Más info: http://stackoverflow.com/questions/6029495/how-can-i-generate-random-number-in-specific-range-in-android
        code = CodeInit.initCode(i1);
        incorrectLinesFound = 0;
            String text = activity.getString(R.string.gameExposition, code.getTotalErrors(),code.getTotalErrors()==0? "" : "s" );
            textView_lineCounter.setText(text);
        adapter = new ArrayAdapter<CodeLine>(activity.getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, code.getCode()){
            @Override
            public int getCount() {
                return code.getCode().size();
            }

            @Override
            public CodeLine getItem(int position) {
                return code.getCode().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                CodeLine line = code.getCode().get(position);
                text1.setTextColor(Color.DKGRAY);
                text2.setTextColor(Color.GRAY);
                text1.setText(line.getLine());
                text2.setText("");
                text1.setOnTouchListener(new CustomTouchListener(activity, textView_lineCounter, line, text1, text2, code.getTotalErrors()));
                return view;
            }
        };
        lv.setAdapter(adapter);

        // On Item Click Listener del ListView. Más info: http://www.ezzylearning.com/tutorial/handling-android-listview-onitemclick-event
        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView <? > adapterView, View view,
                                    int position, long id) {
                //String item = ((TextView)view).getText().toString();
                //TextView textView = (TextView) view.findViewById(R.id.rowListTextView);
                ((TextView)view).setTextColor(Color.GREEN);
                String text;
                if (code.getCode().get(position).getIsError()){
                    text = "Bien!";
                }
                else{
                    text = "Mal!!";
                }
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
                toast.show();


            }
        });*/

        this.handleButtons();
        startGame();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void handleButtons() {
        View.OnClickListener handler1 = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkResult();
            }
        };
        button_check.setOnClickListener(handler1);
    }

    public void checkResult(){
        boolean[] blist = new boolean[code.getCode().size()];
        if (code.getTotalErrors() == Storage.getInstance().getCountLines()){
            for (int i =0; i<code.getCode().size(); i++){
               CodeLine l = code.getCode().get(i);
                if ((l.isSelected() && l.getIsError()) || (!l.isSelected() && !l.getIsError())){
                    blist[i] = true;
                } else{
                    blist[i] = false;
                }
            }
            if (areAllTrue(blist)) {

                if (timestamp <= 10000){
                    triggerInsignia_84();
                }
                if (Storage.getInstance().getFallos() == 0)
                    triggerInsignia_82();

                finishGame();
            }
            else{
                Context context = activity.getApplicationContext();
                String text = "Has seleccionado líneas que no tienen erores!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
        else{
            Context context = activity.getApplicationContext();
            String text = "No has encontrado todos los errores";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

        public void triggerInsignia_82(){
        String s[] = {Storage.getInstance().getNick(), Integer.toString(82)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, activity.getApplicationContext(), updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'HAWKEYE'!";
    }
    public void triggerInsignia_84(){
        String s[] = {Storage.getInstance().getNick(), Integer.toString(84)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, activity.getApplicationContext(), updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'QUICK REACTION FORCE'!";
    }

    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }

    public void finishGame(){
        countDown.cancel();
        countDown = null;
        SharedPrefInfo info = new SharedPrefInfo();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(info.PREF_NAME, info.PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(info.KEY_CORRECT, (code.getTotalErrors() == Storage.getInstance().getCountLines())? "correctamente" : "incorrectamente");
        editor.putString(info.KEY_TOTALTIME, Long.toString(timestamp/1000));
        editor.commit();
        try{
            ((mainInterface) activity).showScores();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
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

    public void startGame(){
        // Checking Internet connection


        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(activity.getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Context context = activity.getApplicationContext();

        boolean conectados = false;
        String text = "";
        int duration = Toast.LENGTH_SHORT;

        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            conectados = true;
            text = context.getString(R.string.connected);
        } else {
            // display error
            text = context.getString(R.string.not_connected);
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        countDown.start();
    }
}
