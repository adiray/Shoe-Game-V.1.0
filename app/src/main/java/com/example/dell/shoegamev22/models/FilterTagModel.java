package com.example.dell.shoegamev22.models;

import com.yalantis.filter.model.FilterModel;

import org.jetbrains.annotations.NotNull;

public class FilterTagModel implements FilterModel {


    private String itemText;

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    @NotNull
    @Override
    public String getText() {
        return itemText;
    }
}
