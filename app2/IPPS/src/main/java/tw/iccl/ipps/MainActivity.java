package tw.iccl.ipps;

import static tw.iccl.service.BackgroundService.haveBackgroundService;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import tw.iccl.service.BackgroundService;

public class MainActivity extends Activity {

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
