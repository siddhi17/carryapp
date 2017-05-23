package com.carryapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.carryapp.R;

public class LoginActivity extends AppCompatActivity {

    private Button mSignUp,mLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignUp = (Button)findViewById(R.id.btn_signUp);
        mLogIn = (Button)findViewById(R.id.login);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

    }

}
