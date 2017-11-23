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
import java.util.concurrent.locks.Lock;

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
        final String host = "http://www.meizhuang.com/makeup/";

        subscription = Observable.just(host).subscribeOn(Schedulers.io()).map(
                new Func1<String, List<ReadCategory>>() {

                    @Override
                    public List<ReadCategory> call(String s) {
                        List<ReadCategory> list = new ArrayList<>();
                        Log.d("test","name: ");
                        try {
                            Document doc = Jsoup.connect(host).timeout(5000).get();
                            Log.d("test","123"+doc.toString());
                            Element cate = doc.select("div.li-main").get(1);
                            Log.d("test","name: "+cate.toString());
                            Elements links = cate.select("span.hide-nav");
                            Log.d("test","links: "+cate.toString());
                            Elements linkss = cate.select("a");
                            for (Element element : linkss) {
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
            fragment.setArguments(data);//但当我们实例化自定义Fragment时，为什么官方推荐Fragment.setArguments(Bundle bundle)这种方式来传递参数，横屏后数据也不会丢失
            adapter.addFrag(fragment, category.getName());
        }
        viewPager.setAdapter(adapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

}
