package com.carryapp.Holders;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.R;

/**
 * Created by siddhi jambhale on 5/27/2017.
 */

public class TripsHolder  extends RecyclerView.ViewHolder {


    public TextView tv_date,tv_from,tv_to;
    public ImageView carImageView;
    public LinearLayout lay_row;

    public TripsHolder(View itemView) {
        super(itemView);
        tv_date = (TextView) itemView.findViewById(R.id.textViewDate);
        tv_from = (TextView) itemView.findViewById(R.id.textViewFrom);
        tv_to = (TextView) itemView.findViewById(R.id.textViewTo);
        carImageView = (ImageView) itemView.findViewById(R.id.imageViewCar);
        lay_row = (LinearLayout) itemView.findViewById(R.id.lay_row);

    }

}
