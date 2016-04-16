package name.tianxiong.traveldiary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tianxiong on 4/13/2016.
 */
public class DiaryManager {
    private static DiaryManager sDiaryManager;
    private List<Diary> mDiaries;

    public static DiaryManager get(){
        if (sDiaryManager == null){
            sDiaryManager = new DiaryManager();
        }
        return sDiaryManager;
    }
    //initiate Diary Manager and fetch data
    private DiaryManager(){
        mDiaries = new ArrayList<>();
    }
    //add diary
    public void addDiary(Diary diary){
        mDiaries.add(diary);
    }
    //return all diaries
    public List<Diary> getDiaries(){
        return mDiaries;
    }
    //return certain diary
    public Diary getDiary(UUID id){
        for (Diary diary : mDiaries){
            if (diary.getId().equals(id)){
                return diary;
            }
        }
        return null;
    }
}
