package name.tianxiong.traveldiary;

import java.util.Date;

/**
 * Created by Tianxiong on 4/13/2016.
 */
public class Diary {
    private String title;
    private Date startTime;
    private Date endTime;
    private boolean diaryState;//activated or stopped
    private String diary;
    //initiate diary
    public Diary(){
        startTime = new Date();
        diaryState = true;
    }
    //set end time and state as stopped
    public void endDiary(){
        endTime = new Date();
        diaryState = false;
    }
    //diary setter and getter
    public String getDiary() {
        return diary.toString();
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }
    //title setter and getter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
