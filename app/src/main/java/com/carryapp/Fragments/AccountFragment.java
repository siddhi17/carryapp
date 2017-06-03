package com.carryapp.Fragments;

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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.carryapp.Activities.HomeActivity;
import com.carryapp.AsyncTasks.UpdateProfileAsyncTask;
import com.carryapp.AsyncTasks.UploadFileAsyncTask;
import com.carryapp.R;
import com.carryapp.helper.SessionData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AccountFragment extends Fragment implements UploadFileAsyncTask.UploadFileCallBack{
    // TODO: Rename parameter arguments, choose names that match

    private Button mBtn_Vehicle,mBtn_Edit;

    private ImageView mImgViewProfile,mImgViewCar,mImgViewDNI;
    File profileImage = null,carImage = null,dniImage;
    private String userChoosenTask;
    private boolean result;
    String mCurrentPhotoPath,mCarModel,mCarType;
    public final static int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private OnFragmentInteractionListener mListener;
    private String mImage = "", mSavedImage="",mCarImage="",mDNIImage="";

    private TextView mTxtEmail,mTxtCarModel,mTxtCarType;
    private EditText mEdtUserName,mEdtAge,mEdtNumber;
    private boolean mProfile,mDNI,mCar;

    private FrameLayout parentLayout;
    private SessionData sessionData;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account, container, false);


        setUpUI(view);

        listeners();


        return view;
    }


    public void setUpUI(View view)
    {
        sessionData = new SessionData(getActivity());

        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView)getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.GONE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.account);

        mBtn_Vehicle = (Button) view.findViewById(R.id.buttonVehicle);
        mBtn_Edit = (Button) view.findViewById(R.id.btnEdit);
        mImgViewProfile = (ImageView) view.findViewById(R.id.profile_image);
        mImgViewCar = (ImageView) view.findViewById(R.id.imageViewCar);
        mImgViewDNI = (ImageView) view.findViewById(R.id.imageViewDNI);

        mEdtAge = (EditText) view.findViewById(R.id.textViewAge);
        mTxtEmail = (TextView) view.findViewById(R.id.textViewEmail);
        mTxtCarModel = (TextView) view.findViewById(R.id.textViewModel);
        mTxtCarType = (TextView) view.findViewById(R.id.textViewType);
        mEdtUserName = (EditText) view.findViewById(R.id.textViewUsername);
        mEdtNumber = (EditText) view.findViewById(R.id.textViewMobile);

        parentLayout = (FrameLayout) view.findViewById(R.id.parentPanel);

        Picasso.with(getActivity()).setLoggingEnabled(true);
        String ur_photo_url = sessionData.getString("ur_photo", "");

        Picasso.with(getActivity())
                .load(ur_photo_url)
                .placeholder(R.drawable.profile_add)
                .error(R.drawable.profile_add)
                .into(mImgViewProfile);

        String dni_photo_url = getString(R.string.photo_url) + sessionData.getString("ur_dni_photo", "");

        Picasso.with(getActivity())
                .load(dni_photo_url)
                .placeholder(R.drawable.card)
                .error(R.drawable.card)
                .into(mImgViewDNI);

        String car_photo_url = getString(R.string.photo_url) + sessionData.getString("ur_car_photo", "");

        Picasso.with(getActivity())
                .load(car_photo_url)
                .placeholder(R.drawable.car_new)
                .error(R.drawable.car_new)
                .into(mImgViewCar);

        mEdtUserName.setText(sessionData.getString("ur_name","Name"));

        if(!sessionData.getString("ur_mob_no","").equals("null")) {
            mEdtNumber.setText(sessionData.getString("ur_mob_no", "Number"));
        }
        else {
            mEdtNumber.setText(getString(R.string.number));
        }
        if(!sessionData.getString("ur_car_type","").equals("null")) {
            mTxtCarType.setText(sessionData.getString("ur_car_type","Car type"));
        }
        else {
            mTxtCarType.setText(getString(R.string.cartype));
        }

        if(!sessionData.getString("ur_car_model","").equals("null")) {
            mTxtCarModel.setText(sessionData.getString("ur_car_model","Model number"));
        }
        else {
            mTxtCarModel.setText(getString(R.string.modelno));
        }

        mTxtEmail.setText(sessionData.getString("ur_email","Email-Id"));

        if(!sessionData.getString("ur_birth_date","").equals("null")) {

        Calendar currentDate = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        Date birthdate = null;

        try {
            birthdate = myFormat.parse(sessionData.getString("ur_birth_date",""));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(birthdate != null) {
            Long time = currentDate.getTime().getTime() / 1000 - birthdate.getTime() / 1000;

            int years = Math.round(time) / 31536000;
            int months = Math.round(time - years * 31536000) / 2628000;

            mEdtAge.setText(String.valueOf(years +  " " +getString(R.string.years)));
        }
        else {
            mEdtAge.setText(sessionData.getString("ur_birth_date","")+ " " + getString(R.string.years));
        }
        }
        else {
            mEdtAge.setText(R.string.age);
        }

    }

    public void listeners()
    {


        mBtn_Vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title(R.string.vehicleTitle)
                        .customView(R.layout.vehicle_dialog,true)
                        .show();

                Button submit = (Button) dialog.findViewById(R.id.buttonSubmit);
                final EditText carModel = (EditText) dialog.findViewById(R.id.editTextModel);
                final EditText carType = (EditText) dialog.findViewById(R.id.editTextType);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCarModel = carModel.getText().toString();
                        mCarType = carType.getText().toString();
                        mTxtCarModel.setText(mCarModel);
                        mTxtCarType.setText(mCarType);
                        dialog.dismiss();
                    }
                });

            }
        });


        mImgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProfile = true;
                selectImage();

            }
        });

        mImgViewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCar = true;
                selectImage();
            }
        });

        mImgViewDNI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDNI = true;
                selectImage();
            }
        });


        mBtn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateProfileAsyncTask task = new UpdateProfileAsyncTask(getActivity(),parentLayout);
                task.execute(mEdtUserName.getText().toString(),mEdtAge.getText().toString(),mEdtNumber.getText().toString(),
                        sessionData.getString("ur_email",""),sessionData.getString("ur_dni_photo", ""),sessionData.getString("ur_car_photo", ""),sessionData.getString("ur_photo", ""),mCarModel,
                        mCarType,sessionData.getString("api_key",""));


            }
        });


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

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CAMERA);

                    } else {
                        cameraIntent();
                    }

                }
                //choose image from gallery

                else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    //check permissions

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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

                if(mProfile) {
                    mImgViewProfile.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.profile_add));
                }else if(mDNI)
                {
                    mImgViewDNI.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.login_logo_1));
                }
                else if(mCar)
                {
                    mImgViewCar.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.car_new));
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

        float maxHeight = 300.0f;
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


                if(mProfile) {
                    mImgViewProfile.setImageBitmap(scaledBitmap);
                }else if(mDNI)
                {
                    mImgViewDNI.setImageBitmap(scaledBitmap);
                }
                else if(mCar)
                {
                    mImgViewCar.setImageBitmap(scaledBitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            FileOutputStream out = null;
            filename = getFilename();
            try {
                out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                UploadFileAsyncTask uploadFileAsyncTask = new UploadFileAsyncTask(getActivity(),AccountFragment.this);
                uploadFileAsyncTask.execute(sessionData.getString("api_key",""),filename);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            if(mProfile) {
                mImgViewProfile.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.profile_add));

            }else if(mDNI)
            {
                mImgViewDNI.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.login_logo_1));
            }
            else if(mCar)
            {
                mImgViewCar.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.car_new));
            }

        }

        return filename;
    }


    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");

        if(mProfile) {

            profileImage = new File(uriSting.toString());

            mImage = profileImage.getAbsolutePath();

        }
        else if(mDNI)
        {
            dniImage = new File(uriSting.toString());

            mDNIImage = dniImage.getAbsolutePath();
        }
        else if(mCar)
        {
            carImage = new File(uriSting.toString());

            mCarImage = carImage.getAbsolutePath();
        }

        return uriSting;

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
    public void doPostExecute(String file)
    {
        Log.e("UploadedFile",file);
     //   Toast.makeText(getActivity(),file,Toast.LENGTH_SHORT).show();

        if(mProfile)
        {
            sessionData.add("ur_photo", file);
            mProfile = false;
        }
        else if(mDNI)
        {
            sessionData.add("ur_dni_photo", file);
            mDNI = false;
        }
        else if(mCar)
        {
            sessionData.add("ur_car_photo", file);
            mCar = false;
        }

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

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
