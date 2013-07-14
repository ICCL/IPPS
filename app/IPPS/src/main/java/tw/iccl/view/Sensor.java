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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
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
    private TextView vHum, vLight, vSoil, vTemp;
    private ImageView iHum, iLight, iSoil, iTemp;

    public final static String BR_SENSORDATA = "SensorData";

    public final static String CV_HUMIDITY = "humiditys";
    public final static String CV_LIGHT = "lights";
    public final static String CV_SOIL = "soils";
    public final static String CV_TEMPERATURE = "temperatures";

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BR_SENSORDATA)) {
                int Kind = intent.getIntExtra("Kind", 0);
                String value = intent.getStringExtra("value");
//                if(D) Log.e(TAG, "value: "+ value);
                switch (Kind) {
                    case GET_HUMIDITY:
                        vHum.setText(value);
                        vHum = setFontsize(vHum);
                        break;
                    case GET_LIGHT:
                        vLight.setText(value);
                        vLight = setFontsize(vLight);
                        break;
                    case GET_SOIL:
                        vSoil.setText(value);
                        vSoil = setFontsize(vSoil);
                        break;
                    case GET_TEMPERATURE:
                        vTemp.setText(value);
                        vTemp = setFontsize(vTemp);
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
        return ((Activity) mContext).findViewById(R);
    }

    private void init_view() {
        Temp = (RelativeLayout) findViewById(R.id.sensor_temp);
        Temp.setOnClickListener(TempOnClick);

        Hum = (RelativeLayout) findViewById(R.id.sensor_hum);
        Hum.setOnClickListener(HumOnClick);

        Light = (RelativeLayout) findViewById(R.id.sensor_light);
        Light.setOnClickListener(LightOnClick);

        Soil = (RelativeLayout) findViewById(R.id.sensor_soil);
        Soil.setOnClickListener(SoilOnClick);

        vTemp = (TextView) findViewById(R.id.val_temp);
        vHum = (TextView) findViewById(R.id.val_hum);
        vLight = (TextView) findViewById(R.id.val_light);
        vSoil = (TextView) findViewById(R.id.val_soil);

        iTemp = (ImageView) findViewById(R.id.ImgTemp);
        iHum = (ImageView) findViewById(R.id.ImgHum);
        iLight = (ImageView) findViewById(R.id.ImgLight);
        iSoil = (ImageView) findViewById(R.id.ImgSoil);

        iTemp = resetImageSize(iTemp);
        iHum = resetImageSize(iHum);
        iLight = resetImageSize(iLight);
        iSoil = resetImageSize(iSoil);
    }

    private void GetAll(int Kind, int getCount) {
        Intent mIntent = new Intent(BR_Pull);
        mIntent.putExtra("Kind", Kind);
        mIntent.putExtra("getCount", getCount);
        mContext.sendBroadcast(mIntent);
    }

    private void GetEquipmentStatus(int Kind) {
        Intent mIntent = new Intent(BR_Pull);
        mIntent.putExtra("Kind", Kind);
        mContext.sendBroadcast(mIntent);
    }

    private ImageView resetImageSize(ImageView iv) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float og;
        if (width / 720.0f > height / 1280.0f) {
            og = width / 720.0f;
        } else {
            og = height / 1280.0f;
        }
        float size = 70 * og;
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mContext.getResources().getDisplayMetrics());

        iv.getLayoutParams().width = (int) px;
        iv.getLayoutParams().height = (int) px;
        return iv;
    }

    private TextView setFontsize(TextView tv) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        float og;
        if (width / 720.0f > height / 1280.0f) {
            og = width / 720.0f;
        } else {
            og = height / 1280.0f;
        }

        int size = (int) (84 * og);
        switch (tv.getText().length()) {
            case 4:
                size = (int) (size * 0.85f);
                break;
            case 5:
                size = (int) (size * 0.85f * 0.8f);
                break;
        }
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        if (D) Log.e(TAG, "name: " + tv.getText() + " size: " + size);
        return tv;
    }

    private View.OnClickListener HumOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mIntent = new Intent(mContext, Chart.class);
            mIntent.putExtra("Kind", CV_HUMIDITY);
            mContext.startActivity(mIntent);
        }
    };

    private View.OnClickListener LightOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mIntent = new Intent(mContext, Chart.class);
            mIntent.putExtra("Kind", CV_LIGHT);
            mContext.startActivity(mIntent);
        }
    };

    private View.OnClickListener SoilOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mIntent = new Intent(mContext, Chart.class);
            mIntent.putExtra("Kind", CV_SOIL);
            mContext.startActivity(mIntent);
        }
    };

    private View.OnClickListener TempOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mIntent = new Intent(mContext, Chart.class);
            mIntent.putExtra("Kind", CV_TEMPERATURE);
            mContext.startActivity(mIntent);
        }
    };

    public void DisableReceiver() {
        mContext.unregisterReceiver(mBroadcastReceiver);
    }
}
