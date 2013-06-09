package tw.iccl.pull;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class PullServer {
	
	/***	Debugging	***/
	private static final String TAG = "PullServer";
	private static final boolean D = true;
	
	private Context mContext;
	
	private List<NameValuePair> mParames;
	
	public BroadcastReceiver mRequestServerReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("RequestServerAll")) {
				
			}
		}
	};
	
	public PullServer(Context context) {
		this.mContext = context;
		
		EnableReceiver();
	}

	private void EnableReceiver() {
		// TODO Auto-generated method stub
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction("PullAllData");
		
		mContext.registerReceiver(mRequestServerReceiver, mIntentFilter);
	}
	
	public void DesableReceiver() {
		mContext.unregisterReceiver(mRequestServerReceiver);
	}
}
