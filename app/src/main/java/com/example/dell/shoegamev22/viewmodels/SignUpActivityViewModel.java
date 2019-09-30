package com.example.dell.shoegamev22.viewmodels;

import com.backendless.BackendlessUser;
import com.example.dell.shoegamev22.models.SubmittedUserObject;
import com.example.dell.shoegamev22.repositories.SignUpUserRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SignUpActivityViewModel extends ViewModel {


    private SignUpUserRepository mUserRepository;


    public void init() {

        mUserRepository = SignUpUserRepository.getInstance();

    }






    //GETTERS
    public LiveData<Boolean> getSignUpResult(){
        return mUserRepository.getSignUpResult();

    }

    public LiveData<BackendlessUser> getNewlyRegisteredUser(){

        return mUserRepository.getSignUpUserResponse();

    }

    public LiveData<BackendlessUser> getNewlySignedInUser(){

        return mUserRepository.getSignInUserResponse();
    }

    public LiveData<Boolean> getNewSignInResult(){

        return mUserRepository.getSignInResult();

    }






    //USER OPERATIONS
    public void registerNewUser(BackendlessUser userObject, SubmittedUserObject thisSubmittedUserObject) {

        mUserRepository.createNewUser(userObject,thisSubmittedUserObject);

    }


    public void logUserIn(SubmittedUserObject thisSubmittedUserObject){

        mUserRepository.logUserIn(thisSubmittedUserObject);

    }








}
