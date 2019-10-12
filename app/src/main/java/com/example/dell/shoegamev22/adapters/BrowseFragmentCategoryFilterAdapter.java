package com.example.dell.shoegamev22.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.dell.shoegamev22.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseFragmentCategoryFilterAdapter extends AbstractItem<BrowseFragmentCategoryFilterAdapter, BrowseFragmentCategoryFilterAdapter.BrowseFragmentCategoryFilterAdapterViewHolder> {




    private String categoryName;
    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }



    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }




    /*******************************************************************************************************************************************************
     * FAST ADAPTER CODE STARTS HERE
     *
     */


    @NonNull
    @Override
    public BrowseFragmentCategoryFilterAdapterViewHolder getViewHolder(View v) {
        return new BrowseFragmentCategoryFilterAdapterViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.browseFragmentCategoryFilterSingleItemCategoryNameTv;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.browse_fragment_category_filter_rec_view_single_item;
    }


    @Override
    public void bindView(BrowseFragmentCategoryFilterAdapterViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);


        holder.categoryNameTv.setText(categoryName);



    }

    protected static class BrowseFragmentCategoryFilterAdapterViewHolder extends RecyclerView.ViewHolder {


        TextView categoryNameTv;

        public BrowseFragmentCategoryFilterAdapterViewHolder(@NonNull View itemView) {
            super(itemView);


            categoryNameTv = itemView.findViewById(R.id.browseFragmentCategoryFilterSingleItemCategoryNameTv);
        }
    }











}
