package name.tianxiong.traveldiary;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Tianxiong on 4/13/2016.
 */
public class Diary {
    private String title;
    private Date startTime;
    private Date endTime;
    private boolean diaryState;//activated or stopped
    private String diaryContent;
    private UUID id;
    //initiate diary
    public Diary(){
        title = "newTitle";
        startTime = new Date();
        diaryState = false;
        diaryContent = "";
        id = UUID.randomUUID();
    }

    //set end time and state as stopped
    public void endDiary(){
        endTime = new Date();
        diaryState = false;
    }

    //get date
    public Date getEndTime() {
        return endTime;
    }
    public Date getStartTime() {
        return startTime;
    }

    //diary setter and getter
    public String getDiaryContent() {
        return diaryContent.toString();
    }
    public void setDiaryContent(String diary) {
        this.diaryContent = diary;
    }

    //title setter and getter
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    //get id
    public UUID getId(){
        return id;
    }

    //state setter and getter
    public boolean getDiaryState() {
        return diaryState;
    }
    public void setDiaryState(boolean diaryState) {
        this.diaryState = diaryState;
    }
}
