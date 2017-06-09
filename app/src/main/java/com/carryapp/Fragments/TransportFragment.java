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
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

public class TransportFragment extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {


    private OnFragmentInteractionListener mListener;
    private EditText mEditTxt_From,mEditTxt_To,mEditTxt_Date,mEditTxt_Time;
    int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;
    private Boolean gpsEnabled;

    private Button mBtnSearch;
    private Snackbar snackbar;
    private FrameLayout parentLayout;

    private LatLng mFromLatLang,mToLatLang;
    private static final String TAG = "PlacePickerSample";

    /**
     * Request code passed to the PlacePicker intent to identify its result when it returns.
     */
    private static final int REQUEST_PLACE_PICKER_FROM = 1;
    private static final int REQUEST_PLACE_PICKER_TO = 2;

    private String mDate,mTime;


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

                Calendar now = Calendar.getInstance();

                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        TransportFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );


                tpd.setVersion(TimePickerDialog.Version.VERSION_2);

                tpd.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });

                tpd.show(getFragmentManager(), "Timepickerdialog");

                tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),now.get(Calendar.SECOND));
            }
        });


        mEditTxt_From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsEnabled = isGPSEnabled();

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
            }
        });

        mEditTxt_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = mEditTxt_Date.getText().toString() + " " +mEditTxt_Time.getText().toString();
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

    public boolean validation()
    {
        if(mEditTxt_From.getText().toString().equals(""))
        {
            snackbar = Snackbar.make(parentLayout,R.string.emptyFrom, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxt_To.getText().toString().equals(""))
        {
            snackbar = Snackbar.make(parentLayout,R.string.emptyTo, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxt_Date.getText().toString().equals(""))
        {
            snackbar = Snackbar.make(parentLayout,R.string.emptyDate, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mEditTxt_Time.getText().toString().equals("")){
            snackbar = Snackbar.make(parentLayout,R.string.emptyTo, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(mDate.equals(""))
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
        if (requestCode == REQUEST_PLACE_PICKER_FROM) {
            // This result is from the PlacePicker dialog.

            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, getActivity());

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();
                String attribution = PlacePicker.getAttributions(data);
                if(attribution == null){
                    attribution = "";
                }

                mFromLatLang = place.getLatLng();

                mEditTxt_From.setText(name.toString());

                // Print data to debug log
                Log.d(TAG, "Place selected: " + placeId + " (" + name.toString() + ")");

            }

        }
        else if(requestCode == REQUEST_PLACE_PICKER_TO)
        {
            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, getActivity());

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();
                String attribution = PlacePicker.getAttributions(data);
                if(attribution == null){
                    attribution = "";
                }

                mToLatLang = place.getLatLng();

                mEditTxt_To.setText(name.toString());

                // Print data to debug log
                Log.d(TAG, "Place selected: " + placeId + " (" + name.toString() + ")");

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
