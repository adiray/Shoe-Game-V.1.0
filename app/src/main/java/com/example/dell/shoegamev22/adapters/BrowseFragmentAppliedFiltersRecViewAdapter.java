package com.example.dell.shoegamev22.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.dell.shoegamev22.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseFragmentAppliedFiltersRecViewAdapter extends AbstractItem<BrowseFragmentAppliedFiltersRecViewAdapter, BrowseFragmentAppliedFiltersRecViewAdapter.BrowseFragmentAppliedFiltersRecViewAdapterViewHolder> {


//BrowseFragmentAppliedFiltersRecViewAdapterViewHolder


    private String filterName;
    private String filterId;


    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    /*******************************************************************************************************************************************************
     * FAST ADAPTER CODE STARTS HERE
     *
     */


    @NonNull
    @Override
    public BrowseFragmentAppliedFiltersRecViewAdapterViewHolder getViewHolder(View v) {
        return new BrowseFragmentAppliedFiltersRecViewAdapterViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.browseFragmentAppliedFiltersRecViewSingleItemFilterNameTv;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.browse_fragment_applied_filters_rec_view_single_object;
    }


    @Override
    public void bindView(BrowseFragmentAppliedFiltersRecViewAdapterViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);


        holder.filterNameTv.setText(filterName);


    }

    public static class BrowseFragmentAppliedFiltersRecViewAdapterViewHolder extends RecyclerView.ViewHolder {


        TextView filterNameTv;

        public BrowseFragmentAppliedFiltersRecViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);


            filterNameTv = itemView.findViewById(R.id.browseFragmentAppliedFiltersRecViewSingleItemFilterNameTv);
        }
    }


}
