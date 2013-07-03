package tw.iccl.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.iccl.ipps.MainActivity;
import tw.iccl.ipps.R;

/**
 * Created by Macintosh on 13/6/30.
 */
public class Notify {
    /***	Debugging	***/
	private static final String TAG = "Notifiy";
	private static final boolean D = true;

    public static final int	defaultVal  = 0;
    public static final int	Lamp        = 1;
    public static final int Fan         = 2;
    public static final int Sprinkler   = 3;

    public static void NotifyMsg(Context mContext, int ErrorCount, String Kind) {
        if (Kind.equals("Lamp")) {
            NotifyUser(mContext, ErrorCount,  Lamp);
        } else if (Kind.equals("Fan")) {
            NotifyUser(mContext, ErrorCount,  Fan);
        } else if (Kind.equals("Sprinkler")) {
            NotifyUser(mContext, ErrorCount,  Sprinkler);
        } else {
            NotifyUser(mContext, ErrorCount,  defaultVal);
        }
    }

    private static void NotifyUser(Context mContext,int ErrorCount, int Kind) {
        int icon;
        String tickerText;
        switch(Kind) {
            case Lamp:
                icon = R.drawable.lamp_on;
                tickerText = mContext.getResources().getString(R.string.notification_lamp);
                break;
            case Fan:
                icon = R.drawable.fan_on;
                tickerText = mContext.getResources().getString(R.string.notification_fan);
                break;
            case Sprinkler:
                icon = R.drawable.sprinkler_on;
                tickerText = mContext.getResources().getString(R.string.notification_sprinkler);
                break;
            default:
                icon = R.drawable.ic_launcher;
                tickerText = mContext.getResources().getString(R.string.notification_default) + ErrorCount;
        }

        String Title = mContext.getResources().getString(R.string.app_name);
        String context = tickerText;

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(mContext, MainActivity.class);
//        notifyIntent.putExtra("Message", Message);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent appIntent 	= PendingIntent.getActivity(mContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification mNotification 	= new Notification();


        mNotification.icon          = icon;
        mNotification.tickerText 	= tickerText;
        mNotification.defaults		= Notification.DEFAULT_ALL;
        mNotification.flags 	   |= Notification.FLAG_AUTO_CANCEL;
        mNotification.setLatestEventInfo(mContext, Title, context, appIntent);
        if(D) Log.e(TAG, "mNotificationManager: " + mNotificationManager.hashCode());
        mNotificationManager.notify(Kind, mNotification);
    }

    public static void NotifyCancel(Context mContext, int Kind) {
        NotificationManager mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(Kind);
    }
}
