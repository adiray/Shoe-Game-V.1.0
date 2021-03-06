package com.example.dell.shoegamev22;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.backendless.BackendlessUser;
import com.example.dell.shoegamev22.adapters.HomeFragmentRecommendationsAdapter;
import com.example.dell.shoegamev22.responseobjects.ShoeObject;
import com.example.dell.shoegamev22.viewmodels.HomeFragmentViewModel;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.novoda.merlin.MerlinsBeard;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import br.vince.easysave.EasySave;
import br.vince.easysave.LoadAsyncCallback;
import br.vince.easysave.SaveAsyncCallback;


public class HomeFragment extends Fragment {

    View view;

    //views
    private IndefinitePagerIndicator bestDealsRecyclerViewIndicator;
    private RecyclerView bestDealsRecyclerView;
    private LottieAnimationView bestDealsRecyclerViewLoadingView;
    ImageView womenCollectionImageView, menCollectionImageView, moreCollectionImageView, sportsShoesCollectionImageView, bestDealsRefreshView;

    ImageView bestDealsMoreOptionsButton, bestDealsShowAllButton, collectionsMenButton;
    TextView bestDealsHeaderTextView;

    //create our FastAdapter which will manage everything
    private ItemAdapter<HomeFragmentRecommendationsAdapter> bestDealsItemAdapter;
    private FastAdapter  < HomeFragmentRecommendationsAdapter> bestDealsFastAdapter;
    private FastAdapter bestDealsFastAdapter2;

    //View models
    private HomeFragmentViewModel mHomeFragmentViewModel;


    //Current User
    private BackendlessUser currentGlobalUser = new BackendlessUser();
    private Integer globalGenderInt = 1;

    //merlin
    private MerlinsBeard merlinsBeard;


    //booleans
    private Boolean bestDealsLoadFailed = false;


