package tw.iccl.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Macintosh on 13/6/30.
 */
public class Notification {
    /***	Debugging	***/
    private static final String TAG = "Notification";
    private static final boolean D  = true;

    private Context mContext;

    public final static String BR_NOTIFICATION = "Notification";

    public BroadcastReceiver mRequestServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BR_NOTIFICATION)) {
                String result = intent.getStringExtra("result");
                try {
                    JSONObject mJsonObject = new JSONObject(result);
                    int ErrorCount = mJsonObject.getInt("ErrorCount");
                    if(ErrorCount == 1) {
                        JSONArray jsonArray = new JSONArray(mJsonObject.getString("data"));
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONArray mJSONArray = new JSONArray(jsonArray.getString(i));
                            String Kind = mJSONArray.getString(0);
                            String Status = mJSONArray.getString(1);
                            if(Status.equals("Error")) {
                                Notify.NotifyMsg(mContext, ErrorCount,  Kind);
                                break;
                            }
                        }
                    } else {
                        Notify.NotifyMsg(mContext, ErrorCount, "Default");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public Notification(Context context) {
        mContext = context;
        EnableReceiver();
    }

    private void EnableReceiver() {
        // TODO Auto-generated method stub
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BR_NOTIFICATION);

        mContext.registerReceiver(mRequestServerReceiver, mIntentFilter);
    }

    public void DisableReceiver() {
        mContext.unregisterReceiver(mRequestServerReceiver);
    }
}
