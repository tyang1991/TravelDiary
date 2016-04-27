package name.tianxiong.traveldiary;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Tianxiong on 4/26/2016.
 */
public class AlarmService extends IntentService {
    private static final String TAG = "AlarmService";
    private static final int Alarm_INTERVAL = 1000 * 5; // 60 seconds
    private static int i = 0;

    public static Intent newIntent(Context context) {
        return new Intent(context, AlarmService.class);
    }

    public AlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + i++ + " " + intent);
        wakeUpDetailActivity();
    }

    private void wakeUpDetailActivity(){
        Log.d(TAG, "Broadcasting message");
        Intent intent = new Intent("DiaryDetailActivity");
        // You can also include some extra data.
        intent.putExtra("Command", "Record Location");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = AlarmService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), Alarm_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
