package com.example.administrator.read.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.read.R;
import com.example.administrator.read.bean.PhotoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/11/24.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<PhotoItem> photoItems = new ArrayList<>();

    private Context mContext;
    public PhotoAdapter(Context context,  List<PhotoItem> photoItems) {

        mContext = context;
        this.photoItems = photoItems;

    }
    /**
     * 添加数据
     *
     * @param datas 新增的数据
     */
    public void addDatas(List<PhotoItem> datas) {

        photoItems.addAll(datas);

        notifyItemChanged(getItemCount());
    }
    /**
     * 设置新内容
     *
     * @param data 新内容
     */
    public void setNewData(List<PhotoItem> data) {
        this.photoItems = data;
        notifyDataSetChanged();
    }
    //取得data数据
    public List<PhotoItem>  getData() {
        return photoItems;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent,
                false);//这个布局就是一个imageview用来显示图片
        PhotoViewHolder holder = new PhotoViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.name.setText(photoItems.get(position).getName());
        holder.date.setText(photoItems.get(position).getDate());

        ViewGroup.LayoutParams params = holder.iv.getLayoutParams();
        params.width = 520;
        params.height = (new Random().nextInt(100) + 500);
        holder.iv.setLayoutParams(params);

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = PicDetailActivity.newIntent(mContext, photoItems.get(position).getImg(), "");
//                mContext.startActivity(intent);
            }
        });


        //使用params,width 和params.heght 去加载图片
        Glide.with(mContext)
                .load(photoItems.get(position)
                        .getImg())
                .override(params.width, params.height) //设置加载尺寸
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((holder).iv);//加载网络图片
    }

    @Override
    public int getItemCount(){ return photoItems == null ? 0 : photoItems.size();
    }

    //自定义ViewHolder，用于加载图片
    class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView name;
        TextView date;

        PhotoViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById( R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.date);
        }
    }
}
