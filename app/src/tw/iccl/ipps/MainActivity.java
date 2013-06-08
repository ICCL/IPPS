package tw.iccl.ipps;

import static tw.iccl.service.BackgroundService.haveBackgroundService;
import tw.iccl.service.BackgroundService;
import tw.iccl.view.Sensor;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class MainActivity extends SherlockActivity {

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
		super.onRestart();
		
		Sensor mSensor = new Sensor(this);
		
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unReceiver();
    }

    private void onReceiver() {
        if(haveBackgroundService){
            Intent intent = new Intent(this, BackgroundService.class);
            startService(intent);
        }
    }

    private void unReceiver() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater mMenuInflater = getSupportMenuInflater();
    	mMenuInflater.inflate(R.menu.main, menu);
        return true;
    }
}
