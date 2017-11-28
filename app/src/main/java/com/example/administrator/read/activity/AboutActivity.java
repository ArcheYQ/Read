package com.example.administrator.read.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.read.R;
import com.example.administrator.read.util.ShareUtils;
import com.example.administrator.read.util.WebUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class AboutActivity extends BaseActivity {
    private TextView tvVersion;
    private ImageSwitcher imageSwitcher;
    private String[] imageUrls = {
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu01.png",
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu02.png",
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu03.png",
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu04.png",
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu05.png"};

    private Subscription subscription;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setDisplayHomeAsUpEnabled(true);
        tvVersion = (TextView) findViewById(R.id.tv_app_version);
        tvVersion.setText("v" + "1.0");
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(AboutActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_out));
    }

    @Override
    protected void loadData() {
        imageSwitcher.post(new Runnable() {
            @Override
            public void run() {
                loadImage();
            }
        });
        subscription = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        loadImage();
                    }
                });
    }

    private void loadImage() {
        Glide.with(this).load(imageUrls[new Random().nextInt(5)]).into(new SimpleTarget<GlideDrawable>(imageSwitcher.getWidth(), imageSwitcher.getHeight()) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageSwitcher.setImageDrawable(resource);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    private void feedBack() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "951342519@qq.com,", null));
        intent.putExtra(Intent.EXTRA_EMAIL, "951342519@qq.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "反馈");
        startActivity(Intent.createChooser(intent, "反馈"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_web_home:
                WebUtil.openInternal(this, "http://blog.csdn.net/archeh");
                break;
            case R.id.btn_feedback:
                feedBack();
                break;
            case R.id.btn_check_update:
//                UpdateUtil.check(AboutActivity.this, false);
                break;
            case R.id.btn_share_app:
                ShareUtils.shareText(this, "么么哒(づ￣ 3￣)づhttps://github.com/ArcheYQ/Read","分享");
                break;
        }
    }
}
