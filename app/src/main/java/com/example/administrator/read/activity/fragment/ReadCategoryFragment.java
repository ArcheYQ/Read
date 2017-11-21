package com.example.administrator.read.activity.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.example.administrator.read.R;
import com.example.administrator.read.adapter.ReadAdapter;
import com.example.administrator.read.bean.ReadItem;
import com.example.administrator.read.databinding.FragmentCategoryBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/21.
 */

/**
 * 阅读的子类
 */

public class ReadCategoryFragment extends BaseFragment{
    private int cutPage = 1;
    private String baseUrl = "";

    private ReadAdapter adapter;
    private Subscription subscription;
    private FragmentCategoryBinding binding;
    @Override
    protected int getLayoutId(){return R.layout.fragment_category;
    }

    @Override
    protected void initViews() {

        binding = (FragmentCategoryBinding) getBinding();
        adapter = new ReadAdapter(getContext(), null);

        setting();
    }

    @Override
    protected void lazyFetchData() {

    }
    /**
     * 设置监听等
     */
    public void setting() {

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getDataFromServer();
            }
        });

    }

    /**
     * 从服务器上获取数据
     */
    public void getDataFromServer() {

        baseUrl = getArguments().getString("url");
        final String url = baseUrl + "/page/" + cutPage;

        binding.swipeRefreshLayout.setRefreshing(true);
        subscription = Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, List<ReadItem>>() {
            @Override
            public List<ReadItem> call(String s) {
                List<ReadItem> readList = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect(url).timeout(5000).get();
                    Element element = doc.select("div.xiandu_items").first();
                    Elements items = element.select("div.xiandu_item");
                    for (Element ele : items) {

                        ReadItem item = new ReadItem();
                        Element left = ele.select("div.xiandu_left").first();
                        Element right = ele.select("div.xiandu_right").first();

                        item.setName(left.select("a[href]").text());
                        item.setFrom(right.select("a").attr("title"));
                        item.setUpdateTime(left.select("span").select("small").text());
                        item.setUrl(left.select("a[href]").attr("href"));
                        item.setIcon(right.select("img").first().attr("src"));
                        readList.add(item);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return readList;

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ReadItem>>() {
            @Override
            public void onCompleted() {

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(List<ReadItem> list) {
                cutPage++;
                if (adapter.getData() == null || adapter.getData().size() == 0) {
                    adapter.setNewData(list);
                } else {
                    adapter.addData(adapter.getData().size(), list);
                }

            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
