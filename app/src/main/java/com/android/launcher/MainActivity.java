package com.android.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.launcher.livemonitor.api.APIFactory;
import com.android.launcher.livemonitor.api.NaoManager;
import com.android.launcher.livemonitor.api.entity.AutocueClassifyRsp;
import com.android.launcher.livemonitor.api.entity.PicImgRsp;
import com.android.launcher.livemonitor.api.entity.TagListRsp;
import com.android.launcher.livemonitor.api.entity.User;
import com.android.launcher.livemonitor.manager.WindowViewManager;
import com.google.gson.JsonElement;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private AppListPageView mAppListPageView = null;
    private AppChangeReceiver mAppChangeReceiver = null;
    private RelativeLayout rl_user_info;
    private TextView tv_id,tv_name;
    private Button btn_exit_login;

    private class AppChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAppListPageView != null) {
                mAppListPageView.notifyAppChange();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowViewManager.getViewManagerInstance().setEnableDrag(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAppListPageView = findViewById(R.id.applist_view);
        mAppListPageView.setVisibility(View.VISIBLE);
        mAppChangeReceiver = new AppChangeReceiver();
        IntentFilter filter1 = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter1.addDataScheme("package");
        this.registerReceiver(mAppChangeReceiver, filter1);

        IntentFilter filter2 = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter2.addDataScheme("package");
        this.registerReceiver(mAppChangeReceiver, filter2);

        IntentFilter filter3 = new IntentFilter(Intent.ACTION_PACKAGE_REPLACED);
        filter3.addDataScheme("package");
        this.registerReceiver(mAppChangeReceiver, filter3);

        btn_exit_login=findViewById(R.id.btn_exit_login);
        tv_name=findViewById(R.id.tv_name);
        tv_id=findViewById(R.id.tv_id);
        ImageView iv_people=findViewById(R.id.iv_people);
        iv_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.UserData userData=GsonUtil.gsonToBean(SharedPreferencesUtils.getParam(MainActivity.this,"userData","").toString(), User.UserData.class);
                String id= TextUtils.isEmpty(userData.getId()+"")?"":userData.getId()+"";
                String name=TextUtils.isEmpty(userData.getName())?"":userData.getName();
                tv_id.setText(id);
                tv_name.setText(name);
                rl_user_info.setVisibility(View.VISIBLE);
            }
        });


        TextView tv_exchange_pass=findViewById(R.id.tv_exchange_pass);
        tv_exchange_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgetPassActivity.class));
            }
        });

        btn_exit_login.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              SharedPreferencesUtils.clear(MainActivity.this);
              finish();
              startActivity(new Intent(MainActivity.this,LoginActivity.class));

          }
      });

        rl_user_info=findViewById(R.id.rl_user_info);

        ceshi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                if (rl_user_info!=null)
                rl_user_info.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowViewManager.getViewManagerInstance().close();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mAppChangeReceiver);
        super.onDestroy();
    }

    private void ceshi() {
         APIFactory.INSTANCE.create().autocueClassifyList(NaoManager.INSTANCE.getAccessToken(),80)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<AutocueClassifyRsp>(){
                    @Override
                    public void accept(AutocueClassifyRsp rsp) throws Exception {

                    }
                });
    }
}
