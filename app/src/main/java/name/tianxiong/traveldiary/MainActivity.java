package name.tianxiong.traveldiary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Window window;
    private RecyclerView mDiaryRecyclerView;
    private DiaryAdapter mAdapter;
    private GoogleMap mMap;

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
        //config map
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onResume() {
        super.onResume();
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
                Diary newDiary = new Diary();
                newDiary.setDiaryState(true);
                DiaryManager.get().addDiary(newDiary);
                Intent intent = DiaryDetialActivity.newIntent(getApplicationContext(), newDiary.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_login:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //update UI every time list of diaries is changed
    private void updateUI(){
        DiaryManager diaryManager = DiaryManager.get();
        List<Diary> diaries = diaryManager.getDiaries();

        if (mAdapter == null){
            mAdapter = new DiaryAdapter(diaries);
            mDiaryRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setDiaries(diaries);
            mAdapter.notifyDataSetChanged();
        }
    }
    //Diary holder holds the view of each item of diary
    //this class communicates with main activity through DiaryAdapter
    private class DiaryHolder extends RecyclerView.ViewHolder{
        private RelativeLayout viewItem;
        private TextView diaryTitle;
        private TextView diaryDate;
        private Diary diary;

        public DiaryHolder (View itemView){
            super(itemView);
            viewItem = (RelativeLayout) itemView;
            diaryTitle = (TextView) itemView.findViewById(R.id.list_item_diary_title);
            diaryDate = (TextView) itemView.findViewById(R.id.list_item_diary_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = DiaryDetialActivity.newIntent(getApplicationContext(), diary.getId());
                    startActivity(intent);
                }
            });
        }
        //bind data shown with diary data
        public void bindDiary(Diary diary){
            this.diary = diary;
            diaryTitle.setText(diary.getTitle());
            diaryDate.setText(diary.getStartTime().toString());
            if (diary.getDiaryState())
                viewItem.setBackgroundResource(R.drawable.item_background_activated);
            Log.d("Diary", "Diary binded");
        }
    }
    //Diary adapter is used to fill diary items into recycler view
    //first getItemCount() is called, then onCreateViewHolder and onBindViewHolder are
    //called for each item
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
            holder.bindDiary(diary);
        }

        public int getItemCount(){
            return mDiaries.size();
        }

        public void setDiaries(List<Diary> diaries){
            mDiaries = diaries;
        }
    }
}
