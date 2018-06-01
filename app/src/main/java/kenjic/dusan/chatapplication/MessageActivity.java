package kenjic.dusan.chatapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private Button logout;
    private EditText msg;
    private Button send;
    private Button refresh;
    private ListView chatMsgs;
    private TextView conName;
    private MessageAdapter mA;

    //private RegisterDbHelper registerDbHelper;
    private static final String MY_PREFS_NAME = "PrefsFile";
    private String receiver_userid;
    private String sender_userid;
    private String receiver_username;
    private MessageClass[] messages;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String POST_MESSAGE_URL = BASE_URL + "/message";
    private static String GET_MESSAGE_URL = BASE_URL + "/message/";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    private HttpHelper httpHelper;
    private Handler handler;
    private Cryptography C;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        logout = findViewById(R.id.logoutMsg_btn);
        logout.setOnClickListener(this);

        refresh = findViewById(R.id.btn_refresh_msg);
        refresh.setOnClickListener(this);

        send = findViewById(R.id.send_btn);
        send.setOnClickListener(this);

        msg = findViewById(R.id.txtMsg);

        /*registerDbHelper = new RegisterDbHelper(this);
        contacts = registerDbHelper.readContacts(null);*/

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        receiver_userid = prefs.getString("receiver_userId", null);
        sender_userid = prefs.getString("signed_in_user", null);
        mA = new MessageAdapter(this);

        chatMsgs = findViewById(R.id.list_msg);
        chatMsgs.setAdapter(mA);
        chatMsgs.setOnItemLongClickListener(this);

        conName = findViewById(R.id.msg_contact_name);

        httpHelper = new HttpHelper();
        handler = new Handler();
        C = new Cryptography();

        /*if(contacts != null) {
            for(int i=0; i<contacts.length; i++) {
                if(contacts[i].getContactID().compareTo(receiver_userid) == 0) {
                    String receiver_user = contacts[i].getFirstNameCon() + " " + contacts[i].getLastNameCon() + " :)";
                    receiver_username = contacts[i].getUserNameCon();
                    conName.setText(receiver_user);
                    break;
                }
            }
        }*/

        conName.setText(receiver_userid);

        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = msg.getText().toString();

                if(text.length() != 0 ){
                    send.setEnabled(true);
                }else{
                    send.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logoutMsg_btn:
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            final boolean response = httpHelper.logOutUserFromServer(MessageActivity.this, LOGOUT_URL);
                            handler.post(new Runnable(){
                                public void run() {
                                    if (response) {
                                        startActivity(new Intent(MessageActivity.this, MainActivity.class));
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String logoutErr = prefs.getString("logoutErr", null);
                                        Toast.makeText(MessageActivity.this, logoutErr, Toast.LENGTH_SHORT).show();
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
                break;

            case R.id.btn_refresh_msg:
                reloadMList();
                break;

            case R.id.send_btn:
                new Thread(new Runnable() {
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            String message = msg.getText().toString();
                            String cryptedMessage = C.cryptography(message);
                            jsonObject.put("receiver", receiver_userid);
                            jsonObject.put("data", cryptedMessage);

                            final boolean success = httpHelper.sendMessageToServer(MessageActivity.this, POST_MESSAGE_URL, jsonObject);

                            handler.post(new Runnable(){
                                public void run() {
                                    if (success) {
                                        Toast.makeText(MessageActivity.this, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
                                        msg.getText().clear();
                                        reloadMList();
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String sendMsgErr = prefs.getString("sendMsgErr", null);
                                        Toast.makeText(MessageActivity.this, sendMsgErr, Toast.LENGTH_SHORT).show();
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

                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update messages list
        reloadMList();
    }

    public void reloadMList() {
        /*MessageClass[] messages = registerDbHelper.readMessages(senderid, receiverid);
        mA.update(messages);*/

        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray json_messages = httpHelper.getMessagesFromServer(MessageActivity.this, GET_MESSAGE_URL+receiver_userid);

                    handler.post(new Runnable(){
                        public void run() {
                            if (json_messages != null) {

                                JSONObject json_message;
                                messages = new MessageClass[json_messages.length()];

                                for (int i = 0; i < json_messages.length(); i++) {
                                    try {
                                        json_message = json_messages.getJSONObject(i);
                                        String msgFromServer = json_message.getString("data");
                                        String decryptedMessage = C.cryptography(msgFromServer);
                                        messages[i] = new MessageClass(json_message.getString("sender"), decryptedMessage);

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                mA.update(messages);
                            } else {
                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                String getMessagesErr = prefs.getString("getMessagesErr", null);
                                Toast.makeText(MessageActivity.this, getMessagesErr, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;

        /*final MessageClass message = (MessageClass)mA.getItem(i);

        if(message.getSenderId().compareTo(sender_userid) == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete");
            alert.setMessage("Are you sure you want to delete this message?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    registerDbHelper.deleteMessage(message.getMsgId());

                    reloadMList(sender_userid, receiver_userid);
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alert.show();
            return true;
        }

        return false;

    }*/

    /*public void chatBot(String txt) {
        String bot_msg = "";

        if(txt.contains("Hello")) {
            bot_msg = "Hey!";
        }
        if (txt.contains("How")) {
            bot_msg = "I'm fine thanks, you ?";
        }
        if (txt.contains("What")) {
            bot_msg = "Nothing special, you?";
        }
        if(txt.contains("Bye")){
            bot_msg = "Bye!";
        }

        if (bot_msg.length() > 0) {
            MessageClass message = new MessageClass(null, receiver_userid, sender_userid, bot_msg);
            registerDbHelper.insert_message(message);
            reloadMList(sender_userid, receiver_userid);
        }*/
    }

}


/*@Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        /*final MessageClass message = (MessageClass) messagelistadapter.getItem(position);
        // Check if user is deleting his messages, if not, show toast
        if (message.getsSenderId().compareTo(sender_userid) == 0) {
            // Delete confirmation dialog
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getText(R.string.dialog_delete_title));
            alert.setMessage(getText(R.string.dialog_delete_message_confirmation_text));
            alert.setPositiveButton(getText(R.string.dialog_delete_positive_btn), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Deleting message from database
                    //chatDbHelper.deleteMessage(message.getsMessageId());
                    // Updating messages list
                    //updateMessagesList(sender_userid, receiver_userid);
                }
            });
            alert.setNegativeButton(getText(R.string.dialog_delete_negative_btn), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
            return true;
        } else {
            Toast.makeText(this, getText(R.string.error_delete_only_your_messages), Toast.LENGTH_SHORT).show();
            return false;
        }*//*
        return false;
                }*/