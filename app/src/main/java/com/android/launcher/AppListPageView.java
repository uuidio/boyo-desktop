package com.android.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.launcher.livemonitor.bean.MainListBean;
import com.android.launcher.livemonitor.manager.WindowViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stone on 20-12-17.
 */

public class AppListPageView extends RelativeLayout {
    private static String TAG = "AppListPageView";

    Context mContext;
    Button mBtnLogout = null;

    private List<MainListBean> mAppList = new ArrayList<>();
    private AppAdapter mAppAdapter = null;
    private GridView mGridView = null;

    private TextView mTvVersion = null;

    private AdapterView.OnItemClickListener mClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String pkg = null;
            MainListBean bean = mAppList.get(position);
            ResolveInfo info = mAppList.get(position).getInfo();
            if(info != null) {
                pkg = info.activityInfo.packageName;
            }
            if (Utils.checkPackInfo(mContext, pkg) && bean.getType() == 0) {
                Utils.openPackage(mContext, pkg);
            } else {
                Intent intent = new Intent();
                intent.setClassName("com.android.launcher", bean.getClassName());
                mContext.startActivity(intent);
            }
            if(bean.getType() == 0) {
                WindowViewManager.getViewManagerInstance().show(getContext());
            }
        }
    };


    public AppListPageView(Context mContext) {
        this(mContext, null);
    }

    public AppListPageView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;
        View view = View.inflate(mContext, R.layout.layout_applist_page, null);
        mGridView = view.findViewById(R.id.app_gridview);
        mBtnLogout = view.findViewById(R.id.btn_logout);
        mTvVersion = view.findViewById(R.id.tv_version);

        mTvVersion.setText(getVerName(mContext));

        loadApps();
        mAppAdapter = new AppAdapter();

        mGridView.setAdapter(mAppAdapter);
        mGridView.setOnItemClickListener(mClickListener);
        AdapterView.OnItemLongClickListener mLongClickListener = new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (mAppList.get(position).getType() == 1) {
                    return false;
                }

                ResolveInfo info = mAppList.get(position).getInfo();
                String pkg = info.activityInfo.packageName;

                Intent uninstall_intent = new Intent();
                uninstall_intent.setAction(Intent.ACTION_DELETE);
                uninstall_intent.setData(Uri.parse("package:" + pkg));
                mContext.startActivity(uninstall_intent);

                return true;
            }
        };
        mGridView.setOnItemLongClickListener(mLongClickListener);

        addView(view);
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    private void loadApps() {
        if (mAppList != null)
            mAppList.clear();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = mContext.getPackageManager().queryIntentActivities(mainIntent, 0);
        resolveInfos.stream().filter(it -> it.activityInfo.packageName.equals("com.ss.android.ugc.aweme")
                || it.activityInfo.packageName.equals("com.taobao.live4anchor")
                || it.activityInfo.packageName.equals("com.smile.gifmaker")
                || it.activityInfo.packageName.equals("com.tencent.mm")
                || it.activityInfo.packageName.equals("com.kol.jumhz"))
                .forEach(it -> mAppList.add(new MainListBean(it)));

        mAppList.forEach(it -> {
            if (it.getInfo().activityInfo.packageName.equals("com.kol.jumhz")) {
                Collections.swap(mAppList, 0, mAppList.indexOf(it));
            }
        });

        mAppList.forEach(it -> {
            switch (it.getInfo().activityInfo.packageName) {
                case "com.ss.android.ugc.aweme":
                    it.getInfo().labelRes = R.mipmap.icon_douyin;
                    it.getInfo().activityInfo.nonLocalizedLabel = "抖音";
                    break;
                case "com.taobao.live4anchor":
                    it.getInfo().labelRes = R.mipmap.icon_taobaozhibo;
                    break;
                case "com.smile.gifmaker":
                    it.getInfo().labelRes = R.mipmap.icon_kuaishou;
                    break;
                case "com.tencent.mm":
                    it.getInfo().labelRes = R.mipmap.icon_weixin;
                    break;
                case "com.kol.jumhz":
                    it.getInfo().labelRes = R.mipmap.iconuous;
                    break;
                default:
                    it.getInfo().labelRes = R.mipmap.ic_launcher;
                    break;
            }
        });


        mAppList.add(new MainListBean("设置", R.mipmap.icon_setting, 1,
                "com.android.launcher.livemonitor.setting.SettingActivity"));
    }

    public void notifyAppChange() {
        loadApps();
        mAppAdapter.notifyDataSetChanged();
    }

    public class AppAdapter extends BaseAdapter {
        public AppAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                //init app node layout
                convertView = View.inflate(mContext, R.layout.item_gride_app_node, null);
                viewHolder = new ViewHolder();
                //init image and text
                viewHolder.image_view = convertView.findViewById(R.id.image_view);
                viewHolder.text_view = convertView.findViewById(R.id.text_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (mAppList.get(position).getType() == 0) {
                ResolveInfo info = mAppList.get(position).getInfo();
                viewHolder.image_view.setImageResource(info.labelRes);
                viewHolder.text_view.setText(info.activityInfo.loadLabel(mContext.getPackageManager()).toString());
            } else {
                viewHolder.image_view.setImageResource(mAppList.get(position).getImgLogoRes());
                viewHolder.text_view.setText(mAppList.get(position).getAppName());
            }


            return convertView;
        }

        public final int getCount() {
            return mAppList.size();
        }

        public final Object getItem(int position) {
            return mAppList.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }

    public final class ViewHolder {
        TextView text_view;
        ImageView image_view;
    }
}