    //variables to be passed to browse recommendations activity
    String currentWhereClause, currentSortByClause , currentRecommendationsCategory;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);


        //initialize the views
        bestDealsRecyclerViewLoadingView = view.findViewById(R.id.homeFragmentBestDealsRecyclerViewLoadingView);
        bestDealsRecyclerViewIndicator = view.findViewById(R.id.homeFragmentBestDealsRecyclerViewIndicator);
        bestDealsRefreshView = view.findViewById(R.id.homeFragmentBestDealsReloadIcon);
        bestDealsMoreOptionsButton = view.findViewById(R.id.homeFragmentBestDealsMoreOptionsIcon);
        bestDealsHeaderTextView = view.findViewById(R.id.mainActivityBestDealsHeader);
        bestDealsShowAllButton = view.findViewById(R.id.homeFragmentBestDealsMoreIcon);
        collectionsMenButton = view.findViewById(R.id.homeFragmentCollectionsWomenIcon);
        bestDealsMoreOptionsButton.setEnabled(false);
        bestDealsShowAllButton.setEnabled(false);


        //create merlin. library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(getActivity());


        //initialize view models
        mHomeFragmentViewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        mHomeFragmentViewModel.init();

        //initialize recycler view
        bestDealsRecyclerView = view.findViewById(R.id.homeFragmentBestDealsRecyclerView);
        bestDealsRecyclerView.setHasFixedSize(true);
        bestDealsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        bestDealsRecyclerViewIndicator.attachToRecyclerView(bestDealsRecyclerView);
        //set snap helper to recycler view
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(bestDealsRecyclerView);
        //remove the indicator and the recycler view while the data request is made
        bestDealsRecyclerView.setVisibility(View.GONE);
        bestDealsRecyclerViewIndicator.setVisibility(View.GONE);

        //initialize our FastAdapter which will manage everything
        bestDealsItemAdapter = new ItemAdapter<>();
        bestDealsFastAdapter = FastAdapter.with(bestDealsItemAdapter);


        bestDealsRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshBestDealsButtonClicked();


            }
        });


        if (merlinsBeard.isConnected()) {

            bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_grey_inactive_24dp);
            bestDealsRefreshView.setEnabled(false);
            retrieveUserFromCache();


        } else {

            bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_grey_inactive_24dp);
            bestDealsRefreshView.setEnabled(false);
            retrieveBestDealsFromCache();


        }


        bestDealsMoreOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (merlinsBeard.isConnected()) {

                    showPopup();

                } else {


                    Toast.makeText(getActivity(), "We can't find the Internet connection", Toast.LENGTH_SHORT).show();


                }


            }
        });






        //add on click to recycler view objects
        bestDealsFastAdapter.withSelectable(true);
       bestDealsFastAdapter.withOnClickListener(new OnClickListener<HomeFragmentRecommendationsAdapter>() {
           @Override
           public boolean onClick( View v, IAdapter<HomeFragmentRecommendationsAdapter> adapter, HomeFragmentRecommendationsAdapter item, int position) {
               return false;
           }
       });

               /*gender: 1 = neutral, 2 = male, 3 = female
category number: 1 = none, 2 = best seller, 3 = recommended, 4 = discounted*/




               bestDealsShowAllButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {



                       //currentRecommendationsCategory = (String) bestDealsHeaderTextView.getText();
                       Intent intent = new Intent(getActivity(), BrowseRecommendations.class);
                       intent.putExtra("currentWhereClause",currentWhereClause);
                       intent.putExtra("currentSortByClause",currentSortByClause);
                       intent.putExtra("currentRecommendationsCategory",currentRecommendationsCategory);
                       startActivity(intent);




                   }
               });




               collectionsMenButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       Intent intent = new Intent(getActivity(), ViewShoeDetails.class);
                       startActivity(intent);




                   }
               });





        return view;
    }


    private void showPopup() {






        /*  new XPopup.Builder(getContext())
                            //.maxWidth(600)
                            .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    toast("click " + text);
                                }
                            })
                            .show();*/


        new XPopup.Builder(getContext()).maxWidth(600).asCenterList("Filters", new String[]{"Latest", "Cheapest", "Best sellers"}, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {

                bestDealsMoreOptionsButton.setEnabled(false);


                if (text.equals("Latest")) {


                    bestDealsHeaderTextView.setText("Latest");
                    currentRecommendationsCategory = "Latest";

                    requestInitialBestDeals(currentGlobalUser);


                } else if (text.equals("Cheapest")) {

                    if (globalGenderInt != null) {

                        String whereClause = "gender = '" + globalGenderInt + "'";
                        String sortBy = "price%20asc";
                        Log.d("MyLogsHomeFrag", "Home fragment: where clause from selection:" + globalGenderInt.toString() + " ,where clause: " + whereClause);
                        mHomeFragmentViewModel.requestBestDeals(whereClause, sortBy);
                        bestDealsHeaderTextView.setText("Cheapest");
                        currentRecommendationsCategory = "Cheapest";
                        currentSortByClause = sortBy;
                        currentWhereClause = whereClause;


                    } else {

                        //todo gender int is null

                        String whereClause = "gender = '1'";
                        String sortBy = "price%20asc";
                        Log.d("MyLogsHomeFrag", "Home fragment: where clause from selection: global gender int is null" + " ,where clause: " + whereClause);
                        mHomeFragmentViewModel.requestBestDeals(whereClause, sortBy);
                        bestDealsHeaderTextView.setText("Cheapest");
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
                    mHomeFragmentViewModel.requestBestDeals(whereClause, sortBy);
                    bestDealsHeaderTextView.setText("Best Sellers");
                    currentRecommendationsCategory = "Best Sellers";
                    currentSortByClause = sortBy;
                    currentWhereClause = whereClause;


                }


                //https://api.backendless.com/05DBC061-3DE1-0252-FF3C-FBCECC684700/25314E66-30EB-BF2D-FF4B-31B310A6FD00/data
                // /shoe?where=gender%20%3D%20'2'&sortBy=price%20asc


            }
        }).show();


    }


    private void refreshBestDealsButtonClicked() {


        bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_grey_inactive_24dp);
        bestDealsRefreshView.setEnabled(false);

        if (merlinsBeard.isConnected()) {

            bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_grey_inactive_24dp);
            bestDealsRefreshView.setEnabled(false);
            retrieveUserFromCache();
            bestDealsHeaderTextView.setText("Best deals");


        } else {

            bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_grey_inactive_24dp);
            bestDealsRefreshView.setEnabled(false);
            retrieveBestDealsFromCache();
            bestDealsHeaderTextView.setText("Best deals");


        }

    }


    private void checkUserValidity() {


    }


    private void retrieveUserFromCache() {


        new EasySave(getActivity()).retrieveModelAsync("current signed in user", BackendlessUser.class, new LoadAsyncCallback<BackendlessUser>() {
            @Override
            public void onComplete(BackendlessUser user) {

                if (user != null) {

                    requestInitialBestDeals(user);
                    currentGlobalUser = user;
                    Log.d("MyLogsHomeFrag", "Home fragment: User retrieved successfully from Cache. Response :" + user.toString());


                } else {

                    requestInitialBestDeals(null);
                    currentGlobalUser = null;
                    Log.d("MyLogsHomeFrag", "Home fragment: User retrieved successfully from Cache. response may be null . Response :NULL");


                }


            }

            @Override
            public void onError(String s) {

                Log.d("MyLogsHomeFrag", "Home fragment: User NOT retrieved from Cache. Response :" + s);
                requestInitialBestDeals(null);
                currentGlobalUser = null;


            }
        });


    }


    private void getBestDealItems(String whereClause) {

        String thisSortBy = "created%20desc";
        currentWhereClause = whereClause; //passed to the browse recommendations activity;
        currentSortByClause = thisSortBy;  //passed to the browse recommendations activity;
        mHomeFragmentViewModel.requestBestDeals(whereClause, thisSortBy);
        mHomeFragmentViewModel.getBestDealsRequestResult().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    mHomeFragmentViewModel.getBestDealsResponse().observe(getActivity(), new Observer<List<Map>>() {
                        @Override
                        public void onChanged(List<Map> maps) {

                            if (maps != null) {

                                List<HomeFragmentRecommendationsAdapter> mData = new ArrayList<>();
                                mData = createShoeObjects(maps);

                                bestDealsRecyclerView.setAdapter(bestDealsFastAdapter);
                                bestDealsItemAdapter.clear();
                                bestDealsItemAdapter.add(mData);
                                bestDealsRecyclerViewLoadingView.pauseAnimation();
                                bestDealsRecyclerViewLoadingView.setVisibility(View.GONE);
                                bestDealsRecyclerView.setVisibility(View.VISIBLE);
                                bestDealsRecyclerViewIndicator.setVisibility(View.VISIBLE);
                                bestDealsLoadFailed = false;
                                saveBestDealsToCache(mData);


                            }


                            bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_black_24dp);
                            bestDealsRefreshView.setEnabled(true);
                            bestDealsMoreOptionsButton.setEnabled(true);
                            bestDealsShowAllButton.setEnabled(true);


                        }
                    });
                } else {


                    bestDealsLoadFailed = true;
                    bestDealsRecyclerViewLoadingView.setAnimation(R.raw.shopping_bag_error);
                    bestDealsRecyclerViewLoadingView.playAnimation();
                    bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_black_24dp);
                    bestDealsRefreshView.setEnabled(true);
                    bestDealsMoreOptionsButton.setEnabled(true);
                    bestDealsShowAllButton.setEnabled(true);


                }


            }
        });


    }


    private List<HomeFragmentRecommendationsAdapter> createShoeObjects(List<Map> shoeObjectMaps) {

        List<HomeFragmentRecommendationsAdapter> shoeObjectData = new ArrayList<>();

        if (shoeObjectMaps != null) {

            Integer count = shoeObjectMaps.size() - 1;

            for (int i = 0; i <= count; i++) {

                HomeFragmentRecommendationsAdapter holder = new HomeFragmentRecommendationsAdapter();
                holder.setMainImage((String) shoeObjectMaps.get(i).get("mainImage"));
                holder.setPrice((String) shoeObjectMaps.get(i).get("price"));
                holder.setTitle((String) shoeObjectMaps.get(i).get("title"));

                shoeObjectData.add(holder);


            }


        } else {

            Log.d("MyLogsHomeFrag", "createShoeObjects method. passed in shoeObjectMap = null");

        }


        return shoeObjectData;

    }


    private void requestInitialBestDeals(BackendlessUser user) {

        Integer genderInt = 1;
        Double genderDouble = 1.0;

        if (user != null) {
            genderDouble = (Double) user.getProperty("gender");
            genderInt = genderDouble.intValue();
            globalGenderInt = genderInt;
            //2 = male, 3= female, 1= none

            if (genderInt != null) {

                if (genderInt == 1) {
                    //no gender
                    String whereClause = "gender = '1'";

                    getBestDealItems(whereClause);


                } else if (genderInt == 2) {

                    //men
                    String whereClause = "gender = '2'";
                    getBestDealItems(whereClause);


                } else if (genderInt == 3) {

                    //women
                    String whereClause = "gender = '3'";
                    getBestDealItems(whereClause);


                } else {

                    String whereClause = "gender = '1'";

                    getBestDealItems(whereClause);
                    Log.d("MyLogsHomeFrag", "failed to retrieve gender int, gender may be NULL");


                }


            } else {


                String whereClause = "gender = '1'";

                getBestDealItems(whereClause);
                Log.d("MyLogsHomeFrag", "failed to retrieve gender int, step two, User may be NULL");


            }


        } else {

            String whereClause = "gender = '1'";

            getBestDealItems(whereClause);

            Log.d("MyLogsHomeFrag", "The user is NULL");
        }


    }


    private void saveBestDealsToCache(List<HomeFragmentRecommendationsAdapter> bestDealsArray) {

        new EasySave(getActivity()).saveModelAsync("best deals", bestDealsArray, new SaveAsyncCallback<List<HomeFragmentRecommendationsAdapter>>() {
            @Override
            public void onComplete(List<HomeFragmentRecommendationsAdapter> shoeObjects) {

                if (shoeObjects != null) {

                    Log.d("MyLogsHomeFrag", "best deals saved successfully in Cache. Response :" + shoeObjects.toString());

                }


            }

            @Override
            public void onError(String s) {

                Log.d("MyLogsHomeFrag", "best deals NOT saved successfully in Cache. Error :" + s);


            }
        });


    }


    private void retrieveBestDealsFromCache() {

        List<HomeFragmentRecommendationsAdapter> bestDealsArray = new ArrayList<>();

        new EasySave(getActivity()).saveModelAsync("best deals", bestDealsArray, new SaveAsyncCallback<List<HomeFragmentRecommendationsAdapter>>() {
            @Override
            public void onComplete(List<HomeFragmentRecommendationsAdapter> shoeObjects) {

                if (shoeObjects != null) {

                    bestDealsRecyclerView.setAdapter(bestDealsFastAdapter);
                    bestDealsItemAdapter.add(shoeObjects);
                    bestDealsRecyclerViewLoadingView.pauseAnimation();
                    bestDealsRecyclerViewLoadingView.setVisibility(View.GONE);
                    bestDealsRecyclerView.setVisibility(View.VISIBLE);
                    bestDealsRecyclerViewIndicator.setVisibility(View.VISIBLE);
                    bestDealsLoadFailed = false;

                    Log.d("MyLogsHomeFrag", "best deals loaded successfully from Cache. Response :" + shoeObjects.toString());


                } else {

                    Log.d("MyLogsHomeFrag", "best deals loaded successfully from Cache. Response: NULL");


                }

                bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_black_24dp);
                bestDealsRefreshView.setEnabled(true);


            }

            @Override
            public void onError(String s) {

                Log.d("MyLogsHomeFrag", "best deals NOT loaded successfully from Cache. Error :" + s);

                bestDealsRefreshView.setImageResource(R.drawable.ic_refresh_black_24dp);
                bestDealsRefreshView.setEnabled(true);


            }
        });


    }


}


