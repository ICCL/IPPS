package tw.iccl.view;

import static tw.iccl.view.DeviceBtnDisplay.Status;
import static tw.iccl.view.DeviceBtnDisplay.Lamp;
import static tw.iccl.view.DeviceBtnDisplay.Fan;
import static tw.iccl.view.DeviceBtnDisplay.Sprinkler;
import static tw.iccl.pullpush.PushService.BR_PUSH;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;

import tw.iccl.ipps.R;

/**
 * Created by Macintosh on 13/6/9.
 */
public class Device {
    public final static boolean D = true;
    public final static String TAG = "Device";

    private Context mContext;

    private LinearLayout layout_status, layout_lamp, layout_fan, layout_sprinkler;
    DeviceBtnDisplay StatusBtn, LampBtn, FanBtn, SprinklerBtn;

    public final static String BR_DEVICEDATA = "DeviceData";

    public Intent mPushIntent = new Intent(BR_PUSH);

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BR_DEVICEDATA)) {
                String result = intent.getStringExtra("result");
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0;i<jsonArray.length();i++) {
                        String Device = new JSONArray(jsonArray.getString(i)).getString(0);
                        String Val = new JSONArray(jsonArray.getString(i)).getString(1);
                        if(Device.equals("Status")) {
                            if(Val.equals("1")) {
                                StatusBtn.Display(false);
                            } else {
                                StatusBtn.Display(true);
                            }
                        } else if(Device.equals("Lamp")) {
                            if(Val.equals("0")) {
                                LampBtn.Display(false);
                            } else {
                                LampBtn.Display(true);
                            }
                        } else if(Device.equals("Fan")) {
                            if(Val.equals("0")) {
                                FanBtn.Display(false);
                            } else {
                                FanBtn.Display(true);
                            }
                        } else if(Device.equals("Sprinkler")) {
                            if(Val.equals("0")) {
                                SprinklerBtn.Display(false);
                            } else {
                                SprinklerBtn.Display(true);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public Device(Context context) {
        this.mContext = context;

        init_view();
        onReceiver();

        mPushIntent = new Intent(BR_PUSH);
        mPushIntent.putExtra("Kind", "Equipment");
    }

    private View findViewById(int R) {
        return ((Activity)mContext).findViewById(R);
    }

    private void onReceiver() {
        IntentFilter intentFilter = new IntentFilter(BR_DEVICEDATA);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public void DisableReceiver() {
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

    private void init_view() {
        layout_status = (LinearLayout) findViewById(R.id.layout_status);
        layout_status.setOnClickListener(StatusOnClick);
        StatusBtn = new DeviceBtnDisplay(mContext, Status);
        findViewById(R.id.status_img).setOnClickListener(StatusOnClick);

        layout_lamp = (LinearLayout) findViewById(R.id.layout_lamp);
        layout_lamp.setOnClickListener(LampOnClick);
        LampBtn = new DeviceBtnDisplay(mContext, Lamp);
        findViewById(R.id.lamp_img).setOnClickListener(LampOnClick);

        layout_fan = (LinearLayout) findViewById(R.id.layout_fan);
        layout_fan.setOnClickListener(FanOnClick);
        FanBtn = new DeviceBtnDisplay(mContext, Fan);
        findViewById(R.id.fan_img).setOnClickListener(FanOnClick);

        layout_sprinkler = (LinearLayout) findViewById(R.id.layout_sprinkler);
        layout_sprinkler.setOnClickListener(SprniklerOnClick);
        SprinklerBtn = new DeviceBtnDisplay(mContext, Sprinkler);
        findViewById(R.id.sprinkler_img).setOnClickListener(SprniklerOnClick);

        init_default();
    }

    private void init_default() {
        StatusBtn.Display(true);
        LampBtn.Display(false);
        FanBtn.Display(false);
        SprinklerBtn.Display(false);
    }

    private View.OnClickListener StatusOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            if(D) Log.e(TAG, "StatusBtn Click");
            if(D) Log.e(TAG, "StatusBtn Click"+ !StatusBtn.getBtnStatus());
            StatusBtn.Display(!StatusBtn.getBtnStatus());

            if(StatusBtn.getBtnStatus()) {
                mPushIntent.putExtra("Item", "Status");
                mPushIntent.putExtra("Status", "Auto");
            } else {
                mPushIntent.putExtra("Item", "Status");
                mPushIntent.putExtra("Status", "Manually");
            }
            mContext.sendBroadcast(mPushIntent);
        }
    };

    private View.OnClickListener LampOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            if(D) Log.e(TAG, "LampBtn Click");
            if(!StatusBtn.getBtnStatus()) {
                if(D) Log.e(TAG, "LampBtn Click"+ !StatusBtn.getBtnStatus());
                LampBtn.Display(!LampBtn.getBtnStatus());

                if(LampBtn.getBtnStatus()) {
                    mPushIntent.putExtra("Item", "Lamp");
                    mPushIntent.putExtra("Status", "on");
                } else {
                    mPushIntent.putExtra("Item", "Lamp");
                    mPushIntent.putExtra("Status", "off");
                }
                mContext.sendBroadcast(mPushIntent);
            }
        }
    };

    private View.OnClickListener FanOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            if(D) Log.e(TAG, "FanBtn Click");
            if(!StatusBtn.getBtnStatus()) {
                if(D) Log.e(TAG, "FanBtn Click"+ !StatusBtn.getBtnStatus());
                FanBtn.Display(!FanBtn.getBtnStatus());

                if(FanBtn.getBtnStatus()) {
                    mPushIntent.putExtra("Item", "Fan");
                    mPushIntent.putExtra("Status", "on");
                } else {
                    mPushIntent.putExtra("Item", "Fan");
                    mPushIntent.putExtra("Status", "off");
                }
                mContext.sendBroadcast(mPushIntent);
            }
        }
    };

    private View.OnClickListener SprniklerOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            if(D) Log.e(TAG, "SprinklerBtn Click");
            if(!StatusBtn.getBtnStatus()) {
                if(D) Log.e(TAG, "SprinklerBtn Click"+ !StatusBtn.getBtnStatus());
                SprinklerBtn.Display(!SprinklerBtn.getBtnStatus());

                if(SprinklerBtn.getBtnStatus()) {
                    mPushIntent.putExtra("Item", "Sprinkler");
                    mPushIntent.putExtra("Status", "on");
                } else {
                    mPushIntent.putExtra("Item", "Sprinkler");
                    mPushIntent.putExtra("Status", "off");
                }
                mContext.sendBroadcast(mPushIntent);
            }
        }
    };
}
