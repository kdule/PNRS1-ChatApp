package kenjic.dusan.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Duuuuuc on 4/1/2018.
 */

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MessageClass> myMessages;

    private static final String MY_PREFS_NAME = "PrefsFile";


    public MessageAdapter(Context c) {
        this.context = c;
        myMessages = new ArrayList<MessageClass>();
    }

    public void addMessageClasses(MessageClass m) {
        myMessages.add(m);
        notifyDataSetChanged();
    }

    public void update(MessageClass[] messages) {
        myMessages.clear();
        if (messages != null) {
            Collections.addAll(myMessages, messages);
        }
        notifyDataSetChanged();
    }

    public void removeMessageClass(int i) {
        myMessages.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myMessages.size();
    }

    @Override
    public Object getItem(int i) {
        Object o = null;
        try{
            o = myMessages.get(i);
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return o;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.raw_msg_activity, null);

            MessageAdapter.MessageHolder holder = new MessageAdapter.MessageHolder();
            holder.txtMsg = (TextView) view.findViewById(R.id.msg_txt1);
            view.setTag(holder);
        }

        MessageClass mc = (MessageClass)getItem(i);
        MessageAdapter.MessageHolder holder = (MessageAdapter.MessageHolder) view.getTag();

        holder.txtMsg.setText(mc.getMsg());

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        String sender_userid = prefs.getString("loggedinUsername", null);

        if(mc.getSenderId().compareTo(sender_userid) == 0) {
            holder.txtMsg.setGravity(Gravity.RIGHT|Gravity.CENTER);
            holder.txtMsg.setBackgroundColor(Color.argb(0, 255,255,255));
            holder.txtMsg.setTextColor(Color.rgb(137,137,137));
        }
        else {
            holder.txtMsg.setGravity(Gravity.LEFT|Gravity.CENTER);
            holder.txtMsg.setTextColor(Color.rgb(220,220,220));
            holder.txtMsg.setBackgroundColor(Color.argb(255, 137,137,137));
        }

        return view;
    }

    private class MessageHolder {
        public TextView txtMsg = null;
    }
}
