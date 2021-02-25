package com.bill.imagedragpro;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * author : Bill
 * date : 2021/2/25
 * description :
 */
public class MyPaperAdapter extends FragmentPagerAdapter {

    private List<Fragment> mData;

    public MyPaperAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void setData(List<Fragment> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }
}
