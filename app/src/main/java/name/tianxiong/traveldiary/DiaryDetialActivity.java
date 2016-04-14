package name.tianxiong.traveldiary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Switch;
import android.widget.TextView;

import java.util.UUID;

public class DiaryDetialActivity extends AppCompatActivity {
    private Window window;
    private static final String EXTRA_DIARY_ID = "extraCrimeId";
    private Diary diary;
    private MenuItem diaryTitle;
    private Switch diaryState;
    private TextView diaryContent;

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
        //get UUID in Bundle
        UUID diaryId = (UUID) getIntent().getSerializableExtra(EXTRA_DIARY_ID);
        //get diary
        diary = DiaryManager.get().getDiary(diaryId);
        //get view
        diaryState = (Switch) findViewById(R.id.diary_state);
        diaryContent = (TextView) findViewById(R.id.diary_content);
        diaryTitle = (MenuItem) findViewById(R.id.menu_item_title);
        //inflate data
//        diaryState.setChecked(diary.getDiaryState());
//        diaryContent.setText(diary.getDiaryContent());
 //       diaryTitle.setTitle(diary.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
            case R.id.menu_item_title:
                return true;
            case R.id.menu_item_post:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
