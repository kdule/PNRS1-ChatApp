package kenjic.dusan.chatapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;


/**
 * Created by student on 29.5.2018.
 */

public class BinderForNotif extends IBinderForNotif.Stub {
    private ICallbackForNotif notifCB;
    private CallbackCaller notifCaller;

    @Override
    public void setCallback(ICallbackForNotif callback) throws RemoteException {
        notifCB = callback;
        notifCaller = new CallbackCaller();
        notifCaller.start();
    }

    public void stop() {
        notifCaller.stop();
    }

    private class CallbackCaller implements Runnable {

        private static final long PERIOD = 5000L;

        private Handler mHandler = null;
        private boolean mRun = true;

        public void start() {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(this, PERIOD);
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (!mRun) {
                return;
            }

            try {
                notifCB.onCallbackCall();
            } catch (NullPointerException e) {
                // callback is null, do nothing
            } catch (RemoteException e) {
            }

            mHandler.postDelayed(this, PERIOD);
        }
    }
}
