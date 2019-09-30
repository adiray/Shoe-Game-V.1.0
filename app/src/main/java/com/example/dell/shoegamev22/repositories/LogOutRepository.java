package com.example.dell.shoegamev22.repositories;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import androidx.lifecycle.MutableLiveData;

public class LogOutRepository {


    private static LogOutRepository instance;


    //LIVE DATA
    private MutableLiveData<Boolean> logUserOutResult = new MutableLiveData<>();

    private LogOutRepository() {
    }


    public static LogOutRepository getInstance() {

        if (instance == null) {

            instance = new LogOutRepository();

        }

        return instance;

    }







    //GETTERS
    public MutableLiveData<Boolean> getLogUserOutResult() {
        return logUserOutResult;
    }





    //USER OPERATIONS


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
