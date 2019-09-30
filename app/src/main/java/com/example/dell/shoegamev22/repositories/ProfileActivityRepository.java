package com.example.dell.shoegamev22.repositories;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import androidx.lifecycle.MutableLiveData;

public class ProfileActivityRepository {


    private static ProfileActivityRepository instance;


    //LIVE DATA
    private MutableLiveData<Boolean> isValidLoginCheckResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isValidLoginCheckResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> retrieveCurrentUserFromTheInternetResult = new MutableLiveData<>();
    private MutableLiveData<BackendlessUser> retrieveCurrentLoggedInUserResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> logUserOutResult = new MutableLiveData<>();



    private ProfileActivityRepository() {
    }


    public static ProfileActivityRepository getInstance() {

        if (instance == null) {

            instance = new ProfileActivityRepository();

        }

        return instance;

    }


    //GETTERS


    public MutableLiveData<Boolean> getIsValidLoginCheckResult() {
        return isValidLoginCheckResult;
    }

    public MutableLiveData<Boolean> getIsValidLoginCheckResponse() {
        return isValidLoginCheckResponse;
    }

    public MutableLiveData<Boolean> getRetrieveCurrentUserFromTheInternetResult() {
        return retrieveCurrentUserFromTheInternetResult;
    }

    public MutableLiveData<BackendlessUser> getRetrieveCurrentLoggedInUserResponse() {
        return retrieveCurrentLoggedInUserResponse;
    }

    public MutableLiveData<Boolean> getLogUserOutResult() {
        return logUserOutResult;
    }


    //USER OPERATIONS


    /**
     * Checks if the current users' session is still valid and if the user has a valid token.
     * If the session is valid, it will update the isValidLoginCheckResponse to 'true' otherwise false.
     * If the request is successful, will update isValidLoginCheckResult to true, if there is an error it will update
     * isValidLoginCheckResult to 'false'
     **/
    public void checkIfUserLoginIsValid() {

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {

                if (response != null) {

                    isValidLoginCheckResult.setValue(true);
                    isValidLoginCheckResponse.setValue(response);
                    Log.d("MyLogsUser", "login validity checked successfully. response: " + response.toString());

                }


            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault != null) {
                    Log.d("MyLogsUser", "failed to check current user login validity. error: " + fault.toString());
                }

                isValidLoginCheckResult.setValue(false);

            }
        });


    }


    /**
     * gets the current logged in user from the internet
     * Must only be called after checkIfUserLoginIsValid has been called and there is a valid login
     */
    public void retrieveCurrentUserFromTheInternet() {

        String currentUserId = Backendless.UserService.loggedInUser();

        if (currentUserId != null) {
            Backendless.UserService.findById(currentUserId, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {

                    if (response != null) {

                        retrieveCurrentUserFromTheInternetResult.setValue(true);
                        retrieveCurrentLoggedInUserResponse.setValue(response);
                        Log.d("MyLogsUser", "current logged in user retrieved from internet. User details: " + response.toString());

                    }


                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    retrieveCurrentUserFromTheInternetResult.setValue(false);
                    Log.d("MyLogsUser", "failed to retrieve current logged in user from internet. error: " + fault.toString());

                }
            });
        }

    }






    /**
     * LOGS OUT THE CURRENT USER, UPDATES THE LOGOUT USER RESULT LIVE DATA WHEN THE RESPONSE RETURNS
     * */

    public void logUserOut(){

        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {

                logUserOutResult.setValue(true);
                Log.d("MyLogsUser", "Profile fragment: current user LOGGED OUT from internet.");



            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault!=null){

                    Log.d("MyLogsUser", "Profile fragment: failed to logout current logged in user from internet. error: " + fault.toString());


                }

                logUserOutResult.setValue(false);
            }
        });



    }




}
