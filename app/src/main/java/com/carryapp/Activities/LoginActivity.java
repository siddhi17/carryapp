package com.carryapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.carryapp.AsyncTasks.LoginAsyncTask;
import com.carryapp.R;
import com.carryapp.helper.SessionData;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private Button mSignUp,mLogIn;
    private EditText mEdtEmail,mEdtPass;
    private CoordinatorLayout parentLayout;
    private SessionData sessionData;
    private Snackbar snackbar;

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
                final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("token", "Refreshed token: " + refreshedToken);

                if(mEdtEmail.getText().toString().equals(""))
                {
                    snackbar = Snackbar.make(parentLayout, R.string.blankEmail, Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
                else if(mEdtPass.getText().toString().equals(""))
                {
                    snackbar = Snackbar.make(parentLayout, R.string.blankPass, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    View view1 = getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }

                    LoginAsyncTask task = new LoginAsyncTask(LoginActivity.this, parentLayout);
                    task.execute(mEdtEmail.getText().toString(), mEdtPass.getText().toString(), refreshedToken);
                }

            }
        });

    }

}
