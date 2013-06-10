package tw.iccl.ipps;

import static tw.iccl.config.config.SENDER_ID;
import static tw.iccl.config.config.EXTRA_MESSAGE;
import static tw.iccl.pull.PullService.BR_GCM;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * Created by Macintosh on 13/6/9.
 */
public class GCMIntentService extends GCMBaseIntentService {
    public final static boolean D = true;
    public final static String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String RegID) {
        if(D) Log.e(TAG, "Device registered: regId = " + RegID);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String msg = intent.getStringExtra(EXTRA_MESSAGE);
//        if(D) Log.e(TAG, " -- Msg -- " + msg);
        Intent mIntent = new Intent(BR_GCM);
        mIntent.putExtra("msg", msg);
        sendBroadcast(mIntent);
    }

    @Override
    protected void onError(Context context, String ErrorID) {
        if(D) Log.e(TAG, "Received error: " + ErrorID);
    }

    @Override
    protected void onUnregistered(Context context, String RegID) {
        if(D) Log.e(TAG, "Device unregistered");
    }

}
