package com.example.dell.shoegamev22.viewmodels;

import com.example.dell.shoegamev22.repositories.BestDealsRepository;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class HomeFragmentViewModel extends ViewModel {


    private BestDealsRepository bestDealsRepository;

    public void init() {

        bestDealsRepository = BestDealsRepository.getInstance();
    }




    // ***********************************************************
    // BEST DEALS METHODS : use these to get the best deals
    // ***********************************************************



    public void requestBestDeals(String whereClause, String sortBy) {

        bestDealsRepository.requestBestDeals(whereClause,sortBy);
    }

    public LiveData<Boolean> getBestDealsRequestResult() {

        return bestDealsRepository.bestDealsRequestResult;
    }

    public LiveData<List<Map>> getBestDealsResponse() {

        return bestDealsRepository.bestDealsShoesResponse;
    }







}
