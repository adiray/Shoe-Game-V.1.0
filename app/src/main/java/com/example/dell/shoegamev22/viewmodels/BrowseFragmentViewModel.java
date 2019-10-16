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




    public void getAllCategories(DataQueryBuilder queryBuilder){


        browseFragmentRepository.requestCategories(queryBuilder);


    }





    public void getAllTags(DataQueryBuilder queryBuilder){


        browseFragmentRepository.requestTags(queryBuilder);


    }



    public void getShoes(DataQueryBuilder queryBuilder){


        browseFragmentRepository.requestShoes(queryBuilder);


    }









    public LiveData<List<Map>> getCategoriesRequestResponse(){


        return browseFragmentRepository.categoriesRequestResponse;
    }


    public LiveData<Boolean> getCategoriesRequestResult(){

        return browseFragmentRepository.categoriesRequestResult;
    }


    public LiveData<List<Map>> getTagsRequestResponse(){


        return browseFragmentRepository.tagsRequestResponse;
    }


    public LiveData<Boolean> getTagsRequestResult(){

        return browseFragmentRepository.tagsRequestResult;
    }



    public LiveData<List<Map>> getShoesRequestResponse(){


        return browseFragmentRepository.shoesRequestResponse;
    }


    public LiveData<Boolean> getShoesRequestResult(){

        return browseFragmentRepository.shoesRequestResult;
    }








}
