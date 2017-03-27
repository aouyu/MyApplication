package com.example.fang.myapplication.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/3/27.
 */

public class MyViewPager extends ViewPager {

    private int startX;
    private int startY;
    private int intHorizontal = 0;  //是否为横向滑动

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int y = (int) ev.getRawY();
        int x = (int) ev.getRawX();
        boolean resume = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                Log.e("FXT", "按下");
                Log.e("FXT", String.valueOf(startX) + "---Y---" + String.valueOf(startY));
                break;
            case MotionEvent.ACTION_MOVE:
                int dX = (int) (ev.getX() - startX);
                int dY = (int) (ev.getY() - startY);
                Log.e("FXT", String.valueOf(Math.abs(dX)) + "---Y---" + String.valueOf(Math.abs(dY)));
                if (intHorizontal == 0) {
                    if (Math.abs(dX) > Math.abs(dY)) {  //左右滑动
                        Log.e("FXT", "左右");
                        intHorizontal = 1;
                        return true;
                    } else {
                        Log.e("FXT", "上下");
                        intHorizontal = 2;
                        return false;
                    }
                }
            case MotionEvent.ACTION_UP:
                Log.e("FXT", "抬起");
                intHorizontal = 0;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
