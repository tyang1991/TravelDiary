package name.tianxiong.traveldiary;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tianxiong on 4/13/2016.
 */
public class Diary {
    public static String DIARY_STATE_ACTIVETED = "Activated";
    public static String DIARY_STATE_STOPPED = "Stopped";
    private String title;
    private Date startTime;
    private Date endTime;
    private boolean diaryState;//activated or stopped
    private String diaryContent;
    private UUID id;
    private List<LocationStamp> locationList;
    private List<LatLng> latLngList;
    private List<LatLng> markerList;
    private List<LocationStamp> preStayedLocations;
    //initiate diary
    public Diary(){
        title = "newTitle";
        startTime = new Date();
        diaryState = false;
        diaryContent = "";
        id = UUID.randomUUID();
        locationList = new ArrayList<LocationStamp>();
        latLngList = new ArrayList<LatLng>();
        markerList = new ArrayList<LatLng>();
        preStayedLocations = new ArrayList<LocationStamp>();
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
    public Diary setDiaryContent(String diary) {
        this.diaryContent = diary;
        return this;
    }

    //title setter and getter
    public String getTitle() {
        return title;
    }
    public Diary setTitle(String title) {
        this.title = title;
        return this;
    }

    //get id
    public UUID getId(){
        return id;
    }

    //state setter and getter
    public boolean getDiaryState() {
        return diaryState;
    }
    public Diary setDiaryState(boolean diaryState) {
        this.diaryState = diaryState;
        return this;
    }

    private class LocationStamp{
        private LatLng latlng;
        private Date date;
        public LocationStamp(Location location, Date date){
            latlng = new LatLng(location.getLatitude(), location.getLongitude());
            this.date = date;
        }
        public LatLng getLatLng() {
            return latlng;
        }
        public Date getDate() {
            return date;
        }
    }

    public Diary addLocation(Location location){
        addLocation(location, new Date());
        return this;
    }

    public Diary addLocation(Location location, Date date){
        locationList.add(new LocationStamp(location, date));
        latLngList.add(new LatLng(location.getLatitude(), location.getLongitude()));
        checkMarker();
        return this;
    }

    public List<LocationStamp> getLocationStamps(){
        return locationList;
    }

    public List<LatLng> getLatLngs(){
        return latLngList;
    }

    private void checkMarker(){
        //if not empty, must larger or equal than two
        //if empty, compare to last location
        if (!preStayedLocations.isEmpty()){
            double lat = 0;
            double lng = 0;
            for(LocationStamp l: preStayedLocations){
                lat += l.getLatLng().latitude;
                lng += l.getLatLng().longitude;
            }
            double avelat = lat/preStayedLocations.size();
            double avelng = lng/preStayedLocations.size();
            LocationStamp lastLocation = locationList.get(latLngList.size()-1);
            double lastLat = lastLocation.getLatLng().latitude;
            double lastLng = lastLocation.getLatLng().longitude;
            //if within scope, add to prestayedlatlngs
            //else, base on stayed time. if time long enough, add marker. if not, clear.
            if(Math.sqrt((avelat-lastLat)*(avelat-lastLng)+(avelng-lastLat)*(avelng-lastLng))
                    <= 0.00050){
                preStayedLocations.add(lastLocation);
            }else{
                if(preStayedLocations.get(preStayedLocations.size()-1).getDate().getTime()-
                        preStayedLocations.get(0).getDate().getTime() < 30*1000){
                    preStayedLocations.clear();
                }else{
                    lat += lastLat;
                    lng += lastLng;
                    lat /= preStayedLocations.size()+1;
                    lng /= preStayedLocations.size()+1;
                    markerList.add(new LatLng(lat, lng));
                    preStayedLocations.clear();
                    Log.d("LocationCheck", "LatLng: " + lat + lng);

                }
            }
        }else{
            if(locationList.size() >= 2){
                double lastLat = locationList.get(locationList.size()-1).getLatLng().latitude;
                double lastLng = locationList.get(locationList.size()-1).getLatLng().longitude;
                double preLat = locationList.get(locationList.size()-2).getLatLng().latitude;
                double preLng = locationList.get(locationList.size()-2).getLatLng().longitude;
                if (Math.sqrt((lastLat-preLat)*(lastLat-preLat)+(lastLng-preLng)*(lastLng-preLng))
                        <0.000100){
                    preStayedLocations.add(locationList.get(locationList.size()-2));
                    preStayedLocations.add(locationList.get(locationList.size()-1));
                }
            }
        }
    }

    public List<LatLng> getMarkerList() {
        return markerList;
    }
}
