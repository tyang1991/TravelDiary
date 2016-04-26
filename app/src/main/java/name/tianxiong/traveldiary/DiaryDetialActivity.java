package name.tianxiong.traveldiary;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;

import java.util.UUID;

public class DiaryDetialActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private Window window;
    private static final String EXTRA_DIARY_ID = "extraCrimeId";
    private Diary diary;
    private Menu menu;
    private EditText diaryTitle;
    private Switch diaryState;
    private EditText diaryContent;
    private ActionBar actionBar;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private GoogleApiClient mGoogleApiClient ;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, DiaryDetialActivity.class);
        intent.putExtra(EXTRA_DIARY_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detial);
        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //get diary
        UUID diaryId = (UUID) getIntent().getSerializableExtra(EXTRA_DIARY_ID);
        diary = DiaryManager.get().getDiary(diaryId);
        //get view
        diaryState = (Switch) findViewById(R.id.diary_state);
        diaryContent = (EditText) findViewById(R.id.diary_content);
        diaryTitle = (EditText) findViewById(R.id.diary_title);
        //init data
        updateUI();
        //set view listener
        diaryState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                diary.setDiaryState(isChecked);
                if (isChecked) {
                    diaryState.setText(Diary.DIARY_STATE_ACTIVETED);
                } else {
                    diaryState.setText(Diary.DIARY_STATE_STOPPED);
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //set up service and listener
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("DiaryDetailActivity"));
        AlarmService.setServiceAlarm(this, true);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("Command");
            Log.d("AlarmService", "Got message: " + message);
            updateLocation();
        }
    };

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        updateLocation();
    }

    private void updateLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            //get location
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation == null){
                Log.i("LocationCheck", "null pointer");
            }else{
                Log.i("LocationCheck", mLastLocation.toString());
            }
        }else{
            Log.i("LocationCheck", "Check Failed");
        }
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);

    }

    private void updateUI(){
        //set state
        diaryState.setChecked(diary.getDiaryState());
        diaryState.setText(diary.getDiaryState()?
                Diary.DIARY_STATE_ACTIVETED:Diary.DIARY_STATE_STOPPED);
        //set title
        diaryTitle.setText(diary.getTitle()=="newTitle"?"":diary.getTitle());
        //set content
        diaryContent.setText(diary.getDiaryContent());
        diaryContent.setCursorVisible(true);
        diaryContent.setFocusableInTouchMode(true);
        diaryContent.setInputType(InputType.TYPE_CLASS_TEXT);
        diaryContent.requestFocus();
        diaryContent.setHorizontallyScrolling(false);
        diaryContent.setLines(10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(diary.getTitle());
        //set menu reference
        this.menu = menu;
        //set status bar color
        window = getWindow();
        window.setStatusBarColor(Color.BLACK);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_item_save:
                updateDiary();
                Toast.makeText(this,"Diary Saved", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_post:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDiary(){
        diary.setTitle(diaryTitle.getText().toString());
        diary.setDiaryContent(diaryContent.getText().toString());
    }

    public class CameraEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, "New Photo Clicked", Toast.LENGTH_LONG).show();

        }
    }
}