//todo best deals retrieved multiple times on refresh





























































































/*
                        <LinearLayout
                            android:id="@+id/shoeDetailsActivityActionsWishListLinearLayout"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginStart="32dp"
                            android:layout_marginTop="24dp"
                            android:gravity="center"
                            android:orientation="vertical"
                            app:layout_constraintEnd_toStartOf="@+id/shoeDetailsActivityActionsShareLinearLayout"
                            app:layout_constraintHorizontal_bias="0.5"
                            app:layout_constraintStart_toStartOf="parent"
                            app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsHeadlineTv">


                            <ImageView
                                android:id="@+id/shoeDetailsActivityActionsWishListButton"
                                android:layout_width="25dp"
                                android:layout_height="25dp"
                                android:src="@drawable/wishlist_blue_colored_bg" />


                            <TextView
                                android:id="@+id/shoeDetailsActivityActionsWishListButtonTextTv"
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="8dp"
                                android:fontFamily="@font/quicksand_bold"
                                android:text="wishlist"
                                android:textColor="#7A7E7E"
                                android:textSize="12sp"

                                />


                        </LinearLayout>


                        <LinearLayout
                            android:id="@+id/shoeDetailsActivityActionsShareLinearLayout"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginStart="16dp"
                            android:layout_marginTop="24dp"
                            android:gravity="center"
                            android:orientation="vertical"
                            app:layout_constraintEnd_toStartOf="@+id/shoeDetailsActivityActionsReviewsLinearLayout"
                            app:layout_constraintHorizontal_bias="0.5"
                            app:layout_constraintStart_toEndOf="@+id/shoeDetailsActivityActionsWishListLinearLayout"
                            app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsHeadlineTv">


                            <ImageView
                                android:id="@+id/shoeDetailsActivityActionsShareButton"
                                android:layout_width="25dp"
                                android:layout_height="25dp"
                                android:src="@drawable/share_green_colored_bg" />


                            <TextView
                                android:id="@+id/shoeDetailsActivityActionsShareButtonTextTv"
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="8dp"
                                android:fontFamily="@font/quicksand_bold"
                                android:text="Share"
                                android:textColor="#7A7E7E"
                                android:textSize="12sp"
                                app:layout_constraintEnd_toEndOf="@+id/shoeDetailsActivityActionsShareButton"
                                app:layout_constraintStart_toStartOf="@+id/shoeDetailsActivityActionsShareButton"
                                app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsShareButton" />


                        </LinearLayout>


                        <LinearLayout
                            android:id="@+id/shoeDetailsActivityActionsReviewsLinearLayout"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginStart="16dp"
                            android:layout_marginTop="24dp"
                            android:gravity="center"
                            android:orientation="vertical"
                            app:layout_constraintEnd_toStartOf="@+id/shoeDetailsActivityActionsHelpLinearLayout"
                            app:layout_constraintHorizontal_bias="0.5"
                            app:layout_constraintStart_toEndOf="@+id/shoeDetailsActivityActionsShareLinearLayout"
                            app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsHeadlineTv">


                            <ImageView
                                android:id="@+id/shoeDetailsActivityActionsReviewsButton"
                                android:layout_width="25dp"
                                android:layout_height="25dp"
                                android:src="@drawable/review_star_blue_colored" />


                            <TextView
                                android:id="@+id/shoeDetailsActivityActionsReviewButtonTextTv"
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="8dp"
                                android:fontFamily="@font/quicksand_bold"
                                android:text="Reviews"
                                android:textColor="#7A7E7E"
                                android:textSize="12sp"
                                app:layout_constraintEnd_toEndOf="@+id/shoeDetailsActivityActionsShareButton"
                                app:layout_constraintStart_toStartOf="@+id/shoeDetailsActivityActionsShareButton"
                                app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsShareButton" />


                        </LinearLayout>


                        <LinearLayout
                            android:id="@+id/shoeDetailsActivityActionsHelpLinearLayout"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginStart="16dp"
                            android:layout_marginTop="24dp"
                            android:layout_marginEnd="32dp"
                            android:gravity="center"
                            android:orientation="vertical"
                            app:layout_constraintEnd_toEndOf="parent"
                            app:layout_constraintHorizontal_bias="0.5"
                            app:layout_constraintStart_toEndOf="@+id/shoeDetailsActivityActionsReviewsLinearLayout"
                            app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsHeadlineTv">


                            <ImageView
                                android:id="@+id/shoeDetailsActivityActionsHelpButton"
                                android:layout_width="25dp"
                                android:layout_height="25dp"
                                android:src="@drawable/faq_blue_colored_bg" />


                            <TextView
                                android:id="@+id/shoeDetailsActivityActionsHelpButtonTextTv"
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="8dp"
                                android:fontFamily="@font/quicksand_bold"
                                android:text="Support"
                                android:textColor="#7A7E7E"
                                android:textSize="12sp"
                                app:layout_constraintEnd_toEndOf="@+id/shoeDetailsActivityActionsShareButton"
                                app:layout_constraintStart_toStartOf="@+id/shoeDetailsActivityActionsShareButton"
                                app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsShareButton" />


                        </LinearLayout>*/

