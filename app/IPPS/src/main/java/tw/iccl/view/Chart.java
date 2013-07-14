package tw.iccl.view;

import static tw.iccl.config.config.Url;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import tw.iccl.ipps.R;

/**
 * Created by Macintosh on 13/6/9.
 */
public class Chart extends SherlockActivity {

    public final static boolean D = true;
    public final static String TAG = "Chart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent = getIntent();
        String Kind = mIntent.getStringExtra("Kind");

        WebView mWebView = (WebView) findViewById(R.id.chartView);
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        if (D) Log.e(TAG, "Url :" + Url + "/" + Kind + "/chart");
        mWebView.loadUrl(Url + "/" + Kind + "/chart");
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
