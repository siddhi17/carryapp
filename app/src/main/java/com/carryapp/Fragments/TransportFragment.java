package com.carryapp.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.carryapp.Classes.Transport;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TransportFragment extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {


    private OnFragmentInteractionListener mListener;
    private EditText mEditTxt_From,mEditTxt_To,mEditTxt_Date,mEditTxt_Time;
    int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;
    private Boolean gpsEnabled;

    private Button mBtnSearch;
    private Snackbar snackbar;
    private FrameLayout parentLayout;

    private Double startlatitude,startlongitude,endlatitude,endlongitude;
    private LatLng mFromLatLang,mToLatLang;
    private static final String TAG = "PlacePickerSample";

    /**
     * Request code passed to the PlacePicker intent to identify its result when it returns.
     */
    private static final int REQUEST_PLACE_PICKER_FROM = 1;
    private static final int REQUEST_PLACE_PICKER_TO = 2,REQUEST_CODE_AUTOCOMPLETE_FROM = 222,REQUEST_CODE_AUTOCOMPLETE_TO = 333;

    private String mDate="",mTime,todayDate,todayTime;
    Date date = null, date1 = null;

    public TransportFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_transport, container, false);


        setUpUI(view);

        listeners();


        return view;
    }

    public void setUpUI(View view)
    {
        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView)getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.GONE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.transportTitle);


        mEditTxt_From = (EditText) view.findViewById(R.id.editTextFrom);
        mEditTxt_To = (EditText) view.findViewById(R.id.editTextTo);
        mEditTxt_Date = (EditText) view.findViewById(R.id.editTextDate);
        mBtnSearch = (Button) view.findViewById(R.id.btnSearch);
        parentLayout = (FrameLayout) view.findViewById(R.id.parentPanel);


        mEditTxt_Time = (EditText) view.findViewById(R.id.editTextTime);



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

    public void listeners()
    {


        mEditTxt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        TransportFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setVersion(DatePickerDialog.Version.VERSION_2);

                dpd.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));

                dpd.show(getFragmentManager(), "Datepickerdialog");

                dpd.setMinDate(now);
            }
        });

        mEditTxt_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!todayDate.equals("")) {

                    String todayDate;
                    try {

                        if (mDate != null) {
                            if (!mDate.equals("") && mDate != null) {
                                DateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
                                date = format.parse(mDate);
                            }
                        }

                        Calendar compareDate = Calendar.getInstance();

                        String Date = compareDate.get(Calendar.DAY_OF_MONTH) + "/" + (compareDate.get(Calendar.MONTH) + 1) + "/" + compareDate.get(Calendar.YEAR);
                        todayDate = CommonUtils.formateDateFromstring("dd/MM/yyyy", "dd MMM, yyyy", Date);

                        if (!todayDate.equals("") && todayDate != null) {
                            DateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
                            date1 = format.parse(todayDate);
                        }


                        Calendar now = Calendar.getInstance();

                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                TransportFragment.this,
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                true
                        );

                        if (!todayDate.equals("") && todayDate != null)  {
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
          /*      gpsEnabled = isGPSEnabled();
           *//*     if(CommonUtils.getLocationMode(getActivity()) == 0)
                {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else if(CommonUtils.getLocationMode(getActivity()) == 1)
                {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else if(CommonUtils.getLocationMode(getActivity()) == 2)
                {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else if(CommonUtils.getLocationMode(getActivity()) == 3)
                {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                if(gpsEnabled) {
                    try {
                        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                        Intent intent = intentBuilder.build(getActivity());
                        // Start the Intent by requesting a result, identified by a request code.
                        startActivityForResult(intent, REQUEST_PLACE_PICKER_FROM);

                    } catch (GooglePlayServicesRepairableException e) {
                        GooglePlayServicesUtil
                                .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                    } catch (GooglePlayServicesNotAvailableException e) {
                        Toast.makeText(getActivity(), "Google Play Services is not available.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else {
                    snackbar = Snackbar.make(parentLayout,R.string.locationAlert, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }*//*

                if(gpsEnabled) {
                    try {
                        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                        Intent intent = intentBuilder.build(getActivity());
                        // Start the Intent by requesting a result, identified by a request code.
                        startActivityForResult(intent, REQUEST_PLACE_PICKER_FROM);

                    } catch (GooglePlayServicesRepairableException e) {
                        GooglePlayServicesUtil
                                .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                    } catch (GooglePlayServicesNotAvailableException e) {
                        Toast.makeText(getActivity(), "Google Play Services is not available.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }

                else if(CommonUtils.getLocationMode(getActivity()) == 1)
                {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else if(CommonUtils.getLocationMode(getActivity()) == 2)
                {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
*/

                openAutocompleteActivityFrom();

            }
        });

        mEditTxt_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                gpsEnabled = isGPSEnabled();

                if(gpsEnabled) {
                    try {


                        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                        Intent intent = intentBuilder.build(getActivity());
                        // Start the Intent by requesting a result, identified by a request code.
                        startActivityForResult(intent, REQUEST_PLACE_PICKER_TO);

                    } catch (GooglePlayServicesRepairableException e) {
                        GooglePlayServicesUtil
                                .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                    } catch (GooglePlayServicesNotAvailableException e) {
                        Toast.makeText(getActivity(), "Google Play Services is not available.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else if(CommonUtils.getLocationMode(getActivity()) == 1)
                {
                    snackbar = Snackbar.make(parentLayout, R.string.locationMode, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    snackbar.addCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            //see Snackbar.Callback docs for event details
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
                }
                else if(CommonUtils.getLocationMode(getActivity()) == 2)
                {
                    snackbar = Snackbar.make(parentLayout, R.string.locationMode, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    snackbar.addCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            //see Snackbar.Callback docs for event details
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
                }
                else {
                    snackbar = Snackbar.make(parentLayout, R.string.locationMode, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    snackbar.addCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            //see Snackbar.Callback docs for event details
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
                }*/

                openAutocompleteActivityTo();
            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = mEditTxt_Date.getText().toString()+" "+mEditTxt_Time.getText().toString();
                mDate = CommonUtils.formateDateFromstring("dd MMM, yyyy HH:mm", "yyyy-MM-dd", date);
                Log.e("date",mDate);


                if(validation()) {

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    TransportListFragment fragment1 = new TransportListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("from", mEditTxt_From.getText().toString());
                    bundle.putString("to", mEditTxt_To.getText().toString());
                    bundle.putParcelable("fromLatLang", mFromLatLang);
                    bundle.putParcelable("toLatLang", mToLatLang);
                    bundle.putString("date", mDate);

                    fragment1.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment1).addToBackStack("G").commit();
                }
            }
        });

    }
    private void openAutocompleteActivityFrom() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
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
        if(mEditTxt_From.getText().toString().equals("") || mEditTxt_From.getText().toString().equals("null"))
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
        else {
            return true;
        }

        return false;
    }

    public boolean isGPSEnabled (){
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                mEditTxt_From.setText(place.getName());

                mFromLatLang = place.getLatLng();

                startlatitude = mFromLatLang.latitude;
                startlongitude = mFromLatLang.longitude;


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

                endlatitude = mToLatLang.latitude;
                endlongitude = mToLatLang.longitude;

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
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;

        String time;

      /*  if(hourOfDay > 12)
        {
            time = (hourOfDay - 12)  + ":" + minuteString + " PM";
        }
        else {

            time = hourOfDay + ":" + minuteString + " AM";
        }*/

        mTime = hourString + ":"+minuteString;
        mEditTxt_Time.setText(mTime);
    }
}
