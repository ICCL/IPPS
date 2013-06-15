package tw.iccl.view;

import static tw.iccl.view.DeviceBtnDisplay.Status;
import static tw.iccl.view.DeviceBtnDisplay.Lamp;
import static tw.iccl.view.DeviceBtnDisplay.Fan;
import static tw.iccl.view.DeviceBtnDisplay.Sprinkler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

    public Device(Context context) {
        this.mContext = context;

        init_view();
    }

    private View findViewById(int R) {
        return ((Activity)mContext).findViewById(R);
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
        }
    };

    private View.OnClickListener LampOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            if(D) Log.e(TAG, "LampBtn Click");
            if(!StatusBtn.getBtnStatus()) {
                if(D) Log.e(TAG, "LampBtn Click"+ !StatusBtn.getBtnStatus());
                LampBtn.Display(!LampBtn.getBtnStatus());
            }
        }
    };

    private View.OnClickListener FanOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            if(D) Log.e(TAG, "FanBtn Click");
            if(!StatusBtn.getBtnStatus()) {
                if(D) Log.e(TAG, "FanBtn Click"+ !StatusBtn.getBtnStatus());
                FanBtn.Display(!FanBtn.getBtnStatus());
            }
        }
    };

    private View.OnClickListener SprniklerOnClick = new View.OnClickListener () {
        public void onClick(View v) {
            if(D) Log.e(TAG, "SprinklerBtn Click");
            if(!StatusBtn.getBtnStatus()) {
                if(D) Log.e(TAG, "SprinklerBtn Click"+ !StatusBtn.getBtnStatus());
                SprinklerBtn.Display(!SprinklerBtn.getBtnStatus());
            }
        }
    };
}
