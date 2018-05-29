package kenjic.dusan.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private Button register;
    private boolean username_enabled = false;
    private boolean password_enabled = false;
    private boolean email_enabled = false;
    private DatePicker datePicker;

    public static final String MY_PREFS_NAME = "PrefsFile";


    private HttpHelper httphelper;
    private Handler handler;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String REGISTER_URL = BASE_URL + "/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.unReg);
        password = findViewById(R.id.passReg);
        email = findViewById(R.id.emailReg);
        datePicker = findViewById(R.id.dateReg);
        first_name = findViewById(R.id.fnReg);
        last_name = findViewById(R.id.lnReg);

        register = findViewById(R.id.regReg_btn);
        register.setOnClickListener(this);

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

                if (txt.equals("")) {
                    username_enabled = false;
                    register.setEnabled(false);
                } else {
                    username_enabled = true;
                    if (password_enabled == true && email_enabled == true) {
                        register.setEnabled(true);
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

                if (txt.length() <= 6) {
                    password_enabled = false;
                    register.setEnabled(false);
                } else {
                    password_enabled = true;
                    if (username_enabled == true && email_enabled == true) {
                        register.setEnabled(true);
                    }
                }
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String txt = email.getText().toString();

                if (txt.length() == 0 || !(Patterns.EMAIL_ADDRESS.matcher(txt).matches())) {
                    email_enabled = false;
                    register.setEnabled(false);
                } else {
                    email_enabled = true;
                    if (username_enabled == true && password_enabled == true) {
                        register.setEnabled(true);
                    }
                }
            }
        });

        Date d = Calendar.getInstance().getTime();
        datePicker.setMaxDate(d.getTime());


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.regReg_btn) {

            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", username.getText().toString());
                        jsonObject.put("password", password.getText().toString());
                        jsonObject.put("email", email.getText().toString());

                        final boolean response = httphelper.registerUserOnServer(RegisterActivity.this, REGISTER_URL, jsonObject);

                        handler.post(new Runnable(){
                            public void run() {
                                if (response) {
//                                    Toast.makeText(RegisterActivity.this, getText(R.string.regCompleted), Toast.LENGTH_SHORT).show();
                                    Log.d( "RegisterActivity","If je tacan");
                                      startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                } else {
                                    Log.d("RegisterActivity", "If je netacan");
                                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                    String registerErr = prefs.getString("registerErr", null);
//                                    Toast.makeText(RegisterActivity.this, registerErr, Toast.LENGTH_SHORT).show();
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
