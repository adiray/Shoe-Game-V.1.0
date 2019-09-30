package com.example.dell.shoegamev22.repositories;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;

public class BestDealsRepository {

    //LIVE DATA
    private MutableLiveData<Boolean> isValidLoginCheckResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isValidLoginCheckResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> retrieveCurrentUserFromTheInternetResult = new MutableLiveData<>();
    private MutableLiveData<BackendlessUser> retrieveCurrentLoggedInUserResponse = new MutableLiveData<>();
    public MutableLiveData<List<Map>> bestDealsShoesResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> bestDealsRequestResult = new MutableLiveData<>();

    private static BestDealsRepository instance;

    private BestDealsRepository() {
    }

    public static BestDealsRepository getInstance() {
        if (instance == null) {
            instance = new BestDealsRepository();
        }
        return instance;
    }


    //USER OPERATIONS


    //check for a valid user session

    /**
     * Checks if the current users' session is still valid and if the user has a valid token.
     * If the session is valid, it will update the isValidLoginCheckResponse to 'true' otherwise false.
     * If the request is successful, will update isValidLoginCheckResult to true, if there is an error it will update
     * isValidLoginCheckResult to 'false'
     **/
    public void checkIfUserLoginIsValid() {

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {

                if (response != null) {

                    isValidLoginCheckResult.setValue(true);
                    isValidLoginCheckResponse.setValue(response);
                    Log.d("MyLogsUser", "Best deals repo: login validity checked successfully. response: " + response.toString());

                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault != null) {
                    Log.d("MyLogsUser", "Best deals repo: failed to check current user login validity. error: " + fault.toString());
                }

                isValidLoginCheckResult.setValue(false);

            }
        });


    }


    //retrieve current user from internet

    /**
     * gets the current logged in user from the internet
     * Must only be called after checkIfUserLoginIsValid has been called and there is a valid login
     */
    public void retrieveCurrentUserFromTheInternet() {

        String currentUserId = Backendless.UserService.loggedInUser();

        if (currentUserId != null) {
            Backendless.UserService.findById(currentUserId, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {

                    if (response != null) {

                        retrieveCurrentUserFromTheInternetResult.setValue(true);
                        retrieveCurrentLoggedInUserResponse.setValue(response);
                        Log.d("MyLogsUser", "Best deals repo: current logged in user retrieved from internet. User details: " + response.toString());

                    }


                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    retrieveCurrentUserFromTheInternetResult.setValue(false);
                    Log.d("MyLogsUser", "Best deals repo: failed to retrieve current logged in user from internet. error: " + fault.toString());

                }
            });
        }

    }



    /**
     * gets the best deals items depending whichever where clause parameters you want
     * */

    public void requestBestDeals(String whereClause, String sortBy) {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setPageSize(4);
        queryBuilder.setSortBy(sortBy);


        Backendless.Data.of("shoe").find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {

                if (response != null) {

                    bestDealsShoesResponse.setValue(response);
                    bestDealsRequestResult.setValue(true);
                    Log.d("MyLogsBestDeal", "best deals retrieved successfully. response: " + response.toString());

                }

//https://api.backendless.com/05DBC061-3DE1-0252-FF3C-FBCECC684700/25314E66-30EB-BF2D-FF4B-31B310A6FD00/data/
// shoe?where=tags%20%3D%20'EF76CBF7-09E0-75D3-FF15-1072F7C51E00'&sortBy=price%20asc

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault != null) {

                    Log.d("MyLogsBestDeal", "best deals retrieval failed. error: " + fault.toString());

                }

                bestDealsRequestResult.setValue(false);

            }
        });


    }


}
