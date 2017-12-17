package com.example.kuba.tvmanager;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.kuba.tvmanager.Activities.FragmentComplete;
import com.example.kuba.tvmanager.Activities.FragmentFavourite;

public class TabActivity extends AppCompatActivity {
    private static final String TAG = "TabActivity";
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*String title = (String) sectionsPageAdapter.getPageTitle(position);
                Fragment frg = null;
                if(position == 0){
                    frg = new FragmentComplete();
                    sectionsPageAdapter.replace(frg, title, position);
                }
                else{
                    frg = new FragmentComplete();
                    sectionsPageAdapter.replace(frg, title, position);
                }*/
                //setupViewPager(viewPager);

                Fragment frg = sectionsPageAdapter.getItem(position);
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        sectionsPageAdapter.addFragment(new FragmentComplete(), "Show List");
        sectionsPageAdapter.addFragment(new FragmentFavourite(), "Favourite List");
        viewPager.setAdapter(sectionsPageAdapter);
    }


}
