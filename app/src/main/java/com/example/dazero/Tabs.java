package com.example.dazero;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dazero.HomePage.HomePageFragment;
import com.example.dazero.Profile.ProfileFragment;
import com.google.android.material.tabs.TabLayout;

public class Tabs extends AppCompatActivity {
    private TabLayout tab;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        tab = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPage);

        tab.setupWithViewPager(viewPager);

        AdapterForTabs adapterForTabs = new AdapterForTabs(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        adapterForTabs.addFragment(new SearchFragment(), "SEARCH");
        adapterForTabs.addFragment(new HomePageFragment(), "HOME");
        adapterForTabs.addFragment(new ProfileFragment(), "PROFILE");

        viewPager.setAdapter(adapterForTabs);

    }
}