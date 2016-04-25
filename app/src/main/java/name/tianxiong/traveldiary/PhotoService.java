package name.tianxiong.traveldiary;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Tianxiong on 4/24/2016.
 */
public class PhotoService extends IntentService {
    private static final String TAG = "PollService";
    private static final int Alarm_INTERVAL = 1000 * 10; // 60 seconds
    private static int i = 0;

    public static Intent newIntent(Context context) {
        return new Intent(context, PhotoService.class);
    }

    public PhotoService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(getApplicationContext(), "Service Started"+i++, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Received an intent: " + i++ +" " + intent);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PhotoService.newIntent(context);
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
