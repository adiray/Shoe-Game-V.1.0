package com.example.dell.shoegamev22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import br.vince.easysave.EasySave;
import br.vince.easysave.SaveAsyncCallback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.airbnb.lottie.LottieAnimationView;
import com.backendless.BackendlessUser;
import com.example.dell.shoegamev22.models.SubmittedUserObject;
import com.example.dell.shoegamev22.viewmodels.LogInActivityViewModel;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.libizo.CustomEditText;
import com.novoda.merlin.MerlinsBeard;

public class LogIn extends AppCompatActivity {


    //views
    CustomEditText eMailET, passwordET;
    Button submitDetailsBtn;
    CheckBox staySignedInCheckBox;
    LottieAnimationView mLottieAnim;

    //Strings
    String email, password;

    //booleans
    Boolean staySignedIn = false;

    //VIEW MODEL
    LogInActivityViewModel logInActivityViewModel;

    //merlin
    MerlinsBeard merlinsBeard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);




        //create merlin
        //library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(this);

        //get references to views

        eMailET = findViewById(R.id.activitySignInEmailEditText);
        passwordET = findViewById(R.id.activitySignInPasswordEditText);
        submitDetailsBtn = findViewById(R.id.activitySignInSubmitButton);
        staySignedInCheckBox = findViewById(R.id.activitySignInStaySignedInCheckBox);
        mLottieAnim = findViewById(R.id.activitySignInLoadingView);


        //View models
        logInActivityViewModel = ViewModelProviders.of(this).get(LogInActivityViewModel.class);
        logInActivityViewModel.init();



        submitDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (merlinsBeard.isConnected()) {

                    Log.d("MyLogsMerlin", "Merlin is connected. Activity:log in");

                    logUserIn();
                } else {

                    Log.d("MyLogsMerlin", "Merlin is NOT connected. Activity:log in");
                    new iOSDialogBuilder(LogIn.this)
                            .setTitle("Sorry")
                            .setSubtitle("We cannot seem to access the internet, please try again!")
                            .setBoldPositiveLabel(true)
                            .setCancelable(true)
                            .setPositiveListener("okay", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {

                                    dialog.dismiss();
                                }
                            })
                            .build().show();


                }


            }
        });








        logInActivityViewModel.getNewSignInResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {


                    if (logInActivityViewModel.getNewlySignedInUser().getValue() != null) {

                        saveSignedInUserToCache(logInActivityViewModel.getNewlySignedInUser().getValue());
                    }

                    mLottieAnim.pauseAnimation();
                    mLottieAnim.setVisibility(View.GONE);





                    //TODO  send user to profile.


                    new iOSDialogBuilder(LogIn.this)
                            .setTitle("Success")
                            .setSubtitle("You have logged in successfully!")
                            .setBoldPositiveLabel(true)
                            .setCancelable(true)
                            .setPositiveListener("okay", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {


                                    dialog.dismiss();

                                    Intent intent = new Intent(LogIn.this,MainActivity.class);
                                    LogIn.this.startActivity(intent);

                                }
                            })
                            .build().show();


                } else {


                    mLottieAnim.pauseAnimation();
                    mLottieAnim.setVisibility(View.GONE);



                    new iOSDialogBuilder(LogIn.this)
                            .setTitle("Success")
                            .setSubtitle("We have failed to log you in! Please check the login details")
                            .setBoldPositiveLabel(true)
                            .setCancelable(true)
                            .setPositiveListener("okay", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {

                                    dialog.dismiss();


                                }
                            })
                            .build().show();

                }

            }
        });








    }






    void logUserIn() {

        if (!TextUtils.isEmpty(eMailET.getText())) {

            if (!TextUtils.isEmpty(passwordET.getText())) {

                email = eMailET.getText().toString();
                password = passwordET.getText().toString();

                if (staySignedInCheckBox.isChecked()) {


                    SubmittedUserObject newlySubmittedUserObject = new SubmittedUserObject(email, password, true);
                    logInActivityViewModel.logUserIn(newlySubmittedUserObject);
                    mLottieAnim.setVisibility(View.VISIBLE);


                }else{

                    SubmittedUserObject newlySubmittedUserObject = new SubmittedUserObject(email, password, staySignedIn);
                    logInActivityViewModel.logUserIn(newlySubmittedUserObject);
                    mLottieAnim.setVisibility(View.VISIBLE);

                }



            } else {

                new iOSDialogBuilder(this)
                        .setTitle("Error")
                        .setSubtitle("Please enter your password")
                        .setBoldPositiveLabel(true)
                        .setCancelable(true)
                        .setPositiveListener("okay", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {

                                dialog.dismiss();
                            }
                        })
                        .build().show();


            }

        } else {


            new iOSDialogBuilder(this)
                    .setTitle("Error")
                    .setSubtitle("Please make sure that all the details are entered")
                    .setBoldPositiveLabel(true)
                    .setCancelable(true)
                    .setPositiveListener("okay", new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {

                            dialog.dismiss();
                        }
                    })
                    .build().show();


        }


    }




    private void saveSignedInUserToCache(BackendlessUser user) {


        new EasySave(this).saveModelAsync("current signed in user", user, new SaveAsyncCallback<BackendlessUser>() {
            @Override
            public void onComplete(BackendlessUser user) {

                Log.d("MyLogsUser", "signed in user saved to cache. token: " + user.toString());

            }

            @Override
            public void onError(String s) {
                if (s != null) {
                    Log.d("MyLogsUser", "signed in user NOT saved to cache. error: " + s);
                }
            }
        });


    }













}
