package name.tianxiong.traveldiary;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Tianxiong on 4/24/2016.
 */
public class PhotoService extends IntentService {
    private static final String TAG = "PollService";

    public static Intent newIntent(Context context) {
        return new Intent(context, PhotoService.class);
    }

    public PhotoService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Received an intent: " + intent);
    }
}
