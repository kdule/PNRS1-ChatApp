package kenjic.dusan.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Duuuuuc on 3/31/2018.
 */

public class ContactAdapter extends BaseAdapter implements View.OnClickListener{
    private Context myChatContext;
    private ArrayList<ContactClass> contactList;

    public static final String MY_PREFS_NAME = "PrefsFile";

    public ContactAdapter(Context context) {
        myChatContext = context;
        contactList = new ArrayList<ContactClass>();
    }

    public void update(ContactClass[] contacts) {
        contactList.clear();
        if (contacts != null) {
            for (ContactClass contact : contacts) {
                contactList.add(contact);
            }
        }
        notifyDataSetChanged();
    }

    // Remove contact from list
    public void removecontact(int position) {
        contactList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nextMsg) {
            Intent intent = new Intent(myChatContext.getApplicationContext(), MessageActivity.class);

            SharedPreferences.Editor editor = myChatContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString("receiver_userId", view.getTag().toString());
            editor.apply();

            myChatContext.startActivity(intent);
        }
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int i) {
        Object c = null;
        try{
            c = contactList.get(i);
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return c;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) myChatContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item, null);

            ContactHolder holder = new ContactHolder();
            holder.firstLetterC = (TextView) view.findViewById(R.id.firstLetter);
            holder.nameC = (TextView) view.findViewById(R.id.name);
            holder.imgBtn = (ImageButton) view.findViewById(R.id.nextMsg);
            holder.imgBtn.setOnClickListener(this);
            view.setTag(holder);
        }

        ContactClass cc = (ContactClass) getItem(i);
        ContactHolder holder = (ContactHolder) view.getTag();

        Random ran = new Random();
        int color = Color.argb(255, ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));

        //String tmp = cc.getConName();
        holder.firstLetterC.setText(cc.getUserNameCon().substring(0,1).toUpperCase());
        holder.firstLetterC.setBackgroundColor(color);
        holder.nameC.setText(cc.getUserNameCon());
        holder.imgBtn.setTag(cc.getUserNameCon());

        return view;
    }

    private class ContactHolder{
        public TextView firstLetterC = null;
        public TextView nameC = null;
        public ImageButton imgBtn = null;
    }

}
