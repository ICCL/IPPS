package tw.iccl.view;

import static tw.iccl.pullpush.PullService.BR_Pull;
import static tw.iccl.pullpush.PullService.GET_ALL;
import static tw.iccl.pullpush.PullService.GET_HUMIDITY;
import static tw.iccl.pullpush.PullService.GET_LIGHT;
import static tw.iccl.pullpush.PullService.GET_SOIL;
import static tw.iccl.pullpush.PullService.GET_TEMPERATURE;
import static tw.iccl.pullpush.PullService.GET_EquipmentStatus;
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
import tw.iccl.pullpush.PullService;

/**
 * Created by Macintosh on 13/6/9.
 */
public class Sensor {

    public final static boolean D = true;
    public final static String TAG = "Sensor";

    private Context mContext;

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
                String value = intent.getIntExtra("value", 0)+"";
//                if(D) Log.e(TAG, "value: "+ value);
                switch(Kind) {
                    case GET_HUMIDITY:
                        vHum.setText(value);
                        break;
                    case GET_LIGHT:
                        vLight.setText(value);
                        break;
                    case GET_SOIL:
                        vSoil.setText(value);
                        break;
                    case GET_TEMPERATURE:
                        vTemp.setText(value);
                        break;
                }
            }
        }
    };

    public Sensor(Context context) {
        this.mContext = context;
        onReceiver();

        init_view();

        GetAll(GET_ALL, 1);
        GetEquipmentStatus(GET_EquipmentStatus);
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
    }

    private void GetAll(int Kind, int getCount) {
        Intent mIntent = new Intent(BR_Pull);
        mIntent.putExtra("Kind", Kind);
        mIntent.putExtra("getCount", getCount);
        mContext.sendBroadcast( mIntent );
    }

    private void GetEquipmentStatus(int Kind) {
        Intent mIntent = new Intent(BR_Pull);
        mIntent.putExtra("Kind", Kind);
        mContext.sendBroadcast( mIntent );
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

    public void DisableReceiver() {
        mContext.unregisterReceiver(mBroadcastReceiver);
    }
}
