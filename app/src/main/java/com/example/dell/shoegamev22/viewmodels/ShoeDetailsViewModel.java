package com.example.dell.shoegamev22.viewmodels;

import com.backendless.persistence.DataQueryBuilder;
import com.example.dell.shoegamev22.repositories.BrowseRecommendationsRepository;
import com.example.dell.shoegamev22.repositories.ShoeDetailsRepository;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShoeDetailsViewModel extends ViewModel {


    private ShoeDetailsRepository shoeDetailsRepository;

    public void init() {

        shoeDetailsRepository = ShoeDetailsRepository.getInstance();
    }






    // ***********************************************************
    // GETTER METHODS : use these to get the RESPONSES
    // ***********************************************************



    public LiveData<List<Map>> getShoeDetailsResponse() {
        return shoeDetailsRepository.getShoeDetailsResponse();
    }

    public LiveData<Boolean> getShoeDetailsRequestResult() {
        return shoeDetailsRepository.getShoeDetailsRequestResult();
    }









    // ***********************************************************
    // SHOE METHODS : use these to get the shoe details
    // ***********************************************************


    public void getShoeDetails(DataQueryBuilder queryBuilder){

        shoeDetailsRepository.requestShoeDetails(queryBuilder);

    }




}
