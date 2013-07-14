package tw.iccl.option;

import static tw.iccl.config.config.Url;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import tw.iccl.ipps.R;

/**
 * Created by Macintosh on 13/7/6.
 */
public class Preferences {

    /**
     * Debugging	**
     */
    private static final String TAG = "Preferences";
    private static final boolean D = true;

    private final Context mContext;

    public final static int HUMIDITY = 1;
    public final static int LIGHT = 2;
    public final static int SOIL = 3;
    public final static int TEMPERATURE = 4;

    public String Key_HUMIDITY = "";
    public String Key_LIGHT = "";
    public String Key_SOIL = "";
    public String Key_TEMPERATURE = "";

    public Preferences(Context context) {
        mContext = context;

        Key_HUMIDITY = mContext.getResources().getString(R.string.key_humidity);
        Key_LIGHT = mContext.getResources().getString(R.string.key_light);
        Key_SOIL = mContext.getResources().getString(R.string.key_soil);
        Key_TEMPERATURE = mContext.getResources().getString(R.string.key_temperature);
    }

    public void setPreferences(int Kind, Object Value) {
        String Item = "";
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor mEditor = spref.edit();
        switch (Kind) {
            case HUMIDITY:
                mEditor.putString(Key_HUMIDITY, (String) Value);
                Item = "humidity";
                break;
            case LIGHT:
                mEditor.putString(Key_LIGHT, (String) Value);
                Item = "light";
                break;
            case SOIL:
                mEditor.putString(Key_SOIL, (String) Value);
                Item = "soil";
                break;
            case TEMPERATURE:
                mEditor.putString(Key_TEMPERATURE, (String) Value);
                Item = "temperature";
                break;
        }
        mEditor.commit();
//        if(D) Log.e(TAG, "Kind: "+ Kind +" Value: "+Value);

        String url = Url + "/api/setSafetys/"+ Item +"/"+ Value;
        new PullServer(url);
    }

    public Object getPreferences(int Kind) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (D) Log.e(TAG, "Kind: " + Kind);
        switch (Kind) {
            case HUMIDITY:
                return spref.getString(Key_HUMIDITY, "");
            case LIGHT:
                return spref.getString(Key_LIGHT, "");
            case SOIL:
                return spref.getString(Key_SOIL, "");
            case TEMPERATURE:
                return spref.getString(Key_TEMPERATURE, "");
            default:
                return "";
        }
    }

    class PullServer implements Runnable {

        private String result;
        private String Url;

        PullServer(String Url) {
            this.Url = Url;
            Thread mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public void run() {
            if (D) Log.e(TAG, "GetUrl: " + Url);
            HttpGet httpGet = new HttpGet(Url);
            try {
                HttpResponse mHttpResponse = new DefaultHttpClient().execute(httpGet);
                if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(mHttpResponse.getEntity());
                    if (D) Log.e(TAG, "result " + result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
