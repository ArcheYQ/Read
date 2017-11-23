package com.example.administrator.read.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.read.R;
import com.example.administrator.read.bean.ReadItem;
import com.example.administrator.read.util.WebUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.ReadHolder> {

    private List<ReadItem> readlist;
    private Context context;
    private LayoutInflater inflater;
    public ReadAdapter(Context context, List<ReadItem> list) {

        this.context = context;
        this.readlist = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ReadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_read, parent, false);
        ReadHolder holder = new ReadHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ReadHolder holder, int position) {
        final ReadItem item = readlist.get(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebUtil.openInternal(context,item.getUrl());
            }
        });
        //将标题设置为 序号.内容这种格式
        holder.tv_name.setText(String.format("%s. %s", position + 1, item.getName()));
        holder.tv_info.setText(item.getUpdateTime()+" • " + item.getFrom());
        Glide.with(context).load(item.getIcon()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(),resource);
                circularBitmapDrawable.setCircular(true);
                holder.iv.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 设置新内容
     * @param data 新内容
     */
    public void setNewData(List<ReadItem> data) {
        this.readlist = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return readlist == null ? 0 : readlist.size();
    }
    //取得data数据
    public List<ReadItem> getData() {
        return readlist;
    }
    //添加data数据
    public void addData(int position, List<ReadItem> data) {
        this.readlist.addAll(position, data);
        this.notifyItemRangeInserted(position, data.size());
    }

    public class ReadHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_info;
        ImageView iv;
        View rootView;

        ReadHolder(View view) {
            super(view);
            rootView = view;
            tv_name =  view.findViewById(R.id.tv_read_name);
            tv_info =  view.findViewById(R.id.tv_read_info);
            iv = view.findViewById(R.id.iv_read_icon);
        }
    }
}

