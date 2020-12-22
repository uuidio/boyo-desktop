package com.android.launcher.livemonitor.bean;

import android.content.pm.ResolveInfo;

/**
 * Created on 2020/12/7.
 *
 * @author Simon
 */
public class MainListBean {
    private ResolveInfo info;

    private String appName;

    private int type; //0代表可卸载，1代表不可卸载

    private int imgLogoRes;

    private String pageName;

    private String className;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getImgLogoRes() {
        return imgLogoRes;
    }

    public void setImgLogoRes(int imgLogoRes) {
        this.imgLogoRes = imgLogoRes;
    }

    public MainListBean(ResolveInfo info) {
        this.info = info;
    }

    public MainListBean(String appName, int res, int type, String className) {
        this.appName = appName;
        this.imgLogoRes = res;
        this.type = type;
        this.className = className;
    }

    public ResolveInfo getInfo() {
        return info;
    }

    public void setInfo(ResolveInfo info) {
        this.info = info;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
