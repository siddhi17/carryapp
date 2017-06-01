package com.carryapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.carryapp.AsyncTasks.LoginAsyncTask;
import com.carryapp.R;
import com.carryapp.helper.SessionData;

public class LoginActivity extends AppCompatActivity {

    private Button mSignUp,mLogIn;
    private EditText mEdtEmail,mEdtPass;
    private CoordinatorLayout parentLayout;
    private SessionData sessionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionData = new SessionData(LoginActivity.this);
        setUpUI();
        listeners();

    }

    public void setUpUI()
    {

        mSignUp = (Button)findViewById(R.id.btn_signUp);
        mLogIn = (Button)findViewById(R.id.login);
        mEdtEmail = (EditText)findViewById(R.id.editTextEmail);
        mEdtPass = (EditText)findViewById(R.id.editText2Pass);
        parentLayout = (CoordinatorLayout)findViewById(R.id.parentPanel);

    }

    public void listeners()
    {

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginAsyncTask task = new LoginAsyncTask(LoginActivity.this,parentLayout);
                task.execute(mEdtEmail.getText().toString(),mEdtPass.getText().toString(),sessionData.getString("ur_device_id",""));

            }
        });

    }

}
