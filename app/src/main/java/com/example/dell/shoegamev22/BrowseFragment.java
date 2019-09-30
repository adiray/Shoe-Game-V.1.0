package com.example.dell.shoegamev22;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BrowseFragment extends Fragment {

    private View view;

    private ExpandableLayout filterExpandableLayout;
    private TextView addFiltersTv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.browse_fragment, container, false);




        filterExpandableLayout = view.findViewById(R.id.browseFragmentExpandableLayout);
        addFiltersTv = view.findViewById(R.id.browseFragmentAddFiltersTv);

        addFiltersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (filterExpandableLayout.isExpanded()){


                    filterExpandableLayout.collapse();
                    addFiltersTv.setTextColor(getResources().getColor(R.color.myColorInactiveTextBg));

                }else {

                    filterExpandableLayout.expand();
                    addFiltersTv.setTextColor(getResources().getColor(R.color.myColorAccentDark));


                }





            }
        });










        return view;
    }
}
