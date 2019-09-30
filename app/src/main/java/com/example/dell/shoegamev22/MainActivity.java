package com.example.dell.shoegamev22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.backendless.Backendless;
import com.example.dell.shoegamev22.viewpageradapter.MainActivityViewPagerAdapter;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

public class MainActivity extends AppCompatActivity {


    //views
    ViewPager mainViewPager;
    BubbleNavigationConstraintView mBubbleNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialize backendless
        Backendless.initApp(getApplicationContext(),
                "05DBC061-3DE1-0252-FF3C-FBCECC684700",
                "EB559093-3624-CE96-FFEF-AECE72F44100");


        initializeViews();


    }


    private void initializeViews() {


        //get references to the views
        mainViewPager = findViewById(R.id.mainActivityViewPager);
        mBubbleNav = findViewById(R.id.mainActivityTopTabLayout);
        //mBubbleNavBottom = findViewById(R.id.main_activity_bottom_navigation_view_linear);

        MainActivityViewPagerAdapter mainActivityViewPagerAdapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
        mainActivityViewPagerAdapter.addFragment(new HomeFragment(), "Home");
        mainActivityViewPagerAdapter.addFragment(new BrowseFragment(), "Browse");
        mainActivityViewPagerAdapter.addFragment(new ProfileFragment(), "Profile");
        mainViewPager.setAdapter(mainActivityViewPagerAdapter);
        //topTabLayout.setupWithViewPager(mainViewPager);


        /*
         Add an OnPageChangeListener so that you can change the active item on the bubble tab layout when the user swipes instead of clicking on the layout
          When the user clicks on the layout add an mBubbleNav.setNavigationChangeListener to change the fragment in the viewpager to the position corresponding
          with the click*/

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mBubbleNav.setCurrentActiveItem(position);
                //mBubbleNavBottom.setCurrentActiveItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBubbleNav.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                mainViewPager.setCurrentItem(position, true);
            }
        });


        mainViewPager.setOffscreenPageLimit(3);


    }


}
