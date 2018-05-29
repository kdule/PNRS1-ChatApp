package kenjic.dusan.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity2";
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private boolean username_enabled = false;
    private boolean password_enabled = false;
    private Context context;

    public static final String MY_PREFS_NAME = "PrefsFile";

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String LOGIN_URL = BASE_URL + "/login";

    private HttpHelper httphelper;
    private Handler handler;

    //private RegisterDbHelper registerDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.unMain);
        password = findViewById(R.id.passMain);

        login = findViewById(R.id.login_btn);
        login.setOnClickListener(this);
        register = findViewById(R.id.register_btn);
        register.setOnClickListener(this);

        //registerDbHelper = new RegisterDbHelper(this);
        context = this;
        httphelper = new HttpHelper();
        handler = new Handler();

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String txt = username.getText().toString();

                if(txt.equals("")) {
                    username_enabled = false;
                    login.setEnabled(false);
                }
                else {
                    username_enabled = true;
                    if(password_enabled == true) {
                        login.setEnabled(true);
                    }
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String txt = password.getText().toString();

                if(txt.length() <= 6) {
                    password_enabled = false;
                    login.setEnabled(false);
                }
                else {
                    password_enabled = true;
                    if(username_enabled == true) {
                        login.setEnabled(true);
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.register_btn) {
            Intent reg_intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(reg_intent);
        }

        if(view.getId() == R.id.login_btn) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", username.getText().toString());
                        jsonObject.put("password", password.getText().toString());
                        final boolean response = httphelper.logInUserOnServer(context, LOGIN_URL, jsonObject);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response) {
                                    SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("loggedinUsername", username.getText().toString());
                                    editor.apply();

                                    startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                                } else {
                                    Log.d(TAG, "run: second else");
                                    SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                    String loginErr = prefs.getString("loginErr", null);
                                    Log.d(TAG, "run: " + loginErr);
                                    Toast.makeText(MainActivity.this, loginErr, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "run: second else finish");
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
