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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.launcher.C001Common;
import com.android.launcher.ForgetPassActivity;
import com.android.launcher.GsonUtil;
import com.android.launcher.livemonitor.adapter.PicImgAdapter;
import com.android.launcher.livemonitor.adapter.PicTypeAdapter;
import com.android.launcher.livemonitor.api.APIFactory;
import com.android.launcher.livemonitor.api.NaoManager;
import com.android.launcher.livemonitor.api.entity.AutocueClassifyRsp;
import com.android.launcher.livemonitor.api.entity.PicImgRsp;
import com.android.launcher.livemonitor.api.entity.TagListRsp;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2020/12/3.
 *
 * @author Simon
 */
public class RemovableView extends FrameLayout implements View.OnClickListener {
    private final Context mContext;
    private View view;
    private RadioGroup radioGroup;
    private RadioButton radioVideo, radioAudio, radioPic, radioAbout,radioInscription;
    private LinearLayout llBar;

    private RelativeLayout llNormal,rl_about,rl_imgku;

    private RecyclerView rvAudio,rvPic,rv_img_type,rv_img,rv_inscription;

    private LinearLayout llRightVideo;
    private ImageView imDrop;
    private TextView tv_back,tv_submit;

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
        rl_imgku=view.findViewById(R.id.rl_imgku);
        rv_img_type=view.findViewById(R.id.rv_img_type);
        rv_img=view.findViewById(R.id.rv_img);
        tv_back=view.findViewById(R.id.tv_back);
        tv_submit=view.findViewById(R.id.tv_submit);
        rv_inscription=view.findViewById(R.id.rv_inscription);
        tv_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
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

        //贴纸
        rvPic.setLayoutManager(new GridLayoutManager(getContext(),2));
        PicAdapter picAdapter=new PicAdapter(getContext(),null, new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position==0){
                        rl_imgku.setVisibility(View.VISIBLE);
                        rv_img_type.setVisibility(View.VISIBLE);
                        tv_submit.setVisibility(View.GONE);
                        //获取贴纸素材
                        tagList();
                    }
            }
        });
        rvPic.setAdapter(picAdapter);

        rv_img_type.setLayoutManager(new GridLayoutManager(getContext(),4));
        rv_img.setLayoutManager(new GridLayoutManager(getContext(),4));

        //题词器
        rv_img.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

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
                    rv_inscription.setVisibility(GONE);
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
                    rv_inscription.setVisibility(GONE);
                    break;
                case R.id.radio_pic:
                    if (rvPic.getVisibility()==GONE)
                    {
                        rvPic.setVisibility(VISIBLE);
                        radioPic.setChecked(true);
                        params.width=425;
                        rvPic.setLayoutParams(params);
                        //获取个人贴纸
                        imgPeopleList();
                    }
                    rvAudio.setVisibility(GONE);
                    llRightVideo.setVisibility(GONE);
                    rv_inscription.setVisibility(GONE);
                    break;
                case R.id.radio_inscription:
                    radioInscription.setChecked(true);
                    rvAudio.setVisibility(GONE);
                    llRightVideo.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    if (rv_inscription.getVisibility()==View.GONE)
                    {
                        rv_inscription.setVisibility(VISIBLE);
                        rv_inscription.setLayoutParams(params);
                    }
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
                    rv_inscription.setVisibility(GONE);
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

    //获取贴纸素材分类
    private void tagList() {
        APIFactory.INSTANCE.create().tagList(NaoManager.INSTANCE.getAccessToken(),Integer.MAX_VALUE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<TagListRsp>(){
                    @Override
                    public void accept(TagListRsp tagListRsp) throws Exception {
                        if (tagListRsp.getCode()==0){

                            PicTypeAdapter picTypeAdapter=new PicTypeAdapter(getContext(), tagListRsp.getResult().getLists().getData(),
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            rv_img_type.setVisibility(View.GONE);
                                            tv_submit.setVisibility(View.VISIBLE);
                                            rv_img.setVisibility(View.VISIBLE);
                                            PicTypeAdapter adapter=(PicTypeAdapter)rv_img_type.getAdapter();
                                            if (adapter!=null && adapter.getList()!=null){
                                                imgList(adapter.getList().get(position).getId());
                                            }
                                        }
                                    });
                            rv_img_type.setAdapter(picTypeAdapter);
                        }
                    }
                });
    }


    //获取贴纸素材图片
    private void imgList(int tag_id) {
        APIFactory.INSTANCE.create().imageList(NaoManager.INSTANCE.getAccessToken(),tag_id,Integer.MAX_VALUE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<PicImgRsp>(){
                    @Override
                    public void accept(PicImgRsp picImgRsp) throws Exception {
                        if (picImgRsp.getCode()==0){
                            PicImgAdapter picImgAdapter=new PicImgAdapter(getContext(), picImgRsp.getResult().getLists().getData());
                            rv_img_type.setAdapter(picImgAdapter);
                        }
                    }
                });
    }


    //获取个人贴纸素材图片
    private void imgPeopleList() {
        APIFactory.INSTANCE.create().imagePeopleList(NaoManager.INSTANCE.getAccessToken(),Integer.MAX_VALUE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<PicImgRsp>(){
                    @Override
                    public void accept(PicImgRsp picImgRsp) throws Exception {
                        if (picImgRsp.getCode()==0){
                            PicAdapter picAdapter=new PicAdapter(getContext(),picImgRsp.getResult().getLists().getData(), new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position==0){
                                        rl_imgku.setVisibility(View.VISIBLE);
                                        rv_img_type.setVisibility(View.VISIBLE);
                                        tv_submit.setVisibility(View.GONE);
                                        //获取贴纸素材
                                        tagList();
                                    }else{
                                        //TODO...贴纸使用....
                                    }
                                }
                            });
                            rvPic.setAdapter(picAdapter);
                        }
                    }
                });
    }


    //获取题词分类列表
    private void autocueClassifyList() {
        APIFactory.INSTANCE.create().autocueClassifyList(NaoManager.INSTANCE.getAccessToken(),Integer.MAX_VALUE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<AutocueClassifyRsp>(){
                    @Override
                    public void accept(AutocueClassifyRsp rsp) throws Exception {
                        if (rsp.getCode()==0){
                           
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                //关闭图库
                if (rv_img.getVisibility()== View.VISIBLE){
                    rv_img.setVisibility(View.GONE);
                    tv_submit.setVisibility(View.GONE);
                }else{
                    rl_imgku.setVisibility(View.GONE);
                    rv_img_type.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_submit:

                break;

        }
    }
}
