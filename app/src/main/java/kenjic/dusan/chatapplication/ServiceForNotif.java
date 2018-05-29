package kenjic.dusan.chatapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by student on 29.5.2018.
 */

public class ServiceForNotif extends Service {
    private BinderForNotif notifBinder = null;


    @Override
    public IBinder onBind(Intent intent) {

        if(notifBinder == null) {
            notifBinder = new BinderForNotif();
        }

        return notifBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        notifBinder.stop();
        return super.onUnbind(intent);
    }


}
