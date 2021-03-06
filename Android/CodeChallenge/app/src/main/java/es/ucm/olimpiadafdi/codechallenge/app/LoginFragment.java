package es.ucm.olimpiadafdi.codechallenge.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.ucm.olimpiadafdi.codechallenge.R;
import es.ucm.olimpiadafdi.codechallenge.connection.JsonRequest;
import es.ucm.olimpiadafdi.codechallenge.data.SharedPrefInfo;
import es.ucm.olimpiadafdi.codechallenge.data.Storage;

public class LoginFragment extends Fragment {

    private static String LOGIN = "login";

    String nick;
    String pass;
    boolean remember;

    private Activity activity;
    private EditText editText_nick;
    private EditText editText_pass;
    private CheckBox checkBox_remember;
    private Button button_login;
    private TextView textView_register;

    public LoginFragment(){}

    public interface loginInterface{
        public void userDetails(String nick, String pass, boolean remember);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        //Asociamos los widgets del layout a variables java
        this.editText_nick = (EditText) rootView.findViewById(R.id.editText_nick);
        this.editText_pass = (EditText) rootView.findViewById(R.id.editText_pass);
        this.checkBox_remember = (CheckBox) rootView.findViewById(R.id.checkBox_remember);
        this.button_login = (Button) rootView.findViewById(R.id.button_login);
        this.textView_register = (TextView) rootView.findViewById(R.id.textView_register);

        SharedPrefInfo info = new SharedPrefInfo();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(info.PREF_NAME, info.PRIVATE_MODE);
        if (pref.contains(info.KEY_NICK) && pref.contains(info.KEY_PASSWORD)){
            this.editText_nick.setText(pref.getString(info.KEY_NICK, null));
            this.editText_pass.setText(pref.getString(info.KEY_PASSWORD, null));
        }

        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Storage.getInstance().getRegisterURL();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        this.handleButtons();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void handleButtons() {
        View.OnClickListener handlerLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = editText_nick.getText().toString();
                pass = editText_pass.getText().toString();
                remember = checkBox_remember.isChecked();

                Storage.getInstance().setNick(nick);

                String s[] = {nick, pass};

                JsonRequest jsonRequest = new JsonRequest(LOGIN, activity.getApplicationContext(), updateDataSuccess, updateDataError, s);
                jsonRequest.request();
            }
        };
        button_login.setOnClickListener(handlerLogin);
    }

    private Runnable updateDataSuccess = new Runnable() {
        public void run() {
            try{
                ((loginInterface) activity).userDetails(nick, pass, remember);
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }
    };

    private Runnable updateDataError = new Runnable() {
        public void run() {
            Context context = activity.getApplicationContext();
            String text = Storage.getInstance().getResultErrorLogin();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    };
}
