package com.palebluedot.potion.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.palebluedot.potion.mypotion.MyPotionsFragment;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mData;

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mData = new ArrayList<>();
        mData.add(new MyPotionsFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
