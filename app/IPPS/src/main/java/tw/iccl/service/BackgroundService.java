package tw.iccl.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import tw.iccl.Notification.Notification;
import tw.iccl.gcm.GCM;
import tw.iccl.pullpush.PullService;
import tw.iccl.pullpush.PushService;

import static tw.iccl.pullpush.PullService.GET_ALL;

public class BackgroundService extends Service {
	
	/***	Debugging	***/
	private static final String TAG = "BackgroundService";
	private static final boolean D 	= true;
	
	private GCM mGCM 	               = null;
    private PullService mPullService   = null;
    private PushService mPushService   = null;
    private Notification mNotification = null;

	/***	Background config State		***/
	public static boolean haveBackgroundService = true;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		if(D) Log.e(TAG, "onStart");
		
		haveBackgroundService = false;
		    	
		EnableReceiver();

        mPullService.Pull(GET_ALL, 1);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(D) Log.e(TAG, "onDestroy");
		DisableReceiver();
	}
	
	private void EnableReceiver() {
		/***	Receiver	***/
        if(mGCM == null) mGCM = new GCM(this);
        if(mPullService == null) mPullService = new PullService(this);
        if(mPushService == null) mPushService = new PushService(this);
        if(mNotification == null) mNotification = new Notification(this);
	}
	
	private void DisableReceiver() {
		if( mGCM != null) mGCM.Unregister();
        if( mPullService != null) mPullService.DisableReceiver();
        if( mPushService != null) mPushService.DisableReceiver();
        if( mNotification != null) mNotification.DisableReceiver();
	}
}
