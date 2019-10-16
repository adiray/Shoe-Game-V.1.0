package com.example.dell.shoegamev22;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.backendless.persistence.DataQueryBuilder;
import com.example.dell.shoegamev22.adapters.BrowseFragmentAppliedFiltersRecViewAdapter;
import com.example.dell.shoegamev22.adapters.BrowseFragmentCategoryFilterAdapter;
import com.example.dell.shoegamev22.adapters.BrowseFragmentResultsAdapter;
import com.example.dell.shoegamev22.adapters.BrowseFragmentTagsFilterAdapter;
import com.example.dell.shoegamev22.adapters.TagsFilterAdapter;
import com.example.dell.shoegamev22.viewmodels.BrowseFragmentViewModel;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.novoda.merlin.MerlinsBeard;
import com.suke.widget.SwitchButton;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class BrowseFragment extends Fragment {

    private View view;


    //VIEWS
    RecyclerView categoryFilterRecView, tagsFilterRecView, shoesResultsRecView;
    SwipeRefreshLayout shoesResultsRecViewSwipeRefresh;
    private ExpandableLayout filterExpandableLayout;
    private TextView addFiltersTv, priceFilterIndicatorTv, andOrSwitchAndTv, andOrSwitchOrTv;
    SeekBar priceSeekBar;
    SwitchButton andOrSwitch;
    Button filterButton;
    TextView cancelFiltersTv;
    LottieAnimationView topLoadingView;
    FloatingTextButton loadMoreButton;
    TextView noResultsTv, noResultsTipTv;
    LottieAnimationView noResultsAnimation;
    NestedScrollView noResultsLayout;

    //VIEW MODELS
    BrowseFragmentViewModel browseFragmentViewModel;

    //merlin
    private MerlinsBeard merlinsBeard;


    //BACKENDLESS
    DataQueryBuilder tagsQueryBuilder = DataQueryBuilder.create();
    DataQueryBuilder categoriesQueryBuilder = DataQueryBuilder.create();
    DataQueryBuilder shoesQueryBuilder = DataQueryBuilder.create();

    //VARIABLES
    List<String> selectedCategoryFilters = new ArrayList<>();
    List<String> selectedCategoryFiltersIds = new ArrayList<>();
    List<String> selectedTagsFilters = new ArrayList<>();
    List<String> selectedTagsFiltersIds = new ArrayList<>();
    Integer selectedMaxPrice = 200;
    String whereClauseConjuction = "AND";
    List<Map> tagsResponse = new ArrayList<>();
    Boolean isRefreshing = false, isLoadMore = false , isNormalLoad = true;
    List<BrowseFragmentCategoryFilterAdapter> categoryFilters = new ArrayList<>();
    List<BrowseFragmentTagsFilterAdapter> tagsFilters = new ArrayList<>();


    //create our FastAdapter which will manage everything
    private ItemAdapter<BrowseFragmentCategoryFilterAdapter> categoryFilterItemAdapter;
    private FastAdapter<BrowseFragmentCategoryFilterAdapter> categoryFilterFastAdapter;
    private ItemAdapter<BrowseFragmentTagsFilterAdapter> tagsFilterItemAdapter;
    private FastAdapter<BrowseFragmentTagsFilterAdapter> tagsFilterFastAdapter;
    private ItemAdapter<BrowseFragmentResultsAdapter> shoesResultsItemAdapter;
    private FastAdapter<BrowseFragmentResultsAdapter> shoesResultsFastAdapter;

    //private FastAdapter bestDealsFastAdapter2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.browse_fragment, container, false);


        //INITIALIZE VIEWS
        filterExpandableLayout = view.findViewById(R.id.browseFragmentExpandableLayout);
        addFiltersTv = view.findViewById(R.id.browseFragmentAddFiltersTv);
        priceSeekBar = view.findViewById(R.id.browseFragmentPriceFilterSeekBar);
        priceFilterIndicatorTv = view.findViewById(R.id.browseFragmentPriceFilterSeekBarValueTV);
        andOrSwitch = view.findViewById(R.id.browseFragmentAndOrSwitch);
        andOrSwitchAndTv = view.findViewById(R.id.browseFragmentAndOrSwitchAndTv);
        andOrSwitchOrTv = view.findViewById(R.id.browseFragmentAndOrSwitchOrTv);
        filterButton = view.findViewById(R.id.browseFragmentFilterButton);
        cancelFiltersTv = view.findViewById(R.id.browseFragmentCancelFiltersTv);
        shoesResultsRecViewSwipeRefresh = view.findViewById(R.id.browseFragmentResultsMainRecViewSwipeRefresh);
        shoesResultsRecView = view.findViewById(R.id.browseFragmentResultsMainRecView);
        topLoadingView = view.findViewById(R.id.browseFragmentLoadingView);
        loadMoreButton = view.findViewById(R.id.browseFragmentLoadMoreButton);
        noResultsAnimation = view.findViewById(R.id.browseFragmentNoResultsAnimationView);
        noResultsTipTv = view.findViewById(R.id.browseFragmentNoResultsTextViewTips);
        noResultsTv = view.findViewById(R.id.browseFragmentNoResultsTextView);
        noResultsLayout = view.findViewById(R.id.browseFragmentNoResultsLayout);



        //create merlin. library used to monitor internet connectivity
        merlinsBeard = MerlinsBeard.from(getActivity());

        //INITIALIZE VIEW MODELS
        browseFragmentViewModel = ViewModelProviders.of(this).get(BrowseFragmentViewModel.class);
        browseFragmentViewModel.init();


        //initialize adapters
        //initialize our FastAdapter which will manage everything
        categoryFilterItemAdapter = new ItemAdapter<>();
        categoryFilterFastAdapter = FastAdapter.with(categoryFilterItemAdapter);
        tagsFilterItemAdapter = new ItemAdapter<>();
        tagsFilterFastAdapter = FastAdapter.with(tagsFilterItemAdapter);
        shoesResultsItemAdapter = new ItemAdapter<>();
        shoesResultsFastAdapter = FastAdapter.with(shoesResultsItemAdapter);


        //INITIALIZE RECYCLER VIEWS
        //category filter rec view
        categoryFilterRecView = view.findViewById(R.id.browseFragmentCategoryFilterRecView);
        categoryFilterRecView.setHasFixedSize(true);
        categoryFilterRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        tagsFilterRecView = view.findViewById(R.id.browseFragmentTagsFilterRecView);
        tagsFilterRecView.setHasFixedSize(true);
        tagsFilterRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        shoesResultsRecView.setHasFixedSize(true);
        shoesResultsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


        //set up and/or conjunction switch
        andOrSwitch.setEnabled(false);
        andOrSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (isChecked) {

                    whereClauseConjuction = "OR";
                    andOrSwitchAndTv.setTextColor(Color.parseColor("#7A7E7E"));
                    andOrSwitchOrTv.setTextColor(getResources().getColor(R.color.myColorAccentDark));


                } else {

                    whereClauseConjuction = "AND";
                    andOrSwitchOrTv.setTextColor(Color.parseColor("#7A7E7E"));
                    andOrSwitchAndTv.setTextColor(getResources().getColor(R.color.myColorAccentDark));

                }


            }
        });


        //set up price seek bar
        priceSeekBar.setMax(200);


        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                String maxPriceText = progress + "$";
                priceFilterIndicatorTv.setText(maxPriceText);
                selectedMaxPrice = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (selectedMaxPrice <= 0) {


                    selectedMaxPrice = 200;
                    String maxPriceText = 200 + "$";
                    priceFilterIndicatorTv.setText(maxPriceText);
                    priceFilterIndicatorTv.setTextColor(Color.parseColor("#7A7E7E"));


                } else {

                    priceFilterIndicatorTv.setTextColor(getResources().getColor(R.color.myColorAccentDark));

                }

            }
        });



        //add on click to tags rec view objects
        tagsFilterFastAdapter.withSelectable(true);
        tagsFilterFastAdapter.withEventHook(new ClickEventHook<BrowseFragmentTagsFilterAdapter>() {


            @Override
            public View onBind(RecyclerView.ViewHolder viewHolder) {


                if (viewHolder instanceof BrowseFragmentTagsFilterAdapter.BrowseFragmentTagsFilterAdapterViewHolder) {
                    return ((BrowseFragmentTagsFilterAdapter.BrowseFragmentTagsFilterAdapterViewHolder) viewHolder).tagNameTv;
                }

                return super.onBind(viewHolder);
            }

            @Override
            public void onClick(View v, int position, FastAdapter<BrowseFragmentTagsFilterAdapter> fastAdapter, BrowseFragmentTagsFilterAdapter item) {





                TextView currentView = (TextView) v;
                String selectedTagString = item.getTagName();
                String selectedTagId = item.getTagId();

                if (!selectedTagsFilters.contains(selectedTagString) && !selectedCategoryFilters.contains(selectedTagString)) {


                    //TODO SOMETIMES A DIFFENT VIEW IS HIGHLIGHTED OTHER THAN SELECTED ONE


                    selectedTagsFilters.add(selectedTagString);
                    selectedTagsFiltersIds.add(selectedTagId);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rect_8dp_green));
                    currentView.setTextColor(getResources().getColor(R.color.white));


                    if (selectedTagsFilters.size() >= 2) {


                        andOrSwitch.setEnabled(true);


                    } else if (selectedCategoryFilters.size() >= 2) {

                        andOrSwitch.setEnabled(true);


                    } else if (selectedCategoryFilters.size() > 0 && selectedTagsFilters.size() > 0) {


                        andOrSwitch.setEnabled(true);


                    } else {


                        andOrSwitch.setChecked(false);
                        andOrSwitch.setEnabled(false);


                    }


                } else {


                    selectedTagsFilters.remove(selectedTagString);
                    selectedTagsFiltersIds.remove(selectedTagId);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rounded_rect_8dp));
                    currentView.setTextColor(Color.parseColor("#7A7E7E"));


                    if (selectedTagsFilters.size() >= 2 || selectedCategoryFilters.size() > 0) {


                        andOrSwitch.setEnabled(true);


                    } else {

                        andOrSwitch.setEnabled(false);

                    }


                }








            }
        });

        //add on click to category rec view objects
        categoryFilterFastAdapter.withSelectable(true);
        categoryFilterFastAdapter.withEventHook(new ClickEventHook<BrowseFragmentCategoryFilterAdapter>() {

           @Override
           public View onBind(RecyclerView.ViewHolder viewHolder) {


               if (viewHolder instanceof BrowseFragmentCategoryFilterAdapter.BrowseFragmentCategoryFilterAdapterViewHolder) {
                   return ((BrowseFragmentCategoryFilterAdapter.BrowseFragmentCategoryFilterAdapterViewHolder) viewHolder).categoryNameTv;
               }

               return super.onBind(viewHolder);
           }

           @Override
           public void onClick(View v, int position, FastAdapter<BrowseFragmentCategoryFilterAdapter> fastAdapter, BrowseFragmentCategoryFilterAdapter item) {




               TextView currentView = (TextView) v;
               String selectedCategoryString = item.getCategoryName();
               String selectedCategoryId = item.getCategoryId();

               if (!selectedCategoryFilters.contains(selectedCategoryString) && !selectedTagsFilters.contains(selectedCategoryString)) {


                   //TODO SOMETIMES A DIFFrENT VIEW IS HIGHLIGHTED OTHER THAN SELECTED ONE


                   selectedCategoryFilters.add(selectedCategoryString);
                   selectedCategoryFiltersIds.add(selectedCategoryId);
                   currentView.setBackground(getResources().getDrawable(R.drawable.rect_8dp_green));
                   currentView.setTextColor(getResources().getColor(R.color.white));


                   if (selectedTagsFilters.size() >= 2) {


                       andOrSwitch.setEnabled(true);


                   } else if (selectedCategoryFilters.size() >= 2) {

                       andOrSwitch.setEnabled(true);


                   } else if (selectedCategoryFilters.size() > 0 && selectedTagsFilters.size() > 0) {


                       andOrSwitch.setEnabled(true);


                   } else {


                       andOrSwitch.setChecked(false);
                       andOrSwitch.setEnabled(false);


                   }


               } else {


                   boolean removeMethodResult = selectedCategoryFilters.remove(selectedCategoryString);
                   selectedCategoryFiltersIds.remove(selectedCategoryId);

                   Log.d("MyLogsBrowseFrag", "Remove method result: " + removeMethodResult);

                   currentView.setBackground(getResources().getDrawable(R.drawable.rounded_rect_8dp));
                   currentView.setTextColor(Color.parseColor("#7A7E7E"));


                   if (selectedCategoryFilters.size() >= 2 || selectedTagsFilters.size() > 0) {


                       andOrSwitch.setEnabled(true);


                   } else {

                       andOrSwitch.setEnabled(false);

                   }


               }




           }
       });




        //add the observers
        addObserversForTagsRequest();
        addObserversForCategoriesRequest();
        addObserversForShoes();


        //initialize data query builder
        tagsQueryBuilder.setSortBy("priority%20desc");
        tagsQueryBuilder.setPageSize(20);
        categoriesQueryBuilder.setWhereClause("type='Category'");
        categoriesQueryBuilder.setSortBy("priority%20desc");
        categoriesQueryBuilder.setPageSize(10);


        //REQUEST INITIAL DATA
        if (merlinsBeard.isConnected()) {


            browseFragmentViewModel.getAllTags(tagsQueryBuilder);
            browseFragmentViewModel.getAllCategories(categoriesQueryBuilder);


        } else {


            Log.d("MyLogsTags", "Browse fragment: MERLIN. response: NO INTERNET ACCESS");
            Log.d("MyLogsCategories", "Browse fragment: MERLIN. response: NO INTERNET ACCESS");


        }


        addFiltersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (filterExpandableLayout.isExpanded()) {


                    filterExpandableLayout.collapse();
                    addFiltersTv.setTextColor(getResources().getColor(R.color.myColorInactiveTextBg));

                } else {

                    filterExpandableLayout.expand();
                    addFiltersTv.setTextColor(getResources().getColor(R.color.myColorAccentDark));


                }


            }
        });


        //add on click to the filter button
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                filterButtonClicked();


            }
        });


        //add an on click for cancel filters button
        cancelFiltersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelFiltersButtonClicked();


            }
        });


        //add on refresh method
        shoesResultsRecViewSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                onRefreshClicked();


            }
        });


        //add on scroll listener to the rec view
        shoesResultsRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    Log.d("MyLogs", "Browse fragment: Recycler view has reached end. count:");
                    loadMoreButton.setVisibility(View.VISIBLE);

                }
            }
        });


        //add on click to the load more button
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadMoreButton.setVisibility(View.INVISIBLE);

                onLoadMoreClicked();


            }
        });


        return view;
    }


    private List<BrowseFragmentCategoryFilterAdapter> createCategoryFilterItems(List<Map> categoriesResponse) {


        List<BrowseFragmentCategoryFilterAdapter> categoriesData = new ArrayList<>();

        Integer count = categoriesResponse.size() - 1;

        for (int i = 0; i <= count; i++) {

            BrowseFragmentCategoryFilterAdapter holder = new BrowseFragmentCategoryFilterAdapter();
            String categoryName = (String) categoriesResponse.get(i).get("name");
            String categoryId = (String) categoriesResponse.get(i).get("objectId");
            holder.setCategoryName(categoryName);
            holder.setCategoryId(categoryId);

            categoriesData.add(holder);


        }


        return categoriesData;


    }


    private void addObserversForTagsRequest() {


        browseFragmentViewModel.getTagsRequestResult().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {


                if (aBoolean) {


                    //todo request was successful

                    browseFragmentViewModel.getTagsRequestResponse().observe(getActivity(), new Observer<List<Map>>() {
                        @Override
                        public void onChanged(List<Map> maps) {


                            if (!maps.isEmpty()) {

                                tagsFilterRecView.setAdapter(tagsFilterFastAdapter);
                                tagsFilterItemAdapter.clear();
                                tagsFilterItemAdapter.add(createTagsFilterItems(maps));
                                tagsResponse = maps;


                            }


                        }
                    });


                } else {

                    //todo request was unsuccessful


                }


            }
        });


    }


    private void addObserversForCategoriesRequest() {

        browseFragmentViewModel.getCategoriesRequestResult().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {


                if (aBoolean) {

                    //todo the request is successful

                    browseFragmentViewModel.getCategoriesRequestResponse().observe(getActivity(), new Observer<List<Map>>() {
                        @Override
                        public void onChanged(List<Map> maps) {


                            if (!maps.isEmpty()) {


                                categoryFilterItemAdapter.add(createCategoryFilterItems(maps));
                                categoryFilterRecView.setAdapter(categoryFilterFastAdapter);


                            }


                        }
                    });


                } else {

                    //todo the request is unsuccessful


                }


            }
        });


    }


    private List<BrowseFragmentTagsFilterAdapter> createTagsFilterItems(List<Map> tagsResponse) {

        List<BrowseFragmentTagsFilterAdapter> tagsData = new ArrayList<>();

        Integer count = tagsResponse.size() - 1;

        for (int i = 0; i <= count; i++) {

            BrowseFragmentTagsFilterAdapter holder = new BrowseFragmentTagsFilterAdapter();
            String tagName = (String) tagsResponse.get(i).get("name");
            String tagId = (String) tagsResponse.get(i).get("objectId");
            holder.setTagName(tagName);
            holder.setTagId(tagId);

            tagsData.add(holder);


        }


        return tagsData;


    }

    private void filterButtonClicked() {


        //start loading animation
        topLoadingView.setVisibility(View.VISIBLE);
        topLoadingView.playAnimation();


        String thisWhereClause = "", priceClause = null, categoryClause = null, tagsClause = null;
        StringBuilder thisWhereClauseBuilder = new StringBuilder();
        boolean firstTime = true;


        if (selectedMaxPrice != null) {

            priceClause = "price <= '" + selectedMaxPrice + "'";

        }

        if (!selectedCategoryFiltersIds.isEmpty()) {

            Integer count = selectedCategoryFiltersIds.size() - 1;
            categoryClause = "tags = '" + selectedCategoryFiltersIds.get(0) + "'";
            StringBuilder categoryClauseBuilder = new StringBuilder();
            categoryClauseBuilder.append(categoryClause);


            for (int i = 1; i <= count; i++) {


                categoryClauseBuilder.append(" ").append(whereClauseConjuction).append(" tags = '").append(selectedCategoryFiltersIds.get(i)).append("'");
                // categoryClause.append(" ").append(whereClauseConjuction).append(" tags = '").append(selectedCategoryFiltersIds.get(i));
                //categoryClause = categoryClause + " " + whereClauseConjuction + " tags = '" + selectedCategoryFiltersIds.get(i);


            }


            categoryClause = categoryClauseBuilder.toString();

        }


        if (!selectedTagsFiltersIds.isEmpty()) {


            Integer count = selectedTagsFiltersIds.size() - 1;
            tagsClause = "tags = '" + selectedTagsFiltersIds.get(0) + "'";
            StringBuilder tagsClauseBuilder = new StringBuilder();
            tagsClauseBuilder.append(tagsClause);


            for (int i = 1; i <= count; i++) {


                tagsClauseBuilder.append(" ").append(whereClauseConjuction).append(" tags = '").append(selectedTagsFiltersIds.get(i)).append("'");
                // categoryClause.append(" ").append(whereClauseConjuction).append(" tags = '").append(selectedCategoryFiltersIds.get(i));
                //categoryClause = categoryClause + " " + whereClauseConjuction + " tags = '" + selectedCategoryFiltersIds.get(i);


            }


            tagsClause = tagsClauseBuilder.toString();

        }


        if (categoryClause != null) {


            thisWhereClauseBuilder.append(categoryClause);


            if (tagsClause != null) {


                thisWhereClauseBuilder.append(" ").append(whereClauseConjuction).append(" ").append(tagsClause);


            }

            if (priceClause != null) {


                thisWhereClauseBuilder.append(" ").append(whereClauseConjuction).append(" ").append(priceClause);


            }


        } else if (tagsClause != null) {


            thisWhereClauseBuilder.append(tagsClause);


            if (priceClause != null) {


                thisWhereClauseBuilder.append(" ").append(whereClauseConjuction).append(" ").append(priceClause);


            }


        } else if (priceClause != null) {


            thisWhereClauseBuilder.append(priceClause);


        }


        thisWhereClause = thisWhereClauseBuilder.toString();



        //MAKE REUEST
        if (merlinsBeard.isConnected()) {


            shoesQueryBuilder.setWhereClause(thisWhereClause);
            shoesQueryBuilder.setPageSize(30);
            requestShoes(shoesQueryBuilder);

        } else {

            topLoadingView.pauseAnimation();
            topLoadingView.setVisibility(View.GONE);

            //remove the recycle view
            shoesResultsItemAdapter.clear();
            shoesResultsRecView.setAdapter(shoesResultsFastAdapter);
            shoesResultsRecViewSwipeRefresh.setVisibility(View.GONE);

            //show the error view
            noResultsLayout.setVisibility(View.VISIBLE);
            noResultsTipTv.setText("Please check your internet connection");
            loadMoreButton.setVisibility(View.INVISIBLE);




            Toast.makeText(getContext(), "There is no internet connection!", Toast.LENGTH_LONG).show();

        }



        if (filterExpandableLayout.isExpanded()){

            filterExpandableLayout.collapse(true);

        }




        //35AE1FCD-F2B8-1E6C-FF57-B3CEB92BFB00
        //757AC723-A3D8-1113-FF95-A3F89FDC1600
    }


    private void cancelFiltersButtonClicked() {


        selectedTagsFiltersIds.clear();
        selectedTagsFilters.clear();
        selectedCategoryFiltersIds.clear();
        selectedCategoryFilters.clear();

        //reset price
        priceSeekBar.setProgress(0);
        selectedMaxPrice = 200;
        String selectedMaxPriceText = selectedMaxPrice + "$";
        priceFilterIndicatorTv.setText(selectedMaxPriceText);
        priceFilterIndicatorTv.setTextColor(Color.parseColor("#7A7E7E"));

        //reset conjunction toggle
        andOrSwitch.setChecked(false);
        whereClauseConjuction = "AND";

        //deselect the views
        tagsFilterRecView.setAdapter(tagsFilterFastAdapter);
        categoryFilterRecView.setAdapter(categoryFilterFastAdapter);


    }


    private void requestShoes(DataQueryBuilder queryBuilder) {

        browseFragmentViewModel.getShoes(queryBuilder);

    }


    private void addObserversForShoes() {


        browseFragmentViewModel.getShoesRequestResult().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {


                if (aBoolean) {


                    //todo request was successful
                    Log.d("MyLogsShoeGame", "Browse Fragment results: REQUEST. Request successful: isLoadMore: "+ isLoadMore + " ,isRefreshing: " + isRefreshing );



                } else {

                    //todo request was unsuccessful


                    if (isRefreshing){

                        //stop the loading views
                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);
                        shoesResultsRecViewSwipeRefresh.setRefreshing(false);
                        isRefreshing = false;
                        Toast.makeText(getContext(), "Sorry, something went wrong!", Toast.LENGTH_LONG).show();




                    }else if (isLoadMore){


                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);
                        isRefreshing = false;
                        isLoadMore = false;
                        Toast.makeText(getContext(), "Sorry, something went wrong!", Toast.LENGTH_LONG).show();







                    }else{


                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);
                        isLoadMore = false;
                        isRefreshing = false;
                        Toast.makeText(getContext(), "Sorry, something went wrong!", Toast.LENGTH_LONG).show();

                        //remove the recycle view
                        shoesResultsItemAdapter.clear();
                        shoesResultsRecView.setAdapter(shoesResultsFastAdapter);
                        shoesResultsRecViewSwipeRefresh.setVisibility(View.GONE);

                        //show the error view
                        noResultsLayout.setVisibility(View.VISIBLE);
                        noResultsTipTv.setText("Please check your internet connection");
                        loadMoreButton.setVisibility(View.INVISIBLE);



                    }




                    Log.d("MyLogsShoeGame", "Browse Fragment results: REQUEST. Request failed: isLoadMore: "+ isLoadMore + " ,isRefreshing: " + isRefreshing );



                }


            }
        });












        browseFragmentViewModel.getShoesRequestResponse().observe(getActivity(), new Observer<List<Map>>() {
            @Override
            public void onChanged(List<Map> maps) {


                if (!maps.isEmpty()) {



                    if (isRefreshing) {


                        Log.d("MyLogsShoeGame", "Browse Fragment results: REQUEST. IS refreshing run" + isRefreshing);

                        shoesResultsRecViewSwipeRefresh.setVisibility(View.VISIBLE);
                        noResultsLayout.setVisibility(View.GONE);
                        //loadMoreButton.setVisibility(View.VISIBLE);


                        isRefreshing = false;
                        isLoadMore = false;
                        shoesResultsItemAdapter.clear();
                        shoesResultsItemAdapter.add(createShoeObjects(maps));
                        shoesResultsRecView.setAdapter(shoesResultsFastAdapter);
                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);
                        shoesResultsRecViewSwipeRefresh.setRefreshing(false);
                        loadMoreButton.setEnabled(true);
                        shoesResultsRecViewSwipeRefresh.setEnabled(true);


                    } else if (isLoadMore) {


                        Log.d("MyLogsShoeGame", "Browse Fragment results: REQUEST. IS load more run: " + isLoadMore);



                        isRefreshing = false;
                        isLoadMore = false;
                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);
                        shoesResultsItemAdapter.add(createShoeObjects(maps));
                        shoesResultsRecView.setAdapter(shoesResultsFastAdapter);
                        loadMoreButton.setEnabled(true);
                        shoesResultsRecViewSwipeRefresh.setEnabled(true);



                    } else {


                        Log.d("MyLogsShoeGame", "Browse Fragment results: REQUEST. other value run: isLoadMore: "+ isLoadMore + " ,isRefreshing: " + isRefreshing );


                        shoesResultsRecViewSwipeRefresh.setVisibility(View.VISIBLE);
                        noResultsLayout.setVisibility(View.GONE);
                       // loadMoreButton.setVisibility(View.VISIBLE);



                        shoesResultsItemAdapter.clear();
                        shoesResultsItemAdapter.add(createShoeObjects(maps));
                        shoesResultsRecView.setAdapter(shoesResultsFastAdapter);
                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);


                    }


                } else {


                    Log.d("MyLogsShoeGame", "Browse Fragment results: REQUEST. No results run: isLoadMore: "+ isLoadMore + " ,isRefreshing: " + isRefreshing );

                    if (isRefreshing){

                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);
                        shoesResultsRecViewSwipeRefresh.setRefreshing(false);
                        isRefreshing = false;
                        Toast.makeText(getContext(), "There are no results", Toast.LENGTH_LONG).show();
                        loadMoreButton.setEnabled(true);
                        shoesResultsRecViewSwipeRefresh.setEnabled(true);

                        //remove the recycle view
                        shoesResultsItemAdapter.clear();
                        shoesResultsRecView.setAdapter(shoesResultsFastAdapter);
                        shoesResultsRecViewSwipeRefresh.setVisibility(View.GONE);

                        //show the error view
                        noResultsLayout.setVisibility(View.VISIBLE);
                        loadMoreButton.setVisibility(View.INVISIBLE);





                    }else if (isLoadMore){


                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);
                        isRefreshing = false;
                        isLoadMore = false;
                        Toast.makeText(getContext(), "There are no more results", Toast.LENGTH_LONG).show();
                        loadMoreButton.setEnabled(true);
                        shoesResultsRecViewSwipeRefresh.setEnabled(true);


                    }else{


                        topLoadingView.pauseAnimation();
                        topLoadingView.setVisibility(View.GONE);
                        isLoadMore = false;
                        isRefreshing = false;
                        Toast.makeText(getContext(), "There are no results", Toast.LENGTH_LONG).show();


                        //remove the recycle view
                        shoesResultsItemAdapter.clear();
                        shoesResultsRecView.setAdapter(shoesResultsFastAdapter);
                        shoesResultsRecViewSwipeRefresh.setVisibility(View.GONE);

                        //show the error view
                        noResultsLayout.setVisibility(View.VISIBLE);
                        loadMoreButton.setVisibility(View.INVISIBLE);





                    }








                }


            }
        });

    }


    private List<BrowseFragmentResultsAdapter> createShoeObjects(List<Map> shoeObjectMaps) {

        List<BrowseFragmentResultsAdapter> shoeObjectData = new ArrayList<>();

        if (shoeObjectMaps != null) {

            Integer count = shoeObjectMaps.size() - 1;

            for (int i = 0; i <= count; i++) {

                BrowseFragmentResultsAdapter holder = new BrowseFragmentResultsAdapter();
                holder.setMainImage((String) shoeObjectMaps.get(i).get("mainImage"));
                holder.setPrice((String) shoeObjectMaps.get(i).get("price"));
                holder.setTitle((String) shoeObjectMaps.get(i).get("title"));
                holder.setItemId((String) shoeObjectMaps.get(i).get("objectId"));

                shoeObjectData.add(holder);


            }


        } else {

            Log.d("MyLogsShoeGame", "Browse Fragment results: createShoeObjects method. passed in shoeObjectMap = null");

        }


        return shoeObjectData;

    }


    private void onRefreshClicked() {

        isRefreshing = true;
        isLoadMore = false;
        loadMoreButton.setEnabled(false);
        shoesResultsRecViewSwipeRefresh.setEnabled(false);


        if (merlinsBeard.isConnected()) {


            shoesQueryBuilder.setOffset(0);
            topLoadingView.setVisibility(View.VISIBLE);
            topLoadingView.playAnimation();
            browseFragmentViewModel.getShoes(shoesQueryBuilder);


        } else {

            Toast.makeText(getContext(), "There is no internet connection!", Toast.LENGTH_LONG).show();
            isLoadMore = false;
            isRefreshing= false;
            loadMoreButton.setEnabled(true);
            shoesResultsRecViewSwipeRefresh.setEnabled(true);
            shoesResultsRecViewSwipeRefresh.setRefreshing(false);


            //remove the recycle view
            shoesResultsItemAdapter.clear();
            shoesResultsRecView.setAdapter(shoesResultsFastAdapter);
            shoesResultsRecView.setVisibility(View.GONE);

            //show the error view
            noResultsLayout.setVisibility(View.VISIBLE);
            noResultsTipTv.setText("Please check your internet connection");


        }


    }


    private void onLoadMoreClicked() {

        isLoadMore = true;
        shoesResultsRecViewSwipeRefresh.setEnabled(false);
        loadMoreButton.setEnabled(false);

        if (merlinsBeard.isConnected()) {


            shoesQueryBuilder.prepareNextPage();
            topLoadingView.setVisibility(View.VISIBLE);
            topLoadingView.playAnimation();
            browseFragmentViewModel.getShoes(shoesQueryBuilder);


        } else {


            Toast.makeText(getContext(), "There is no internet connection!", Toast.LENGTH_LONG).show();
            isLoadMore = false;
            loadMoreButton.setEnabled(true);
            shoesResultsRecViewSwipeRefresh.setEnabled(true);


        }


    }


    public List<BrowseFragmentAppliedFiltersRecViewAdapter> createAppliedFilters(List<String> categoryFilterNames, List<String> categoryFilterIds, List<String> tagsFilterNames, List<String> tagsFiltersIds) {

        List<BrowseFragmentAppliedFiltersRecViewAdapter> appliedFiltersData = new ArrayList<>();

        if (!categoryFilterNames.isEmpty()) {


            Integer count = categoryFilterNames.size() - 1;


            for (int i = 0; i <= count; i++) {

                BrowseFragmentAppliedFiltersRecViewAdapter holder = new BrowseFragmentAppliedFiltersRecViewAdapter();

                holder.setFilterName(categoryFilterNames.get(i));
                holder.setFilterId(categoryFilterIds.get(i));
                appliedFiltersData.add(holder);
                Log.d("MyLogsFilters", "Applied filters data 0: " + holder.getFilterName());
            }



            if (!tagsFilterNames.isEmpty()) {


                Integer count2 = tagsFilterNames.size() - 1;


                for (int i = 0; i <= count2; i++) {

                    BrowseFragmentAppliedFiltersRecViewAdapter holder = new BrowseFragmentAppliedFiltersRecViewAdapter();

                    holder.setFilterName(tagsFilterNames.get(i));
                    holder.setFilterId(tagsFiltersIds.get(i));
                    appliedFiltersData.add(holder);
                    Log.d("MyLogsFilters", "Applied filters data 1: " + holder.getFilterName());
                }

            }

        }else if (!tagsFilterNames.isEmpty()) {


            Integer count = tagsFilterNames.size() - 1;


            for (int i = 0; i <= count; i++) {

                BrowseFragmentAppliedFiltersRecViewAdapter holder = new BrowseFragmentAppliedFiltersRecViewAdapter();

                holder.setFilterName(tagsFilterNames.get(i));
                holder.setFilterId(tagsFiltersIds.get(i));
                appliedFiltersData.add(holder);
                Log.d("MyLogsFilters", "Applied filters data 2: " + holder.getFilterName());

            }

        }


        Log.d("MyLogsFilters", "Applied filters data: " + appliedFiltersData.get(0).getFilterName());


        return appliedFiltersData;


    }


}


