package com.example.housing.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.housing.R;
import com.example.housing.fragments.BookingFragment;
import com.example.housing.fragments.DashboardFragment;
import com.example.housing.fragments.NotificationFragment;
import com.example.housing.fragments.ProfileFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity
{
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private HomePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.bottom_tabs);

        pagerAdapter = new HomePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy()
                {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position)
                    {
                        switch (position)
                        {
                            case 0:
                                tab.setIcon(R.drawable.home);
                                break;
                            case 1:
                                tab.setIcon(R.drawable.book);
                                break;
                            case 2:
                                tab.setIcon(R.drawable.notification);
                                break;
                            case 3:
                                tab.setIcon(R.drawable.profile);
                                break;
                        }
                    }
                }).attach();
    }

    private static class HomePagerAdapter extends FragmentStateAdapter
    {
        public HomePagerAdapter(@NonNull AppCompatActivity fragmentActivity)
        {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
            switch (position)
            {
                case 1:
                    return new BookingFragment();
                case 2:
                    return new NotificationFragment();
                case 3:
                    return new ProfileFragment();
                default:
                    return new DashboardFragment();
            }
        }

        @Override
        public int getItemCount()
        {
            return 4;
        }
    }
}
