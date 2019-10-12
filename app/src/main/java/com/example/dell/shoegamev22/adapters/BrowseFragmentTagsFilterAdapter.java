package com.example.dell.shoegamev22.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.dell.shoegamev22.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseFragmentTagsFilterAdapter extends AbstractItem<BrowseFragmentTagsFilterAdapter, BrowseFragmentTagsFilterAdapter.BrowseFragmentTagsFilterAdapterViewHolder> {




    private String tagName, tagId;


    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }






    /*******************************************************************************************************************************************************
     * FAST ADAPTER CODE STARTS HERE
     *
     */


    @NonNull
    @Override
    public BrowseFragmentTagsFilterAdapterViewHolder getViewHolder(View v) {
        return new BrowseFragmentTagsFilterAdapterViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.browseFragmentTagsFilterSingleItemTagNameTv;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.browse_fragment_tags_filter_rec_view_single_item;
    }


    @Override
    public void bindView(BrowseFragmentTagsFilterAdapterViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);


        holder.tagNameTv.setText(tagName);



    }

    protected static class BrowseFragmentTagsFilterAdapterViewHolder extends RecyclerView.ViewHolder {


        TextView tagNameTv;

        public BrowseFragmentTagsFilterAdapterViewHolder(@NonNull View itemView) {
            super(itemView);


            tagNameTv = itemView.findViewById(R.id.browseFragmentTagsFilterSingleItemTagNameTv);
        }
    }










}