//todo the empty request determination should be decentralized














































/*   //add on click to tags rec view objects
        tagsFilterFastAdapter.withSelectable(true);
        tagsFilterFastAdapter.withOnClickListener(new OnClickListener<BrowseFragmentTagsFilterAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<BrowseFragmentTagsFilterAdapter> adapter, BrowseFragmentTagsFilterAdapter item, int position) {


                TextView currentView = (TextView) v;
                String selectedTagString = item.getTagName();
                String selectedTagId = item.getTagId();

                if (!selectedTagsFilters.contains(selectedTagString) && !selectedCategoryFilters.contains(selectedTagString)) {


                    //TODO SOMETIMES A DIFFENT VIEW IS HIGHLIGHTED OTHER THAN SELECTED ONE


                    selectedTagsFilters.add(selectedTagString);
                    selectedTagsFiltersIds.add(selectedTagId);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rect_8dp_green));
                    currentView.setTextColor(getResources().getColor(R.color.white));


                    if (selectedTagsFilters.size() >= 2) {


                        andOrSwitch.setEnabled(true);


                    } else if (selectedCategoryFilters.size() >= 2) {

                        andOrSwitch.setEnabled(true);


                    } else if (selectedCategoryFilters.size() > 0 && selectedTagsFilters.size() > 0) {


                        andOrSwitch.setEnabled(true);


                    } else {


                        andOrSwitch.setChecked(false);
                        andOrSwitch.setEnabled(false);


                    }


                } else {


                    selectedTagsFilters.remove(selectedTagString);
                    selectedTagsFiltersIds.remove(selectedTagId);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rounded_rect_8dp));
                    currentView.setTextColor(Color.parseColor("#7A7E7E"));


                    if (selectedTagsFilters.size() >= 2 || selectedCategoryFilters.size() > 0) {


                        andOrSwitch.setEnabled(true);


                    } else {

                        andOrSwitch.setEnabled(false);

                    }


                }





                return true;
            }
        });



*/














