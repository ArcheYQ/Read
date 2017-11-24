package com.example.administrator.read.activity.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.example.administrator.read.R;
import com.example.administrator.read.activity.MainActivity;
import com.example.administrator.read.adapter.ViewPagerAdapter;
import com.example.administrator.read.bean.PhotoCategory;
import com.example.administrator.read.databinding.FragmentPhotoBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class PhotoFragment extends BaseFragment{

    FragmentPhotoBinding binding;
    List<PhotoCategory> photoCategories= new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void initViews() {
        binding = (FragmentPhotoBinding) getBinding();
        binding.toolbar.setTitle("图片");
        ((MainActivity) getActivity()).initDrawer(binding.toolbar);
        initCategorys();
    }

    @Override
    protected void lazyFetchData() {
        initTabLayout(photoCategories);
    }
    /**
     * 初始化TabLayout
     */
    public void initTabLayout(List<PhotoCategory> photoCategories) {
        setUpViewPager(binding.viewPager, photoCategories);
        binding.viewPager.setOffscreenPageLimit(binding.viewPager.getAdapter().getCount());
        binding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.white));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
    }
    /**
     * 设置ViewPager
     *
     * @param viewPager
     */
    public void setUpViewPager(ViewPager viewPager, List<PhotoCategory> photoCategories) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        for (PhotoCategory category : photoCategories) {
            Fragment categoryFragment = new PhotoCategoryFragment();
            Bundle data = new Bundle();
            data.putString("url", category.getUrl());
            categoryFragment.setArguments(data);
            adapter.addFrag(categoryFragment, category.getName());
        }

        viewPager.setAdapter(adapter);

    }
    public void initCategorys(){

        photoCategories.add(new PhotoCategory("狗狗","http://www.tooopen.com/img/89_321.aspx"));
        photoCategories.add(new PhotoCategory("猫咪","http://www.tooopen.com/img/89_869.aspx"));
        photoCategories.add(new PhotoCategory("兔子","http://www.tooopen.com/img/89_870.aspx"));
        photoCategories.add(new PhotoCategory("鸽子","http://www.tooopen.com/img/89_871.aspx"));
        photoCategories.add(new PhotoCategory("骏马","http://www.tooopen.com/img/89_872.aspx"));
        photoCategories.add(new PhotoCategory("老虎","http://www.tooopen.com/img/89_873.aspx"));
        photoCategories.add(new PhotoCategory("大熊猫","http://www.tooopen.com/img/89_874.aspx"));

    }
}
