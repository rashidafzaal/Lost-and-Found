package com.example.rashid.lostandfound.Start;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rashid.lostandfound.Fragment1;
import com.example.rashid.lostandfound.Fragment2;

/**
 * Created by rashid on 7/29/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0)
        {
            return new Fragment1();
        }
        else
            return new Fragment2();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
