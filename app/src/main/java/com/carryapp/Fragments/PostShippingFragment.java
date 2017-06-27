package com.carryapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.MainActivity;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.SessionData;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PostShippingFragment extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    private Button mBtnContinue;
    private TransportFragment.OnFragmentInteractionListener mListener;
    private EditText mEditTxt_From,mEditTxt_To,mEditTxt_Date,mEditTxt_Time,mEditTxtProductName,mEditTxtProductDetails;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mFromLatLang,mToLatLang;

    private static final String TAG = "PlacePickerSample";
    private LatLngBounds BOUNDS_MOUNTAIN_VIEW;
    /**
     * Request code passed to the PlacePicker intent to identify its result when it returns.
     */
    private static final int REQUEST_PLACE_PICKER_FROM = 1;
    private static final int REQUEST_PLACE_PICKER_TO = 2;
    public final static int REQUEST_CAMERA = 3, SELECT_FILE = 4, CAMERA_PERMISSION = 11,GALLERY_PERMISSION = 22;
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 10;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111,REQUEST_CODE_AUTOCOMPLETE_FROM = 222,REQUEST_CODE_AUTOCOMPLETE_TO = 333;
    private String mDate="",mTime,mCurrentPhotoPath,mImage = "",userChoosenTask,todayDate,todayTime;
    private ImageView mImgViewProduct;
    File productImage = null;
    private Boolean gpsEnabled,networkEnabled;
    private Snackbar snackbar;
    private FrameLayout parentLayout;
    private Date date = null,date1 = null;
    private ProgressDialog loadingDialog;
    private SessionData sessionData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post_shipping, container, false);


        setUpUI(view);

        listeners();


        return view;
    }

    public void listeners()
    {

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
                        PostShippingFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setMinDate(now);

                dpd.setVersion(DatePickerDialog.Version.VERSION_2);

                dpd.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
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
                                PostShippingFragment.this,
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                true
                        );

                            if (date.compareTo(date1) == 0) {
                                tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));

                                tpd.setVersion(TimePickerDialog.Version.VERSION_2);

                                tpd.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        Log.d("TimePicker", "Dialog was cancelled");
                                    }
                                });


                                tpd.show(getFragmentManager(), "Timepickerdialog");
                            } else {

                                tpd.setVersion(TimePickerDialog.Version.VERSION_2);

                                tpd.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

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


        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = mEditTxt_Date.getText().toString() + " " + mEditTxt_Time.getText().toString();
                mDate = CommonUtils.formateDateFromstring("dd MMM, yyyy HH:mm", "yyyy-MM-dd HH:mm:ss", date);
                Log.e("date",mDate);
                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                if(validation()) {

                    FragmentManager fragmentManager = getActivity().getFragmentManager();

                    CarPickerFragment fragment = new CarPickerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("pt_name", mEditTxtProductName.getText().toString());
                    bundle.putString("pt_detail", mEditTxtProductDetails.getText().toString());
                    bundle.putString("pt_photo", mImage);
                    bundle.putParcelable("pt_from_latlang", mFromLatLang);
                    bundle.putParcelable("pt_to_latlang", mToLatLang);
                    bundle.putString("pt_from_address", mEditTxt_From.getText().toString());
                    bundle.putString("pt_to_address", mEditTxt_To.getText().toString());
                    bundle.putString("pt_date", mDate);
                    fragment.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment, "CAR_PICKER_FRAGMENT").addToBackStack("E").commit();
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
                    .build(getActivity());

            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_FROM);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
    private void openAutocompleteActivityTo() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_TO);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        else if(mImage.equals("") || mImage.equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.imageAlert, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxtProductDetails.getText().toString().equals("") || mEditTxtProductDetails.getText().toString().equals("null"))
        {
            snackbar = Snackbar.make(parentLayout,R.string.productDetailsWarning, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else {
            return true;
        }

        return false;
    }

    public void setUpUI(View view)
    {


        sessionData = new SessionData(getActivity());

        //get lat and long from session data
        Double lati=Double.parseDouble(sessionData.getString("latitude","-1"));
        Double longi=Double.parseDouble(sessionData.getString("longitude","-1"));

        double radiusDegrees = 0.01;
        LatLng northEast = new LatLng(lati + radiusDegrees, longi + radiusDegrees);
        LatLng southWest = new LatLng(lati - radiusDegrees, longi - radiusDegrees);
        BOUNDS_MOUNTAIN_VIEW= LatLngBounds.builder()
                .include(northEast)
                .include(southWest)
                .build();

        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView)getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.GONE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.shipping);


        mEditTxt_From = (EditText) view.findViewById(R.id.editTextFrom);
        mEditTxt_To = (EditText) view.findViewById(R.id.editTextTo);
        mEditTxtProductName = (EditText) view.findViewById(R.id.editTextProduct);
        mEditTxtProductDetails = (EditText) view.findViewById(R.id.editTextProductDetails);

        mBtnContinue = (Button) view.findViewById(R.id.post);

        mEditTxt_Date = (EditText) view.findViewById(R.id.editTextDate);
        mEditTxt_Time = (EditText) view.findViewById(R.id.editTextTime);
        mImgViewProduct = (ImageView) view.findViewById(R.id.imageViewProduct);

        parentLayout = (FrameLayout) view.findViewById(R.id.parentPanel);

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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                //take photo by camera

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    //check permissions
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CAMERA_PERMISSION);

                    } else {
                        cameraIntent();
                    }

                }
                //choose image from gallery

                else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    //check permissions

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                       requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                GALLERY_PERMISSION);

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
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                        Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", createImageFile());
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

            Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
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
                    mImgViewProduct.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.product));
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

            mImgViewProduct.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.product));

        }

        return filename;
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
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
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                mEditTxt_From.setText(place.getName());

                mFromLatLang = place.getLatLng();

                // Display attributions if required.
              /*  CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mEditTxt_From.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mEditTxt_From.setText("");
                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
        else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                mEditTxt_To.setText(place.getName());

                mToLatLang = place.getLatLng();

                // Display attributions if required.
              /*  CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mEditTxt_From.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mEditTxt_From.setText("");
                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
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
}
