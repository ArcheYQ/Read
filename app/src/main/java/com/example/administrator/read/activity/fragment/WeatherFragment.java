package com.example.administrator.read.activity.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.read.R;
import com.example.administrator.read.activity.MainActivity;
import com.example.administrator.read.adapter.SuggestionAdapter;
import com.example.administrator.read.databinding.FragmentWeatherBinding;

import view.DividerGridItemDecoration;

/**
 * Created by Administrator on 2017/11/27.
 */

public class WeatherFragment extends BaseFragment {
    private FragmentWeatherBinding binding;
    SuggestionAdapter suggestionAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initViews() {
        binding= (FragmentWeatherBinding) getBinding();
        binding.toolbar2.setTitle("天气");
        ((MainActivity)getActivity()).initDrawer(binding.toolbar2);
        binding.toolbar2.inflateMenu(R.menu.menu_weather);
        binding.toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        binding.rvSuggestion.setLayoutManager(new GridLayoutManager(getContext(),4));
        suggestionAdapter = new SuggestionAdapter(R.layout.item_suggestion,null);
        suggestionAdapter.setDuration(1000);
        suggestionAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        binding.rvSuggestion.addItemDecoration(new DividerGridItemDecoration(getContext()) );
        binding.rvSuggestion.setAdapter(suggestionAdapter);
    }

    @Override
    protected void lazyFetchData() {

    }
}
