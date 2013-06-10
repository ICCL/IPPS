package tw.iccl.view;

import static tw.iccl.pull.PullService.GET_ALL;
import static tw.iccl.pull.PullService.GET_HUMIDITY;
import static tw.iccl.pull.PullService.GET_LIGHT;
import static tw.iccl.pull.PullService.GET_SOIL;
import static tw.iccl.pull.PullService.GET_TEMPERATURE;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tw.iccl.ipps.R;
import tw.iccl.pull.PullService;

/**
 * Created by Macintosh on 13/6/9.
 */
public class Sensor {

    public final static boolean D = true;
    public final static String TAG = "MainActivity";

    private Context mContext;
    private PullService mPullServer;

    private RelativeLayout Temp, Hum, Light, Soil;
    private TextView  vHum, vLight, vSoil, vTemp;

    public final static String BR_SENSORDATA = "SensorData";

    public final static String CV_HUMIDITY = "humiditys";
    public final static String CV_LIGHT = "lights";
    public final static String CV_SOIL = "soils";
    public final static String CV_TEMPERATURE = "temperatures";

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BR_SENSORDATA)) {
                int Kind = intent.getIntExtra("Kind", 0);
                String val = intent.getStringExtra("result");
                if(D) Log.e(TAG, "Kind " + Kind + " val: " + val);
                switch(Kind) {
                    case GET_HUMIDITY:
                        vHum.setText(val);
                        break;
                    case GET_LIGHT:
                        vLight.setText(val);
                        break;
                    case GET_SOIL:
                        vSoil.setText(val);
                        break;
                    case GET_TEMPERATURE:
                        vTemp.setText(val);
                        break;
                }
            }
        }
    };

    public Sensor(Context context) {
        this.mContext = context;

        init_view();
        onReceiver();
    }

    private void onReceiver() {
        IntentFilter intentFilter = new IntentFilter(BR_SENSORDATA);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private View findViewById(int R) {
        return ((Activity)mContext).findViewById(R);
    }

    private void init_view() {
        Temp = (RelativeLayout)findViewById(R.id.sensor_temp);
        Temp.setOnClickListener(TempOnClick);

        Hum = (RelativeLayout)findViewById(R.id.sensor_hum);
        Hum.setOnClickListener(HumOnClick);

        Light = (RelativeLayout)findViewById(R.id.sensor_light);
        Light.setOnClickListener(LightOnClick);

        Soil = (RelativeLayout)findViewById(R.id.sensor_soil);
        Soil.setOnClickListener(SoilOnClick);

         vTemp = (TextView) findViewById(R.id.val_temp);
          vHum = (TextView) findViewById(R.id.val_hum);
        vLight = (TextView) findViewById(R.id.val_light);
         vSoil = (TextView) findViewById(R.id.val_soil);

        getInitData();
    }

    private View.OnClickListener HumOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            Intent mIntent = new Intent(mContext, Chart.class);
            mIntent.putExtra("Kind", CV_HUMIDITY);
            mContext.startActivity( mIntent );
        }
    };

    private View.OnClickListener LightOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            Intent mIntent = new Intent(mContext, Chart.class);
            mIntent.putExtra("Kind", CV_LIGHT);
            mContext.startActivity( mIntent );
        }
    };

    private View.OnClickListener SoilOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            Intent mIntent = new Intent(mContext, Chart.class);
            mIntent.putExtra("Kind", CV_SOIL);
            mContext.startActivity( mIntent );
        }
    };

    private View.OnClickListener TempOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            Intent mIntent = new Intent(mContext, Chart.class);
            mIntent.putExtra("Kind", CV_TEMPERATURE);
            mContext.startActivity( mIntent );
        }
    };

    private void getInitData() {
        mPullServer = new PullService(mContext);
        mPullServer.Pull(GET_ALL, 1);
    }

    public void DesableReceiver() {
        mContext.unregisterReceiver(mBroadcastReceiver);
        mPullServer.DesableReceiver();
    }
}
