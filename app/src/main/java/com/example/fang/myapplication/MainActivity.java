package com.example.fang.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fang.myapplication.views.MyViewPager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 用户的icon
     */
    private ImageView mIconHeaderusinf;
    /**
     * 用户头像虚化背景
     */
    private ImageView imageView;
    /**
     * 底部的关注按钮
     */
    private TextView mConcern;
    /**
     * 底部的私信按钮
     */
    private TextView mSixin, tvTitle;
    private View mBottomContainer;

    private MyViewPager viewPager;
    private TabLayout tabLayout;

    private AppBarLayout appBarLayout;
    private String strImageUrl = "http://img4.imgtn.bdimg.com/it/u=2609553881,3298543509&fm=23&gp=0.jpg";

    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        initView();

        initListener();

        initData();
    }

    private void initView() {

        //找到viewpager
        viewPager = (MyViewPager) findViewById(R.id.viewpager);
        //找到了tabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mIconHeaderusinf = (ImageView) findViewById(R.id.icon_headerusinf);
        imageView = (ImageView) findViewById(R.id.image);
        mConcern = (TextView) findViewById(R.id.cancelfocus);
        mSixin = (TextView) findViewById(R.id.tv_shixin);
        mBottomContainer = findViewById(R.id.bottom_userdetail);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
//        setSupportActionBar(toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        tvTitle.setVisibility(View.VISIBLE);//隐藏播放按钮
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if(state == CollapsingToolbarLayoutState.COLLAPSED){
                            tvTitle.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }

    private void initListener() {
        mConcern.setOnClickListener(this);
        mSixin.setOnClickListener(this);
    }

    private void initData() {
        if (viewPager != null) {
            //设置viewpager 的数据
            setupViewPager(viewPager);
        }
        //给viewpager 关联一共tablayotu
        tabLayout.setupWithViewPager(viewPager);
        Picasso.with(this).load(strImageUrl).into(imageView);
//        new BlurThread().start();
    }

    //虚化图片Thread
    public class BlurThread extends Thread {
        public void run() {
            try {
                Blurry.with(MainActivity.this)
                        //半径
                        .radius(10)
                        //采样
                        .sampling(8)
                        //颜色
//                        .color(Color.argb(66, 255, 255, 0))
                        //异步
                        .async()
                        //动画
//                        .animate(500)
                        .from(Picasso.with(MainActivity.this).load(strImageUrl).get())
                        .into(imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeWindowColor() {
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.WHITE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //为了设置全屏
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        //创建了一个viewpager的数据适配器
        Adapter adapter = new Adapter(getSupportFragmentManager());
        //Fragment作为viewpager 要展示的内容
        adapter.addFragment(new ItemFragment(), "状态");
        adapter.addFragment(new ItemFragment(), "主页");
        adapter.addFragment(new ItemFragment(), "抢聊记录");

        //给viewpager设置数据适配器  viewpager的内容就显示出来 了
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelfocus:
                Toast.makeText(this, "SecondActivity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_shixin:
                Toast.makeText(this, "ThirdActivity", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, ThirdActivity.class);
                startActivity(intent2);
                break;
        }
    }


    //就是vewpager 要展示的数据适配器
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            //添加Fragment
            mFragments.add(fragment);
            //添加标题
            mFragmentTitles.add(title);
        }

        //返回viewpager 中每个条目展示的内容
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        //返回viewpager中一共要展示的条目
        @Override
        public int getCount() {
            //集合的大小
            return mFragments.size();

        }

        //返回内容对应的标题  标题的内容来自于mFragmentTitles 集合
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
