package com.example.dell.shoegamev22.viewmodels;

import com.backendless.BackendlessUser;
import com.example.dell.shoegamev22.repositories.ProfileActivityRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileActivityViewModel extends ViewModel {


    private ProfileActivityRepository mProfileActivityRepository;


    public void init() {

        mProfileActivityRepository = ProfileActivityRepository.getInstance();


    }


    //GETTERS

    public LiveData<Boolean> getIsValidLoginCheckResult() {

        return mProfileActivityRepository.getIsValidLoginCheckResult();
    }

    public LiveData<Boolean> getIsValidLoginCheckResponse() {
        return mProfileActivityRepository.getIsValidLoginCheckResponse();
    }

    public LiveData<Boolean> getRetrieveCurrentUserFromTheInternetResult() {
        return mProfileActivityRepository.getRetrieveCurrentUserFromTheInternetResult();
    }

    public LiveData<BackendlessUser> getRetrieveCurrentUserFromTheInternetResponse() {
        return mProfileActivityRepository.getRetrieveCurrentLoggedInUserResponse();
    }


    public LiveData<Boolean> getLogUserOutResult() {
        return mProfileActivityRepository.getLogUserOutResult();
    }


    //USER OPERATIONS

    public void checkIfUserLoginIsValid() {
        mProfileActivityRepository.checkIfUserLoginIsValid();
    }

    public void retrieveCurrentUserFromTheInternet() {
        mProfileActivityRepository.retrieveCurrentUserFromTheInternet();
    }

    public void logUserOut() {

        mProfileActivityRepository.logUserOut();
    }


}
