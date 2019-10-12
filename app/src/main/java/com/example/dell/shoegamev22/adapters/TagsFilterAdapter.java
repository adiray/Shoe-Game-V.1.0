package com.example.dell.shoegamev22.adapters;

import android.content.Context;

import com.example.dell.shoegamev22.models.FilterTagModel;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.core.content.ContextCompat;

public class TagsFilterAdapter extends FilterAdapter<FilterTagModel> {


    public TagsFilterAdapter(@NotNull List<? extends FilterTagModel> items, Context context) {
        super(items);
        this.context = context;
    }
    private Context context;
   private int[] mColors;

    public TagsFilterAdapter(@NotNull List<? extends FilterTagModel> items) {
        super(items);
    }




    @NotNull
    @Override
    public FilterItem createView(int i, FilterTagModel filterTagModel) {


        FilterItem filterItem = new FilterItem(context);

        filterItem.setStrokeColor(Integer.valueOf("#c467f4"));
        filterItem.setTextColor(mColors[0]);
        filterItem.setCheckedTextColor(ContextCompat.getColor(context, android.R.color.white));
        filterItem.setColor(ContextCompat.getColor(context, android.R.color.white));
        filterItem.setCheckedColor(Integer.valueOf("#3b9aee"));
        filterItem.setText(filterTagModel.getText());
        filterItem.deselect();

        return filterItem;

    }
}
