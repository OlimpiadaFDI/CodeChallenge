package es.ucm.olimpiadafdi.codechallenge.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import es.ucm.olimpiadafdi.codechallenge.R;
import es.ucm.olimpiadafdi.codechallenge.connection.JsonRequest;
import es.ucm.olimpiadafdi.codechallenge.data.CalculaInsignias;
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

    private CalculaInsignias calculaInsignias;

    private Activity activity;
    private TextView textView_countDown;
    private TextView  textView_lineCounter;
    private Button button_check;
    private ListView lv;
    private CountDownTimer countDown;
    private static long timestamp;
    private ArrayAdapter<CodeLine> adapter;

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
        String countDownString = activity.getString(R.string.countDown, "300");
        textView_countDown.setText(countDownString);

        countDown = new CountDownTimer(300000, 1) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {}
        }.start();

        //calculaInsignias = new CalculaInsignias(activity.getApplicationContext());

        Random r = new Random();
        int i1 = r.nextInt(2); //i1 vale 0 ó 1. Más info: http://stackoverflow.com/questions/6029495/how-can-i-generate-random-number-in-specific-range-in-android
        final Code code = CodeInit.initCode(i1);
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
        Context context = activity.getApplicationContext();
/*
        //Calculamos las insignias:
        calculaInsignias.nuevaPregunta(q, answer,timestamp);

        String text;
        if (answer == q.getCorrect()){
            text = context.getString(R.string.correct_answer);
        }
        else{
            text = context.getString(R.string.incorrect_answer);
        }
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        if (Storage.getInstance().getQuestionsAlreadyAsked().size()<10){
            JsonRequest jsonRequest = new JsonRequest(GETQUESTION, context, updateDataSuccess, updateDataError, null);
            jsonRequest.request();
        }
        else{
            try{
                SharedPrefInfo info = new SharedPrefInfo();
                SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(info.PREF_NAME, info.PRIVATE_MODE);
                SharedPreferences.Editor editor = pref.edit();
                    editor.putString(info.KEY_TOTALTIME, Long.toString(calculaInsignias.getTiempoTotal()));
                editor.commit();

                ((mainInterface) activity).showScores();
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }*/
    }

    public void startGame(){
        // Checking Internet connection
        countDown = new CountDownTimer(300000, 1) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {}
        }.start();

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

        //Retrieving the next question

        //JsonRequest jsonRequest = new JsonRequest(GETQUESTION, context, updateDataSuccess, updateDataError, null);
        //jsonRequest.request();
    }

    private Runnable updateDataSuccess = new Runnable() {
        public void run() {
            Log.d("QuickTest", "success");
            /*Question q = Storage.getInstance().getQuestion();
            Storage.getInstance().getQuestionsAlreadyAsked().add(q.getId());

            int num = Storage.getInstance().getQuestionsAlreadyAsked().size();
            String questionCount = activity.getString(R.string.questionsCount, num, 10);
            textView_questionCount.setText(questionCount);

            //Visualizar la pregunta
            if (q != null){
                textView_question.setText(q.getQuestion());

                Answer a1 = q.getAnswers().get(0);
                button1.setText(a1.getAnswer());
                if (a1.getId() == q.getCorrect())
                    q.setCorrect(1);

                Answer a2 = q.getAnswers().get(1);
                button2.setText(a2.getAnswer());
                if (a2.getId() == q.getCorrect())
                    q.setCorrect(2);

                Answer a3 = q.getAnswers().get(2);
                button3.setText(a3.getAnswer());
                if (a3.getId() == q.getCorrect())
                    q.setCorrect(3);

                Answer a4 = q.getAnswers().get(3);
                button4.setText(a4.getAnswer());
                if (a4.getId() == q.getCorrect())
                    q.setCorrect(4);

                Storage.getInstance().setQuestion(q);*/

                countDown.cancel();
                countDown = new CountDownTimer(30000, 1) {
                    public void onTick(long millisUntilFinished) {
                        timestamp = 30000 - millisUntilFinished;
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

                        try{
                            ((mainInterface) activity).showScores();
                        }catch (ClassCastException e){
                            e.printStackTrace();
                        }
                    }
                }.start();

                //AlertDialogManager d = new AlertDialogManager();
                //d.showAlertDialog(getApplicationContext(), "Correcto", "Muy bien",true);
            /*}
            else{
                Log.e("QuickTest", "Question is null");
            }*/
        }
    };
    private Runnable updateDataError = new Runnable() {
        public void run() {
            Log.d("QuickTest", "updateDataError");
        }
    };
}
