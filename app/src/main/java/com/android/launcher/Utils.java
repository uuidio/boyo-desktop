package com.android.launcher;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;

import com.android.launcher.livemonitor.manager.WindowViewManager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import sm.utils.AppUtils;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by grant on 18-12-5.
 */

public class Utils {
    private static final Utils instance = new Utils();

    private Utils() {
    }

    public static Utils getInstance() {
        return instance;
    }


    public static Intent getAppOpenIntentByPackageName(Context context, String packageName) {
        //Activity完整名
        String mainAct = null;
        //根据包名寻找
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

        @SuppressLint("WrongConstant") List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
    }

    public static Context getPackageContext(Context context, String packageName) {
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境
            try {
                pkgContext = context.createPackageContext(packageName,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    public static boolean openPackage(Context context, String packageName) {
        Context pkgContext = getPackageContext(context, packageName);
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (pkgContext != null && intent != null) {
            pkgContext.startActivity(intent);
            return true;
        }
        return false;
    }

    public static void refreshWindowView(Context context) {
        String foregroundActivityName = ForegroundAppUtill.getForegroundActivityName(context);
        if ("com.android.launcher".equals(foregroundActivityName)) {
            WindowViewManager.getViewManagerInstance().close();
        } else {
            WindowViewManager.getViewManagerInstance().show(context);
        }
    }

    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    public static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }


    /**
     * 将应用置顶到最前端
     * 当本应用位于后台时，则将它切换到最前端
     *
     * @param context
     */
    public static void setTopApp(Context context,String packageName) {
        if (!isRunningForeground(context)) {
            /**获取ActivityManager*/
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

            /**获得当前运行的task(任务)*/
            List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                /**找到本应用的 task，并将它切换到前台*/
                if (taskInfo.topActivity.getPackageName().equals(packageName)) {
                    activityManager.moveTaskToFront(taskInfo.id, 0);
                    break;
                }
            }
        }
    }


    /**
     * 判断本应用是否已经位于最前端
     *
     * @param context
     * @return 本应用已经位于最前端时，返回 true；否则返回 false
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        /**枚举进程*/
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 压缩图片到目标大小以下
     *
     * @param mbitmap
     * @param targetSize
     */
    public static Bitmap compressBmpFileToTargetSize(Bitmap mbitmap, long targetSize) {
        if (mbitmap.getByteCount() > targetSize) {
            // 每次宽高各缩小一半
            int ratio = 2;
            // 获取图片原始宽高
            BitmapFactory.Options options = new BitmapFactory.Options();

            int targetWidth =  mbitmap.getWidth() / ratio;
            int targetHeight = mbitmap.getHeight() / ratio;

            // 压缩图片到对应尺寸
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            Bitmap result = generateScaledBmp(mbitmap, targetWidth, targetHeight, baos, quality);

            // 计数保护，防止次数太多太耗时。
            int count = 0;
            while (baos.size() > targetSize && count <= 10) {
                targetWidth /= ratio;
                targetHeight /= ratio;
                count++;

                // 重置，不然会累加
                baos.reset();
                result = generateScaledBmp(result, targetWidth, targetHeight, baos, quality);
            }

            return result;
        }else{
            return  mbitmap;
        }
    }


    /**
     * 图片缩小一半
     *
     * @param srcBmp
     * @param targetWidth
     * @param targetHeight
     * @param baos
     * @param quality
     * @return
     */
    private static Bitmap generateScaledBmp(Bitmap srcBmp, int targetWidth, int targetHeight, ByteArrayOutputStream baos, int quality) {
        Bitmap result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, result.getWidth(), result.getHeight());
        canvas.drawBitmap(srcBmp, null, rect, null);
        if (!srcBmp.isRecycled()) {
            srcBmp.recycle();
        }
        result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        return result;
    }


    public static File saveBitmapFile(Context context,Bitmap bitmap){
        String filePath = context.getExternalCacheDir().getPath() + File.separator + "ce.jpg";
        File file=new File(filePath);//将要保存图片的路径
        try {
            if (file.exists()){
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
