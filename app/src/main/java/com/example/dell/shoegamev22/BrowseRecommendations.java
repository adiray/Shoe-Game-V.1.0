package com.example.dell.shoegamev22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.backendless.BackendlessUser;
import com.backendless.persistence.DataQueryBuilder;
import com.example.dell.shoegamev22.adapters.BrowseRecommendationsActivityRecViewAdapter;
import com.example.dell.shoegamev22.adapters.HomeFragmentRecommendationsAdapter;
import com.example.dell.shoegamev22.viewmodels.BrowseRecommendationsViewModel;
import com.example.dell.shoegamev22.viewmodels.HomeFragmentViewModel;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.novoda.merlin.MerlinsBeard;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowseRecommendations extends AppCompatActivity {

    //views
    RecyclerView mainRecView;
    TextView browseRecommendationsHeadlineTv;
    SwipeRefreshLayout mainRecViewSwipeRefreshLayout;
    LottieAnimationView browseRecommendationsActivityLoadingView;
    FloatingTextButton browseRecommendationsActivityLoadMoreButton;
    TextView browseRecommendationsActivityOptionsTV;


    //Booleans
    Boolean isOnLoadMore = false, isRefreshing = false, isNormalLoad = true;

    //Fast Adapter
    //create our FastAdapter which will manage everything
    private ItemAdapter<BrowseRecommendationsActivityRecViewAdapter> itemAdapter;
    private FastAdapter<BrowseRecommendationsActivityRecViewAdapter> mainFastAdapter;


    //View models
    private BrowseRecommendationsViewModel browseRecommendationsViewModel;


    //Backendless
    DataQueryBuilder queryBuilder;
    //Current User
    private BackendlessUser currentGlobalUser = new BackendlessUser();
    private Integer globalGenderInt = 1;

    //merlin
    private MerlinsBeard merlinsBeard;

    //passed in parameters
    String passedInWhereClause, passedInSortByClause, currentRecommendationsCategory;
    //current parameters
    String currentSortByClause, currentWhereClause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recommendations);


        //receive intent
        final Intent intent = getIntent();
        passedInSortByClause = intent.getStringExtra("currentSortByClause");
        currentSortByClause = passedInSortByClause;
        passedInWhereClause = intent.getStringExtra("currentWhereClause");
        currentWhereClause = passedInWhereClause;
        currentRecommendationsCategory = intent.getStringExtra("currentRecommendationsCategory");

        //create merlin. library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(this);

        //initialize query builder
        queryBuilder = DataQueryBuilder.create();


        //initialize views
        browseRecommendationsHeadlineTv = findViewById(R.id.browseRecommendationsActivityHeadlineTV);
        mainRecViewSwipeRefreshLayout = findViewById(R.id.browseRecommendationsActivityMainRecViewRefreshLayout);
        browseRecommendationsActivityLoadingView = findViewById(R.id.browseRecommendationsActivityLoadingView);
        browseRecommendationsActivityLoadMoreButton = findViewById(R.id.browseRecommendationsActivityLoadMoreButton);
        browseRecommendationsActivityOptionsTV =findViewById(R.id.browseRecommendationsActivityOptionsTV);

        //add text to the headline
        if (currentRecommendationsCategory != null) {
            browseRecommendationsHeadlineTv.setText(currentRecommendationsCategory);
        } else {
            browseRecommendationsHeadlineTv.setText("Recommendations");
        }


        //add an on refresh listener to on refresh view
        mainRecViewSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                onRefreshClicked();


            }
        });


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


        //fill the query builder
        queryBuilder.setWhereClause(passedInWhereClause);
        queryBuilder.setSortBy(passedInSortByClause);
        queryBuilder.setPageSize(10);
        //make the request
        requestBestDeals2(queryBuilder);
        //requestBestDeals(passedInWhereClause,passedInSortByClause);


        mainRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    Log.d("MyLogs", "Recycler view has reached end. count:");
                    browseRecommendationsActivityLoadMoreButton.setVisibility(View.VISIBLE);

                }
            }
        });


        browseRecommendationsActivityLoadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onLoadMoreClicked();

            }
        });


        browseRecommendationsActivityOptionsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup();

            }
        });







        mainFastAdapter.withSelectable(true);
        mainFastAdapter.withOnClickListener(new OnClickListener<BrowseRecommendationsActivityRecViewAdapter>() {
            @Override
            public boolean onClick( View v, IAdapter<BrowseRecommendationsActivityRecViewAdapter> adapter, BrowseRecommendationsActivityRecViewAdapter item, int position) {


                Intent intent1 = new Intent(BrowseRecommendations.this,ViewShoeDetails.class);
                intent1.putExtra("currentShoeId",item.getItemId());
                startActivity(intent1);




                return true;
            }
        });











    }


    void onLoadMoreClicked() {

        //fill the query builder
        isOnLoadMore = true;
        queryBuilder.prepareNextPage();
        requestBestDeals2(queryBuilder);
        browseRecommendationsActivityLoadMoreButton.setVisibility(View.INVISIBLE);

    }


    void onRefreshClicked() {

        isRefreshing=true;

        //fill the query builder
        queryBuilder.setWhereClause(currentWhereClause);
        queryBuilder.setSortBy(currentSortByClause);
        queryBuilder.setOffset(0);
        queryBuilder.setPageSize(10);

        requestBestDeals2(queryBuilder);


    }


    private void requestBestDeals(String whereClause, String sortByClause) {


        if (merlinsBeard.isConnected()) {


            if (whereClause != null && sortByClause != null) {

                browseRecommendationsViewModel.requestBestDeals(whereClause, sortByClause);

            } else {


                //todo use generic where clause and sortby clause
                String genericSortBy = "created%20desc";
                String genericWhereClause = "categoryNumber = '2'";  //request best sellers
                browseRecommendationsViewModel.requestBestDeals(genericWhereClause, genericSortBy);


            }


        } else {


            //todo display error animation


        }


    }


    private void requestBestDeals2(DataQueryBuilder queryBuilder) {


        if (merlinsBeard.isConnected()) {


            if (queryBuilder != null) {

                browseRecommendationsActivityLoadingView.setVisibility(View.VISIBLE);
                browseRecommendationsActivityLoadingView.playAnimation();
                browseRecommendationsViewModel.requestBestDeals2(queryBuilder);

            } else {


                //todo use generic where clause and sortby clause
                String genericSortBy = "created%20desc";
                String genericWhereClause = "categoryNumber = '2'";  //request best sellers


                queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(genericWhereClause);
                queryBuilder.setPageSize(10);
                queryBuilder.setOffset(0);
                queryBuilder.setSortBy(genericSortBy);

                browseRecommendationsActivityLoadingView.setVisibility(View.VISIBLE);
                browseRecommendationsViewModel.requestBestDeals2(queryBuilder);


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

                                if(isOnLoadMore){

                                    browseRecommendationsActivityLoadingView.setVisibility(View.GONE);
                                    mainRecViewSwipeRefreshLayout.setRefreshing(false);
                                    isOnLoadMore = false;


                                    if(mData.size()>0){

                                        mainRecView.setAdapter(mainFastAdapter);
                                        itemAdapter.add(mData);

                                        isOnLoadMore = false;
                                        isRefreshing=false;
                                        isNormalLoad=false;


                                    }else{

                                        Toast.makeText(BrowseRecommendations.this,"No more items",Toast.LENGTH_LONG).show();

                                    }

                                }else if(isRefreshing){

                                    mainRecView.setAdapter(mainFastAdapter);
                                    itemAdapter.clear();
                                    itemAdapter.add(mData);
                                    browseRecommendationsActivityLoadingView.setVisibility(View.GONE);
                                    mainRecViewSwipeRefreshLayout.setRefreshing(false);
                                    isOnLoadMore = false;
                                    isRefreshing=false;
                                    isNormalLoad=false;
                                    browseRecommendationsActivityLoadMoreButton.setVisibility(View.INVISIBLE);


                                }else if(isNormalLoad){

                                    mainRecView.setAdapter(mainFastAdapter);
                                    itemAdapter.clear();
                                    itemAdapter.add(mData);
                                    browseRecommendationsActivityLoadingView.setVisibility(View.GONE);
                                    mainRecViewSwipeRefreshLayout.setRefreshing(false);
                                    isOnLoadMore = false;
                                    isRefreshing=false;
                                    isNormalLoad=false;

                                }





                            }else {


                                Toast.makeText(BrowseRecommendations.this,"No more items",Toast.LENGTH_LONG).show();




                            }


                        }
                    });


                } else {

                    //todo the request is UNsuccesfull
                    Toast.makeText(BrowseRecommendations.this,"Failed to load items",Toast.LENGTH_LONG).show();
                    browseRecommendationsActivityLoadingView.setVisibility(View.GONE);
                    mainRecViewSwipeRefreshLayout.setRefreshing(false);

                    isOnLoadMore = false;
                    isRefreshing=false;
                    isNormalLoad=false;

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
                holder.setItemId((String) shoeObjectMaps.get(i).get("objectId"));

                shoeObjectData.add(holder);


            }


        } else {

            Log.d("MyLogsShoeGame", "Browse recommendations: createShoeObjects method. passed in shoeObjectMap = null");

        }


        return shoeObjectData;

    }


    private void showPopup() {

        new XPopup.Builder(BrowseRecommendations.this).maxWidth(600).asCenterList("Filters", new String[]{"Latest", "Cheapest", "Best sellers"}, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {


                isNormalLoad= true;


                if (text.equals("Latest")) {

                    browseRecommendationsHeadlineTv.setText("Latest");
                    currentRecommendationsCategory = "Latest";

                    //todo use generic where clause and sortby clause
                    String genericSortBy = "created%20desc";
                    String genericWhereClause = "categoryNumber = '2'";  //request best sellers
                    currentSortByClause = genericSortBy;
                    currentWhereClause = genericSortBy;


                    queryBuilder = DataQueryBuilder.create();
                    queryBuilder.setWhereClause(genericWhereClause);
                    queryBuilder.setPageSize(10);
                    queryBuilder.setOffset(0);
                    queryBuilder.setSortBy(genericSortBy);
                    requestBestDeals2(queryBuilder);


                } else if (text.equals("Cheapest")) {

                    if (globalGenderInt != null) {

                        String whereClause = "gender = '" + globalGenderInt + "'";
                        String sortBy = "price%20asc";
                        queryBuilder = DataQueryBuilder.create();
                        queryBuilder.setWhereClause(whereClause);
                        queryBuilder.setPageSize(10);
                        queryBuilder.setOffset(0);
                        queryBuilder.setSortBy(sortBy);
                        requestBestDeals2(queryBuilder);
                        Log.d("MyLogsHomeFrag", "Home fragment: where clause from selection:" + globalGenderInt.toString() + " ,where clause: " + whereClause);
                        browseRecommendationsHeadlineTv.setText("Cheapest");
                        currentRecommendationsCategory = "Cheapest";
                        currentSortByClause = sortBy;
                        currentWhereClause = whereClause;


                    } else {

                        //todo gender int is null

                        String whereClause = "gender = '1'";
                        String sortBy = "price%20asc";
                        Log.d("MyLogsHomeFrag", "Home fragment: where clause from selection: global gender int is null" + " ,where clause: " + whereClause);
                        queryBuilder = DataQueryBuilder.create();
                        queryBuilder.setWhereClause(whereClause);
                        queryBuilder.setPageSize(10);
                        queryBuilder.setOffset(0);
                        queryBuilder.setSortBy(sortBy);
                        requestBestDeals2(queryBuilder);
                        browseRecommendationsHeadlineTv.setText("Cheapest");
                        currentRecommendationsCategory = "Cheapest";
                        currentSortByClause = sortBy;
                        currentWhereClause = whereClause;


                    }


                } else if (text.equals("Best sellers")) {


                    //https://api.backendless.com/05DBC061-3DE1-0252-FF3C-FBCECC684700/25314E66-30EB-BF2D-FF4B-31B310A6FD00/
                    // data/shoe?where=gender%20%3D%20'1'%20AND%20categoryNumber%20%3D%20'2'

                    String whereClause = "gender = '" + globalGenderInt + "' AND categoryNumber = '2'";
                    String sortBy = "created%20desc";
                    Log.d("MyLogsHomeFrag", "Home fragment: where clause from selection:" + globalGenderInt.toString() + " ,where clause: " + whereClause);
                    queryBuilder = DataQueryBuilder.create();
                    queryBuilder.setWhereClause(whereClause);
                    queryBuilder.setPageSize(10);
                    queryBuilder.setOffset(0);
                    queryBuilder.setSortBy(sortBy);
                    requestBestDeals2(queryBuilder);
                    browseRecommendationsHeadlineTv.setText("Best Sellers");
                    currentRecommendationsCategory = "Best Sellers";
                    currentSortByClause = sortBy;
                    currentWhereClause = whereClause;


                }


                //https://api.backendless.com/05DBC061-3DE1-0252-FF3C-FBCECC684700/25314E66-30EB-BF2D-FF4B-31B310A6FD00/data
                // /shoe?where=gender%20%3D%20'2'&sortBy=price%20asc


            }
        }).show();


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












/*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                Log.d("-----","end");

            }
        }
    });*/















