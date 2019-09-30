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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.backendless.BackendlessUser;
import com.example.dell.shoegamev22.models.SubmittedUserObject;
import com.example.dell.shoegamev22.viewmodels.SignUpActivityViewModel;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.libizo.CustomEditText;
import com.novoda.merlin.MerlinsBeard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity {


    //VIEWS
    CustomEditText firstNameET, lastNameET, eMailET, phoneET, passwordET;
    Button submitDetailsBtn;
    CheckBox staySignedInCheckBox, termsAndConditionsCheckBox;
    Spinner genderSpinner;

    //VARIABLES
    String firstName, lastName, eMail, phone, password;
    Boolean staySignedIn = false, agreeToTermsAndConditions = false;
    Integer gender = 1;  //none selected:1, male:2, female:3
    Map<String, Object> userDetailsMap = new HashMap<>();  //used to create a backendless user object


    //USER
    SubmittedUserObject submittedUserObject;


    //VIEW MODEL
    SignUpActivityViewModel signUpActivityViewModel;

    //merlin
    MerlinsBeard merlinsBeard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //create merlin
        //library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(this);


        //get references to views
        firstNameET = findViewById(R.id.activitySignUpFirstNameEditText);
        lastNameET = findViewById(R.id.activitySignUpLastNameEditText);
        eMailET = findViewById(R.id.activitySignUpEmailEditText);
        phoneET = findViewById(R.id.activitySignUpPhoneEditText);
        passwordET = findViewById(R.id.activitySignUpPasswordEditText);
        genderSpinner = findViewById(R.id.activitySignUpGenderSpinner);
        submitDetailsBtn = findViewById(R.id.activitySignUpSubmitButton);
        staySignedInCheckBox = findViewById(R.id.activitySignUpStaySignedInCheckBox);
        termsAndConditionsCheckBox = findViewById(R.id.activitySignUpTermsCheckBox);


        //set up gender options spinner
        List<String> genderSpinnerOptions = new ArrayList<>();
        genderSpinnerOptions.add("Male");
        genderSpinnerOptions.add("Female");
        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderSpinnerOptions);
        genderSpinner.setAdapter(genderSpinnerAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String genderString = (String) parent.getItemAtPosition(position);

                if (genderString.equals("Male")) {

                    gender = 2;

                } else if (genderString.equals("Female")) {

                    gender = 3;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        signUpActivityViewModel = ViewModelProviders.of(this).get(SignUpActivityViewModel.class);
        signUpActivityViewModel.init();


        displaySuccessDialog();
       /* signUpActivityViewModel.getSignUpResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {

                    saveUserToCache(signUpActivityViewModel.getNewlyRegisteredUser().getValue());
                }


            }
        });*/


        //set up on click listeners
        submitDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (merlinsBeard.isConnected()) {

                    Log.d("MyLogsMerlin", "Merlin is connected. Activity:Sign Up");
                    createSubmittedUserObject();

                } else {

                    Log.d("MyLogsMerlin", "Merlin is NOT connected. Activity:Sign Up");

                    new iOSDialogBuilder(SignUp.this)
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


    }


    private void createSubmittedUserObject() {

        if (!TextUtils.isEmpty(firstNameET.getText())) {

            if (!TextUtils.isEmpty(lastNameET.getText())) {


                if (!TextUtils.isEmpty(eMailET.getText())) {


                    if (!TextUtils.isEmpty(phoneET.getText())) {


                        if (!TextUtils.isEmpty(passwordET.getText())) {


                            if (termsAndConditionsCheckBox.isChecked()) {

                                firstName = firstNameET.getText().toString();
                                lastName = lastNameET.getText().toString();
                                eMail = eMailET.getText().toString();
                                phone = phoneET.getText().toString();
                                password = passwordET.getText().toString();

                                if (staySignedInCheckBox.isChecked()) {

                                    staySignedIn = true;

                                }

                                SubmittedUserObject thisSubmittedUserObject = new SubmittedUserObject(firstName, lastName, eMail, phone,
                                        password, staySignedIn, agreeToTermsAndConditions, gender);

                                submittedUserObject = thisSubmittedUserObject;

                                userDetailsMap.put("gender", gender);
                                userDetailsMap.put("email", eMail);
                                userDetailsMap.put("first_name", firstName);
                                userDetailsMap.put("last_name", lastName);
                                userDetailsMap.put("password", password);
                                userDetailsMap.put("phone_number", phone);

                                BackendlessUser thisBackendlessUser = new BackendlessUser();
                                thisBackendlessUser.putProperties(userDetailsMap);


                                signUpActivityViewModel.registerNewUser(thisBackendlessUser, thisSubmittedUserObject);


                            } else {


                                new iOSDialogBuilder(this)
                                        .setTitle("Error")
                                        .setSubtitle("Please agree to the terms and conditions")
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
                                    .setSubtitle("Please choose a password")
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
                                .setSubtitle("Please enter a valid phone number")
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
                            .setSubtitle("Please enter your E-mail")
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
                        .setSubtitle("Please enter your last name")
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
                    .setTitle("Error!")
                    .setSubtitle("Please make sure all the relevant details are entered")
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


    private void saveUserToCache(BackendlessUser user) {

        new EasySave(this).saveModelAsync("current signed in user", user, new SaveAsyncCallback<BackendlessUser>() {
            @Override
            public void onComplete(BackendlessUser user) {


                if (user != null) {
                    Log.d("myLogsUserCache", "Sign up activity: Signed up user saved to cache: " + user.toString());
                }

            }

            @Override
            public void onError(String s) {

                Log.d("myLogsUserCache", "Sign up activity: Signed up user not saved to cache. Error : " + s);

            }
        });

    }


    public void displaySuccessDialog() {


        signUpActivityViewModel.getNewSignInResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {

                    //todo user log in is okay.


                    signUpActivityViewModel.getNewlySignedInUser().observe(SignUp.this, new Observer<BackendlessUser>() {
                        @Override
                        public void onChanged(BackendlessUser user) {

                            if (user != null) {

                                //todo user log in is okay.

                                String userName = (String) user.getProperty("last_name");
                                String displayString = "hello," + userName + " .You have successfully signed up ";

                                saveUserToCache(user);

                                new iOSDialogBuilder(SignUp.this)
                                        .setTitle("Success!")
                                        .setSubtitle(displayString)
                                        .setBoldPositiveLabel(true)
                                        .setCancelable(false)
                                        .setPositiveListener("okay", new iOSDialogClickListener() {
                                            @Override
                                            public void onClick(iOSDialog dialog) {

                                                dialog.dismiss();
                                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                startActivity(intent);



                                            }
                                        })
                                        .build().show();


                            } else {


                                new iOSDialogBuilder(SignUp.this)
                                        .setTitle("Error!")
                                        .setSubtitle("There might have been an error while trying to log you in. Do you want to try again?")
                                        .setBoldPositiveLabel(true)
                                        .setCancelable(false)
                                        .setPositiveListener("Try again", new iOSDialogClickListener() {
                                            @Override
                                            public void onClick(iOSDialog dialog) {


                                                if (submittedUserObject != null) {

                                                    signUpActivityViewModel.logUserIn(submittedUserObject);

                                                } else {


                                                    Intent intent = new Intent(SignUp.this, LogIn.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    dialog.dismiss();

                                                }


                                            }
                                        }).setNegativeListener("Cancel", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {

                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                        startActivity(intent);


                                    }
                                })
                                        .build().show();


                                //todo user log in is NOT okay.


                            }


                        }
                    });


                } else {

                    //todo user log in is not okay


                    new iOSDialogBuilder(SignUp.this)
                            .setTitle("Error!")
                            .setSubtitle("Sorry, there has been a problem logging you in. Please try again")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener("Try again", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {


                                    if (submittedUserObject != null) {

                                        signUpActivityViewModel.logUserIn(submittedUserObject);

                                    } else {

                                        dialog.dismiss();
                                        Intent intent = new Intent(SignUp.this, LogIn.class);
                                        startActivity(intent);

                                    }


                                }
                            }).setNegativeListener("Cancel", new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {

                            dialog.dismiss();
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);


                        }
                    })
                            .build().show();


                }


            }
        });


    }


}














