package kenjic.dusan.chatapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, ServiceConnection {

    private Button logout;
    private Button refresh;
    private ListView friends;
    private String userId;
    private TextView logged_user;

    private ContactAdapter cA;
    //private RegisterDbHelper registerDbHelper;
    private ContactClass[] contacts;

    private static final String MY_PREFS_NAME = "PrefsFile";

    private HttpHelper httpHelper;
    private Handler handler;
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGOUT_URL = BASE_URL + "/logout";
    private static String DELETE_CONTACT_URL = BASE_URL + "/contact/";
    private IBinderForNotif mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //registerDbHelper = new RegisterDbHelper(this);
        //contacts = registerDbHelper.readContacts(null);

        logged_user = findViewById(R.id.signed_in_user);
        logout = findViewById(R.id.logout_btn);
        refresh = findViewById(R.id.btn_refresh);
        logout.setOnClickListener(this);
        refresh.setOnClickListener(this);

        SharedPreferences temp = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId = temp.getString("signed_in_user", null);

        cA = new ContactAdapter(this);
        friends = findViewById(R.id.chatList);
        friends.setAdapter(cA);
        friends.setOnItemLongClickListener(this);

        httpHelper = new HttpHelper();
        handler = new Handler();

        //if(contacts != null) {
        //for(int i=0; i<contacts.length; i++) {
        //if(contacts[i].getContactID().compareTo(userId) == 0) {
        // String logged_in_user = contacts[i].getUserNameCon();
        logged_user.setText(userId/*logged_in_user*/);
        //break;
        //}
        //}
        //}

        bindService(new Intent(ContactsActivity.this, ServiceForNotif.class), this, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        reloadList();
    }

    public void reloadList() {

        /*if (!registerDbHelper.searchContactByUsername("chatbot")) {
            ContactClass contact = new ContactClass("Chat", "Bot", "chatbot", null);
            registerDbHelper.insert_contacts(contact);
        }

        contacts = registerDbHelper.readContacts(userId);
        cA.update(contacts);*/


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONArray contacts = httpHelper.getContactsFromServer(ContactsActivity.this, CONTACTS_URL);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(contacts != null) {
                                JSONObject contact;
                                ContactClass[] contactsClass = new ContactClass[contacts.length()];

                                for (int i = 0; i < contacts.length(); i++) {
                                    try {
                                        contact = contacts.getJSONObject(i);
                                        contactsClass[i] = new ContactClass(contact.getString("username"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                cA.update(contactsClass);

                            }

                        }
                    });
                } catch (JSONException e2) {
                    e2.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();



    }

    @Override
    public void onClick(View view) {
        /*if (view.getId() == R.id.logout_btn) {
            Intent main_intent = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(main_intent);
        }*/

        switch (view.getId()){
            case R.id.logout_btn:

                new Thread(new Runnable() {
                    public void run() {
                        try {

                            final boolean temp_check = httpHelper.logOutUserFromServer(ContactsActivity.this, LOGOUT_URL);

                            handler.post(new Runnable(){
                                public void run() {
                                    if (temp_check) {
                                        startActivity(new Intent(ContactsActivity.this, MainActivity.class));
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String logoutErr = prefs.getString("logoutErr", null);
                                        Toast.makeText(ContactsActivity.this, logoutErr, Toast.LENGTH_SHORT).show();}
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

            case R.id.btn_refresh:
                reloadList();
                break;
        }

    }





    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        final int deletePos = position;
        final ContactClass contact = (ContactClass) cA.getItem(deletePos);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.delete_contact));
        alert.setMessage(getText(R.string.delete_contact_conf));
        alert.setPositiveButton(getText(R.string.delete_con_positive), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String userToDelete = contact.getUserNameCon();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String userToDelete = contact.getUserNameCon();
                            Log.d("ContactsActivity", userToDelete);
                            final boolean temp_check_long_click = httpHelper.deleteFromServer(ContactsActivity.this, DELETE_CONTACT_URL + userToDelete);
                            handler.post(new Runnable(){
                                public void run() {
                                    if (temp_check_long_click) {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String deleteContactErr = prefs.getString("deleteContactErr", null);
                                        Toast.makeText(ContactsActivity.this, deleteContactErr, Toast.LENGTH_SHORT).show();


                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String logoutErr = prefs.getString("deleteContactErr", null);
                                        Toast.makeText(ContactsActivity.this, logoutErr, Toast.LENGTH_SHORT).show();}
                                }
                            });


                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }).start();


                reloadList();
            }
        });

        alert.setNegativeButton(getText(R.string.delete_con_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();

        return false;

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = IBinderForNotif.Stub.asInterface(iBinder);
        try {
            mService.setCallback(new NotificationCallback());
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    private class NotificationCallback extends ICallbackForNotif.Stub {

        @Override
        public void onCallbackCall() throws RemoteException {

            final HttpHelper httpHelper = new HttpHelper();
            final Handler handler = new Handler();

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getText(R.string.have_new_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


            new Thread(new Runnable() {
                public void run() {
                    try {
                        final boolean response = httpHelper.getNotification(ContactsActivity.this);

                        handler.post(new Runnable() {
                            public void run() {
                                if (response) {
                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(2, mBuilder.build());
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
