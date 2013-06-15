package tw.iccl.ipps;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.actionbarsherlock.app.SherlockActivity;

import tw.iccl.gcm.GCM;
import tw.iccl.view.Device;
import tw.iccl.view.Sensor;

public class MainActivity extends SherlockActivity {
    public final static boolean D = true;
    public final static String Tag = "MainActivity";

    private GCM mGCM;
    private Sensor mSensor;
    private Device mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensor = new Sensor(this);
        mDevice = new Device(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unReceiver();

    }

    private void onReceiver() {
        if(mGCM == null) mGCM = new GCM(this);
    }

    private void unReceiver() {
        mGCM.Unregister();
        mSensor.DesableReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
