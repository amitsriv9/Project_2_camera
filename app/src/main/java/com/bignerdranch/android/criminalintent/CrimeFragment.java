package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID = "criminalintent.CRIME_ID";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;

    Crime mCrime;
    EditText mTitleField;
    Button mDateButton;
    CheckBox mSolvedCheckBox;
    ImageButton mPhotoButton;
    ImageView mPhotoView;
    //
    ImageView mPhotoView2;
    ImageView mPhotoView3;
    ImageView mPhotoView4;
    Button mDeleteButton;
    //
    //========


    //=====

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        setHasOptionsMenu(true);
    }

    public void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);
 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCrime.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                    .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
        ////====================JUN button start
        mDeleteButton = (Button) v.findViewById(R.id.delete_photos);
        mDeleteButton.setOnClickListener(new View.OnClickListener(){
               public void onClick(View v){
                   ((CrimeWithExtraPictures)mCrime).removeAllPhotos();
                   clearPictures();
//                   showPhoto();
               }
           }
        );
        ////====================JUN button end




        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });

        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // launch the camera activity
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });
        
        // if camera is not available, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            mPhotoButton.setEnabled(false);
        }

        mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
        mPhotoView2 = (ImageView)v.findViewById(R.id.crime_imageView2);
        mPhotoView3 = (ImageView)v.findViewById(R.id.crime_imageView3);
        mPhotoView4 = (ImageView)v.findViewById(R.id.crime_imageView4);

        setImageListenner(mPhotoView,  1);
        setImageListenner(mPhotoView2, 2);
        setImageListenner(mPhotoView3, 3);
        setImageListenner(mPhotoView4, 4);

        return v; 
    }

    private void setImageListenner(ImageView mPhotoView, final int photoID) {
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Photo p = ((CrimeWithExtraPictures)mCrime).getPhoto(photoID);
                if (p == null)
                    return;

                FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
                String path = getActivity()
                    .getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.createInstance(path)
                    .show(fm, DIALOG_IMAGE);
            }
        });
    }

    private void showPhoto(ImageView mPhotoView, int photoID) {
        // (re)set the image button's image based on our photo
        Photo p = ((CrimeWithExtraPictures)mCrime).getPhoto(photoID);
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity()
                    .getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        else{
            System.out.println("null photo for id" + photoID);
        }
        mPhotoView.setImageDrawable(b);
    }

    private void showPhoto(){
        showPhoto(mPhotoView, 0);
        showPhoto(mPhotoView2, 1);
        showPhoto(mPhotoView3, 2);
        showPhoto(mPhotoView4, 3);
    }
    //commented by JUN
//    private void showPhoto() {
//        // (re)set the image button's image based on our photo
//        Photo p = mCrime.getPhoto();
//        BitmapDrawable b = null;
//        if (p != null) {
//            String path = getActivity()
//                .getFileStreamPath(p.getFilename()).getAbsolutePath();
//            b = PictureUtils.getScaledDrawable(getActivity(), path);
//        }
//        mPhotoView.setImageDrawable(b);
//
//        //todo show extra photos ??
//    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            // create a new Photo object and attach it to the crime
            String filename = data
                .getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Photo p = new Photo(filename);
                mCrime.setPhoto(p); //get photo from camera
                showPhoto();
            }
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } 
    }

    private void clearPictures(){
        mPhotoView.setImageDrawable(null);
        mPhotoView2.setImageDrawable(null);
        mPhotoView3.setImageDrawable(null);
        mPhotoView4.setImageDrawable(null);
    }
}
