package com.carryapp.Fragments;


import android.app.FragmentManager;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.Activities.EditPostActivity;
import com.carryapp.Activities.HomeActivity;
import com.carryapp.AsyncTasks.DeletePostAsyncTask;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.SessionData;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailsFragment extends Fragment {

    private TextView mTextViewDate,mTextViewTime,mTextViewFrom,mTextViewTo,mTextViewProductName,mTextViewProductDetails,mTextViewCharges;
    private ImageView imageViewProduct;
    private String newDate,newTime;
    private Date date;
    private Button mBtnDeletePost,mBtnEditPost;
    private LinearLayout parentLayout;
    private Bundle bundle;

    private SessionData sessionData;

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        setUpUI(view);

        listeners();

        return view;
    }


    public void setUpUI(View view)
    {

        sessionData = new SessionData(getActivity());

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
        parentLayout = (LinearLayout) view.findViewById(R.id.parentPanel);
        mTextViewCharges = (TextView) view.findViewById(R.id.textViewCharges);

        mBtnDeletePost = (Button) view.findViewById(R.id.btn_deletePost);
        mBtnEditPost = (Button) view.findViewById(R.id.btn_editPost);

        imageViewProduct = (ImageView) view.findViewById(R.id.imageViewProduct);

        bundle = this.getArguments();

        newDate = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy", bundle.getString("pt_date"));

        newTime = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "hh:mm", bundle.getString("pt_date"));

        String amount = bundle.getString("pt_charges") + " " +"€";

        mTextViewDate.setText(newDate);
        mTextViewTime.setText(newTime);
        mTextViewFrom.setText(bundle.getString("from"));
        mTextViewTo.setText(bundle.getString("to"));
        mTextViewProductName.setText(bundle.getString("pt_name"));
        mTextViewCharges.setText(amount);

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

    public void listeners()
    {

        mBtnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeletePostAsyncTask deletePostAsyncTask = new DeletePostAsyncTask(getActivity(),parentLayout);
                deletePostAsyncTask.execute(bundle.getString("pt_id"),sessionData.getString("api_key",""));

            }
        });


        mBtnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), EditPostActivity.class);
                intent.putExtra("editPost",true);
                intent.putExtra("pt_id", bundle.getString("pt_id"));
                intent.putExtra("pt_name", bundle.getString("pt_name"));
                intent.putExtra("pt_details", bundle.getString("pt_details"));
                intent.putExtra("pt_photo", bundle.getString("pt_photo"));
                intent.putExtra("pt_date", bundle.getString("pt_date"));
                intent.putExtra("from", bundle.getString("from"));
                intent.putExtra("to", bundle.getString("to"));
                intent.putExtra("pt_charges", bundle.getString("pt_charges"));
                intent.putExtra("pt_size", bundle.getString("pt_size"));
                intent.putExtra("st_lati", bundle.getDouble("st_lati"));
                intent.putExtra("st_longi", bundle.getDouble("st_longi"));
                intent.putExtra("ed_lati", bundle.getDouble("ed_lati"));
                intent.putExtra("ed_longi", bundle.getDouble("ed_longi"));
                intent.putExtra("pt_track", bundle.getString("pt_track"));

                getActivity().finish();
                startActivity(intent);
            }
        });
    }
}
