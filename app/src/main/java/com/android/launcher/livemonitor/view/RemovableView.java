package com.android.launcher.livemonitor.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.android.launcher.C001Common;
import com.android.launcher.ForgetPassActivity;
import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.hotron.c002fac.tools.HotronJni;
import com.android.launcher.R;
import com.android.launcher.livemonitor.adapter.AudioSelectAdapter;
import com.android.launcher.livemonitor.adapter.PicAdapter;
import com.hotron.c002fac.camera.CameraWrapper;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2020/12/3.
 *
 * @author Simon
 */
public class RemovableView extends FrameLayout {
    private final Context mContext;
    private View view;
    private RadioGroup radioGroup;
    private RadioButton radioVideo, radioAudio, radioPic, radioAbout,radioInscription;
    private LinearLayout llBar;

    private RelativeLayout llNormal,rl_about;

    private RecyclerView rvAudio,rvPic;

    private LinearLayout llRightVideo;
    private ImageView imDrop;

    private int minTouchSlop=0;
    private float mDownX;
    private float mDownY;

    public RemovableView(Context context) {
        this(context, null);
    }

    public RemovableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemovableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    @SuppressLint("NonConstantResourceId")
    private void init() {
        Log.d("RemovableView", "init");
        minTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        view = LayoutInflater.from(mContext).inflate(R.layout.view_removable, this);
        radioGroup = view.findViewById(R.id.radio_group);
        radioVideo = view.findViewById(R.id.radio_video);
        radioAudio = view.findViewById(R.id.radio_audio);
        radioInscription= view.findViewById(R.id.radio_inscription);
        radioPic = view.findViewById(R.id.radio_pic);
        radioAbout = view.findViewById(R.id.radio_about);
        llRightVideo = view.findViewById(R.id.ll_right_video);
        llBar=view.findViewById(R.id.ll_bar);
        rvAudio=view.findViewById(R.id.rv_audio);
        rvPic=view.findViewById(R.id.rv_pic);
        llNormal=view.findViewById(R.id.rl_normal);
        imDrop=view.findViewById(R.id.im_drop);
        rl_about=view.findViewById(R.id.rl_about);
        llNormal.setOnClickListener(v -> {
            if (radioVideo.getVisibility()==VISIBLE)
            {
                radioVideo.setVisibility(GONE);
                radioVideo.setChecked(false);
                radioAudio.setVisibility(GONE);
                radioAudio.setChecked(false);
                radioPic.setVisibility(GONE);
                radioPic.setChecked(false);
                radioAbout.setVisibility(GONE);
                radioAbout.setChecked(false);
                radioInscription.setVisibility(GONE);
                radioInscription.setChecked(false);
                rvAudio.setVisibility(GONE);
                rvPic.setVisibility(GONE);
                llRightVideo.setVisibility(GONE);
                imDrop.setBackgroundResource(R.mipmap.btn_dropdpwn);

                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(110, ViewGroup.LayoutParams.WRAP_CONTENT);
                llBar.setLayoutParams(params);

            }else {
                radioVideo.setVisibility(VISIBLE);
                radioAudio.setVisibility(VISIBLE);
                radioPic.setVisibility(VISIBLE);
                radioAbout.setVisibility(VISIBLE);
                radioInscription.setVisibility(VISIBLE);
                imDrop.setBackgroundResource(R.mipmap.btn_dropup);
            }
        });

        rvAudio.setLayoutManager(new LinearLayoutManager(getContext()));
        AudioSelectAdapter audioSelectAdapter=new AudioSelectAdapter(getContext(),null);
        rvAudio.setAdapter(audioSelectAdapter);

        rvPic.setLayoutManager(new GridLayoutManager(getContext(),2));
        PicAdapter picAdapter=new PicAdapter(getContext(),null);
        rvPic.setAdapter(picAdapter);



        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) llRightVideo.getLayoutParams();
            RelativeLayout.LayoutParams llp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (!isLeft())
            {
                llp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
                llBar.setLayoutParams(llp);
                params.removeRule(RelativeLayout.RIGHT_OF);
                params.rightMargin=10;
                params.addRule(RelativeLayout.LEFT_OF,R.id.ll_bar);
            }else {
                llp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
                llBar.setLayoutParams(llp);
                params.removeRule(RelativeLayout.LEFT_OF);
                params.leftMargin=10;
                params.addRule(RelativeLayout.RIGHT_OF,R.id.ll_bar);
            }

