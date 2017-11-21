package com.example.administrator.read.activity.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.administrator.read.R;
import com.example.administrator.read.activity.MainActivity;
import com.example.administrator.read.adapter.ViewPagerAdapter;
import com.example.administrator.read.bean.ReadCategory;
import com.example.administrator.read.databinding.FragmentReadBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/19.
 */

public class ReadFragment extends BaseFragment {
    private FragmentReadBinding binding;

    private Subscription subscription;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read;
    }

    @Override
    protected void initViews() {
        binding= (FragmentReadBinding) getBinding();
        binding.toolbar.setTitle("阅读");
        ((MainActivity)getActivity()).initDrawer(binding.toolbar);
    }

    @Override
    protected void lazyFetchData() {
        getCategory();
    }
    /**
     * 获取分类
     */
    private void getCategory() {
        final String host = "http://gank.io/xiandu";

        subscription = (Subscription) Observable.just(host).subscribeOn(Schedulers.io()).map(
                new Func1<String, List<ReadCategory>>() {
                    @Override
                    public List<ReadCategory> call(String s) {
                        List<ReadCategory> list = new ArrayList<ReadCategory>();
                        try {
                            Document doc = Jsoup.connect(host).timeout(5000).get();
                            Element cate = doc.select("div#xiandu_cat").first();
                            Elements links = cate.select("a[href]");
                            for (Element element : links) {
                                ReadCategory category = new ReadCategory();
                                category.setName(element.text());
                                category.setUrl(element.attr("abs:href"));
                                list.add(category);
                                Log.d("test","name: "+category.getName());
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if (list.size() > 0) {
                            list.get(0).setUrl(host + "/wow");
                        }
                        return list;
                    }
                }
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ReadCategory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ReadCategory> readCategories) {
                initTabLayout(readCategories);
            }
        });
    }
    /**
     * 初始化TabLayout
     * @param readCategories 标签类
     */
    private void initTabLayout(List<ReadCategory> readCategories) {

        setupViewPager(binding.viewPager, readCategories);
        binding.viewPager.setOffscreenPageLimit(binding.viewPager.getAdapter().getCount());
        binding.tablayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(),R.color.white));
        binding.tablayout.setupWithViewPager(binding.viewPager);
        binding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    /**
     * 初始化ViewPager
     * @param viewPager ViewPager
     * @param readCategories 标签类
     */
    private void setupViewPager(ViewPager viewPager, List<ReadCategory> readCategories) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (ReadCategory category : readCategories) {
            Fragment fragment = new ReadCategoryFragment();
            Bundle data = new Bundle();
            data.putString("url",category.getUrl());
            fragment.setArguments(data);
            adapter.addFrag(fragment, category.getName());
        }
        viewPager.setAdapter(adapter);
    }

}
