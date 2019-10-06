package com.example.dell.shoegamev22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.backendless.persistence.DataQueryBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.shoegamev22.adapters.ShoeDetailsMainImageRecViewAdapter;
import com.example.dell.shoegamev22.viewmodels.ShoeDetailsViewModel;
import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.novoda.merlin.MerlinsBeard;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewShoeDetails extends AppCompatActivity {


    //VIEWS
    RecyclerView mainImageRecView;
    IndefinitePagerIndicator mainImageRecViewIndicator;


    //FAST ADAPTER
    private ItemAdapter<ShoeDetailsMainImageRecViewAdapter> itemAdapter;
    private FastAdapter<ShoeDetailsMainImageRecViewAdapter> mainFastAdapter;

    //VIEW MODELS
    private ShoeDetailsViewModel shoeDetailsViewModel;

    //MERLIN
    private MerlinsBeard merlinsBeard;

    //PASSED IN PARAMETERS
    private String currentShoeId;

    //BACKENDLESS OBJECTS
    DataQueryBuilder classWideQueryBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shoe_details);

        //get the intent
        Intent intent = getIntent();
        currentShoeId = intent.getStringExtra("currentShoeId");
        Log.d("MyLogsShoeDet", "Current shoe id: "+ currentShoeId);

        //create merlin. library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(this);

        //initialize view models
        shoeDetailsViewModel = ViewModelProviders.of(this).get(ShoeDetailsViewModel.class);
        shoeDetailsViewModel.init();

        //initialize the query builder
        classWideQueryBuilder = DataQueryBuilder.create();
        classWideQueryBuilder.setWhereClause("objectId = '"+ currentShoeId + "'");
        Log.d("MyLogsShoeDet", "Current where clause: "+ classWideQueryBuilder.getWhereClause());


        //initialize our FastAdapter which will manage everything
        itemAdapter = new ItemAdapter<>();
        mainFastAdapter = FastAdapter.with(itemAdapter);

        //initialize the views
        mainImageRecViewIndicator = findViewById(R.id.shoeDetailsActivityMainImageRecViewIndicator);


        //initialize recycler view
        mainImageRecView = findViewById(R.id.shoeDetailsActivityMainImageRecView);
        mainImageRecView.setHasFixedSize(true);
        mainImageRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mainImageRecView.setAdapter(mainFastAdapter);
        //attach the indicator to the recycler view
        mainImageRecViewIndicator.attachToRecyclerView(mainImageRecView);
        //set snap helper to recycler view
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mainImageRecView);








        //make request
        if(merlinsBeard.isConnected()){

            makeShoeRequest(classWideQueryBuilder);

        }else{


            //todo tell user no internet

        }



        //add observers
        attachObservers();




    }


    private void makeShoeRequest(DataQueryBuilder queryBuilder) {

        shoeDetailsViewModel.getShoeDetails(queryBuilder);

    }


    private void attachObservers(){


        shoeDetailsViewModel.getShoeDetailsRequestResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){

                    //todo request was successful

                    shoeDetailsViewModel.getShoeDetailsResponse().observe(ViewShoeDetails.this, new Observer<List<Map>>() {
                        @Override
                        public void onChanged(List<Map> maps) {




                            String shoeId = (String) maps.get(0).get("objectId");

                            if (shoeId.equals(currentShoeId)){

                                ArrayList<ShoeDetailsMainImageRecViewAdapter> mImages =new ArrayList<>();
                                ArrayList<String>imageReferences = new ArrayList<>();
                                imageReferences = createShoeImageReferenceList(maps);
                                mImages = createShoeObjectsUsingReferences(imageReferences);

                                itemAdapter.clear();
                                itemAdapter.add(mImages);
                                mainFastAdapter.notifyAdapterDataSetChanged();

                            }









                            /*

                            List<ShoeDetailsMainImageRecViewAdapter> mImages =new ArrayList<>();
                            mImages.clear();
                            mImages = createShoeImageObjects(maps);

                            Log.d("MyLogsShoeDet", "OnChanged: shoe objects array: " + maps.toString());
*/







                        }
                    });




                }else{

                    //todo request was UNsuccessful


                }


            }
        });



    }





    private ArrayList<ShoeDetailsMainImageRecViewAdapter> createShoeImageObjects(List<Map> shoeObjectMap){


        ArrayList<ShoeDetailsMainImageRecViewAdapter> imageList = new ArrayList<>();

        if(shoeObjectMap!=null){

            ShoeDetailsMainImageRecViewAdapter holder = new ShoeDetailsMainImageRecViewAdapter();
            ArrayList<String> imageReferences = new ArrayList<>();
            imageReferences.add((String) shoeObjectMap.get(0).get("mainImage"));
            imageReferences.add((String) shoeObjectMap.get(0).get("image2"));
            imageReferences.add((String) shoeObjectMap.get(0).get("image3"));
            imageReferences.add((String) shoeObjectMap.get(0).get("image4"));
            imageReferences.add((String) shoeObjectMap.get(0).get("image5"));

            Log.d("MyLogsShoeDet", "image refs array: "+ imageReferences.toString());


            Integer count = imageReferences.size()-1;

            for (int i = 0; i<=count; i++){

                holder.setImageReference(imageReferences.get(i));
                imageList.add(holder);
                Log.d("MyLogsShoeDet", "holder images: "+ imageList.isEmpty() + " : "+ holder.getImageReference() );
                Log.d("MyLogsShoeDet", "Image list 1: "+ imageList.isEmpty() + " : "+ imageList.get(i).getImageReference());


            }





        }

        Log.d("MyLogsShoeDet", "Image list 2: "+ imageList.isEmpty() + " : "+ imageList.get(0).getImageReference());

        return imageList;



    }



    private  ArrayList<ShoeDetailsMainImageRecViewAdapter> createShoeObjectsUsingReferences( ArrayList<String> imageReferences ){


        Integer count = imageReferences.size()-1;
        ArrayList<ShoeDetailsMainImageRecViewAdapter> imageList = new ArrayList<>();

        for (int i = 0; i<=count; i++){

            ShoeDetailsMainImageRecViewAdapter holder = new ShoeDetailsMainImageRecViewAdapter();
            holder.setImageReference(imageReferences.get(i));
            imageList.add(holder);
            Log.d("MyLogsShoeDet", "holder images: "+ imageList.isEmpty() + " : "+ holder.getImageReference() );
            Log.d("MyLogsShoeDet", "Image list 1: "+ imageList.isEmpty() + " : "+ imageList.get(i).getImageReference());


        }

        Log.d("MyLogsShoeDet", "Image list 2: "+ imageList.isEmpty() + " : "+ imageList.get(0).getImageReference());
        return imageList;


    }




    private ArrayList<String> createShoeImageReferenceList(List<Map> shoeObjectMap){


        ArrayList<String> imageReferences = new ArrayList<>();

        if(shoeObjectMap!=null){


            imageReferences.add((String) shoeObjectMap.get(0).get("mainImage"));
            imageReferences.add((String) shoeObjectMap.get(0).get("image2"));
            imageReferences.add((String) shoeObjectMap.get(0).get("image3"));
            imageReferences.add((String) shoeObjectMap.get(0).get("image4"));
            imageReferences.add((String) shoeObjectMap.get(0).get("image5"));

            Log.d("MyLogsShoeDet", "image refs array1: "+ imageReferences.toString());

        }


        Log.d("MyLogsShoeDet", "image refs array2: "+ imageReferences.toString());
        return imageReferences;



    }





}

































/* scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                Log.d("MyLogsScroll", "scrollX_" + scrollX + "_scrollY_" + scrollY + "_oldScrollX_" + oldScrollX + "_oldScrollY_" + oldScrollY);


                DisplayMetrics metrics = getResources().getDisplayMetrics();
                float screenWidth = metrics.widthPixels;
                float screenHeight = metrics.heightPixels;


                float scrollPercentage = (scrollY / screenHeight) * 100;

                Log.d("MyLogsScroll", "scroll percentage: " + scrollPercentage);







            }
        });
*/
























/*  <ImageView
                        android:layout_width="0dp"
                        android:layout_height="0dp"
                        android:background="@drawable/rounded_rect_top_radius"
                        android:src="@drawable/test_img_shoes"
                        app:layout_constraintDimensionRatio="H,16:16"
                        />

*/




/*<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:fab="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:card_view="http://schemas.android.com/tools"
    android:background="@color/myColorAccent"
    android:orientation="vertical"
    tools:context=".ViewShoeDetails">


    <TextView
        android:id="@+id/activitySignInHeadlineTV"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginStart="16dp"
        android:layout_marginTop="24dp"
        android:layout_marginEnd="16dp"
        android:layout_marginBottom="8dp"
        android:fontFamily="@font/quicksand_bold"
        android:text="Product"
        android:textAlignment="center"
        android:textColor="#fff"
        android:textSize="20sp" />


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:background="#FFF"
        android:orientation="vertical">


        <com.github.nitrico.stickyscrollview.StickyScrollView
            android:id="@+id/shoeDetailsActivityMainScrollView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">


            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">


                <androidx.constraintlayout.widget.ConstraintLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">


                    <ImageView
                        android:id="@+id/shoeDetailsActivityMainImageRecView"
                        android:layout_width="0dp"
                        android:layout_height="0dp"
                        android:background="@drawable/rounded_rect_top_radius"
                        android:src="@drawable/test_img_shoes"
                        app:layout_constraintDimensionRatio="H,16:16"
                        app:layout_constraintEnd_toEndOf="parent"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />




                </androidx.constraintlayout.widget.ConstraintLayout>




                <androidx.cardview.widget.CardView
                    card_view:cardElevation="0dp"
                    android:tag="sticky"
                    android:id="@+id/shoeDetailsActivityProductTitleSectionLayout"
                    android:background="#FFF"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">


                <androidx.constraintlayout.widget.ConstraintLayout
                    android:layout_marginBottom="16dp"
                    android:layout_marginTop="16dp"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    >

                    <TextView

                        android:id="@+id/shoeDetailsActivityProductTitleTv"
                        android:layout_width="220dp"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:maxLines="1"
                        android:text="Jordan Air Ones' Kendrick Drake zorb"
                        android:textColor="#000"
                        android:textSize="15sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                    <TextView
                        android:id="@+id/shoeDetailsActivityProductPriceTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="16dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:maxLines="1"
                        android:text="74$"
                        android:textColor="#000"
                        android:textSize="15sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityProductTitleTv" />


                    <TextView
                        android:id="@+id/shoeDetailsActivityProductStockInfoTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="16dp"
                        android:fontFamily="@font/quicksand_light"
                        android:maxLines="1"
                        android:text="In stock"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintBottom_toBottomOf="@+id/shoeDetailsActivityProductPriceTv"
                        app:layout_constraintStart_toEndOf="@+id/shoeDetailsActivityProductPriceTv" />


                    <TextView
                        android:id="@+id/shoeDetailsActivityProductDeliveryTimeTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="8dp"
                        android:layout_marginTop="16dp"
                        android:fontFamily="@font/quicksand_light"
                        android:maxLines="1"
                        android:text="Shipping: 3 days"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintBottom_toBottomOf="@+id/shoeDetailsActivityProductStockInfoTv"
                        app:layout_constraintStart_toEndOf="@+id/shoeDetailsActivityProductStockInfoTv" />






                    <com.github.clans.fab.FloatingActionButton
                        app:layout_constraintBottom_toBottomOf="parent"
                        app:layout_constraintTop_toBottomOf="parent"
                        app:layout_constraintEnd_toEndOf="parent"
                        android:id="@+id/fab"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginEnd="8dp"
                        android:src="@drawable/ic_playlist"
                        fab:fab_colorNormal="@color/yellow_inactive"
                        fab:fab_colorPressed="@color/yellow_active"
                        fab:fab_colorRipple="@color/myColorLightYellow" />





                </androidx.constraintlayout.widget.ConstraintLayout>



                </androidx.cardview.widget.CardView>

                <androidx.constraintlayout.widget.ConstraintLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">


                    <TextView
                        android:id="@+id/browseFragmentCategoryFilterTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="32dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:text="Select size"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                    <TextView
                        android:id="@+id/browseFragmentCategoryFilterMenTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="24dp"
                        android:layout_marginTop="16dp"
                        android:background="@drawable/rounded_rect_8dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:paddingStart="10dp"
                        android:paddingTop="8dp"
                        android:paddingEnd="10dp"
                        android:paddingBottom="8dp"
                        android:text="39"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toBottomOf="@+id/browseFragmentCategoryFilterTv" />


                    <TextView
                        android:id="@+id/browseFragmentCategoryFilterWomenTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="8dp"
                        android:layout_marginTop="16dp"
                        android:background="@drawable/rounded_rect_8dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:paddingStart="10dp"
                        android:paddingTop="8dp"
                        android:paddingEnd="10dp"
                        android:paddingBottom="8dp"
                        android:text="40"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toEndOf="@+id/browseFragmentCategoryFilterMenTv"
                        app:layout_constraintTop_toBottomOf="@+id/browseFragmentCategoryFilterTv" />


                    <TextView
                        android:id="@+id/browseFragmentCategoryFilterSneakersTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="8dp"
                        android:layout_marginTop="16dp"
                        android:background="@drawable/rounded_rect_8dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:paddingStart="10dp"
                        android:paddingTop="8dp"
                        android:paddingEnd="10dp"
                        android:paddingBottom="8dp"
                        android:text="41"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toEndOf="@+id/browseFragmentCategoryFilterWomenTv"
                        app:layout_constraintTop_toBottomOf="@+id/browseFragmentCategoryFilterTv" />


                    <TextView
                        android:id="@+id/browseFragmentCategoryFilterAllCategoriesTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="8dp"
                        android:layout_marginTop="16dp"
                        android:background="@drawable/rounded_rect_8dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:paddingStart="10dp"
                        android:paddingTop="8dp"
                        android:paddingEnd="10dp"
                        android:paddingBottom="8dp"
                        android:text="42"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toEndOf="@+id/browseFragmentCategoryFilterSneakersTv"
                        app:layout_constraintTop_toBottomOf="@+id/browseFragmentCategoryFilterTv" />


                </androidx.constraintlayout.widget.ConstraintLayout>


                <androidx.constraintlayout.widget.ConstraintLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">


                    <TextView
                        android:id="@+id/browseFragmentTagsFilterTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="16dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:text="Select color"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                    <TextView
                        android:id="@+id/browseFragmentTagsFilterTag1Tv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="24dp"
                        android:layout_marginTop="16dp"
                        android:background="@drawable/rounded_rect_8dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:maxWidth="60dp"
                        android:maxLines="1"
                        android:paddingStart="10dp"
                        android:paddingTop="8dp"
                        android:paddingEnd="10dp"
                        android:paddingBottom="8dp"
                        android:text="Red"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toBottomOf="@+id/browseFragmentTagsFilterTv" />


                    <TextView
                        android:id="@+id/browseFragmentTagsFilterTag2Tv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="8dp"
                        android:layout_marginTop="16dp"
                        android:background="@drawable/rounded_rect_8dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:maxWidth="60dp"
                        android:maxLines="1"
                        android:paddingStart="10dp"
                        android:paddingTop="8dp"
                        android:paddingEnd="10dp"
                        android:paddingBottom="8dp"
                        android:text="Blue"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toEndOf="@+id/browseFragmentTagsFilterTag1Tv"
                        app:layout_constraintTop_toBottomOf="@+id/browseFragmentTagsFilterTv" />


                    <TextView
                        android:id="@+id/browseFragmentTagsFilterTag3Tv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="8dp"
                        android:layout_marginTop="16dp"
                        android:background="@drawable/rounded_rect_8dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:maxWidth="60dp"
                        android:maxLines="1"
                        android:paddingStart="10dp"
                        android:paddingTop="8dp"
                        android:paddingEnd="10dp"
                        android:paddingBottom="8dp"
                        android:text="Maroon"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toEndOf="@+id/browseFragmentTagsFilterTag2Tv"
                        app:layout_constraintTop_toBottomOf="@+id/browseFragmentTagsFilterTv" />


                    <TextView
                        android:id="@+id/browseFragmentTagsFilterTag4Tv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="8dp"
                        android:layout_marginTop="16dp"
                        android:background="@drawable/rounded_rect_8dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:maxWidth="60dp"
                        android:maxLines="1"
                        android:paddingStart="10dp"
                        android:paddingTop="8dp"
                        android:paddingEnd="10dp"
                        android:paddingBottom="8dp"
                        android:text="White"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toEndOf="@+id/browseFragmentTagsFilterTag3Tv"
                        app:layout_constraintTop_toBottomOf="@+id/browseFragmentTagsFilterTv" />


                </androidx.constraintlayout.widget.ConstraintLayout>


                <androidx.constraintlayout.widget.ConstraintLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">


                    <TextView
                        android:id="@+id/shoeDetailsActivityActionsHeaderTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="24dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:text="Actions"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                    <ImageView
                        android:id="@+id/shoeDetailsActivityActionsSaveIcon"
                        android:layout_width="25dp"
                        android:layout_height="25dp"
                        android:layout_marginStart="32dp"
                        android:layout_marginTop="16dp"
                        android:src="@drawable/ic_save_grey_24dp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsHeaderTv" />


                    <ImageView
                        android:layout_width="25dp"
                        android:layout_height="25dp"
                        android:layout_marginStart="32dp"
                        android:layout_marginTop="16dp"
                        android:src="@drawable/ic_share_grey_24dp"
                        app:layout_constraintStart_toEndOf="@+id/shoeDetailsActivityActionsSaveIcon"
                        app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityActionsHeaderTv" />


                </androidx.constraintlayout.widget.ConstraintLayout>


                <androidx.constraintlayout.widget.ConstraintLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">


                    <TextView
                        android:id="@+id/shoeDetailsActivityDescriptionHeaderTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="24dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:text="Description"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                    <ImageView
                        android:id="@+id/shoeDetailsActivityDescriptionHeaderTvDropdownIcon"
                        android:layout_width="20dp"
                        android:layout_height="20dp"
                        android:layout_marginTop="24dp"
                        android:layout_marginEnd="16dp"
                        android:src="@drawable/ic_keyboard_arrow_down_grey_24dp"
                        app:layout_constraintEnd_toEndOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                    <net.cachapa.expandablelayout.ExpandableLayout
                        android:id="@+id/shoeDetailsActivityDescriptionExpandableLayout"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        app:layout_constraintEnd_toEndOf="parent"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toBottomOf="@+id/shoeDetailsActivityDescriptionHeaderTv">

                        <TextView
                            android:id="@+id/shoeDetailsActivityDescriptionTv"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content" />


                    </net.cachapa.expandablelayout.ExpandableLayout>


                </androidx.constraintlayout.widget.ConstraintLayout>


                <androidx.constraintlayout.widget.ConstraintLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">


                    <TextView
                        android:id="@+id/shoeDetailsActivitySimilarProductsHeaderTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="24dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:text="Similar shoes"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                    <ImageView
                        android:id="@+id/shoeDetailsActivitySimilarShoesHeaderTvMoreIcon"
                        android:layout_width="20dp"
                        android:layout_height="20dp"
                        android:layout_marginTop="24dp"
                        android:layout_marginEnd="16dp"
                        android:src="@drawable/ic_keyboard_arrow_down_grey_24dp"
                        app:layout_constraintEnd_toEndOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                </androidx.constraintlayout.widget.ConstraintLayout>


                <androidx.constraintlayout.widget.ConstraintLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">


                    <TextView
                        android:id="@+id/shoeDetailsActivityReviewsHeaderTv"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="16dp"
                        android:layout_marginTop="24dp"
                        android:fontFamily="@font/quicksand_bold"
                        android:text="Reviews"
                        android:textColor="#7E7A7A"
                        android:textSize="12sp"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                    <ImageView
                        android:id="@+id/shoeDetailsActivityReviewHeaderTvMoreIcon"
                        android:layout_width="20dp"
                        android:layout_height="20dp"
                        android:layout_marginTop="24dp"
                        android:layout_marginEnd="16dp"
                        android:src="@drawable/ic_keyboard_arrow_down_grey_24dp"
                        app:layout_constraintEnd_toEndOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />


                </androidx.constraintlayout.widget.ConstraintLayout>









                <ImageView

                    android:layout_width="match_parent"
                    android:layout_height="200dp" />







            </LinearLayout>


        </com.github.nitrico.stickyscrollview.StickyScrollView>















    </LinearLayout>


</LinearLayout>*/