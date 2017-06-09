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
    public ImageView imageViewR1,imageViewR1_empty,imageViewR2,imageViewR2_empty,imageViewR3,imageViewR3_empty,imageViewR4,imageViewR4_empty,
    imageViewR5,imageViewR5_empty;


    public TransportListHolder(View itemView) {
        super(itemView);
        tv_dateTime = (TextView) itemView.findViewById(R.id.tv_dateTime);
        tv_product = (TextView) itemView.findViewById(R.id.tv_product);
        tv_username = (TextView) itemView.findViewById(R.id.tv_username);
        img_product = (CircleImageView) itemView.findViewById(R.id.img_product);
        imageViewR1 = (ImageView) itemView.findViewById(R.id.imageViewR1);
        imageViewR2 = (ImageView) itemView.findViewById(R.id.imageViewR2);
        imageViewR3 = (ImageView) itemView.findViewById(R.id.imageViewR3);
        imageViewR4 = (ImageView) itemView.findViewById(R.id.imageViewR4);
        imageViewR5 = (ImageView) itemView.findViewById(R.id.imageViewR5);
        imageViewR1_empty = (ImageView) itemView.findViewById(R.id.imageViewR1_empty);
        imageViewR2_empty = (ImageView) itemView.findViewById(R.id.imageViewR2_empty);
        imageViewR3_empty = (ImageView) itemView.findViewById(R.id.imageViewR3_empty);
        imageViewR4_empty = (ImageView) itemView.findViewById(R.id.imageViewR4_empty);
        imageViewR5_empty = (ImageView) itemView.findViewById(R.id.imageViewR5_empty);

        lay_row = (LinearLayout) itemView.findViewById(R.id.list_row);
    }


}
