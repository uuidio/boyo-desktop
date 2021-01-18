package com.android.launcher.livemonitor.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.text.Html;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.launcher.C001Common;
import com.android.launcher.ForgetPassActivity;
import com.android.launcher.GsonUtil;
import com.android.launcher.livemonitor.adapter.AutocueClassifyAdapter;
import com.android.launcher.livemonitor.adapter.PicImgAdapter;
import com.android.launcher.livemonitor.adapter.PicTypeAdapter;
import com.android.launcher.livemonitor.api.APIFactory;
import com.android.launcher.livemonitor.api.NaoManager;
import com.android.launcher.livemonitor.api.entity.AboutRsp;
import com.android.launcher.livemonitor.api.entity.AutocueClassifyRsp;
import com.android.launcher.livemonitor.api.entity.AutocueRsp;
import com.android.launcher.livemonitor.api.entity.PicImgRsp;
import com.android.launcher.livemonitor.api.entity.TagListRsp;
import com.android.launcher.livemonitor.common.ToastUtils;
import com.android.launcher.livemonitor.manager.WindowViewManager;
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

import java.util.AbstractCollection;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.leefeng.promptlibrary.PromptDialog;

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

    private RelativeLayout llNormal,rl_about,rl_imgku,rl_books,rl_pic_adjust,rl_pic_bg,rl_loading_dialog;

    private RecyclerView rvAudio,rvPic,rv_img_type,rv_img,rv_inscription;

    private LinearLayout llRightVideo;
    private ImageView imDrop,iv_books,iv_pic_image;
    private TextView tv_back,tv_submit,tv_about,tv_books_tag1,tv_books_tag2,
            tv_books_tag3,tv_books_content,tv_page_num,tv_books_title,tv_pic_back;
    private Button btn_books_narrow,btn_books_close,btn_pre_page,btn_next_page,
            btn_pic_resize,btn_pic_roation,btn_pic_reset,btn_pic_submit;
    private SeekBar seekbar_pic;

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
        tv_about=view.findViewById(R.id.tv_about);
        rl_pic_adjust=view.findViewById(R.id.rl_pic_adjust);
        iv_pic_image=view.findViewById(R.id.iv_pic_image);
        tv_pic_back=view.findViewById(R.id.tv_pic_back);
        btn_pic_resize=view.findViewById(R.id.btn_pic_resize);
        btn_pic_roation=view.findViewById(R.id.btn_pic_roation);
        btn_pic_reset=view.findViewById(R.id.btn_pic_reset);
        btn_pic_submit=view.findViewById(R.id.btn_pic_submit);
        seekbar_pic=view.findViewById(R.id.seekbar_pic);
        rl_pic_bg=view.findViewById(R.id.rl_pic_bg);
        rl_loading_dialog=view.findViewById(R.id.rl_loading_dialog);

        tv_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        tv_pic_back.setOnClickListener(this);
        btn_pic_resize.setOnClickListener(this);
        btn_pic_roation.setOnClickListener(this);
        btn_pic_reset.setOnClickListener(this);
        btn_pic_submit.setOnClickListener(this);

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
                rv_inscription.setVisibility(GONE);
                rl_about.setVisibility(GONE);
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
        rv_inscription.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });


        radioVideo.setOnClickListener(radioClicklistener);
        radioAudio.setOnClickListener(radioClicklistener);
        radioPic.setOnClickListener(radioClicklistener);
        radioInscription.setOnClickListener(radioClicklistener);
        radioAbout.setOnClickListener(radioClicklistener);

    }

    private OnClickListener radioClicklistener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) llRightVideo.getLayoutParams();
            RelativeLayout.LayoutParams llp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (!isLeft())
            {
                llp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
                llBar.setLayoutParams(llp);
                params.removeRule(RelativeLayout.RIGHT_OF);
                params.rightMargin=10;
                params.topMargin=60;
                params.addRule(RelativeLayout.LEFT_OF,R.id.ll_bar);
            }else {
                llp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
                llBar.setLayoutParams(llp);
                params.removeRule(RelativeLayout.LEFT_OF);
                params.leftMargin=10;
                params.topMargin=60;
                params.addRule(RelativeLayout.RIGHT_OF,R.id.ll_bar);
            }

            switch (v.getId()) {
                case R.id.radio_video:
                    if (llRightVideo.getVisibility() == View.GONE) {
                        llRightVideo.setVisibility(VISIBLE);
                        radioVideo.setChecked(true);
                        params.width=318;
                        params.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
                        llRightVideo.setLayoutParams(params);
                    }else{
                        radioVideo.setChecked(false);
                        llRightVideo.setVisibility(GONE);
                        radioGroup.clearCheck();
                    }
                    rvAudio.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    rv_inscription.setVisibility(GONE);
                    rl_about.setVisibility(GONE);
                    break;
                case R.id.radio_audio:
                    if (rvAudio.getVisibility()==View.GONE)
                    {
                        rvAudio.setVisibility(VISIBLE);
                        radioAudio.setChecked(true);
                        params.width=318;
                        params.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
                        rvAudio.setLayoutParams(params);
                    }else{
                        rvAudio.setVisibility(GONE);
                        radioAudio.setChecked(false);
                        radioGroup.clearCheck();
                    }
                    llRightVideo.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    rv_inscription.setVisibility(GONE);
                    rl_about.setVisibility(GONE);
                    break;
                case R.id.radio_pic:
                    if (rvPic.getVisibility()==GONE)
                    {
                        rvPic.setVisibility(VISIBLE);
                        radioPic.setChecked(true);
                        params.width=425;
                        params.height=795;
                        rvPic.setLayoutParams(params);
                        ((PicAdapter)rvPic.getAdapter()).reset();
                        //获取个人贴纸
                        imgPeopleList();
                    }else{
                        rvPic.setVisibility(GONE);
                        radioPic.setChecked(false);
                        radioGroup.clearCheck();
                    }
                    rvAudio.setVisibility(GONE);
                    llRightVideo.setVisibility(GONE);
                    rv_inscription.setVisibility(GONE);
                    rl_about.setVisibility(GONE);
                    break;
                case R.id.radio_inscription:
                    if (rv_inscription.getVisibility()==View.GONE)
                    {
                        rv_inscription.setAdapter(null);
                        rv_inscription.setVisibility(VISIBLE);
                        radioInscription.setChecked(true);
                        params.width=425;
                        params.height=795;
                        params.topMargin=270;
                        rv_inscription.setLayoutParams(params);
                        autocueClassifyList();
                    }else{
                        rv_inscription.setVisibility(GONE);
                        radioInscription.setChecked(false);
                        radioGroup.clearCheck();
                    }

                    rvAudio.setVisibility(GONE);
                    llRightVideo.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    rl_about.setVisibility(GONE);
                    break;
                case R.id.radio_about:
                    if (rl_about.getVisibility()==GONE)
                    {
                        rl_about.setVisibility(VISIBLE);
                        radioAbout.setChecked(true);
                        params.width=490;
                        params.height=590;
                        params.topMargin=290;
                        rl_about.setLayoutParams(params);
                        //获取公告
                        notice();
                    }else{
                        rl_about.setVisibility(GONE);
                        radioAbout.setChecked(false);
                        radioGroup.clearCheck();
                    }
                    rvAudio.setVisibility(GONE);
                    llRightVideo.setVisibility(GONE);
                    rvPic.setVisibility(GONE);
                    rv_inscription.setVisibility(GONE);
                    break;
            }
        }
    };

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
                if (radioVideo.getVisibility()==VISIBLE)
                {
                    interceptd=false;
                }
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
//                myFloatViewParama.setX( (int) event.getRawX());
                myFloatViewParama.setY((int) event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();
//                int movedX = nowX -  myFloatViewParama.getX();
                int movedY = nowY -  myFloatViewParama.getY();
//                myFloatViewParama.setX( nowX );
                myFloatViewParama.setY( nowY );
//                myFloatViewParama.getLayoutParams().x = myFloatViewParama.getLayoutParams().x + movedX;
                myFloatViewParama.getLayoutParams().y = myFloatViewParama.getLayoutParams().y + movedY;
                // 更新悬浮窗控件布局
                windowManager.updateViewLayout(this, myFloatViewParama.getLayoutParams());
                break;
            case MotionEvent.ACTION_UP:
//                if (myFloatViewParama.getLayoutParams().x<540)
//                {
//                    myFloatViewParama.getLayoutParams().x=10;
//                }else {
//                    myFloatViewParama.getLayoutParams().x=1070;
//                }
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
        APIFactory.INSTANCE.create().tagList(NaoManager.INSTANCE.getAccessToken(),3000)
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
                                            rv_img.setAdapter(null);
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
        APIFactory.INSTANCE.create().imageList(NaoManager.INSTANCE.getAccessToken(),tag_id,3000)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<PicImgRsp>(){
                    @Override
                    public void accept(PicImgRsp picImgRsp) throws Exception {
                        if (picImgRsp.getCode()==0){
                            PicImgAdapter picImgAdapter=new PicImgAdapter(getContext(), picImgRsp.getResult().getLists().getData());
                            rv_img.setAdapter(picImgAdapter);
                        }
                    }
                });
    }


    //获取个人贴纸素材图片
    private void imgPeopleList() {
        APIFactory.INSTANCE.create().imagePeopleList(NaoManager.INSTANCE.getAccessToken(),3000)
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
                                        rv_img_type.setAdapter(null);
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


    //添加贴纸素材图片
    private void imagePeopleSave(int tag_id,String location) {
        rl_loading_dialog.setVisibility(View.VISIBLE);
        APIFactory.INSTANCE.create().imagePeopleSave(NaoManager.INSTANCE.getAccessToken(),tag_id,1,location)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<PicImgRsp>() {
                    @Override
                    public void accept(PicImgRsp picImgRsp) throws Exception {
                        if (picImgRsp.getCode() == 0 ) {
                            if (rvPic.getVisibility() == View.VISIBLE)
                                imgPeopleList();
                        }else{
                            ToastUtils.showLong(picImgRsp.getMessage());

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showLong("网络错误!");
                        rl_loading_dialog.setVisibility(View.GONE);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        rl_loading_dialog.setVisibility(View.GONE);
                    }
                });
    }

    //获取题词分类列表
    private void autocueClassifyList() {
        APIFactory.INSTANCE.create().autocueClassifyList(NaoManager.INSTANCE.getAccessToken(),3000)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<AutocueClassifyRsp>(){
                    @Override
                    public void accept(AutocueClassifyRsp rsp) throws Exception {
                        if (rsp.getCode()==0){
                            AutocueClassifyAdapter adapter=new AutocueClassifyAdapter(getContext(), rsp.getResult().getLists().getData(), new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        showBooksView();
                                        autocueList(rsp.getResult().getLists().getData().get(position).getId());
                                }
                            });
                            rv_inscription.setAdapter(adapter);
                        }
                    }
                });
    }


    //获取题词笔记列表
    private List<AutocueRsp.Data> booksData;//笔记数据
    private int bookIndex=-1;//当前笔记下标
    private void autocueList(int cid) {
        APIFactory.INSTANCE.create().autocueList(NaoManager.INSTANCE.getAccessToken(),cid,3000)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<AutocueRsp>(){
                    @Override
                    public void accept(AutocueRsp rsp) throws Exception {
                        if (rsp.getCode()==0){
                            booksData=rsp.getResult().getLists().getData();
                            if (booksData!=null && booksData.size()>0){
                                bookIndex=0;
                                tv_books_title.setText(booksData.get(bookIndex).getTitle());
                                tv_books_tag1.setText(booksData.get(bookIndex).getAntistop_one());
                                tv_books_tag2.setText(booksData.get(bookIndex).getAntistop_two());
                                tv_books_tag3.setText(booksData.get(bookIndex).getAntistop_three());
                                tv_books_content.setText(booksData.get(bookIndex).getContent());
                                tv_page_num.setText((bookIndex+1)+"/"+booksData.size());
                            }
                        }
                    }
                });
    }



    //获取公告
    private void notice() {
        APIFactory.INSTANCE.create().notice(NaoManager.INSTANCE.getAccessToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<AboutRsp>(){
                    @Override
                    public void accept(AboutRsp rsp) throws Exception {
                        if (rsp.getCode()==0){
                            String html="<h5><font color='#FFFFFF' >"+rsp.getResult().getTitle()+"</font></h5>"
                                    +rsp.getResult().getNotice();
                            tv_about.setText(Html.fromHtml(html));
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
                    rv_img_type.setVisibility(View.VISIBLE);
                    tv_submit.setVisibility(View.GONE);
                }else{
                    rl_imgku.setVisibility(View.GONE);
                    rv_img_type.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_submit:
                //跳转贴纸调整页面
                if (rv_img.getAdapter()!=null){
                    PicImgAdapter adapter= (PicImgAdapter)rv_img.getAdapter();
                   if (adapter.getCurrySel()>-1 && !adapter.getAdapterData().isEmpty()){
                       //打开调整界面
                       openPicAdjust(adapter.getAdapterData().get(adapter.getCurrySel()));
                   }
                }
                break;
            case R.id.tv_pic_back:
                //退出贴纸调整页面
                rl_pic_adjust.setVisibility(View.GONE);
                break;
            case R.id.btn_pic_roation:
                //贴纸旋转
                iv_pic_image.setRotation(iv_pic_image.getRotation()+90f);
                break;
            case R.id.btn_pic_resize:
                //贴纸重置大小
                ViewGroup.LayoutParams params=  iv_pic_image.getLayoutParams();
                params.width=270;
                params.height=270;
                iv_pic_image.setLayoutParams(params);
                break;
            case R.id.btn_pic_reset:
                //贴纸重置大小
                RelativeLayout.LayoutParams resetParams= (RelativeLayout.LayoutParams) iv_pic_image.getLayoutParams();
                resetParams.width=270;
                resetParams.height=270;
                resetParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                iv_pic_image.setLayoutParams(resetParams);
                iv_pic_image.requestLayout();
                break;
            case R.id.btn_pic_submit:
                //贴纸添加
                PicImgAdapter adapter= (PicImgAdapter)rv_img.getAdapter();
                PicImgRsp.Location location=adapter.getAdapterData().get(adapter.getCurrySel()).getLocation();
                location.setWidth(iv_pic_image.getWidth());
                location.setHeight(iv_pic_image.getHeight());
                location.setLeftPer(iv_pic_image.getLeft()/rl_pic_bg.getWidth());
                location.setRoate(iv_pic_image.getRotation());
                location.setTopPer(iv_pic_image.getTop()/rl_pic_bg.getHeight());
                imagePeopleSave(adapter.getAdapterData().get(adapter.getCurrySel()).getId()
                ,GsonUtil.gsonString(location));
                break;
            case R.id.btn_pre_page:
                //笔记上一页
                if (booksData!=null && booksData.size()>bookIndex-1 && bookIndex-1>=0){
                    bookIndex--;
                    tv_books_title.setText(booksData.get(bookIndex).getTitle());
                    tv_books_tag1.setText(booksData.get(bookIndex).getAntistop_one());
                    tv_books_tag2.setText(booksData.get(bookIndex).getAntistop_two());
                    tv_books_tag3.setText(booksData.get(bookIndex).getAntistop_three());
                    tv_books_content.setText(booksData.get(bookIndex).getContent());
                    tv_page_num.setText((bookIndex+1)+"/"+booksData.size());
                }
                break;
            case R.id.btn_next_page:
                //笔记下一页
                if (booksData!=null && booksData.size()>bookIndex+1){
                    bookIndex++;
                    tv_books_title.setText(booksData.get(bookIndex).getTitle());
                    tv_books_tag1.setText(booksData.get(bookIndex).getAntistop_one());
                    tv_books_tag2.setText(booksData.get(bookIndex).getAntistop_two());
                    tv_books_tag3.setText(booksData.get(bookIndex).getAntistop_three());
                    tv_books_content.setText(booksData.get(bookIndex).getContent());
                    tv_page_num.setText((bookIndex+1)+"/"+booksData.size());
                }
                break;
            case R.id.btn_books_close:
                //关闭笔记
                dismissFloatBooks();

                break;
            case R.id.btn_books_narrow:
                //笔记缩小
                if (floatBooks!=null){
                    rl_books.setVisibility(View.GONE);
                    iv_books.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.iv_books:
                //笔记张开
                if (floatBooks!=null){
                    iv_books.setVisibility(View.GONE);
                    rl_books.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    //打开调整贴纸界面

    private void openPicAdjust(PicImgRsp.Data data){
        rl_pic_adjust.setVisibility(View.VISIBLE);
    }


    //添加笔记界面
    private View floatBooks;
    public void showBooksView(){
        if (floatBooks==null){
            floatBooks=WindowViewManager.getViewManagerInstance().floatBooks;
            RightFloatViewParama rightFloatViewParama = new RightFloatViewParama(10, 300);
            tv_books_tag1=floatBooks.findViewById(R.id.tv_books_tag1);
            tv_books_tag2=floatBooks.findViewById(R.id.tv_books_tag2);
            tv_books_tag3=floatBooks.findViewById(R.id.tv_books_tag3);
            tv_books_content=floatBooks.findViewById(R.id.tv_books_content);
            tv_books_title=floatBooks.findViewById(R.id.tv_books_title);
            tv_page_num=floatBooks.findViewById(R.id.tv_page_num);
            btn_books_narrow=floatBooks.findViewById(R.id.btn_books_narrow);
            btn_books_close=floatBooks.findViewById(R.id.btn_books_close);
            btn_pre_page=floatBooks.findViewById(R.id.btn_pre_page);
            btn_next_page=floatBooks.findViewById(R.id.btn_next_page);
            iv_books=floatBooks.findViewById(R.id.iv_books);
            rl_books=floatBooks.findViewById(R.id.rl_books);
            tv_books_content.setMovementMethod(ScrollingMovementMethod.getInstance());
            btn_books_narrow.setOnClickListener(this);
            btn_books_close.setOnClickListener(this);
            iv_books.setOnClickListener(this);
            btn_pre_page.setOnClickListener(this);
            btn_next_page.setOnClickListener(this);
            windowManager.addView(floatBooks, rightFloatViewParama.getLayoutParams());
        }else{
            tv_books_title.setText("");
            tv_books_tag1.setText("");
            tv_books_tag2.setText("");
            tv_books_tag3.setText("");
            tv_page_num.setText("0/0");
            tv_books_content.setText("");
            floatBooks.setVisibility(View.VISIBLE);
        }
    }


    public void dismissFloatBooks(){
        if (floatBooks!=null){
            floatBooks.setVisibility(View.GONE);
            tv_books_title.setText("");
            tv_books_tag1.setText("");
            tv_books_tag2.setText("");
            tv_books_tag3.setText("");
            tv_page_num.setText("0/0");
            tv_books_content.setText("");
        }
    }
}
