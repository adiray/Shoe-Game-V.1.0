package com.example.dell.shoegamev22.viewmodels;

import com.backendless.BackendlessUser;
import com.example.dell.shoegamev22.models.SubmittedUserObject;
import com.example.dell.shoegamev22.repositories.LogInRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class LogInActivityViewModel extends ViewModel {


    private LogInRepository mUserRepository;

    public void init() {

        mUserRepository = LogInRepository.getInstance();

    }





    //GETTERS


    public LiveData<BackendlessUser> getNewlySignedInUser() {

        return mUserRepository.getSignInUserResponse();
    }


    public LiveData<Boolean> getNewSignInResult() {

        return mUserRepository.getSignInResult();

    }




    //USER OPERATIONS


    public void logUserIn(SubmittedUserObject thisSubmittedUserObject) {

        mUserRepository.logUserIn(thisSubmittedUserObject);

    }


}