/*
        categoryFilterFastAdapter.withOnClickListener(new OnClickListener<BrowseFragmentCategoryFilterAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<BrowseFragmentCategoryFilterAdapter> adapter, BrowseFragmentCategoryFilterAdapter item, int position) {


                TextView currentView = (TextView) v;
                String selectedCategoryString = item.getCategoryName();
                String selectedCategoryId = item.getCategoryId();

                if (!selectedCategoryFilters.contains(selectedCategoryString) && !selectedTagsFilters.contains(selectedCategoryString)) {


                    //TODO SOMETIMES A DIFFrENT VIEW IS HIGHLIGHTED OTHER THAN SELECTED ONE


                    selectedCategoryFilters.add(selectedCategoryString);
                    selectedCategoryFiltersIds.add(selectedCategoryId);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rect_8dp_green));
                    currentView.setTextColor(getResources().getColor(R.color.white));


                    if (selectedTagsFilters.size() >= 2) {


                        andOrSwitch.setEnabled(true);


                    } else if (selectedCategoryFilters.size() >= 2) {

                        andOrSwitch.setEnabled(true);


                    } else if (selectedCategoryFilters.size() > 0 && selectedTagsFilters.size() > 0) {


                        andOrSwitch.setEnabled(true);


                    } else {


                        andOrSwitch.setChecked(false);
                        andOrSwitch.setEnabled(false);


                    }


                } else {


                    boolean removeMethodResult = selectedCategoryFilters.remove(selectedCategoryString);
                    selectedCategoryFiltersIds.remove(selectedCategoryId);

                    Log.d("MyLogsBrowseFrag", "Remove method result: " + removeMethodResult);

                    currentView.setBackground(getResources().getDrawable(R.drawable.rounded_rect_8dp));
                    currentView.setTextColor(Color.parseColor("#7A7E7E"));


                    if (selectedCategoryFilters.size() >= 2 || selectedTagsFilters.size() > 0) {


                        andOrSwitch.setEnabled(true);


                    } else {

                        andOrSwitch.setEnabled(false);

                    }


                }

                return true;
            }
        });
*/

























