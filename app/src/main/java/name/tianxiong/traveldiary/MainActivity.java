package name.tianxiong.traveldiary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Window window;
    private RecyclerView mDiaryRecyclerView;
    private DiaryAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //set recycler view
        mDiaryRecyclerView = (RecyclerView) findViewById(R.id.diary_list_recycler_view);
        mDiaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //update UI
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.menu_item_new_diary:
                return true;
            case R.id.menu_item_login:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(){
        DiaryManager diaryManager = DiaryManager.get();
        List<Diary> diaries = diaryManager.getDiaries();

        mAdapter = new DiaryAdapter(diaries);
        mDiaryRecyclerView.setAdapter(mAdapter);
    }

    private class DiaryHolder extends RecyclerView.ViewHolder {
        public TextView diaryTitle;
        public TextView diaryDate;

        public DiaryHolder (View itemView){
            super(itemView);

            diaryTitle = (TextView) itemView.findViewById(R.id.list_item_diary_title);
            diaryDate = (TextView) itemView.findViewById(R.id.list_item_diary_date);
        }
    }

    private class DiaryAdapter extends RecyclerView.Adapter<DiaryHolder> {
        private List<Diary> mDiaries;

        public DiaryAdapter(List<Diary> diaries){
            mDiaries = diaries;
        }

        public DiaryHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.list_item_main, parent, false);
            return new DiaryHolder(view);
        }

        public void onBindViewHolder(DiaryHolder holder, int position){
            Diary diary = mDiaries.get(position);
            holder.diaryTitle.setText(diary.getTitle());
            holder.diaryDate.setText(diary.getStartTime().toString());
        }

        public int getItemCount(){
            return mDiaries.size();
        }
    }
}
