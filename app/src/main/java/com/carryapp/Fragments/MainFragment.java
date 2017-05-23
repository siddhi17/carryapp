package com.carryapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.R;


public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayout mLinearPostDelivery,mLinearTransport;

    private TextView mTextViewPostDelivery,mTextViewTransport;

    public MainFragment() {
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

        View view = inflater.inflate(R.layout.fragment_main, container, false);


        mLinearPostDelivery = (LinearLayout)view.findViewById(R.id.linearDelivery);
        mLinearTransport = (LinearLayout)view.findViewById(R.id.linearTransport);

        mLinearTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();;
                TransportFragment fragment = new TransportFragment();
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment,"TRANSPORT_FRAGMENT").addToBackStack("C").commit();
            }
        });

        mLinearPostDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();;
                PostShippingFragment fragment = new PostShippingFragment();
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment,"SHIPPING_FRAGMENT").addToBackStack("D").commit();
            }
        });

        return view;
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
}
