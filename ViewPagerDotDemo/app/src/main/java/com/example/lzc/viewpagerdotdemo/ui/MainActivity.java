package com.example.lzc.viewpagerdotdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.lzc.viewpagerdotdemo.R;
import com.example.lzc.viewpagerdotdemo.adapter.ViewPagerAdapter;
import com.example.lzc.viewpagerdotdemo.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 类描述：ViewPager无限循环
 * 创建人：zz
 * 创建时间：2017/1/5 10:30
 */
public class MainActivity extends Activity {

    private ViewPager viewPagerId;
    private LinearLayout linearDot;
    private RelativeLayout relativeViewPager;
    private ImageView imageBig;
    private List<View> images = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private int perPosition = 0;
    private int dotTotal = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化UI
     */
    private void initView() {
        timer = new Timer();

        relativeViewPager = ((RelativeLayout) findViewById(R.id.relative_viewpager));
        imageBig = ((ImageView) findViewById(R.id.imageview_big));

        viewPagerId = ((ViewPager) findViewById(R.id.viewpager_id));
        viewPagerAdapter = new ViewPagerAdapter(images,getApplicationContext());
        viewPagerId.setAdapter(viewPagerAdapter);
        linearDot = ((LinearLayout) findViewById(R.id.linearlayout_dot));
        initViewPager();
        viewPagerId.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               linearDot.getChildAt(perPosition).setEnabled(false);
               linearDot.getChildAt(position % dotTotal).setEnabled(true);
               perPosition = position %dotTotal;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        List<Integer> res = new ArrayList<>();
        res.add(R.mipmap.ic_launcher);
        res.add(R.mipmap.ic_launcher);
        res.add(R.mipmap.ic_launcher);
        if(res.size() == 1){
            relativeViewPager.setVisibility(View.GONE);
            imageBig.setVisibility(View.VISIBLE);
            imageBig.setImageResource(res.get(0));
        }else {
            dotTotal = res.size();
            relativeViewPager.setVisibility(View.VISIBLE);
            imageBig.setVisibility(View.GONE);
            //当尺寸为2或者3时自动扩充，否则无限循环会出错
            if(res.size() == 2 || res.size() == 3){
                res.addAll(res);
            }
            for (int i = 0; i < res.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(res.get(i));
                images.add(imageView);
            }
            for (int i = 0; i < dotTotal; i++) {
                //添加圆点
                View dot = new View(this);
                int width = DensityUtils.dip2px(getApplicationContext(),6);
                int marginLeft = DensityUtils.dip2px(getApplicationContext(),8);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,width);
                lp.setMargins(marginLeft,0,0,0);
                dot.setBackgroundResource(R.drawable.dot_selector);
                dot.setLayoutParams(lp);
                dot.setEnabled(false);
                linearDot.addView(dot);
            }
            viewPagerAdapter.addAll(images);
            viewPagerId.setCurrentItem(0+res.size()*10000);   //将ViewPager当前位置放置在第一张，并且足够大
            linearDot.getChildAt(0).setEnabled(true);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         viewPagerId.setCurrentItem(viewPagerId.getCurrentItem() + 1);
                        }
                    });
                }
            };
            timer.schedule(task,2000,2000);             //2s一滚动
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
