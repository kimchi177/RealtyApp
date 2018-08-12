package com.example.thu.realtysocial.Adapter;


import android.app.NotificationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.thu.realtysocial.Fragment.MesageFragment;
import com.example.thu.realtysocial.Fragment.NewsFragment;
import com.example.thu.realtysocial.Fragment.NotificationFragment;
import com.example.thu.realtysocial.Fragment.ProfileFragment;
//import com.example.thu.realtysocial.Fragment.NotificationManager;

public class TabPaperAdapter extends FragmentPagerAdapter {
    public TabPaperAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewsFragment newsFragment = new NewsFragment();
                return newsFragment;
            case 1:
                MesageFragment mesageFragment = new MesageFragment();
                return mesageFragment;
            case 2:
                NotificationFragment notificationFragment=new NotificationFragment();
                return notificationFragment;
            case 3:
                ProfileFragment profileFragment=new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "News";
            case 1:
                return "Message";
            case 2:
                return "Notification";
            case 3:
                return "Profile";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
