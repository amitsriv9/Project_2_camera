package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jun on 3/1/2015.
 */
public class CrimeWithExtraPictures extends Crime{
    private static final String JSON_PHOTO_COUNT = "photo_count";
    private static final String JSON_EXTRA_PHOTO = "extra_photo";
    private static final String JSON_EXTRA_NUMBER = "extra_number";

    private List<Photo> mExtraPhotos;
    private int photo_count = 0;
    private int extra_number = 3;
    public CrimeWithExtraPictures(){
        super();
        mExtraPhotos = new LinkedList<Photo>();
    }

    //todo implement 2 jason related functions
    public CrimeWithExtraPictures(JSONObject json) throws JSONException {
        super(json);
        photo_count = json.getInt(JSON_PHOTO_COUNT);
        extra_number = json.getInt(JSON_EXTRA_NUMBER);
        mExtraPhotos = new LinkedList<Photo>();
        for(int i = 1; i < photo_count; i++){
            mExtraPhotos.add( new Photo(json.getJSONObject(JSON_EXTRA_PHOTO + i)));
        }
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();
        json.put(JSON_PHOTO_COUNT, photo_count);
        for(int i = 1; i <= photo_count; i++){
            json.put(JSON_EXTRA_PHOTO + i, mExtraPhotos.get(i));
        }
        json.put(JSON_EXTRA_NUMBER, extra_number);
//        json.put()
//        JSONObject json = new JSONObject();
//        json.put(JSON_ID, mId.toString());
//        json.put(JSON_TITLE, mTitle);
//        json.put(JSON_SOLVED, mSolved);
//        json.put(JSON_DATE, mDate.getTime());
//        if (mPhoto != null)
//            json.put(JSON_PHOTO, mPhoto.toJSON());
        return json;
    }


    @Override
    public void setPhoto(Photo photo) {
        if(photo_count >= extra_number + 1){
            mExtraPhotos.remove(0);
        }else {
            photo_count++;
        }
        mExtraPhotos.add(getPhoto());
        super.setPhoto(photo);
    }

    public List<Photo> getExtraPhotos(){
        return mExtraPhotos;
    }

    public void removeAllPhotos(){
        //todo remove files from file system
        super.setPhoto(null); //todo check if this is right way
        mExtraPhotos.clear();
    }

    public int getPhoto_count(){
        return photo_count;
    }


    public Photo getPhoto(int id){
        if(id == 0){return super.getPhoto();}
        else if(id <= photo_count){
            return mExtraPhotos.get(id - 1);
        }else {
            return null;
        }
    }
//    public Crime() {
//        mId = UUID.randomUUID();
//        mDate = new Date();
//    }

//    public Crime(JSONObject json) throws JSONException {
//        mId = UUID.fromString(json.getString(JSON_ID));
//        mTitle = json.getString(JSON_TITLE);
//        mSolved = json.getBoolean(JSON_SOLVED);
//        mDate = new Date(json.getLong(JSON_DATE));
//        if (json.has(JSON_PHOTO))
//            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
//    }
}
