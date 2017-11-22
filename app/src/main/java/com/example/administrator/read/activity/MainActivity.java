package com.example.administrator.read.activity;

import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.read.R;
import com.example.administrator.read.activity.fragment.ReadFragment;
import com.example.administrator.read.app.AppGlobal;
import com.example.administrator.read.util.DoubleClickExit;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;

    private String currentFragmentTag;

    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    private static final String FRAGMENT_TAG_PHOTO = "photo";
    private static final String FRAGMENT_TAG_READING = "reading";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        Log.i("", "initViews: "+"m");
        fragmentManager = getSupportFragmentManager();
        initNavigationViewHeader();
        initFragment(savedInstanceState);
        Log.i("", "initViews: "+"m");
    }

    @Override
    protected void loadData() {

    }


    private void initNavigationViewHeader() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.inflateHeaderView(R.layout.drawer_header);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelected());
    }
    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            switchContent(FRAGMENT_TAG_READING);
        } else {
            currentFragmentTag = savedInstanceState.getString(AppGlobal.CURRENT_INDEX);
            switchContent(currentFragmentTag);
        }
    }
    /**
     * 选择当前的Fragment
     *
     * @param name Fragment的名字
     */
    public void switchContent(String name) {

        Log.d("error", "switchContent");
        if (currentFragmentTag != null && currentFragmentTag.equals(name))
            return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        Fragment foundFragment = fragmentManager.findFragmentByTag(name);
        if (foundFragment == null) {
            switch (name) {
                case FRAGMENT_TAG_PHOTO:
                 foundFragment = new ReadFragment();
                    break;
                case FRAGMENT_TAG_READING:
                    foundFragment = new ReadFragment();
                    break;
            }
        }
        if (foundFragment == null) {
        } else if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.layout_content, foundFragment, name);
        }
        ft.commit();
        currentFragmentTag = name;
        invalidateOptionsMenu();
        //3.2执行时改变Menu ItemonCreateOptionsMenu()仅仅有在menu刚被创建时才会执行。因此要想随时动态改变OptionMenu就要实现onPrepareOptionsMenu()方法。该方法会传给你Menu对象，供使用
       // Android2.3或更低的版本号会在每次Menu打开的时候调用一次onPrepareOptionsMenu().
                //Android3.0及以上版本号默认menu是打开的。所以必须调用invalidateOptionsMenu()方法，然后系统将调用onPrepareOptionsMenu()运行update操作。

    }

    /**
     * 用来开关DrawerLayout
     *
     * @param toolbar
     */
    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.refresh, R.string.refresh) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            //更新状态
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }
    }

    /**
     * showSnack
     *
     * @param s 要显示的内容
     */
    public void showSnack(String s) {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.drawerLayout), s, Toast.LENGTH_LONG);
        snackbar.show();
    }


    private class NavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_read:
                    menuItem.setChecked(true);//勾选状态
                    switchContent(FRAGMENT_TAG_READING);
                    break;
                case R.id.navigation_photo:
                    menuItem.setChecked(true);
                    switchContent(FRAGMENT_TAG_PHOTO);
                    break;
                case R.id.navigation_setting:
                    menuItem.setChecked(true);
//                    SettingActivity.createActivity(MainActivity.this);
                    break;
                case R.id.navigation_about:
//                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
            }
            mDrawerLayout.closeDrawer(Gravity.START);
            return false;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    @Override
    protected void onDestroy() {
//        if (EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickExit.check()) {
                Snackbar.make(MainActivity.this.getWindow().getDecorView().findViewById(android.R.id.content), "再按一次退出 App!", Snackbar.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppGlobal.CURRENT_INDEX, currentFragmentTag);
    }

}
