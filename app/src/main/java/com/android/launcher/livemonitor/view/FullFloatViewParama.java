package com.android.launcher.livemonitor.view;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

/**
 *  悬浮控件的参数
 */
public class FullFloatViewParama {
    private int x;
    private int y;
    private WindowManager.LayoutParams layoutParams ;

    public FullFloatViewParama(int x, int y) {
        this.x = x;
        this.y = y;

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = PixelFormat.TRANSLUCENT; // 背景透明
        // 参数配置比较重要
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.gravity= Gravity.TOP | Gravity.END;
        layoutParams.x = x; // 起始坐标
        layoutParams.y = y;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT ;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return layoutParams;
    }
}
