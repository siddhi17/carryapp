package com.carryapp.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Owner on 14-11-2016.
 */
public class TransportListHolder extends RecyclerView.ViewHolder {

    public CircleImageView img_product;
    public TextView tv_dateTime, tv_product, tv_username;
    public LinearLayout lay_row;
    public Button btnStatus;


    public TransportListHolder(View itemView) {
        super(itemView);
        tv_dateTime = (TextView) itemView.findViewById(R.id.tv_dateTime);
        tv_product = (TextView) itemView.findViewById(R.id.tv_product);
        tv_username = (TextView) itemView.findViewById(R.id.tv_username);
        img_product = (CircleImageView) itemView.findViewById(R.id.img_product);

    }


}
