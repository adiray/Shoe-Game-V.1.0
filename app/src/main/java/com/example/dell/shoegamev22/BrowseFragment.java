package com.example.dell.shoegamev22;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.backendless.persistence.DataQueryBuilder;
import com.example.dell.shoegamev22.adapters.BrowseFragmentCategoryFilterAdapter;
import com.example.dell.shoegamev22.adapters.BrowseFragmentTagsFilterAdapter;
import com.example.dell.shoegamev22.adapters.TagsFilterAdapter;
import com.example.dell.shoegamev22.viewmodels.BrowseFragmentViewModel;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.novoda.merlin.MerlinsBeard;
import com.suke.widget.SwitchButton;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseFragment extends Fragment {

    private View view;


    //VIEWS
    RecyclerView categoryFilterRecView, tagsFilterRecView;
    private ExpandableLayout filterExpandableLayout;
    private TextView addFiltersTv, priceFilterIndicatorTv , andOrSwitchAndTv,andOrSwitchOrTv;
    SeekBar priceSeekBar;
    SwitchButton andOrSwitch;
    Button filterButton;

    //VIEW MODELS
    BrowseFragmentViewModel browseFragmentViewModel;

    //merlin
    private MerlinsBeard merlinsBeard;


    //BACKENDLESS
    DataQueryBuilder tagsQueryBuilder = DataQueryBuilder.create();
    DataQueryBuilder shoesQueryBuilder = DataQueryBuilder.create();

    //VARIABLES
    List<String> selectedCategoryFilters = new ArrayList<>();
    List<String> selectedTagsFilters = new ArrayList<>();
    Integer selectedMaxPrice = 200;
    String whereClauseConjuction = "AND";
    List<Map>tagsResponse = new ArrayList<>();



    //create our FastAdapter which will manage everything
    private ItemAdapter<BrowseFragmentCategoryFilterAdapter> categoryFilterItemAdapter;
    private FastAdapter<BrowseFragmentCategoryFilterAdapter> categoryFilterFastAdapter;
    private ItemAdapter<BrowseFragmentTagsFilterAdapter> tagsFilterItemAdapter;
    private FastAdapter<BrowseFragmentTagsFilterAdapter> tagsFilterFastAdapter;
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



        //INITIALIZE RECYCLER VIEWS
        //category filter rec view
        categoryFilterRecView = view.findViewById(R.id.browseFragmentCategoryFilterRecView);
        categoryFilterRecView.setHasFixedSize(true);
        categoryFilterRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        tagsFilterRecView = view.findViewById(R.id.browseFragmentTagsFilterRecView);
        tagsFilterRecView.setHasFixedSize(true);
        tagsFilterRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));


        //add data to category filter
        categoryFilterItemAdapter.add(createCategoryFilterItems());
        categoryFilterRecView.setAdapter(categoryFilterFastAdapter);



        //set up and/or conjunction switch
        andOrSwitch.setEnabled(false);
        andOrSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (isChecked){

                    whereClauseConjuction = "OR";
                    andOrSwitchAndTv.setTextColor(Color.parseColor("#7A7E7E"));
                    andOrSwitchOrTv.setTextColor(getResources().getColor(R.color.myColorAccentDark));


                }else {

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



                if (selectedMaxPrice<=0){


                    selectedMaxPrice = 200;
                    String maxPriceText = 200 + "$";
                    priceFilterIndicatorTv.setText(maxPriceText);
                    priceFilterIndicatorTv.setTextColor(Color.parseColor("#7A7E7E"));



                }else {

                    priceFilterIndicatorTv.setTextColor(getResources().getColor(R.color.myColorAccentDark));

                }

            }
        });


        //add on click to tags rec view objects
        tagsFilterFastAdapter.withSelectable(true);
        tagsFilterFastAdapter.withOnClickListener(new OnClickListener<BrowseFragmentTagsFilterAdapter>() {
            @Override
            public boolean onClick( View v, IAdapter<BrowseFragmentTagsFilterAdapter> adapter, BrowseFragmentTagsFilterAdapter item, int position) {

                TextView currentView = (TextView) v;
                String selectedTagString = item.getTagName();

                if (!selectedTagsFilters.contains(selectedTagString)) {


                    //TODO SOMETIMES A DIFFENT VIEW IS HIGHLIGHTED OTHER THAN SELECTED ONE


                    selectedTagsFilters.add(selectedTagString);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rect_8dp_green));
                    currentView.setTextColor(getResources().getColor(R.color.white));



                    if (selectedTagsFilters.size()>=2){


                        andOrSwitch.setEnabled(true);



                    }else if(selectedCategoryFilters.size()>=2){

                        andOrSwitch.setEnabled(true);


                    }else if(selectedCategoryFilters.size()>0 && selectedTagsFilters.size()>0){


                        andOrSwitch.setEnabled(true);



                    }else{


                        andOrSwitch.setChecked(false);
                        andOrSwitch.setEnabled(false);


                    }




                } else {


                    selectedTagsFilters.remove(selectedTagString);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rounded_rect_8dp));
                    currentView.setTextColor(Color.parseColor("#7A7E7E"));


                    if (selectedTagsFilters.size()>=2 || selectedCategoryFilters.size()>0){


                        andOrSwitch.setEnabled(true);



                    }else{

                        andOrSwitch.setEnabled(false);

                    }






                }


                return true;
            }
        });



        //add on click to category rec view objects
        categoryFilterFastAdapter.withSelectable(true);
        categoryFilterFastAdapter.withOnClickListener(new OnClickListener<BrowseFragmentCategoryFilterAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<BrowseFragmentCategoryFilterAdapter> adapter, BrowseFragmentCategoryFilterAdapter item, int position) {


                TextView currentView = (TextView) v;
                String selectedCategoryString = item.getCategoryName();

                if (!selectedCategoryFilters.contains(selectedCategoryString)) {


                    //TODO SOMETIMES A DIFFENT VIEW IS HIGHLIGHTED OTHER THAN SELECTED ONE


                    selectedCategoryFilters.add(selectedCategoryString);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rect_8dp_green));
                    currentView.setTextColor(getResources().getColor(R.color.white));


                    if (selectedTagsFilters.size()>=2){


                        andOrSwitch.setEnabled(true);



                    }else if(selectedCategoryFilters.size()>=2){

                        andOrSwitch.setEnabled(true);


                    }else if(selectedCategoryFilters.size()>0 && selectedTagsFilters.size()>0){


                        andOrSwitch.setEnabled(true);



                    }else{


                        andOrSwitch.setChecked(false);
                        andOrSwitch.setEnabled(false);


                    }




                } else {


                    selectedCategoryFilters.remove(selectedCategoryString);
                    currentView.setBackground(getResources().getDrawable(R.drawable.rounded_rect_8dp));
                    currentView.setTextColor(Color.parseColor("#7A7E7E"));





                    if (selectedCategoryFilters.size()>=2 || selectedTagsFilters.size()>0){


                        andOrSwitch.setEnabled(true);



                    }else{

                        andOrSwitch.setEnabled(false);

                    }


                }

                return true;
            }
        });




        //add the observers
        addObservers();


        //initialize data query builder
        tagsQueryBuilder.setSortBy("priority%20desc");
        tagsQueryBuilder.setPageSize(20);


        //REQUEST INITIAL DATA
        if (merlinsBeard.isConnected()) {


            browseFragmentViewModel.getAllTags(tagsQueryBuilder);


        }else{



            Log.d("MyLogsTags", "Browse fragment: MERLIN. response: NO INTERNET ACCESS");



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


        return view;
    }


    private List<BrowseFragmentCategoryFilterAdapter> createCategoryFilterItems() {


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

    private void addObservers() {


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


    private List<BrowseFragmentTagsFilterAdapter> createTagsFilterItems(List<Map> tagsResponse) {

        List<BrowseFragmentTagsFilterAdapter> tagsData = new ArrayList<>();

        Integer count = tagsResponse.size() - 1;

        for (int i = 0; i <= count; i++) {

            BrowseFragmentTagsFilterAdapter holder = new BrowseFragmentTagsFilterAdapter();
            String tagName = (String) tagsResponse.get(i).get("name");
            holder.setTagName(tagName);

            tagsData.add(holder);


        }


        return tagsData;


    }

    private void filterButtonClicked(){





        String whereClause = "";








                //35AE1FCD-F2B8-1E6C-FF57-B3CEB92BFB00
        //757AC723-A3D8-1113-FF95-A3F89FDC1600


    }


}
