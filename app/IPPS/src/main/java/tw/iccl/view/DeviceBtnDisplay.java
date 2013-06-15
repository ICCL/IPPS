package tw.iccl.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import tw.iccl.ipps.R;

/**
 * Created by Macintosh on 13/6/15.
 */
public class DeviceBtnDisplay {

    private Context mContext;

    private int Device;
    public static final int Status = 1;
    public static final int Lamp = 2;
    public static final int Fan = 3;
    public static final int Sprinkler = 4;

    private LinearLayout Line = null;
    private ImageButton IBut = null;

    private boolean BtnStatus = false;

    public DeviceBtnDisplay(Context context, int DName) {
        mContext = context;
        Device = DName;

        init();
    }

    private View findViewById(int R) {
        return ((Activity)mContext).findViewById(R);
    }

    private void init() {
        switch(Device) {
            case Status:
                Line = (LinearLayout) findViewById(R.id.status_line);
                IBut = (ImageButton) findViewById(R.id.status_img);
                break;
            case Lamp:
                Line = (LinearLayout) findViewById(R.id.lamp_line);
                IBut = (ImageButton) findViewById(R.id.lamp_img);
                break;
            case Fan:
                Line = (LinearLayout) findViewById(R.id.fan_line);
                IBut = (ImageButton) findViewById(R.id.fan_img);
                break;
            case Sprinkler:
                Line = (LinearLayout) findViewById(R.id.sprinkler_line);
                IBut = (ImageButton) findViewById(R.id.sprinkler_img);
                break;
        }
    }

    public void Display(boolean status) {
        BtnStatus = status;
        if(status)
            Line.setBackgroundColor(mContext.getResources().getColor(R.color.DeviceOn));
        else
            Line.setBackgroundColor(mContext.getResources().getColor(R.color.DeviceOff));

        switch(Device) {
            case Status:
                if(status) {
                    IBut.setImageResource(R.drawable.auto);
                } else {
                    IBut.setImageResource(R.drawable.not_auto);
                }
                break;
            case Lamp:
                if(status) {
                    IBut.setImageResource(R.drawable.lamp_on);
                } else {
                    IBut.setImageResource(R.drawable.lamp_off);
                }
                break;
            case Fan:
                if(status) {
                    IBut.setImageResource(R.drawable.fan_on);
                } else {
                    IBut.setImageResource(R.drawable.fan_off);
                }
                break;
            case Sprinkler:
                if(status) {
                    IBut.setImageResource(R.drawable.sprinkler_on);
                } else {
                    IBut.setImageResource(R.drawable.sprinkler_off);
                }
                break;
        }
    }

    public boolean getBtnStatus() {
        return BtnStatus;
    }
}
