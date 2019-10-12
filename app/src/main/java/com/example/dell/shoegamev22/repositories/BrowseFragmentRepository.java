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


    private static BrowseFragmentRepository instance;

    private BrowseFragmentRepository() {
    }


    public static BrowseFragmentRepository getInstance() {
        if (instance == null) {
            instance = new BrowseFragmentRepository();
        }
        return instance;
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


}
