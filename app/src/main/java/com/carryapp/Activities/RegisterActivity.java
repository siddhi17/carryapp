package com.carryapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carryapp.R;

public class RegisterActivity extends AppCompatActivity {

    private TextView mLoginText;
    private Button mButton_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.register);
        toolbar.setTitleTextColor(Color.BLACK);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(RegisterActivity.this,R.mipmap.ic_arrow_back_black_24dp));

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onBackPressed();
            }
        });


        mLoginText = (TextView) findViewById(R.id.textViewLogin);
        mButton_register = (Button) findViewById(R.id.btn_SignUp);

        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });


        mButton_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }


    @Override
    public void onBackPressed()
    {
       finish();

    }
}
