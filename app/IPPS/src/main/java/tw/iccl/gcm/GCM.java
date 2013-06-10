package tw.iccl.gcm;

import static tw.iccl.config.config.SENDER_ID;
import static tw.iccl.config.config.UserRegistrarId;
import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

/**
 * Created by Macintosh on 13/6/9.
 */
public class GCM {
    private final static String TAG = "GCM";
    private final static boolean D = true;

    Context mContext = null;

    public GCM(Context mContext) {
        if(D) Log.e(TAG, "Start GCM");
        this.mContext = mContext;
        if(mContext != null) Registrar();
    }

    private void Registrar() {
        GCMRegistrar.checkDevice(this.mContext);
        GCMRegistrar.checkManifest(mContext);
        String regId = GCMRegistrar.getRegistrationId(mContext);
        if (regId.equals("")) {
            if(D) Log.e(TAG, "+++ regId.equals +++");
            GCMRegistrar.register(mContext, SENDER_ID);
        } else {
            Log.e(TAG, "regId: "+regId);
            UserRegistrarId = regId;
        }
    }

    public void Unregister() {
        GCMRegistrar.onDestroy(mContext);

    }

}
