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

import tw.iccl.option.Preferences;

import static tw.iccl.Notification.Notification.BR_NOTIFICATION;
import static tw.iccl.config.config.Url;
import static tw.iccl.view.Device.BR_DEVICEDATA;
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
                if(D) Log.e(TAG, "BR_GCM");
                String result = intent.getStringExtra("msg");
                if(D) Log.e(TAG, "result: "+ result);
                try {
                    JSONObject obj = new JSONObject(result);
                    String Kind = obj.getString("Kind");
                    if(Kind.equals(Kind_UpdateData)) {
                        String Response = obj.getString("data");
                        Response(Response);
                    } else if(Kind.equals(Kind_UpdateSensorStatus)) {
                        if(D) Log.e(TAG, "Kind_UpdateSensorStatus");
                        Intent mIntent = new Intent(BR_DEVICEDATA);
                        mIntent.putExtra("result", obj.getString("data"));
                        mContext.sendBroadcast(mIntent);
                    } else if(Kind.equals(Kind_Notification)) {
                        if(D) Log.e(TAG, "Kind_Notification");
                        Intent mIntent = new Intent(BR_NOTIFICATION);
                        mIntent.putExtra("result", obj.getString("data"));
                        mContext.sendBroadcast(mIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(BR_Pull)) {
                if(D) Log.e(TAG, "BR_Pull");
                int Kind = intent.getIntExtra("Kind", 0);
                switch(Kind) {
                    case GET_EquipmentStatus:
                        if(D) Log.e(TAG, "GET_EquipmentStatus");
                        Pull(Kind);
                        break;
                    default:
                        if(D) Log.e(TAG, "default");
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
        }
        new PullServer(GetUrl, Kind);
    }

    private void Response(String result) {
        try {
            if(D) Log.e(TAG, "result: " + result);
            JSONArray jsonArray = new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++) {
                JSONArray data = new JSONArray(jsonArray.getJSONObject(i).getString("data"));
                JSONObject safety = new JSONObject(jsonArray.getJSONObject(i).getString("safety"));

                String name = getSafety(safety);
                getData(data, name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getSafety(JSONObject result) throws JSONException {
        String en_name = result.getString("en_name");
        String value = result.getString("value");
        if(D) Log.e(TAG, "name: " + en_name + " value: " + value);
        Preferences mPreferences = new Preferences(mContext);
        if(en_name.equals("Humidity")) {
            mPreferences.setPreferences(mPreferences.HUMIDITY, value);
        } else if (en_name.equals("Light")){
            mPreferences.setPreferences(mPreferences.LIGHT, value);
        } else if (en_name.equals("Soil")){
            mPreferences.setPreferences(mPreferences.SOIL, value);
        } else if (en_name.equals("Temperature")){
            mPreferences.setPreferences(mPreferences.TEMPERATURE, value);
        }
        return en_name;
    }

    private void getData(JSONArray result, String name) throws JSONException {
        String value = result.getJSONObject(0).getString("value");
        Intent mIntent = new Intent(BR_SENSORDATA);
        if(name.equals("Humidity")) {
            mIntent.putExtra("Kind", GET_HUMIDITY);
        } else if (name.equals("Light")){
            mIntent.putExtra("Kind", GET_LIGHT);
        } else if (name.equals("Soil")){
            mIntent.putExtra("Kind", GET_SOIL);
        } else if (name.equals("Temperature")){
            mIntent.putExtra("Kind", GET_TEMPERATURE);
        }
        mIntent.putExtra("value", value);
        mContext.sendBroadcast(mIntent);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            int Kind = msg.arg1;
            switch(Kind) {
                case GET_EquipmentStatus:
                    break;
                default:
                    Response(result);
            }

        }
    };

    class PullServer implements Runnable {

        private String result;
        private String Url;
        private int Kind;

        PullServer(String Url, int kind) {
            this.Url = Url;
            this.Kind = kind;
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
            m.arg1 = Kind;
            m.obj = result;
            mHandler.sendMessage(m);
        }
    }
}
