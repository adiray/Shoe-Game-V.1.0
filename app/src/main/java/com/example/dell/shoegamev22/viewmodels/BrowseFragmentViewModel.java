package com.example.dell.shoegamev22.viewmodels;

import com.backendless.persistence.DataQueryBuilder;
import com.example.dell.shoegamev22.repositories.BrowseFragmentRepository;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BrowseFragmentViewModel extends ViewModel {

    private BrowseFragmentRepository browseFragmentRepository;

    public void init(){

        browseFragmentRepository = BrowseFragmentRepository.getInstance();


    }






    public void getAllTags(DataQueryBuilder queryBuilder){


        browseFragmentRepository.requestTags(queryBuilder);


    }



    public LiveData<List<Map>> getTagsRequestResponse(){


        return browseFragmentRepository.tagsRequestResponse;
    }


    public LiveData<Boolean> getTagsRequestResult(){

        return browseFragmentRepository.tagsRequestResult;
    }








}
