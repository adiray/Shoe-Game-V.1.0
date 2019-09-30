package com.example.dell.shoegamev22.repositories;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.dell.shoegamev22.models.SubmittedUserObject;

import androidx.lifecycle.MutableLiveData;

public class LogInRepository {

    //VARIABLES
    private static LogInRepository instance;

    //LIVE DATA
    private MutableLiveData<Boolean> signInResult = new MutableLiveData<>();
    private MutableLiveData<BackendlessUser> signInUserResponse = new MutableLiveData<>();


    //CONSTRUCTOR
    private LogInRepository() {
    }

    //suggest that you eagerly create the instance to avoid synchronization issues since this class will definitely be accessed

    /**
     * Returns the single available instance of the user repository
     */
    public static LogInRepository getInstance() {
        if (instance == null) {
            instance = new LogInRepository();
        }

        return instance;
    }


    //Getters

    public MutableLiveData<Boolean> getSignInResult() {
        return signInResult;
    }

    public MutableLiveData<BackendlessUser> getSignInUserResponse() {
        return signInUserResponse;
    }


    //USER OPERATIONS

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
                    Log.d("MyLogsUser", "user signed in successfully. token: " + response.toString());

                }


            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault != null) {

                    Log.d("MyLogsUser", "user sign in failed. error: " + fault.toString());

                }

            }
        }, thisSubmittedUserObject.getStayLoggedIn());


    }


}
