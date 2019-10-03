package com.example.dell.shoegamev22.repositories;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;

public class BrowseRecommendationsRepository {

    //LIVE DATA
    private MutableLiveData<Boolean> isValidLoginCheckResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isValidLoginCheckResponse = new MutableLiveData<>();
    public MutableLiveData<List<Map>> bestDealsShoesResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> bestDealsRequestResult = new MutableLiveData<>();


    private static BrowseRecommendationsRepository instance;

    private BrowseRecommendationsRepository() {
    }


    public static BrowseRecommendationsRepository getInstance() {
        if (instance == null) {
            instance = new BrowseRecommendationsRepository();
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
                    Log.d("MyLogsUser", "Browse recommendations repo: login validity checked successfully. response: " + response.toString());

                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault != null) {
                    Log.d("MyLogsUser", "Browse recommendations repo: failed to check current user login validity. error: " + fault.toString());
                }

                isValidLoginCheckResult.setValue(false);

            }
        });


    }


    /**
     * gets the best deals items depending whichever where clause parameters you want
     */

    public void requestBestDeals(String whereClause, String sortBy) {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setPageSize(10);
        queryBuilder.setSortBy(sortBy);


        Backendless.Data.of("shoe").find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {

                if (response != null) {

                    bestDealsShoesResponse.setValue(response);
                    bestDealsRequestResult.setValue(true);
                    Log.d("MyLogsBestDeal", "Browse recommendations repo: best deals retrieved successfully. response: " + response.toString());

                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault != null) {

                    Log.d("MyLogsBestDeal", "Browse recommendations repo: best deals retrieval failed. error: " + fault.toString());

                }

                bestDealsRequestResult.setValue(false);

            }
        });


    }








    /**
     * gets the best deals items depending whichever where clause parameters you want
     */

    public void requestBestDeals2(DataQueryBuilder queryBuilder) {

        Backendless.Data.of("shoe").find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {

                if (response != null) {

                    bestDealsShoesResponse.setValue(response);
                    bestDealsRequestResult.setValue(true);
                    Log.d("MyLogsBestDeal", "Browse recommendations repo: best deals retrieved successfully. response: " + response.toString());

                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (fault != null) {

                    Log.d("MyLogsBestDeal", "Browse recommendations repo: best deals retrieval failed. error: " + fault.toString());

                }

                bestDealsRequestResult.setValue(false);

            }
        });


    }

    // Called reconfigure on a bitmap that is in use! This may cause graphical corruption









}