/* appliedFiltersItemAdapter.clear();
        appliedFiltersItemAdapter.add(createAppliedFilters(selectedCategoryFilters, selectedCategoryFiltersIds, selectedTagsFilters, selectedTagsFiltersIds));
        appliedFiltersRecView.setAdapter(appliedFiltersFastAdapter);
*/











/*    private List<BrowseFragmentCategoryFilterAdapter> createCategoryFilterItems() {


        ArrayList<BrowseFragmentCategoryFilterAdapter> categoryFilterItems = new ArrayList<>();


        BrowseFragmentCategoryFilterAdapter categoryMen = new BrowseFragmentCategoryFilterAdapter();
        categoryMen.setCategoryName("Men");
        BrowseFragmentCategoryFilterAdapter categoryWomen = new BrowseFragmentCategoryFilterAdapter();
        categoryWomen.setCategoryName("Women");
        BrowseFragmentCategoryFilterAdapter categorySports = new BrowseFragmentCategoryFilterAdapter();
        categorySports.setCategoryName("Sports");
        BrowseFragmentCategoryFilterAdapter categoryAll = new BrowseFragmentCategoryFilterAdapter();
        categoryAll.setCategoryName("All");


        categoryFilterItems.add(categoryMen);
        categoryFilterItems.add(categoryWomen);
        categoryFilterItems.add(categorySports);
        categoryFilterItems.add(categoryAll);


        return categoryFilterItems;

    }
*/