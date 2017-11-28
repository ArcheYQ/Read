package com.example.administrator.read.activity.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.example.administrator.read.R;
import com.example.administrator.read.activity.SettingActivity;
import com.example.administrator.read.app.App;
import com.example.administrator.read.app.AppGlobal;
import com.example.administrator.read.util.FileSizeUtil;
import com.example.administrator.read.util.FileUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/28.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference cleanCache;
    private Preference theme;
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == cleanCache) {
            Observable
                    .just(FileUtil.delete(FileUtil.getInternalCacheDir(App.getContext())))
                    .map(new Func1<Boolean, Boolean>() {
                        @Override
                        public Boolean call(Boolean result) {
                            return result && FileUtil.delete(FileUtil.getExternalCacheDir(App.getContext()));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            cleanCache.setSummary(
                                    FileSizeUtil.
                                            getAutoFileOrFilesSize(
                                                    FileUtil.getInternalCacheDir(App.getContext()),
                                                    FileUtil.getExternalCacheDir(App.getContext())));
                            Snackbar.make(getView(), "缓存已清除 (*^__^*)", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        } else if (preference == theme) {
            new ColorChooserDialog.Builder((SettingActivity) getActivity(), R.string.theme)
                    .customColors(R.array.colors, null)
                    .doneButton(R.string.done)
                    .cancelButton(R.string.cancel)
                    .allowUserColorInput(false)
                    .allowUserColorInputAlpha(false)
                    .show();
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        cleanCache = findPreference("clean_cache");
        theme = findPreference("theme_color");

        String[] colorNames = getActivity().getResources().getStringArray(R.array.color_name);
        SharedPreferences sp = getActivity().getSharedPreferences(AppGlobal.FILE_NAME,
                Context.MODE_PRIVATE);
        int currentThemeIndex = sp.getInt("theme", 1);
        if (currentThemeIndex >= colorNames.length) {
            theme.setSummary("自定义色");
        } else {
            theme.setSummary(colorNames[currentThemeIndex]);
        }

        cleanCache.setSummary(
                FileSizeUtil.
                        getAutoFileOrFilesSize(
                                FileUtil.getInternalCacheDir(App.getContext()),
                                FileUtil.getExternalCacheDir(App.getContext())));
        theme.setOnPreferenceClickListener(this);
        cleanCache.setOnPreferenceClickListener(this);

    }
}
