package com.bill.imagedragpro;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private final int[] imgRes = new int[]{R.drawable.sl, R.drawable.bg};

    public View mParentView;

    private ViewPager mViewPager;
    private MyPaperAdapter mPaperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mParentView = findViewById(R.id.image_parent);

        mViewPager = findViewById(R.id.vp_image);
        mPaperAdapter = new MyPaperAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPaperAdapter);

        mPaperAdapter.setData(getFragments());
        mPaperAdapter.notifyDataSetChanged();

    }

    private List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < imgRes.length; i++) {
            Fragment fragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", imgRes[i]);
            fragment.setArguments(bundle);
            list.add(fragment);
        }
        return list;
    }

}