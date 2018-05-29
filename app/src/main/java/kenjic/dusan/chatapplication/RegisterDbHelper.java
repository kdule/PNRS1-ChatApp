package kenjic.dusan.chatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by student on 20.4.2018.
 */
/*
public class RegisterDbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "chat.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_CON = "contacts";
    public static final String COLUMN_CON_ID= "ContactId";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";
    public static final String COLUMN_USERNAME = "UserName";

    public static final String TABLE_NAME_MSG = "contacts";
    public static final String COLUMN_MSG_ID= "MessageId";
    public static final String COLUMN_SENDER_ID = "SenderId";
    public static final String COLUMN_RECEIVER_ID = "ReceiverId";
    public static final String COLUMN_MSG = "Message";

    public RegisterDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_CON + " (" +
                    COLUMN_CON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_FIRST_NAME + " TEXT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_USERNAME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_NAME_MSG + " (" +
                COLUMN_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_SENDER_ID + " TEXT, " +
                COLUMN_RECEIVER_ID + " TEXT, " +
                COLUMN_MSG + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert_contacts(ContactClass con) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CON_ID, con.getContactID());
        values.put(COLUMN_FIRST_NAME, con.getFirstNameCon());
        values.put(COLUMN_LAST_NAME, con.getLastNameCon());
        values.put(COLUMN_USERNAME, con.getUserNameCon());

        db.insert(TABLE_NAME_CON, null, values);
        close();
    }

    public void insert_message(MessageClass msg) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MSG_ID, msg.getMsgId());
        values.put(COLUMN_SENDER_ID, msg.getSenderId());
        values.put(COLUMN_RECEIVER_ID, msg.getReceiverId());
        values.put(COLUMN_MSG, msg.getMsg());

        db.insert(TABLE_NAME_MSG, null, values);
        close();
    }

    public ContactClass[] readContacts(String loggedinuserid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if (loggedinuserid == null) {
            cursor = db.query(TABLE_NAME_CON, null, null,
                    null, null, null, null, null);
        } else {
            cursor = db.query(TABLE_NAME_CON, null, COLUMN_CON_ID + "!=?",
                    new String[]{loggedinuserid}, null, null, null, null);
        }

        if (cursor.getCount() <= 0) {
            return null;
        }

        ContactClass[] contacts = new ContactClass[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContacts(cursor);
        }

        close();
        return contacts;
    }

    private ContactClass createContacts(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(COLUMN_CON_ID));
        String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
        String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));

        return new ContactClass(firstName, lastName, userName, id);
    }

    public boolean searchContactByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_CON, null, COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            db.close();
            cursor.close();
            return false;
        } else {
            db.close();
            cursor.close();
            return true;
        }
    }

    public void deleteContact(String contactId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_CON, COLUMN_CON_ID + "=?", new String[]{contactId});
        close();
    }


    public MessageClass[] readMessages(String sender, String receiver) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_MSG, null, "(SenderId =? AND ReceiverId =?) OR (SenderId =? AND ReceiverId =?)", new String[]{sender, receiver, receiver, sender}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        MessageClass[] messages = new MessageClass[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        close();
        return messages;
    }

    private MessageClass createMessage(Cursor cursor) {
        String sMessageId = cursor.getString(cursor.getColumnIndex(COLUMN_MSG_ID));
        String sSenderId = cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID));
        String sReceiverId = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_ID));
        String sMessage = cursor.getString(cursor.getColumnIndex(COLUMN_MSG));

        return new MessageClass(sMessageId, sSenderId, sReceiverId, sMessage);
    }

    public void deleteMessage(String messageId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_MSG, COLUMN_MSG_ID + "=?", new String[]{messageId});
        close();
    }
}
*/