package tw.iccl.pullpush;

import static tw.iccl.config.config.Url;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Macintosh on 13/6/30.
 */
public class PushService {
    /***	Debugging	***/
    private static final String TAG = "PushService";
    private static final boolean D  = true;

    private Context mContext;

    public final static String BR_PUSH = "Push";

    private String PushUrl = "";

    public BroadcastReceiver mRequestServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BR_PUSH)) {
                String Kind = intent.getStringExtra("Kind");
                String Status = intent.getStringExtra("Status");
                PushUrl = Url+"/api/equipment/" + Kind + "/" + Status;
                new PushServer();
            }
        }
    };

    public PushService(Context context) {
        this.mContext = context;

        EnableReceiver();
    }

    private void EnableReceiver() {
        // TODO Auto-generated method stub
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BR_PUSH);

        mContext.registerReceiver(mRequestServerReceiver, mIntentFilter);
    }

    public void DisableReceiver() {
        mContext.unregisterReceiver(mRequestServerReceiver);
    }

    class PushServer implements Runnable {

        private String result;

        PushServer() {
            Thread mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public void run() {
            HttpGet httpGet = new HttpGet(PushUrl);
            try {
                HttpResponse mHttpResponse = new DefaultHttpClient().execute(httpGet);
                if(mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(mHttpResponse.getEntity());
//                    if(D) Log.e(TAG, "result " + result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
