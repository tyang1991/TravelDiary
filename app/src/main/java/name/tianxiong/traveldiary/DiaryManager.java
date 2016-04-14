package name.tianxiong.traveldiary;

/**
 * Created by Tianxiong on 4/13/2016.
 */
public class DiaryManager {
    private static DiaryManager sDiaryManager;

    public static DiaryManager get(){
        if (sDiaryManager == null){
            sDiaryManager = new DiaryManager();
        }
        return sDiaryManager;
    }

    private DiaryManager(){

    }
}
