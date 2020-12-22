package com.android.launcher.livemonitor.common;

/**
 * Created by Angus on 2016/8/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.launcher.R;

import sm.utils.UtilsInitializer;


public class ToastUtils {

    public static boolean isScreenLong = true;
    private static Toast toast;
    private static Handler handler = new Handler();
    private static Context context = UtilsInitializer.getContext();

    private static ToastShower toastShowTask = new ToastShower();

    private static class ToastShower implements Runnable {

        private final long BASE_TIME = 500;

        private long timeMillis;

        private long count;

        void setTimeMillis(long timeMillis) {
            if (timeMillis < 2000) {
                this.timeMillis = 0;
            } else {
                this.timeMillis = timeMillis - 2000;
            }
            this.count = 0;
        }

        @Override
        public void run() {
            if (count <= timeMillis) {
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(this, BASE_TIME);
                count += BASE_TIME;
            }
        }
    }

    /**
     * 弹出较长时间的居中提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showCenterLong(String msg) {
        show(msg, 5000, false, true, true);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showShort(String msg) {
        show(msg, 3500, false, true, false);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showLong(String msg) {
        show(msg, 5000, false, true, false);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showShortNormal(String msg) {
        show(msg, 3500, false, false, false);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showLongNormal(String msg) {
        show(msg, 5000, false, false, false);
    }

    public static void showLongAlert(String msg) {
        show(msg, 5000, true, true, false);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void show(final String msg, final long timeMillis, boolean alert, boolean isJumpWhenMore, boolean isCenter) {
        if (isJumpWhenMore) {
            buildToast(msg, Toast.LENGTH_SHORT, alert, isCenter);
            showToast(timeMillis);
        } else {
            int duration;
            if (timeMillis == 2500) {
                duration = Toast.LENGTH_SHORT;
            } else {
                duration = Toast.LENGTH_LONG;
            }
            handler.removeCallbacksAndMessages(null);
            buildToast(msg, duration, alert, isCenter).show();
        }
    }

    private static void showToast(long timeMillis) {
        toastShowTask.setTimeMillis(timeMillis);
        handler.removeCallbacksAndMessages(null);
        handler.post(toastShowTask);
    }

    /**
     * 构造Toast
     *
     * @param msg 消息
     * @return
     */
    public static Toast buildToast(String msg, int duration, boolean alert, boolean isCenter) {
        if (null == toast) {
            toast = new Toast(context);
        }
        if (alert) {
           // setAlertToast(toast, createAlertView(context, msg), duration);
        } else {
            if (isCenter) setCenterToast(toast, createTextView(context, msg), duration);
            else setNormalToast(toast, createTextView(context, msg), duration);
        }
        return toast;
    }

    private static void setNormalToast(Toast toast, View view, int duration) {
        final int yOffset;
        if (isScreenLong) {
            yOffset = 140;
        } else {
            yOffset = 120;
        }
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, yOffset);
        toast.setDuration(duration);
        //将自定义View覆盖Toast的View
        toast.setView(view);
    }

    private static void setCenterToast(Toast toast, View view, int duration) {
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        //将自定义View覆盖Toast的View
        toast.setView(view);
    }

    private static void setAlertToast(Toast toast, View view, int duration) {
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        //将自定义View覆盖Toast的View
        toast.setView(view);
    }

    private static View createTextView(Context context, String msg) {
        final float textSp;
        final int textPaddingLeftRight;
        final int textPaddingTopBottom;
        if (isScreenLong) {
            textSp = 30;
            textPaddingLeftRight = 138;
            textPaddingTopBottom = 29;
        } else {
            textSp = 24;
            textPaddingLeftRight = 103;
            textPaddingTopBottom = 14;
        }
        //设置Toast文字
        TextView tv = new TextView(context);
        tv.setPadding(textPaddingLeftRight, textPaddingTopBottom, textPaddingLeftRight, textPaddingTopBottom);
        tv.setText(msg);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(textSp);

        //Toast文字TextView容器
        LinearLayout mLayout = new LinearLayout(context);
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.parseColor("#D8000000"));
        shape.setCornerRadius(50);
        shape.setAlpha(180);
        tv.setBackground(shape);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mLayout.addView(tv, params);
        return mLayout;
    }


}
