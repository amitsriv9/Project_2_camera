package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jun on 3/1/2015.
 */
public class CrimeWithExtraPictures extends Crime{
    private List<Photo> mExtraPhotos;
    private int photo_count = 0;
    private int extra_number = 3;
    public CrimeWithExtraPictures(){
        super();
        mExtraPhotos = new LinkedList<Photo>();
    }

    public CrimeWithExtraPictures(JSONObject json)throws JSONException {
        super(json);
        mExtraPhotos = new LinkedList<Photo>();
    }

    @Override
    public void setPhoto(Photo photo) {
        if(photo_count >= extra_number + 1){
            mExtraPhotos.remove(0);
        }
        mExtraPhotos.add(getPhoto());
        super.setPhoto(photo);
    }

    public List<Photo> getExtraPhotos(){
        return mExtraPhotos;
    }

    public void removeAllPhotos(){

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
