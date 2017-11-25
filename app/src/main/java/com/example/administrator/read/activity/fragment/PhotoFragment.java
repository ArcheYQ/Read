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
        binding.toolbar1.setTitle("图片");
        ((MainActivity) getActivity()).initDrawer(binding.toolbar1);
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
        setUpViewPager(binding.viewPager1, photoCategories);
        binding.viewPager1.setOffscreenPageLimit(binding.viewPager1.getAdapter().getCount());
        binding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.white));
        binding.tabLayout.setupWithViewPager(binding.viewPager1);
        binding.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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

        photoCategories.add(new PhotoCategory("摄影世界o(*￣▽￣*)ブ","http://www.egouz.com/pics/icon/"));
        photoCategories.add(new PhotoCategory("插画设计(●'◡'●)","http://www.egouz.com/pics/vector/"));
        photoCategories.add(new PhotoCategory("桌面壁纸( ▼-▼ )","http://www.egouz.com/pics/wallpaper/"));
        photoCategories.add(new PhotoCategory("艺术人生(￣o￣) . z Z","http://www.egouz.com/pics/pattern/"));

    }
}
