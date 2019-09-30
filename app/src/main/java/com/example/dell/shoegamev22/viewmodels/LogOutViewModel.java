package com.example.dell.shoegamev22.viewmodels;

import com.example.dell.shoegamev22.repositories.LogOutRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class LogOutViewModel extends ViewModel {


    private LogOutRepository mLogOutRepository;


    public void init() {

        mLogOutRepository = LogOutRepository.getInstance();

    }


    //GETTERS


    public LiveData<Boolean> getLogUserOutResult() {
        return mLogOutRepository.getLogUserOutResult();
    }


    //USER OPERATIONS
    public void logUserOut() {
        mLogOutRepository.logUserOut();
    }


}
