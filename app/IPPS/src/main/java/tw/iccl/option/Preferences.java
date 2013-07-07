package tw.iccl.option;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import tw.iccl.ipps.R;

/**
 * Created by Macintosh on 13/7/6.
 */
public class Preferences {

    /***	Debugging	***/
    private static final String TAG = "Preferences";
    private static final boolean D  = true;

    private final Context mContext;

    public final static int HUMIDITY        = 1;
    public final static int LIGHT           = 2;
    public final static int SOIL            = 3;
    public final static int TEMPERATURE     = 4;

    public String Key_HUMIDITY    = "";
    public String Key_LIGHT       = "";
    public String Key_SOIL        = "";
    public String Key_TEMPERATURE = "";

    public Preferences(Context context) {
        mContext = context;

        Key_HUMIDITY = mContext.getResources().getString(R.string.key_humidity);
        Key_LIGHT = mContext.getResources().getString(R.string.key_light);
        Key_SOIL = mContext.getResources().getString(R.string.key_soil);
        Key_TEMPERATURE = mContext.getResources().getString(R.string.key_temperature);
    }

    public void setPreferences(int Kind, Object Value) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor mEditor = spref.edit();
        switch(Kind) {
            case HUMIDITY:
                mEditor.putString(Key_HUMIDITY, (String) Value);
            case LIGHT:
                mEditor.putString(Key_LIGHT, (String) Value);
            case SOIL:
                mEditor.putString(Key_SOIL, (String) Value);
            case TEMPERATURE:
                mEditor.putString(Key_TEMPERATURE, (String) Value);
        }
        mEditor.commit();
        if(D) Log.e(TAG, "Kind: "+ Kind +" Value: "+Value);
    }

    public Object getPreferences(int Kind) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(D) Log.e(TAG, "Kind: "+ Kind);
        switch(Kind) {
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
}
