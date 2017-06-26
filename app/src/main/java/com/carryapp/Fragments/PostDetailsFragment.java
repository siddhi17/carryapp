package com.carryapp.Fragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailsFragment extends Fragment {

    private TextView mTextViewDate,mTextViewTime,mTextViewFrom,mTextViewTo,mTextViewProductName,mTextViewProductDetails;
    private ImageView imageViewProduct;
    private String newDate,newTime;
    private Date date;

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        setUpUI(view);

        return view;
    }


    public void setUpUI(View view)
    {

        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView) getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.GONE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.tripDetails);


        mTextViewDate = (TextView) view.findViewById(R.id.textViewDate);
        mTextViewTime = (TextView) view.findViewById(R.id.textViewTime);
        mTextViewFrom = (TextView) view.findViewById(R.id.textViewFrom);
        mTextViewTo = (TextView) view.findViewById(R.id.textViewTo);
        mTextViewProductName = (TextView) view.findViewById(R.id.textViewProductName);
        mTextViewProductDetails =  (TextView) view.findViewById(R.id.textViewProductDetails);

        imageViewProduct = (ImageView) view.findViewById(R.id.imageViewProduct);

        Bundle bundle = this.getArguments();

        newDate = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy", bundle.getString("pt_date"));

        newTime = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "hh:mm", bundle.getString("pt_date"));

        mTextViewDate.setText(newDate);
        mTextViewTime.setText(newTime);
        mTextViewFrom.setText(bundle.getString("from"));
        mTextViewTo.setText(bundle.getString("to"));
        mTextViewProductName.setText(bundle.getString("pt_name"));

        if(!bundle.getString("pt_details").equals("") || !bundle.getString("pt_details").equals("null"))
        {
            mTextViewProductDetails.setText(bundle.getString("pt_details"));
        }

        String url = getString(R.string.photo_url) + bundle.getString("pt_photo");
        Log.e("url",url);

        Picasso.with(getActivity())
                .load(url)
                .resize(400,400)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(imageViewProduct);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView) getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.VISIBLE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.GONE);

    }
}
