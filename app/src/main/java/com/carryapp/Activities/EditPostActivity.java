package com.carryapp.Activities;

import android.Manifest;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carryapp.AsyncTasks.EditPostAsyncTask;
import com.carryapp.AsyncTasks.GetCostAsyncTask;
import com.carryapp.Fragments.CarPickerFragment;
import com.carryapp.Fragments.MyTripsFragment;
import com.carryapp.Fragments.PostShippingFragment;
import com.carryapp.Fragments.TransportFragment;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.SessionData;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditPostActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener,GetCostAsyncTask.GetCostCallBack,CheckBox.OnCheckedChangeListener{
    private Button mBtnEdit;
    private TransportFragment.OnFragmentInteractionListener mListener;
    private EditText mEditTxt_From,mEditTxt_To,mEditTxt_Date,mEditTxt_Time,mEditTxtProductName,mEditTxtProductDetails;

    private static final String TAG = "PlacePickerSample";

    public final static int REQUEST_CAMERA = 3, SELECT_FILE = 4, CAMERA_PERMISSION = 11,GALLERY_PERMISSION = 22;
    public static final int REQUEST_CODE_AUTOCOMPLETE_FROM = 222,REQUEST_CODE_AUTOCOMPLETE_TO = 333;
    private String mTime,mCurrentPhotoPath,mImage = "",userChoosenTask,todayDate,todayTime;
    private ImageView mImgViewProduct;
    File productImage = null;
    private Date date = null,date1 = null;

    private Intent mIntent;
    private String mProductName,mProductDetail,mProductPhoto,mFromLocation,mToLocation,mDate="",mSize="",mWeight="",mCost;

    private String mParsedDistance,mDistance,deviceId,mFrom,mTo;
    private LatLng mFromLatLang,mToLatLang;
    private ImageView imgSmall,imgMedium,imgLarge,imgExtraLarge,imgDoubleExtraLarge,back;
    private TextView mTextViewCost;
    private FrameLayout parentLayout;
    private SessionData sessionData;
    private CheckBox cbTrack;
    private int mTrack;
    private Snackbar snackbar;
    private ProgressDialog progressDialog;
    double precision,startLatitude,startLongitude,endLatitude,endLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        setUpUI();

        setPostDetails();

        listeners();

    }
    public void setUpUI()
    {

        sessionData = new SessionData(EditPostActivity.this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        TextView title = (TextView) findViewById(R.id.textTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.editPostTitle);


        mEditTxt_From = (EditText) findViewById(R.id.editTextFrom);
        mEditTxt_To = (EditText) findViewById(R.id.editTextTo);
        mEditTxtProductName = (EditText) findViewById(R.id.editTextProduct);
        mEditTxtProductDetails = (EditText) findViewById(R.id.editTextProductDetails);

        mBtnEdit = (Button) findViewById(R.id.edit_post);

        mEditTxt_Date = (EditText) findViewById(R.id.editTextDate);
        mEditTxt_Time = (EditText) findViewById(R.id.editTextTime);
        mImgViewProduct = (ImageView) findViewById(R.id.imageViewProduct);

        parentLayout = (FrameLayout) findViewById(R.id.parentPanel);

        imgSmall = (ImageView) findViewById(R.id.imageViewSmall);
        imgMedium = (ImageView) findViewById(R.id.imageViewMedium);
        imgLarge = (ImageView) findViewById(R.id.imageViewLarge);
        imgExtraLarge = (ImageView) findViewById(R.id.imageViewExtraLarge);
        imgDoubleExtraLarge = (ImageView) findViewById(R.id.imageViewDoubleExtraLarg);

        mTextViewCost = (TextView) findViewById(R.id.textViewCost);
        cbTrack = (CheckBox) findViewById(R.id.checkBox);


        cbTrack.setOnCheckedChangeListener(EditPostActivity.this);

        Calendar compareDate = Calendar.getInstance();

        String Date = compareDate.get(Calendar.DAY_OF_MONTH) + "/" + (compareDate.get(Calendar.MONTH) + 1) + "/" + compareDate.get(Calendar.YEAR);
        todayDate = CommonUtils.formateDateFromstring("dd/MM/yyyy", "dd MMM, yyyy", Date);

        mEditTxt_Date.setText(todayDate);

        String hourString =  compareDate.get(Calendar.HOUR_OF_DAY)< 10 ? "0"+ compareDate.get(Calendar.HOUR_OF_DAY) : ""+ compareDate.get(Calendar.HOUR_OF_DAY);
        String minuteString =compareDate.get(Calendar.MINUTE) < 10 ? "0"+compareDate.get(Calendar.MINUTE): ""+compareDate.get(Calendar.MINUTE);

        todayTime = hourString + ":" +minuteString;

        mEditTxt_Time.setText(todayTime);

        mTime = todayTime;

        mDate = todayDate + " " + todayTime;

    }

    public void setPostDetails()
    {

        final  int sdk = android.os.Build.VERSION.SDK_INT;

        mIntent = getIntent();

            mEditTxtProductName.setText(mIntent.getStringExtra("pt_name"));
            mEditTxtProductDetails.setText(mIntent.getStringExtra("pt_details"));
            mEditTxt_From.setText(mIntent.getStringExtra("from"));
            mEditTxt_To.setText(mIntent.getStringExtra("to"));

            mFrom = mIntent.getStringExtra("from");
            mTo = mIntent.getStringExtra("to");

            String newDate = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy", mIntent.getStringExtra("pt_date"));

            String newTime = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "hh:mm", mIntent.getStringExtra("pt_date"));

            mEditTxt_Date.setText(newDate);
            mEditTxt_Time.setText(newTime);

            String url = getString(R.string.photo_url) + mIntent.getStringExtra("pt_photo");
            Log.e("url",url);

            Picasso.with(EditPostActivity.this)
                    .load(url)
                    .resize(400,400)
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .into(mImgViewProduct);

            mTextViewCost.setText(mIntent.getStringExtra("pt_charges"));

            startLatitude = mIntent.getDoubleExtra("st_lati",0);
            startLongitude = mIntent.getDoubleExtra("st_longi",0);
            endLatitude = mIntent.getDoubleExtra("ed_lati",0);
            endLongitude = mIntent.getDoubleExtra("ed_longi",0);

        if(mIntent.getStringExtra("pt_track").equals("1"))
        {
            cbTrack.setChecked(true);
        }

            if(mIntent.getStringExtra("pt_size").equals("0"))
            {
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgSmall.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                } else {
                    imgSmall.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                }

                mSize = "0";
                mWeight = "2";

              //  imgSmall.setAlpha(3);
            }
            else if(mIntent.getStringExtra("pt_size").equals("1"))
            {
                mSize = "1";
                mWeight = "8";

                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgMedium.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                } else {
                    imgMedium.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                }
               // imgSmall.setAlpha(3);
            }
            else if(mIntent.getStringExtra("pt_size").equals("2"))
            {

                mSize = "2";
                mWeight = "10";

                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                } else {
                    imgLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                }
                //imgSmall.setAlpha(3);
            }
            else if(mIntent.getStringExtra("pt_size").equals("3"))
            {

                mSize = "3";
                mWeight = "14";

                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                } else {
                    imgExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                }
                //imgSmall.setAlpha(3);
            }
            else if(mIntent.getStringExtra("pt_size").equals("4"))
            {

                mSize = "4";
                mWeight = "28";

                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgDoubleExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                } else {
                    imgDoubleExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                }
                //imgSmall.setAlpha(3);
            }


        GetDistanceAsyncTask getDistanceAsyncTask = new GetDistanceAsyncTask(startLatitude,startLongitude,endLatitude,endLongitude,parentLayout);
        getDistanceAsyncTask.execute();

    }

    public void listeners()
    {
        final  int sdk = android.os.Build.VERSION.SDK_INT;

        imgSmall.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                imgSmall.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                imgMedium.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                imgLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                imgExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                imgDoubleExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
            } else {
                imgSmall.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                imgMedium.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                imgLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                imgExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                imgDoubleExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
            }

            mSize = "0";
            mWeight = "2";

            GetCostAsyncTask getCostAsyncTask = new GetCostAsyncTask(EditPostActivity.this,EditPostActivity.this);
            getCostAsyncTask.execute(mSize,mWeight,mDistance.trim());

        }
    });

        imgMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgMedium.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                    imgSmall.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgDoubleExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                } else {
                    imgMedium.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                    imgSmall.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgDoubleExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                }

                mSize = "1";
                mWeight = "8";

                GetCostAsyncTask getCostAsyncTask = new GetCostAsyncTask(EditPostActivity.this,EditPostActivity.this);
                getCostAsyncTask.execute(mSize,mWeight,mDistance.trim());
            }
        });

        imgLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                    imgSmall.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgMedium.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgDoubleExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                } else {
                    imgLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                    imgSmall.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgMedium.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgDoubleExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                }

                mSize = "2";
                mWeight = "10";

                GetCostAsyncTask getCostAsyncTask = new GetCostAsyncTask(EditPostActivity.this,EditPostActivity.this);
                getCostAsyncTask.execute(mSize,mWeight,mDistance.trim());
            }
        });

        imgExtraLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                    imgSmall.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgMedium.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgDoubleExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                } else {
                    imgExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                    imgSmall.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgMedium.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgDoubleExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                }

                mSize = "3";
                mWeight = "14";

                GetCostAsyncTask getCostAsyncTask = new GetCostAsyncTask(EditPostActivity.this,EditPostActivity.this);
                getCostAsyncTask.execute(mSize,mWeight,mDistance.trim());
            }
        });
        imgDoubleExtraLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    imgDoubleExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                    imgSmall.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgExtraLarge.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgMedium.setBackgroundDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                } else {
                    imgDoubleExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border_selected));
                    imgSmall.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgExtraLarge.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                    imgMedium.setBackground(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.border));
                }

                mSize = "4";
                mWeight = "28";

                GetCostAsyncTask getCostAsyncTask = new GetCostAsyncTask(EditPostActivity.this,EditPostActivity.this);
                getCostAsyncTask.execute(mSize,mWeight,mDistance.trim());
            }
        });

        mImgViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

            }
        });


        mEditTxt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditPostActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setMinDate(now);

                dpd.setVersion(DatePickerDialog.Version.VERSION_2);

                dpd.setAccentColor(ContextCompat.getColor(EditPostActivity.this,R.color.colorAccent));

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        mEditTxt_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!todayDate.equals("")) {


                    try {

                        if (mDate != null) {
                            if (!mDate.equals("") && mDate != null) {
                                DateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
                                date = format.parse(mDate);
                            }
                        }


                        if (!todayDate.equals("") && todayDate != null) {
                            DateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
                            date1 = format.parse(todayDate);
                        }


                        Calendar now = Calendar.getInstance();

                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                EditPostActivity.this,
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                true
                        );

                        if (date.compareTo(date1) == 0) {
                            tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));

                            tpd.setVersion(TimePickerDialog.Version.VERSION_2);

                            tpd.setAccentColor(ContextCompat.getColor(EditPostActivity.this, R.color.colorAccent));

                            tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    Log.d("TimePicker", "Dialog was cancelled");
                                }
                            });


                            tpd.show(getFragmentManager(), "Timepickerdialog");
                        } else {

                            tpd.setVersion(TimePickerDialog.Version.VERSION_2);

                            tpd.setAccentColor(ContextCompat.getColor(EditPostActivity.this, R.color.colorAccent));

                            tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    Log.d("TimePicker", "Dialog was cancelled");
                                }
                            });


                            tpd.show(getFragmentManager(), "Timepickerdialog");
                        }


                    } catch (ParseException e) {
                        Log.e("exception", e.toString());

                    }
                }
                else{
                    snackbar = Snackbar.make(parentLayout,R.string.requestDate ,Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

            }

        });


        mEditTxt_From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openAutocompleteActivityFrom();


            }
        });


        mEditTxt_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAutocompleteActivityTo();
            }
        });


        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = mEditTxt_Date.getText().toString() + " " + mEditTxt_Time.getText().toString();
                mDate = CommonUtils.formateDateFromstring("dd MMM, yyyy HH:mm", "yyyy-MM-dd HH:mm:ss", date);
                Log.e("date",mDate);
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                precision =  Math.pow(10,6);

                if (mImage != null && !mImage.equals("")) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(mImage);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    if(myBitmap != null) {
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
                        byte[] ba = bao.toByteArray();
                        mImage = Base64.encodeToString(ba, Base64.DEFAULT);
                    }
                }

                startLatitude =  (int)(precision * startLatitude)/precision;
                startLongitude =  (int)(precision * startLongitude)/precision;
                endLatitude =  (int)(precision * endLatitude)/precision;
                endLongitude =  (int)(precision * endLongitude)/precision;

                if(validation()) {

                    EditPostAsyncTask editPostAsyncTask = new EditPostAsyncTask(EditPostActivity.this,parentLayout);
                    editPostAsyncTask.execute(mIntent.getStringExtra("pt_id"),mEditTxtProductName.getText().toString(),mEditTxtProductDetails.getText().toString(),
                            mEditTxt_From.getText().toString(),mEditTxt_To.getText().toString(),mDate,mImage,mSize,mWeight,mCost,String.valueOf(mTrack),String.valueOf(startLatitude),
                            String.valueOf(startLongitude),String.valueOf(endLatitude),String.valueOf(endLongitude),sessionData.getString("api_key",""));

                }

            }
        });

    }
    private void openAutocompleteActivityFrom() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(EditPostActivity.this);

            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_FROM);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(EditPostActivity.this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(EditPostActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
    private void openAutocompleteActivityTo() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(EditPostActivity.this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_TO);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(EditPostActivity.this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(EditPostActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validation()
    {
        if(mEditTxtProductName.getText().toString().equals("") || mEditTxtProductName.getText().toString().equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.emptyName, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxt_From.getText().toString().equals("") || mEditTxt_From.getText().toString().equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.emptyFrom, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxt_To.getText().toString().equals("") || mEditTxt_To.getText().toString().equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.emptyTo, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxt_Date.getText().toString().equals("") || mEditTxt_Date.getText().toString().equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.emptyDate, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxt_Time.getText().toString().equals("") || mEditTxt_Time.getText().toString().equals("null")){
            snackbar = Snackbar.make(parentLayout,R.string.emptyTime, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mDate.equals("") || mDate.equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.dateTimeSelect, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mTime.equals("") || mTime.equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.dateTimeSelect, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxtProductDetails.getText().toString().equals("") || mEditTxtProductDetails.getText().toString().equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.productDetailsWarning, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mSize.equals("") || mWeight.equals("") || mSize.equals("null") || mWeight.equals("null"))

        {
            snackbar = Snackbar.make(parentLayout,R.string.emptySize, Snackbar.LENGTH_LONG);
            snackbar.show();

            return false;
        }
        else {
            return true;
        }

        return false;
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditPostActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                //take photo by camera

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    //check permissions
                    if (ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(EditPostActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CAMERA);

                    } else {
                        cameraIntent();
                    }

                }
                //choose image from gallery

                else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    //check permissions

                    if (ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(EditPostActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                SELECT_FILE);

                    } else {

                        galleryIntent();
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    //call gallery intent

    private void galleryIntent() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE);
    }
    //call camera intent

    private void cameraIntent() {
        try {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    return;
                }
                // Continue only if the File was successfully created

                //use file provider above naugat version

                if (photoFile != null) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Uri photoURI = FileProvider.getUriForFile(EditPostActivity.this,getApplicationContext().getPackageName() + ".provider", createImageFile());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA);

                    } else {
                        Uri photoURI = Uri.fromFile(createImageFile());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                    }
                }
            }
        } catch (IOException e) {

        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void onCaptureImageResult() {

        compressImage(mCurrentPhotoPath);

    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if(data != null) {
            Uri uri = (Uri) data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                if (picturePath != null) {
                    File file = new File(picturePath);

                    if (file != null) {
                        compressImage(file.getPath());
                    }
                } else {
                    mImgViewProduct.setImageDrawable(ContextCompat.getDrawable(EditPostActivity.this, R.drawable.product));
                }

            }
        }
    }

    //scale image

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        String filename = "";
        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 400.0f;
        float maxWidth = 450.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            if (actualHeight > 0 && actualWidth > 0)
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        if (scaledBitmap != null) {
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);

                mImgViewProduct.setImageBitmap(scaledBitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }


            FileOutputStream out = null;
            filename = getFilename();
            try {
                out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            mImgViewProduct.setImageDrawable(ContextCompat.getDrawable(EditPostActivity.this,R.drawable.product));

        }

        return filename;
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        /*switch (requestCode) {

            case*/

        if(requestCode == CAMERA_PERMISSION) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraIntent();
                // permission was granted, yay! do the

            } else {

            }
        }

       /*         return;
            }
*/
          /*  case GALLERY_PERMISSION: {*/

        else if(requestCode == GALLERY_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                galleryIntent();
                // permission was granted, yay! do the
                // calendar task you need to do.

            } else {


            }

             /*   return;
            }*/
        }

    }
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");

        productImage = new File(uriSting.toString());

        mImage = productImage.getPath();

        return uriSting;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // BEGIN_INCLUDE(activity_result)
        if (requestCode == SELECT_FILE) {
            onSelectFromGalleryResult(data);
        }

        else if (requestCode == REQUEST_CAMERA) {
            onCaptureImageResult();
        }
        // Check that the result was from the autocomplete widget.
        else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(EditPostActivity.this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                mEditTxt_From.setText(place.getName());

                mFromLatLang = place.getLatLng();

                startLatitude = mFromLatLang.latitude;
                startLongitude = mFromLatLang.longitude;

                if(!mFrom.equals(mEditTxt_From.getText().toString()) || !mTo.equals(mEditTxt_To.getText().toString()))
                {

                    GetDistanceAsyncTask getDistanceAsyncTask = new GetDistanceAsyncTask(startLatitude,startLongitude,endLatitude,endLongitude,parentLayout);
                    getDistanceAsyncTask.execute();

                }
                // Display attributions if required.
              /*  CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mEditTxt_From.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mEditTxt_From.setText("");
                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(EditPostActivity.this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
        else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(EditPostActivity.this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                mEditTxt_To.setText(place.getName());

                mToLatLang = place.getLatLng();

                endLatitude = mToLatLang.latitude;
                endLongitude = mToLatLang.longitude;


                if(!mFrom.equals(mEditTxt_From.getText().toString()) || !mTo.equals(mEditTxt_To.getText().toString()))
                {

                    GetDistanceAsyncTask getDistanceAsyncTask = new GetDistanceAsyncTask(startLatitude,startLongitude,endLatitude,endLongitude,parentLayout);
                    getDistanceAsyncTask.execute();

                }

                // Display attributions if required.
              /*  CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mEditTxt_From.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mEditTxt_From.setText("");
                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(EditPostActivity.this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // END_INCLUDE(activity_result)
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int month= monthOfYear + 1;

        mDate = CommonUtils.formateDateFromstring("dd/MM/yyyy", "dd MMM, yyyy",dayOfMonth+"/"+month +"/"+year);
        mEditTxt_Date.setText(mDate);

        mDate = mDate + mTime;

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;

        mTime = hourString + ":"+minuteString;
        mEditTxt_Time.setText(mTime);

        mDate = mDate + mTime;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        // TODO Auto-generated method stub

        // Toast.makeText(CheckBoxCheckedDemo.this, &quot;Checked =&gt; &quot;+isChecked, Toast.LENGTH_SHORT).show();

        if(isChecked) {
            mTrack = 1;
        }
        else {
            mTrack = 0;
        }
    }

    @Override

    public void doPostExecute(Double cost)
        {
        mCost = String.valueOf(cost);
        mTextViewCost.setText(mCost +" " + "€");
    }

    public class GetDistanceAsyncTask  extends AsyncTask<String, Void, String[]> {

        private ProgressDialog loadingDialog;
        private Snackbar snackbar;
        private FrameLayout parentLayout;
        private double lat1, lon1, lat2, lon2;
        private String[] response = new String[1], parsedDistance= new String[1];
        private HttpURLConnection conn;

        public GetDistanceAsyncTask(double lat1, double lon1, double lat2, double lon2,FrameLayout parentLayout) {

            this.lat1 = lat1;
            this.lon1 = lon1;
            this.lat2 = lat2;
            this.lon2 = lon2;
            this.parentLayout = parentLayout;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isOnline()) {

                snackbar = Snackbar.make(parentLayout, R.string.check_network, Snackbar.LENGTH_LONG);
                snackbar.show();

            } else {

                loadingDialog = ProgressDialog.show(EditPostActivity.this, null, getString(R.string.wait));
                loadingDialog.setCancelable(false);
            }
        }

        @Override
        protected String[] doInBackground(String... params) {
            try {
                URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                InputStream in = new BufferedInputStream(conn.getInputStream());
                response[0] = IOUtils.toString(in, "UTF-8");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch(ProtocolException e)
            {

                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }
        //end of doInBackground

        @Override
        protected void onPostExecute(String[] response) {
            super.onPostExecute(response);

            try {

                if (response[0] != null) {

                    JSONObject jsonObject = new JSONObject(response[0]);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance[0] = distance.getString("text");

                    mParsedDistance = parsedDistance[0];

                    if (mParsedDistance != null) {
                        Log.e("distance", String.valueOf(mParsedDistance));

                        mParsedDistance = mParsedDistance.trim().replace("km", "");

                        mDistance = mParsedDistance.trim().replace(",", "");

                        GetCostAsyncTask getCostAsyncTask = new GetCostAsyncTask(EditPostActivity.this,EditPostActivity.this);
                        getCostAsyncTask.execute(mSize,mWeight,mDistance.trim());
                    }

                    Log.e("dist", "=" + parsedDistance[0]);

                    if (loadingDialog != null)
                        loadingDialog.dismiss();

                }
            }

            catch(JSONException e){
                if (loadingDialog != null)
                    loadingDialog.dismiss();
                snackbar = Snackbar.make(parentLayout, R.string.locationWarning, Snackbar.LENGTH_LONG);
                snackbar.show();
                e.printStackTrace();
            }


        }
        //end of onPostExecute

        //check network
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs

        finish();

        startActivity(new Intent(EditPostActivity.this,HomeActivity.class).putExtra("editPost",true));

    }
}
