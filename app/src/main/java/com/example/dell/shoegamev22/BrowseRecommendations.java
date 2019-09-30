package com.example.dell.shoegamev22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.backendless.BackendlessUser;
import com.example.dell.shoegamev22.adapters.BrowseRecommendationsActivityRecViewAdapter;
import com.example.dell.shoegamev22.adapters.HomeFragmentRecommendationsAdapter;
import com.example.dell.shoegamev22.viewmodels.BrowseRecommendationsViewModel;
import com.example.dell.shoegamev22.viewmodels.HomeFragmentViewModel;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.novoda.merlin.MerlinsBeard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowseRecommendations extends AppCompatActivity {

    //views
    RecyclerView mainRecView;

    //Fast Adapter
    //create our FastAdapter which will manage everything
    private ItemAdapter<BrowseRecommendationsActivityRecViewAdapter> itemAdapter;
    private FastAdapter<BrowseRecommendationsActivityRecViewAdapter> mainFastAdapter;

    //View models
    private BrowseRecommendationsViewModel browseRecommendationsViewModel;


    //Current User
    private BackendlessUser currentGlobalUser = new BackendlessUser();
    private Integer globalGenderInt = 1;

    //merlin
    private MerlinsBeard merlinsBeard;

    //passed in parameters
    String passedInWhereClause, passedInSortByClause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recommendations);


        //receive intent
        Intent intent = getIntent();
        passedInSortByClause = intent.getStringExtra("currentSortByClause");
        passedInWhereClause = intent.getStringExtra("currentWhereClause");


        //create merlin. library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(this);


        //initialize view models
        browseRecommendationsViewModel = ViewModelProviders.of(this).get(BrowseRecommendationsViewModel.class);
        browseRecommendationsViewModel.init();


        //initialize recycler view
        mainRecView = findViewById(R.id.browseRecommendationsActivityMainRecView);
        mainRecView.setHasFixedSize(true);
        mainRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        //initialize our FastAdapter which will manage everything
        itemAdapter = new ItemAdapter<>();
        mainFastAdapter = FastAdapter.with(itemAdapter);


        //add observers
        addObservers();



        if (merlinsBeard.isConnected()) {


            if (passedInWhereClause != null && passedInSortByClause != null) {

                browseRecommendationsViewModel.requestBestDeals(passedInWhereClause, passedInSortByClause);

            } else {


                //todo use generic where clause and sortby clause
                String thisSortBy = "created%20desc";
                String whereClause = "categoryNumber = '2'";  //request best sellers
                browseRecommendationsViewModel.requestBestDeals(whereClause, thisSortBy);


            }


        } else {


            //todo display error animation


        }


    }


    private void addObservers() {


        browseRecommendationsViewModel.getBestDealsRequestResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {


                if (aBoolean) {

                    //todo the request is succesfull

                    browseRecommendationsViewModel.getBestDealsResponse().observe(BrowseRecommendations.this, new Observer<List<Map>>() {
                        @Override
                        public void onChanged(List<Map> maps) {


                            if (maps != null) {

                                List<BrowseRecommendationsActivityRecViewAdapter> mData = new ArrayList<>();
                                mData = createShoeObjects(maps);

                                mainRecView.setAdapter(mainFastAdapter);
                                itemAdapter.clear();
                                itemAdapter.add(mData);


                            }


                        }
                    });


                } else {

                    //todo the request is UNsuccesfull


                }


            }
        });


    }


    private List<BrowseRecommendationsActivityRecViewAdapter> createShoeObjects(List<Map> shoeObjectMaps) {

        List<BrowseRecommendationsActivityRecViewAdapter> shoeObjectData = new ArrayList<>();

        if (shoeObjectMaps != null) {

            Integer count = shoeObjectMaps.size() - 1;

            for (int i = 0; i <= count; i++) {

                BrowseRecommendationsActivityRecViewAdapter holder = new BrowseRecommendationsActivityRecViewAdapter();
                holder.setMainImage((String) shoeObjectMaps.get(i).get("mainImage"));
                holder.setPrice((String) shoeObjectMaps.get(i).get("price"));
                holder.setTitle((String) shoeObjectMaps.get(i).get("title"));

                shoeObjectData.add(holder);


            }


        } else {

            Log.d("MyLogsShoeGame", "Browse recommendations: createShoeObjects method. passed in shoeObjectMap = null");

        }


        return shoeObjectData;

    }


}






/*
            <androidx.constraintlayout.widget.ConstraintLayout

                android:layout_width="match_parent"
                android:layout_height="wrap_content">


                <com.airbnb.lottie.LottieAnimationView
                    android:id="@+id/activitySignInLoadingView"
                    android:layout_width="match_parent"
                    android:layout_height="70dp"
                    android:visibility="gone"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintTop_toTopOf="parent"
                    app:lottie_autoPlay="true"
                    app:lottie_loop="true"
                    app:lottie_rawRes="@raw/refresh" />










            </androidx.constraintlayout.widget.ConstraintLayout>









                <ImageView
                android:layout_width="match_parent"
                android:layout_height="80dp"
                android:background="@drawable/rounded_rect_top_radius" />
*/