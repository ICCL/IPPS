package tw.iccl.pullpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static tw.iccl.config.config.Url;
import static tw.iccl.Notification.Notification.BR_NOTIFICATION;
import static tw.iccl.view.Device.BR_DEVICEDATA;
import static tw.iccl.view.Device.TAG;
import static tw.iccl.view.Sensor.BR_SENSORDATA;

/**
 * Created by Macintosh on 13/6/9.
 */
public class PullService {
    /***	Debugging	***/
    private static final String TAG = "PullServer";
    private static final boolean D  = true;

    private Context mContext;

    public static final String BR_GCM = "RequestGCM";
    public static final String BR_Pull = "Pull";

    public final static int GET_ALL             = 1;
    public final static int GET_HUMIDITY        = 2;
    public final static int GET_LIGHT           = 3;
    public final static int GET_SOIL            = 4;
    public final static int GET_TEMPERATURE     = 5;
    public final static int GET_EquipmentStatus = 6;

    private final static String Kind_UpdateData         = "UpdateData";
    private final static String Kind_UpdateSensorStatus = "UpdateSensorStatus";
    private final static String Kind_Notification       = "Notification";

    private String GetUrl = "";
    private final static String url_ALL             = Url + "/all/json/";
    private final static String url_TEMPERATURE     = Url + "/Temperatures/json/";
    private final static String url_HUMIDITY        = Url + "/Humiditys/json/";
    private final static String url_LIGHT           = Url + "/Lights/json/";
    private final static String url_SOIL            = Url + "/Soils/json/";
    private final static String url_EquipmentStatus = Url + "/api/equipmentStatus/";

    public BroadcastReceiver mRequestServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BR_GCM)) {
                String result = intent.getStringExtra("msg");
                if(D) Log.e(TAG, "result: "+ result);
                try {
                    JSONObject obj = new JSONObject(result);
                    String Kind = obj.getString("Kind");
                    if(Kind.equals(Kind_UpdateData)) {
                        Intent mIntent = new Intent(BR_SENSORDATA);
                        JSONArray jsonArray = new JSONArray(obj.getString("data"));
                        for(int i=0;i<jsonArray.length();i++) {
                            mIntent.putExtra("Kind", i+2);
                            mIntent.putExtra("result", GetVal(jsonArray.getJSONObject(i).toString()));
                            mContext.sendBroadcast(mIntent);
                        }
                    } else if(Kind.equals(Kind_UpdateSensorStatus)) {
                        Intent mIntent = new Intent(BR_DEVICEDATA);
                        mIntent.putExtra("result", obj.getString("data"));
                        mContext.sendBroadcast(mIntent);
                    } else if(Kind.equals(Kind_Notification)) {
                        Intent mIntent = new Intent(BR_NOTIFICATION);
                        mIntent.putExtra("result", obj.getString("data"));
                        mContext.sendBroadcast(mIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(BR_Pull)) {
                int Kind = intent.getIntExtra("Kind", 0);
                switch(Kind) {
                    case GET_EquipmentStatus:
                        Pull(Kind);
                        if(D) Log.e(TAG, "GET_EquipmentStatus");
                        break;
                    default:
                        int getCount = intent.getIntExtra("getCount", 0);
                        Pull(Kind, getCount);
                        if(D) Log.e(TAG, "default");
                }

            }
        }
    };

    public PullService(Context context) {
        this.mContext = context;

        EnableReceiver();
    }

    private void EnableReceiver() {
        // TODO Auto-generated method stub
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BR_GCM);
        mIntentFilter.addAction(BR_Pull);

        mContext.registerReceiver(mRequestServerReceiver, mIntentFilter);
    }

    public void DisableReceiver() {
        mContext.unregisterReceiver(mRequestServerReceiver);
    }

    public void Pull(int Kind) {
        switch(Kind) {
            case GET_EquipmentStatus:
                GetUrl = url_EquipmentStatus;
                break;
        }
        new PullServer(GetUrl, Kind);
    }

    public void Pull(int Kind, int getCount) {
        switch(Kind) {
            case GET_ALL:
                GetUrl = url_ALL + getCount;
                break;
            case GET_TEMPERATURE:
                GetUrl = url_TEMPERATURE + getCount;
                break;
            case GET_HUMIDITY:
                GetUrl = url_HUMIDITY + getCount;
                break;
            case GET_LIGHT:
                GetUrl = url_LIGHT + getCount;
                break;
            case GET_SOIL:
                GetUrl = url_SOIL + getCount;
                break;
            case GET_EquipmentStatus:
                GetUrl = url_EquipmentStatus;
                break;
        }
        new PullServer(GetUrl, Kind);
    }

    private String GetVal(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        JSONArray jsonArray = new JSONArray(obj.getString("data"));
        return jsonArray.getJSONObject(0).getString("value");
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            int kind = (int)msg.arg1;
            Intent mIntent = new Intent(BR_SENSORDATA);
            if(D) Log.e(TAG, "result: "+ result);
            try {
                switch(kind) {
                    case GET_ALL:
                        JSONArray jsonArray = new JSONArray(result);
                        for(int i=0;i<jsonArray.length();i++) {
                            mIntent.putExtra("Kind", i+2);
                            mIntent.putExtra("result", GetVal(jsonArray.getJSONObject(i).toString()));
                            mContext.sendBroadcast(mIntent);
                        }
                        break;
                    case GET_HUMIDITY:
                        mIntent.putExtra("Kind", GET_HUMIDITY);
                        mIntent.putExtra("result", GetVal(result));
                        mContext.sendBroadcast(mIntent);
                        break;
                    case GET_LIGHT:
                        mIntent.putExtra("Kind", GET_LIGHT);
                        mIntent.putExtra("result", GetVal(result));
                        mContext.sendBroadcast(mIntent);
                        break;
                    case GET_SOIL:
                        mIntent.putExtra("Kind", GET_SOIL);
                        mIntent.putExtra("result", GetVal(result));
                        mContext.sendBroadcast(mIntent);
                        break;
                    case GET_TEMPERATURE:
                        mIntent.putExtra("Kind", GET_TEMPERATURE);
                        mIntent.putExtra("result", GetVal(result));
                        mContext.sendBroadcast(mIntent);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    class PullServer implements Runnable {

        private String result;
        private String Url;
        private int kind;

        PullServer(String Url, int kind) {
            this.Url = Url;
            this.kind = kind;
            Thread mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public void run() {
            if(D) Log.e(TAG, "GetUrl: "+ Url);
            HttpGet httpGet = new HttpGet(Url);
            try {
                HttpResponse mHttpResponse = new DefaultHttpClient().execute(httpGet);
                if(mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(mHttpResponse.getEntity());
//                    if(D) Log.e(TAG, "result " + result);
                    sendMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendMessage() {
            Message m = Message.obtain(mHandler);
            m.obj = result;
            m.arg1 = kind;
            mHandler.sendMessage(m);
        }
    }
}
