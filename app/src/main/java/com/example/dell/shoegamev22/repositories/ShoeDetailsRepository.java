package com.example.dell.shoegamev22.repositories;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;

public class ShoeDetailsRepository {

    public MutableLiveData<List<Map>> shoeDetailsResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> shoeDetailsRequestResult = new MutableLiveData<>();



    private static ShoeDetailsRepository instance;

    private ShoeDetailsRepository() {
    }

    public static ShoeDetailsRepository getInstance() {
        if (instance == null) {
            instance = new ShoeDetailsRepository();
        }
        return instance;
    }











    //GETTERS
    public MutableLiveData<List<Map>> getShoeDetailsResponse() {
        return shoeDetailsResponse;
    }

    public MutableLiveData<Boolean> getShoeDetailsRequestResult() {
        return shoeDetailsRequestResult;
    }











    //SERVER OPERATIONS

    public void requestShoeDetails(DataQueryBuilder queryBuilder) {

        if (queryBuilder != null) {
            Backendless.Data.of("shoe").find(queryBuilder, new AsyncCallback<List<Map>>() {
                @Override
                public void handleResponse(List<Map> response) {

                    shoeDetailsRequestResult.setValue(true);

                    if(response!=null){

                        Log.d("MyLogsShoeRepo", "shoes retrieved successfully. response: " + response.toString());

                        shoeDetailsResponse.setValue(response);





                    }else {

                        Log.d("MyLogsShoeRepo", "shoes retrieved successfully. response: NULL");




                    }




                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    shoeDetailsRequestResult.setValue(false);

                    if(fault!=null){

                        Log.d("MyLogsShoeRepo", "shoes NOT retrieved successfully. error: " + fault.toString());


                    }


                }
            });
        }


    }


}
