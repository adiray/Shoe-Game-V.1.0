package com.example.dell.shoegamev22.viewmodels;


import com.example.dell.shoegamev22.repositories.BrowseRecommendationsRepository;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BrowseRecommendationsViewModel extends ViewModel {


    private BrowseRecommendationsRepository browseRecommendationsRepository;

    public void init() {

        browseRecommendationsRepository = BrowseRecommendationsRepository.getInstance();
    }


    // ***********************************************************
    // BEST DEALS METHODS : use these to get the best deals
    // ***********************************************************


    public void requestBestDeals(String whereClause, String sortBy) {

        browseRecommendationsRepository.requestBestDeals(whereClause, sortBy);
    }

    public LiveData<Boolean> getBestDealsRequestResult() {

        return browseRecommendationsRepository.bestDealsRequestResult;
    }

    public LiveData<List<Map>> getBestDealsResponse() {

        return browseRecommendationsRepository.bestDealsShoesResponse;
    }


}
