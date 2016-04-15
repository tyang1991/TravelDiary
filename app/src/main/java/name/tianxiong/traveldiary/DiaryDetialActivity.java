package name.tianxiong.traveldiary;

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
import android.widget.EditText;
import android.widget.Switch;

import java.util.UUID;

public class DiaryDetialActivity extends AppCompatActivity {
    private Window window;
    private static final String EXTRA_DIARY_ID = "extraCrimeId";
    private Diary diary;
    private Menu menu;
    private EditText diaryTitle;
    private Switch diaryState;
    private EditText diaryContent;
    private ActionBar actionBar;

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
    }

    private void updateUI(){
        //set state
        diaryState.setChecked(diary.getDiaryState());
        //set title
        diaryTitle.setText(diary.getTitle());
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
            case R.id.menu_item_post:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
