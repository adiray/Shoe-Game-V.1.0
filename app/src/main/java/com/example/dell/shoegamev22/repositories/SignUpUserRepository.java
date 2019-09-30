package com.example.dell.shoegamev22.repositories;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.dell.shoegamev22.models.SubmittedUserObject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SignUpUserRepository {


    //VARIABLES
    private static SignUpUserRepository instance;

    //LIVE DATA
    private MutableLiveData<Boolean> signUpResult = new MutableLiveData<>();
    private MutableLiveData<BackendlessUser> signUpUserResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> signInResult = new MutableLiveData<>();
    private MutableLiveData<BackendlessUser> signInUserResponse = new MutableLiveData<>();


    //CONSTRUCTOR
    private SignUpUserRepository() {
    }


    //suggest that you eagerly create the instance to avoid synchronization issues since this class will definitely be accessed

    /**
     * Returns the single available instance of the user repository
     */
    public static SignUpUserRepository getInstance() {
        if (instance == null) {
            instance = new SignUpUserRepository();
        }

        return instance;
    }


    //Getters
    public MutableLiveData<BackendlessUser> getSignUpUserResponse() {
        return signUpUserResponse;
    }

    public LiveData<Boolean> getSignUpResult() {

        return signUpResult;
    }

    public MutableLiveData<Boolean> getSignInResult() {
        return signInResult;
    }

    public MutableLiveData<BackendlessUser> getSignInUserResponse() {
        return signInUserResponse;
    }









    //USER OPERATIONS






    /**
     * This method is used with the sign up form.
     * accepts both a backendless user object and a submittedUserObject model.
     * the backendless user object is sent to the server to be added to the users table while
     * the submitted user object can be used to store extra details collected from the user that may not be
     * accepted by the backendless table but may be useful during registration.
     * for example user preferences and settings
     * for example if the user wants to stay signed in or if they want to receive promotional emails
     */
    public void createNewUser(BackendlessUser userObject, final SubmittedUserObject thisSubmittedUserObject) {

        //Todo sign user in


        if (userObject != null) {

            Backendless.UserService.register(userObject, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {

                    if (response != null) {
                        signUpResult.setValue(true);
                        signUpUserResponse.setValue(response);
                        Log.d("MyLogsUser", "user registered successfully. token: " + response.toString());
                        logUserIn(thisSubmittedUserObject);

                    } else {

                        signUpResult.setValue(false);

                    }


                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    signUpResult.setValue(false);

                    if (fault != null) {
                        Log.d("MyLogsUser", "user registration failed. error: " + fault.toString());
                    }


                }
            });


        }


    }






    /**
     * this method logs a user in manually using details submitted by the user.
     * Extra details can be supported by adding them to the submitted user object model and passing them
     * in the constructor
     * call this when user is logging in using a form
     */
    public void logUserIn(SubmittedUserObject thisSubmittedUserObject) {

        Backendless.UserService.login(thisSubmittedUserObject.geteMail(), thisSubmittedUserObject.getPassword(), new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {

                if (response != null) {

                    signInUserResponse.setValue(response);
                    signInResult.setValue(true);
                    Log.d("MyLogsUser", "Sign up activity: user logged in successfully. token: " + response.toString());

                }


            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault != null) {

                    Log.d("MyLogsUser", "Sign up activity: user sign in failed. error: " + fault.toString());

                }

            }
        }, thisSubmittedUserObject.getStayLoggedIn());


    }












}
