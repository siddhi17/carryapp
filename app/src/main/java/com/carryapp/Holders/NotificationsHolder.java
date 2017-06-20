package com.carryapp.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carryapp.R;

/**
 * Created by siddhi jambhale on 6/12/2017.
 */

public class NotificationsHolder extends RecyclerView.ViewHolder  {

    public TextView textViewMessage,textViewStatus;
    public Button btnAccept,btnReject;

    public NotificationsHolder(View itemView) {
        super(itemView);

        textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
        textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
        btnAccept = (Button) itemView.findViewById(R.id.buttonAccept);
        btnReject = (Button) itemView.findViewById(R.id.buttonReject);

    }
}
