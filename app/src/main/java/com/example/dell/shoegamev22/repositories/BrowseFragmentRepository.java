package com.example.dell.shoegamev22.repositories;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.dell.shoegamev22.BrowseFragment;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;

public class BrowseFragmentRepository {


    //LIVE DATA
    public MutableLiveData<List<Map>> tagsRequestResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> tagsRequestResult = new MutableLiveData<>();
    public MutableLiveData<List<Map>> categoriesRequestResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> categoriesRequestResult = new MutableLiveData<>();
    public MutableLiveData<List<Map>> shoesRequestResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> shoesRequestResult = new MutableLiveData<>();


    private static BrowseFragmentRepository instance;

    private BrowseFragmentRepository() {
    }


    public static BrowseFragmentRepository getInstance() {
        if (instance == null) {
            instance = new BrowseFragmentRepository();
        }
        return instance;
    }


    /***
     *CATEGORIES ARE JUST TAGS OF THE TYPE 'CATEGORY' SO WE REQUEST THEM FROM THE TAGS TABLE WITH A WHERE CLAUSE THAT SPECIFIES TO RETURN THE TAGS WITH THE APPROPRIATE TYPE
     */

    public void requestCategories(DataQueryBuilder queryBuilder){


        Backendless.Data.of("tags").find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {

                if (!response.isEmpty()) {


                    categoriesRequestResult.setValue(true);
                    categoriesRequestResponse.setValue(response);
                    Log.d("MyLogsCategories", "Browse fragment repo: categories retrieved successfully. response: " + response.toString());


                } else {


                    categoriesRequestResult.setValue(false);
                    Log.d("MyLogsCategories", "Browse fragment repo: categories retrieved successfully but response is empty. response: " + response.toString());


                }


            }

            @Override
            public void handleFault(BackendlessFault fault) {


                categoriesRequestResult.setValue(false);

                if (fault != null) {

                    Log.d("MyLogsCategories", "Browse fragment repo: categories retrieval failed. error: " + fault.getMessage());

                }


            }
        });




    }





    public void requestTags(DataQueryBuilder queryBuilder) {


        Backendless.Data.of("tags").find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {

                if (!response.isEmpty()) {


                    tagsRequestResult.setValue(true);
                    tagsRequestResponse.setValue(response);
                    Log.d("MyLogsTags", "Browse fragment repo: tags retrieved successfully. response: " + response.toString());


                } else {


                    tagsRequestResult.setValue(false);
                    Log.d("MyLogsTags", "Browse fragment repo: tags retrieved successfully but response is empty. response: " + response.toString());


                }


            }

            @Override
            public void handleFault(BackendlessFault fault) {


                tagsRequestResult.setValue(false);

                if (fault != null) {

                    Log.d("MyLogsTags", "Browse fragment repo: tags retrieval failed. error: " + fault.getMessage());

                }


            }
        });


    }




    public void requestShoes(DataQueryBuilder queryBuilder){


        Backendless.Data.of("shoe").find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {

                if (response!=null) {


                    shoesRequestResult.setValue(true);
                    shoesRequestResponse.setValue(response);
                    Log.d("MyLogsShoes", "Browse fragment repo: shoes retrieved successfully. response: " + response.toString());


                } else {


                    tagsRequestResult.setValue(false);
                    Log.d("MyLogsShoes", "Browse fragment repo: shoes retrieved successfully but response is empty. response: " + response.toString());


                }


            }

            @Override
            public void handleFault(BackendlessFault fault) {


                tagsRequestResult.setValue(false);

                if (fault != null) {

                    Log.d("MyLogsShoes", "Browse fragment repo: shoes retrieval failed. error: " + fault.getMessage());

                }


            }
        });







    }


}
