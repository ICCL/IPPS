package tw.iccl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import tw.iccl.gcm.GCM;

/**
 * Created by Macintosh on 13/6/2.
 */
public class BackgroundService extends Service {

    private final static String TAG = "BackgroundService";
    private final static boolean D = true;

    /* Background Config */
    public static boolean haveBackgroundService = true;

    private GCM mGCM = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        haveBackgroundService = false;

        GCM_Receiver();
    }

    private void GCM_Receiver() {
        if(mGCM == null) mGCM = new GCM(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DisableReceiver();

        haveBackgroundService = true;
    }

    private void DisableReceiver() {
        if( mGCM != null) mGCM.Unregister();
    }
}
