package com.carryapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.AsyncTasks.RegisterAsyncTask;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private TextView mLoginText;
    private Button mButton_register;
    private EditText mEdtFullName,mEdtEmail,mEdtNumber,mEdtPass,mEdtConfirmPass;
    private Toolbar toolbar;
    private String mName,mEmail,mNumber,mPass,mConfirmPass,mLatitude,mLongitude,mImage = "",userChoosenTask;
    private LinearLayout parentLayout;
    private ImageView mImgViewProfile;
    private File profileImage = null;
    String mCurrentPhotoPath;
    public final static int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Snackbar snackbar;
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 10;
    public static String TAG = "Location";
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private LatLng mLatLang;
    LocationManager mLocationManager = null;

    boolean gps_enabled = false;
    boolean network_enabled = false;

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.NETWORK_PROVIDER),
            new LocationListener(LocationManager.GPS_PROVIDER)

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpUI();

        listeners();

        buildGoogleApiClient();
        mGoogleApiClient.connect();

    }


    public void setUpUI()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.register);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(RegisterActivity.this,R.mipmap.ic_arrow_back_black_24dp));
        setSupportActionBar(toolbar);

        mLoginText = (TextView) findViewById(R.id.textViewLogin);
        mButton_register = (Button) findViewById(R.id.btn_SignUp);
        mEdtFullName = (EditText) findViewById(R.id.editTextName);
        mEdtEmail = (EditText) findViewById(R.id.editTextEmailId);
        mEdtNumber = (EditText) findViewById(R.id.editTextNumber);
        mEdtPass = (EditText) findViewById(R.id.editTextPass);
        mEdtConfirmPass = (EditText) findViewById(R.id.editTextConfirmPass);
        parentLayout = (LinearLayout) findViewById(R.id.parentPanel);
        mImgViewProfile = (ImageView) findViewById(R.id.profile_image);


        Log.e("Location",String.valueOf(mLatLang));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

        } else {

            initializeLocationManager();
            requestLocation();
        }

    }


    public void listeners()
    {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });


        mButton_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d(TAG, "Refreshed token: " + refreshedToken);

                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                // get texts from edit text

                mName = mEdtFullName.getText().toString();
                mEmail = mEdtEmail.getText().toString();
                mNumber = mEdtNumber.getText().toString();
                mPass = mEdtPass.getText().toString().replaceAll(" ", "").trim();
                mConfirmPass = mEdtConfirmPass.getText().toString();


                //validate Registration params
                if (!checkValidation()) {

               /*     CommonUtils.showAlert(RegisterCustomerActivity.this, getResources().getString(R.string.detailsAlert), "Invalid");*/

                    // showAlert(getString(R.string.detailsAlert));
                    snackbar = Snackbar.make(parentLayout,R.string.detailsAlert, Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {
                    if (mEdtNumber.length() < 9 || mEdtNumber.length() > 14) {

                        // showAlert(getString(R.string.phoneAlert));

                     /*   CommonUtils.showAlert(RegisterCustomerActivity.this, getResources().getString(R.string.phoneAlert), "Invalid");*/
                        snackbar = Snackbar.make(parentLayout,R.string.phoneAlert, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else if (!CommonUtils.checkEmail(mEmail)) {
                        snackbar = Snackbar.make(parentLayout,R.string.emailAlert, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        //   showAlert(getString(R.string.emailAlert));
                    /*    CommonUtils.showAlert(RegisterCustomerActivity.this, getResources().getString(R.string.emailAlert), "Invalid");*/

                    } else if (mEdtPass.length() < 8) {
                        snackbar = Snackbar.make(parentLayout,R.string.passAlert, Snackbar.LENGTH_LONG);
                        snackbar.show();
                     //   CommonUtils.showAlert(RegisterCustomerActivity.this, getResources().getString(R.string.passAlert), "Invalid");
                        //   showAlert(getString(R.string.passAlert));

                    } else if (mEdtConfirmPass.equals("")) {
                        snackbar = Snackbar.make(parentLayout,R.string.confirmAlert, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        // showAlert(getString(R.string.confirmAlert));

                      //  CommonUtils.showAlert(RegisterCustomerActivity.this, getResources().getString(R.string.confirmAlert), "Invalid");

                    } else if (!mPass.equals(mConfirmPass)) {
                        snackbar = Snackbar.make(parentLayout,R.string.passwordAlert, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        // showAlert(getString(R.string.passwordAlert));

                       // CommonUtils.showAlert(RegisterCustomerActivity.this, getResources().getString(R.string.passwordAlert), "Invalid");

                    } else {
                        //Registration AsyncTask

                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }


                        if(!gps_enabled && !network_enabled)
                        {
                            RegisterAsyncTask task = new RegisterAsyncTask(RegisterActivity.this,parentLayout);
                            task.execute(mName, mEmail, mNumber, mPass,"0.0", "0.0", refreshedToken, mImage);
                        }
                        else {
                            if (mLatLang != null) {
                                RegisterAsyncTask task = new RegisterAsyncTask(RegisterActivity.this,parentLayout);
                                task.execute(mName, mEmail, mNumber, mPass, String.valueOf(mLatLang.latitude), String.valueOf(mLatLang.longitude), refreshedToken, mImage);
                            }
                            else {
                                RegisterAsyncTask task = new RegisterAsyncTask(RegisterActivity.this,parentLayout);
                                task.execute(mName, mEmail, mNumber, mPass,"0.0", "0.0", refreshedToken, mImage);
                            }
                        }
                        if(mLocationManager != null) {

                            mLocationManager.removeUpdates(mLocationListeners[0]);
                            mLocationManager.removeUpdates(mLocationListeners[1]);
                        }
                    }
                }
            }
        });


        mImgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                //take photo by camera

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    //check permissions

                    if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CAMERA);

                    } else {
                        cameraIntent();
                    }

                }
                //choose image from gallery

                else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    //check permissions

                    if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                        Uri photoURI = FileProvider.getUriForFile(RegisterActivity.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
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

    //call camera intent


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult();

        }
    }

    private void onCaptureImageResult() {

        compressImage(mCurrentPhotoPath);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

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

                    mImgViewProfile.setImageDrawable(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.profile_add));

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

        float maxHeight = 450.0f;
        float maxWidth = 400.0f;
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

                    mImgViewProfile.setImageBitmap(scaledBitmap);


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

                mImgViewProfile.setImageDrawable(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.profile_add));

        }

        return filename;
    }


    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");


            profileImage = new File(uriSting.toString());

            mImage = profileImage.getAbsolutePath();


        return uriSting;

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
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    cameraIntent();
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                }
                return;
            }

            case SELECT_FILE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    galleryIntent();
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    initializeLocationManager();
                    requestLocation();
                } else {
                    //   ((HomeActivity) getActivity()).showAlert(getResources().getString(R.string.locationAlert));
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!CommonUtils.hasText(mEdtFullName)) ret = false;
        if (!CommonUtils.hasText(mEdtEmail)) ret = false;
        if (!CommonUtils.hasText(mEdtNumber)) ret = false;
        if (!CommonUtils.hasText(mEdtPass)) ret = false;
        if (!CommonUtils.hasText(mEdtConfirmPass)) ret = false;

        return ret;
    }


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(RegisterActivity.this)
                .addConnectionCallbacks(RegisterActivity.this)
                .addOnConnectionFailedListener(RegisterActivity.this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Toast.makeText(getActivity(),"onConnected",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {
        //  Toast.makeText(getActivity(),"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //    Toast.makeText(getActivity(),"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    private void initializeLocationManager() {
        // Log.e(Application.TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user

        }

    }

    public class LocationListener implements android.location.LocationListener {

        public LocationListener() {
        }

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);

            //get current location

            if(mLastLocation != null && !mLastLocation.equals("")) {
                mLastLocation.set(location);
                mLatLang = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
            else {

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    //request for location, first by network, then by gps

    public void requestLocation() {

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    //get location when permissions are allowed


    @Override
    public void onBackPressed()
    {
       finish();

    }
}
