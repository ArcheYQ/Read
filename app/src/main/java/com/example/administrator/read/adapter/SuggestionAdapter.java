package com.example.administrator.read.adapter;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.read.R;
import com.example.administrator.read.app.App;
import com.example.administrator.read.bean.SuggestionBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class SuggestionAdapter extends BaseQuickAdapter<Object,BaseViewHolder> {
    public SuggestionAdapter(int layoutResId, List<Object> data) {
        super(layoutResId, data);
    }
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) App.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }
    @Override
    protected void convert(BaseViewHolder holder, Object item) {
        int width = getScreenWidth() / 4;
        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        if (item instanceof SuggestionBean.AirBean) {
            holder.setText(R.id.tvName, "空气");
            holder.setText(R.id.tvMsg, ((SuggestionBean.AirBean) item).getBrf());
        } else if (item instanceof SuggestionBean.ComfBean) {
            holder.setText(R.id.tvName, "舒适度");
            holder.setText(R.id.tvMsg, ((SuggestionBean.ComfBean) item).getBrf());
        } else if (item instanceof SuggestionBean.CwBean) {
            holder.setText(R.id.tvName, "洗车");
            holder.setText(R.id.tvMsg, ((SuggestionBean.CwBean) item).getBrf());
        } else if (item instanceof SuggestionBean.DrsgBean) {
            holder.setText(R.id.tvName, "穿衣");
            holder.setText(R.id.tvMsg, ((SuggestionBean.DrsgBean) item).getBrf());
        } else if (item instanceof SuggestionBean.FluBean) {
            holder.setText(R.id.tvName, "感冒");
            holder.setText(R.id.tvMsg, ((SuggestionBean.FluBean) item).getBrf());
        } else if (item instanceof SuggestionBean.SportBean) {
            holder.setText(R.id.tvName, "运动");
            holder.setText(R.id.tvMsg, ((SuggestionBean.SportBean) item).getBrf());
        } else if (item instanceof SuggestionBean.TravBean) {
            holder.setText(R.id.tvName, "旅游");
            holder.setText(R.id.tvMsg, ((SuggestionBean.TravBean) item).getBrf());
        } else if (item instanceof SuggestionBean.UvBean) {
            holder.setText(R.id.tvName, "紫外线");
            holder.setText(R.id.tvMsg, ((SuggestionBean.UvBean) item).getBrf());
        } else if (item instanceof SuggestionBean.AqiBean) {
            holder.setText(R.id.tvName, "空气指数");
            holder.setText(R.id.tvMsg, ((SuggestionBean.AqiBean) item).getCity().getAqi());
        }
    }
}
