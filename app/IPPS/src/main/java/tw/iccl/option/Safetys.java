package tw.iccl.option;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import tw.iccl.ipps.R;

import static tw.iccl.pullpush.PushService.BR_PUSH;

/**
 * Created by Macintosh on 13/7/3.
 */
public class Safetys extends SherlockPreferenceActivity implements OnPreferenceChangeListener {

    /***	Debugging	***/
    private static final String TAG = "Safetys";
    private static final boolean D  = true;

    private EditTextPreference Hum, Light, Soil, Temp;

    private Preferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.safetys);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {
        mPreferences = new Preferences(this);

        Hum     = (EditTextPreference) findPreference(mPreferences.Key_HUMIDITY);
        Light   = (EditTextPreference) findPreference(mPreferences.Key_LIGHT);
        Soil    = (EditTextPreference) findPreference(mPreferences.Key_SOIL);
        Temp    = (EditTextPreference) findPreference(mPreferences.Key_TEMPERATURE);

        Hum.setOnPreferenceChangeListener(this);
        Light.setOnPreferenceChangeListener(this);
        Soil.setOnPreferenceChangeListener(this);
        Temp.setOnPreferenceChangeListener(this);

        UpdateValue();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        if(D) Log.e(TAG, "Change preference.key: "+preference.getKey().toString());
        if(D) Log.e(TAG, "Change preference.key: " + value.toString());

        String Key = preference.getKey();
        String Value = value.toString();

        Intent mPushIntent = new Intent(BR_PUSH);
        mPushIntent.putExtra("Kind", "Safetys");
        mPushIntent.putExtra("Item", Key);
        mPushIntent.putExtra("Value", Value);

        if(Key.equals(mPreferences.Key_HUMIDITY)) {
            mPreferences.setPreferences(mPreferences.HUMIDITY, value);
        } else if (Key.equals(mPreferences.Key_LIGHT)) {
            mPreferences.setPreferences(mPreferences.LIGHT, value);
        } else if (Key.equals(mPreferences.Key_SOIL)) {
            mPreferences.setPreferences(mPreferences.SOIL, value);
        } else if (Key.equals(mPreferences.Key_TEMPERATURE)) {
            mPreferences.setPreferences(mPreferences.TEMPERATURE, value);
        }

        UpdateValue();

        return true;
    }

    private void UpdateValue() {
        Hum.setSummary((String)mPreferences.getPreferences(mPreferences.HUMIDITY));
        Light.setSummary((String)mPreferences.getPreferences(mPreferences.LIGHT));
        Soil.setSummary((String)mPreferences.getPreferences(mPreferences.SOIL));
        Temp.setSummary((String)mPreferences.getPreferences(mPreferences.TEMPERATURE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
