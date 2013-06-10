package tw.iccl.pull;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static tw.iccl.config.config.Url;
import static tw.iccl.view.Sensor.BR_SENSORDATA;

/**
 * Created by Macintosh on 13/6/9.
 */
public class PullService {
    /***	Debugging	***/
    private static final String TAG = "PullServer";
    private static final boolean D = true;

    private Context mContext;

    private List<NameValuePair> mParames;

    private int KIND = 0;
    public final static int GET_ALL = 1;
    public final static int GET_HUMIDITY = 2;
    public final static int GET_LIGHT = 3;
    public final static int GET_SOIL = 4;
    public final static int GET_TEMPERATURE = 5;

    private String GetUrl = "";
    private final static String url_ALL = Url + "/all/json/";
    private final static String url_TEMPERATURE = Url + "/Temperatures/json/";
    private final static String url_HUMIDITY = Url + "/Humiditys/json/";
    private final static String url_LIGHT = Url + "/Lights/json/";
    private final static String url_SOIL = Url + "/Soils/json/";

    public BroadcastReceiver mRequestServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("RequestServerAll")) {

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
        mIntentFilter.addAction("PullAllData");

        mContext.registerReceiver(mRequestServerReceiver, mIntentFilter);
    }

    public void DesableReceiver() {
        mContext.unregisterReceiver(mRequestServerReceiver);
    }

    public void Pull(int Kind, int getCount) {
        this.KIND = Kind;
        switch(KIND) {
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
        }
        new PullServer();
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
            Intent mIntent = new Intent(BR_SENSORDATA);

            try {
                switch(KIND) {
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

        PullServer() {
            Thread mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public void run() {
            HttpGet httpGet = new HttpGet(GetUrl);
            try {
                HttpResponse mHttpResponse = new DefaultHttpClient().execute(httpGet);
                if(mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(mHttpResponse.getEntity());
                    if(D) Log.e(TAG, "result " + result);
                    sendMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendMessage() {
            Message m = Message.obtain(mHandler);
            m.obj = result;
            mHandler.sendMessage(m);
        }
    }
}