            switch (checkedId) {
                case R.id.radio_video:
                    if (llRightVideo.getVisibility() == View.GONE) {
                        llRightVideo.setVisibility(VISIBLE);
                        radioVideo.setChecked(true);
                        params.width=318;
                        llRightVideo.setLayoutParams(params);
                    }
                    rvAudio.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    break;
                case R.id.radio_audio:
                    if (rvAudio.getVisibility()==View.GONE)
                    {
                        rvAudio.setVisibility(VISIBLE);
                        radioAudio.setChecked(true);
                        params.width=318;
                        rvAudio.setLayoutParams(params);
                    }
                    llRightVideo.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    break;
                case R.id.radio_pic:
                    if (rvPic.getVisibility()==GONE)
                    {
                        rvPic.setVisibility(VISIBLE);
                        radioPic.setChecked(true);
                        params.width=425;
                        rvPic.setLayoutParams(params);
                    }
                    rvAudio.setVisibility(GONE);
                    llRightVideo.setVisibility(GONE);
                    break;
                case R.id.radio_inscription:
                    radioInscription.setChecked(true);
                    rvAudio.setVisibility(GONE);
                    llRightVideo.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    Intent intent=new Intent(getContext(),ForgetPassActivity.class);
                    getContext().startActivity(intent);
                    break;
                case R.id.radio_about:
                    radioAbout.setChecked(true);
                    if (rl_about.getVisibility()==GONE)
                    {
//                        rl_about.setVisibility(VISIBLE);
                    }
                    rvAudio.setVisibility(GONE);
                    llRightVideo.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    break;
            }

        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean interceptd  =false ;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                interceptd = false;
                mDownX = event.getX();
                mDownY = event.getY();
                // todo 重要点，要在此处初始化位置参数，否则拖动时效果有偏差。
                // 因为 onTouchEvent 的 MotionEvent.ACTION_DOWN 不一定会被触发。
                myFloatViewParama.setX( (int) event.getRawX() );
                myFloatViewParama.setY((int) event.getRawY());
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - mDownX;
                float dy = event.getY() - mDownY;
                // 根据滑动的距离来判断是否是拖动操作
                interceptd =   Math.abs(dx) > minTouchSlop || Math.abs(dy) > minTouchSlop  ;
                break;

            case MotionEvent.ACTION_UP:
                interceptd = false;
                break;
        }
        return interceptd;
    }

    private MyFloatViewParama myFloatViewParama ;
    private WindowManager windowManager ;
    public void updateConfig(MyFloatViewParama myFloatViewParama, WindowManager windowManager){
        this.myFloatViewParama =myFloatViewParama ;
        this.windowManager=windowManager;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(myFloatViewParama==null){
            return false ;
        }

        if(windowManager ==null){
            return false;
        }
        if (radioVideo.getVisibility()==VISIBLE)
        {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //todo 因为 ViewGroup 中有 子View 被挡触摸时， ViewGroup ACTION_DOWN 没法被
                // 触发，所以为了确保触发，要在 onInterceptTouchEvent ACTION_DOWN 中进行初始化。
                myFloatViewParama.setX( (int) event.getRawX());
                myFloatViewParama.setY((int) event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();
                int movedX = nowX -  myFloatViewParama.getX();
                int movedY = nowY -  myFloatViewParama.getY();
                myFloatViewParama.setX( nowX );
                myFloatViewParama.setY( nowY );
                myFloatViewParama.getLayoutParams().x = myFloatViewParama.getLayoutParams().x + movedX;
                myFloatViewParama.getLayoutParams().y = myFloatViewParama.getLayoutParams().y + movedY;
                // 更新悬浮窗控件布局
                windowManager.updateViewLayout(this, myFloatViewParama.getLayoutParams());
                break;
            case MotionEvent.ACTION_UP:
                if (myFloatViewParama.getLayoutParams().x<540)
                {
                    myFloatViewParama.getLayoutParams().x=10;
                }else {
                    myFloatViewParama.getLayoutParams().x=1070;
                }
                windowManager.updateViewLayout(this, myFloatViewParama.getLayoutParams());
                performClick(); // 消除警告
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private boolean isLeft()
    {
        int[] location=new int[2];
        getLocationOnScreen(location);
        return location[0]<100?true:false;
    }
}
