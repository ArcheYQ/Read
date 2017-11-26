package com.example.administrator.read.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.example.administrator.read.R;
import com.example.administrator.read.util.ImgUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PhotoDetailActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, OnItemClickListener {
    private ImageView imageView;
    private String url;
    private LinearLayout linearLayout;
    private static final int REQUEST_CODE_SAVE_IMG = 10;
    private GestureDetector mGestureDetector;
    private AlertView mAlertView;
    private Subscription subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        imageView = (ImageView) findViewById(R.id.image1);
        url = getIntent().getStringExtra("ImageUrl");
        Glide.with(this).load(url).into(imageView);
        linearLayout = (LinearLayout) findViewById(R.id.content1);
        mAlertView = new AlertView(null, null, null, new String[]{"保存图片"},
                new String[]{"取消"},
                this, AlertView.Style.Alert, this);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 请求读取sd卡的权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(this, mPermissionList)) {
                //已经同意过
                getHttpBitmap(url);
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        "保存图片需要读取sd卡的权限", //提示文言
                        REQUEST_CODE_SAVE_IMG, //请求码
                        mPermissionList //权限列表
                );
            }
        } else {
            getHttpBitmap(url);
        }
    }
    //授权结果，分发下去
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //跳转到onPermissionsGranted或者onPermissionsDenied去回调授权结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    //保存图片
    private void saveImage(Bitmap bitmap) {

        boolean isSaveSuccess = ImgUtil.saveImageToGallery(this, bitmap);
        if (isSaveSuccess) {
            Toast.makeText(this, "保存图片成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }
    public void getHttpBitmap(String url){
        subscription =  Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                Bitmap bitmap = null;
                try
                    {
                        URL pictureUrl = new URL(s);
                        InputStream in = pictureUrl.openStream();
                        bitmap = BitmapFactory.decodeStream(in);
                        in.close();
                    } catch (MalformedURLException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                 return bitmap;

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Bitmap bitmap) {
                saveImage(bitmap);
            }
        });
    }

    //同意授权
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        getHttpBitmap(url);
    }
    //拒绝授权
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //打开系统设置，手动授权
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //拒绝授权后，从系统设置了授权后，返回APP进行相应的操作
            getHttpBitmap(url);
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position){
            case 0:
                requestPermission();
                break;
            case 1:
                break;
        }
    }

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            mAlertView.show();
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            finish();
            return super.onSingleTapUp(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}