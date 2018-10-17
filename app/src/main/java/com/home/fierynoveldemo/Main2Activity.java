package com.home.fierynoveldemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";

    private boolean isClickCencter = false, isClickLeft = false, isClickRight = false;
    private int width, height, functionMenuCoordinatesXCenter, functionMenuCoordinatesX1,
            functionMenuCoordinatesX2, functionMenuCoordinatesY1, functionMenuCoordinatesY2;
    private float downX, downY, moveX, moveY;
    private ViewPager viewPager;
    private PagerFragmentPagerAdapter pagerFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeStatusBar();
        setContentView(R.layout.activity_main2);

        /** 初始化ViewPager */
        viewPager = findViewById(R.id.viewPager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {  // 当前状态
                    case MotionEvent.ACTION_DOWN:

                        /** 判斷點擊螢幕的哪個部分, 然後如果0.1秒內鬆開手指, 就會觸發該功能 */
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        if (downX >= functionMenuCoordinatesX1 && downX <= functionMenuCoordinatesX2) {
                            if (downY >= functionMenuCoordinatesY1 && downY <= functionMenuCoordinatesY2) {
                                isClickCencter = true;
                                delayedRecovery("center");
                            } else {
                                if (downX < functionMenuCoordinatesXCenter) {
                                    isClickLeft = true;
                                    delayedRecovery("left");
                                } else {
                                    isClickRight = true;
                                    delayedRecovery("right");
                                }
                            }
                        } else {
                            if (downX < functionMenuCoordinatesXCenter) {
                                isClickLeft = true;
                                delayedRecovery("left");
                            } else {
                                isClickRight = true;
                                delayedRecovery("right");
                            }
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:

                        /** 如果點擊螢幕後, 有滑動的動作, 而且滑動超過一定距離, 就會結束實現點擊翻頁的功能 */
                        moveX = motionEvent.getX();
                        moveY = motionEvent.getY();
                        if (moveX > downX + 100 || moveX < downX - 100 ||
                                moveY > downY + 100 || moveY < downY - 100) {
                            isClickCencter = false;
                            isClickLeft = false;
                            isClickRight = false;
                        }
                        break;

                    case MotionEvent.ACTION_UP:

                        /** 判斷手指點擊哪個部分, 並執行對應的功能 */
                        if (isClickCencter) {
                            Toast.makeText(Main2Activity.this, "彈出控制選單", Toast.LENGTH_SHORT).show();
                        }
                        if (isClickLeft) {
                            simulatedSlidingEvent(viewPager, "left", width);
                        }
                        if (isClickRight) {
                            simulatedSlidingEvent(viewPager, "right", width);
                        }
                        break;
                }
                return false;
            }
        });
    }

    /** 0.1秒後恢復數據 */
    private void delayedRecovery(final String section) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    switch (section) {
                        case "center":
                            isClickCencter = false;
                            break;
                        case "left":
                            isClickLeft = false;
                            break;
                        case "right":
                            isClickRight = false;
                            break;
                    }
                }
            }
        }).start();
    }

    /**
     * 模拟滑动事件
     */
    public static void simulatedSlidingEvent(final View view, String direction, int slidingdistance) {
        int x1 = 0, x2 = 0, x3 = 0;
        switch (direction) {
            case "left":
                x1 = 1;
                x2 = (int) (slidingdistance * 0.5);
                x3 = slidingdistance;
                break;
            case "right":
                x1 = slidingdistance;
                x2 = (int) (slidingdistance * 0.5);
                x3 = 1;
                break;
        }
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, x1, 1, 0));
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_MOVE, x2, 1, 0));
        final int finalX3 = x3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_MOVE, finalX3, 1, 0));
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP, finalX3, 1, 0));
            }
        }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            public void run() {

                /** 取得螢幕長寬以及相關參數 */
                width = viewPager.getWidth();
                height = viewPager.getHeight();
                functionMenuCoordinatesXCenter = width / 2;
                functionMenuCoordinatesX1 = width / 3;
                Log.d(TAG, "functionMenuCoordinatesX1: " + functionMenuCoordinatesX1);
                functionMenuCoordinatesX2 = width / 3 * 2;
                Log.d(TAG, "functionMenuCoordinatesX2: " + functionMenuCoordinatesX2);
                functionMenuCoordinatesY1 = (height / 2) - (width / 3 / 2);
                Log.d(TAG, "functionMenuCoordinatesY1: " + functionMenuCoordinatesY1);
                functionMenuCoordinatesY2 = (height / 2) + (width / 3 / 2);
                Log.d(TAG, "functionMenuCoordinatesY2: " + functionMenuCoordinatesY2);
            }
        }, 100);
    }

    /**
     * 去除状态栏
     */
    private void removeStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerFragmentPagerAdapter = new PagerFragmentPagerAdapter(getSupportFragmentManager(), this);
        for (int i = 0; i < DataGenerator.getPageEntityList().size(); i++) {
            pagerFragmentPagerAdapter.addFragment(new ItemFragment().newInstance(i));
        }
        int margin = getResources().getDisplayMetrics().widthPixels / 67;
        viewPager.setAdapter(pagerFragmentPagerAdapter);
        viewPager.setPageMargin(margin);  // DropShadowViewPager
        viewPager.setPageMarginDrawable(R.drawable.shadow);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new com.home.fierynoveldemo.StackTransformer());  // 設置翻頁動畫
        viewPager.setCurrentItem(1);
    }
}
