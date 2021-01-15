package com.android.launcher.livemonitor.manager;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.android.launcher.R;
import com.android.launcher.livemonitor.view.MyFloatViewParama;
import com.android.launcher.livemonitor.view.RemovableView;

/**
 * Created on 2020/12/3.
 *
 * @author Simon
 */
public class WindowViewManager {
    private static WindowViewManager viewManager;
    private WindowManager windowManager;
    private RemovableView floatBall;
    public View floatBooks;
    private boolean ismove;
    private boolean isShow = false;
    private static Vibrator sVibrator;
    private SparseArray<MyFloatViewParama> flaotViewParams = new SparseArray<>();

    private WindowViewManager() {
    }

    public synchronized static WindowViewManager getViewManagerInstance() {
        if (viewManager == null) {
            viewManager = new WindowViewManager();
        }
        return viewManager;
    }

    public void show(final Context context) {
        if (isShow) {
            return;
        }
        sVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        floatBall = new RemovableView(context.getApplicationContext(),null);
        floatBooks=View.inflate(context.getApplicationContext(), R.layout.view_float_autocue,null);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //layoutParams.flags = 40;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.RGBA_8888 | PixelFormat.TRANSLUCENT;
        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR; //竖屏

        //layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//横屏
        //floatBall.setLayoutParams(layoutParams);
        MyFloatViewParama myFloatViewParama = new MyFloatViewParama(10, 300, false);
        int viewId = flaotViewParams.size();
        floatBall.setId(viewId);
        windowManager.addView(floatBall, myFloatViewParama.getLayoutParams());
        flaotViewParams.put(viewId, myFloatViewParama);
        floatBall.updateConfig(myFloatViewParama, windowManager);

        isShow = true;
    }

    public void close() {
        if (isShow) {
            windowManager.removeView(floatBall);
            try {
                windowManager.removeView(floatBooks);
            }catch (Exception e){}
            isShow = false;
        }
    }

    private static Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("ndh--", "vibrate---");
            sVibrator.vibrate(50);
        }
    };

    //移除弹出框
    private void closeView(View view) {
        windowManager.removeView(view);
    }

    private boolean isDrag = false;

    public void setEnableDrag(boolean isDrag) {
        this.isDrag = isDrag;
    }


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int viewId = view.getId();
            MyFloatViewParama myFloatViewParama = flaotViewParams.get(viewId);
            if (myFloatViewParama == null) {
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    myFloatViewParama.setX((int) event.getRawX());
                    myFloatViewParama.setY((int) event.getRawY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - myFloatViewParama.getX();
                    int movedY = nowY - myFloatViewParama.getY();
                    myFloatViewParama.setX(nowX);
                    myFloatViewParama.setY(nowY);
                    myFloatViewParama.getLayoutParams().x = myFloatViewParama.getLayoutParams().x + movedX;
                    myFloatViewParama.getLayoutParams().y = myFloatViewParama.getLayoutParams().y + movedY;
                    windowManager.updateViewLayout(view, myFloatViewParama.getLayoutParams()); // 更新悬浮窗控件布局
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
}
