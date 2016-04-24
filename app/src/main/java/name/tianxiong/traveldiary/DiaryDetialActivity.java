package name.tianxiong.traveldiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.UUID;

public class DiaryDetialActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Window window;
    private static final String EXTRA_DIARY_ID = "extraCrimeId";
    private Diary diary;
    private Menu menu;
    private EditText diaryTitle;
    private Switch diaryState;
    private EditText diaryContent;
    private ActionBar actionBar;
    private GoogleMap mMap;
    //private CameraEventReceiver camera;

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
        //camera
        //camera = new CameraEventReceiver();
        Intent photoService = PhotoService.newIntent(this);
        startService(photoService);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
