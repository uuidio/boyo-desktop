package com.android.launcher.livemonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.android.launcher.R;
import com.android.launcher.livemonitor.api.APIFactory;
import com.android.launcher.livemonitor.api.NaoManager;
import com.android.launcher.livemonitor.api.entity.PicImgRsp;
import com.android.launcher.livemonitor.bean.PicBean;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2020/12/3.
 *
 * @author Simon
 */
public class PicAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<PicImgRsp.Data> list;
    private AdapterView.OnItemClickListener listener;
    private AdapterView.OnItemClickListener updatelistener;
    public PicAdapter(Context context,List<PicImgRsp.Data> list, AdapterView.OnItemClickListener listener)
    {
        this.context=context;
        this.list=list;
        this.listener=listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_pic,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder= (MyViewHolder) holder;
         if (position==0)
         {
             viewHolder.image.setBackgroundResource(R.drawable.bg_pic_add);
             viewHolder.reduce.setVisibility(View.GONE);
             viewHolder.update.setVisibility(View.GONE);
         }else {
             Glide.with(context).load(list.get(position-1).getImg()).into(viewHolder.image);
         }

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(null,viewHolder.image,position,getItemId(position));
                }
            }
        });

        viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删去
                imagePeopleSave(list.get(position-1).getId());
            }
        });

        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改
                if (updatelistener!=null && position>0){
                    updatelistener.onItemClick(null,viewHolder.image,position,getItemId(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size()+1;
        }
        return 1;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView image;
        private ImageView reduce;
        private ImageView update;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            reduce=itemView.findViewById(R.id.im_reduce);
            update=itemView.findViewById(R.id.im_update);
        }
    }


    //删除贴纸素材图片
    private void imagePeopleSave(int tag_id) {
        APIFactory.INSTANCE.create().imagePeopleSave(NaoManager.INSTANCE.getAccessToken(),tag_id,0,"")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<PicImgRsp>(){
                    @Override
                    public void accept(PicImgRsp picImgRsp) throws Exception {
                        if (picImgRsp.getCode()==0){
                            imgPeopleList();
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
                           list=picImgRsp.getResult().getLists().getData();
                           notifyDataSetChanged();
                        }
                    }
                });
    }

    //重置数据
    public void reset(){
        list=null;
        notifyDataSetChanged();
    }

    public void setUpdatelistener(AdapterView.OnItemClickListener listener){
        updatelistener=listener;
    }

    public PicImgRsp.Data getListData(int position) {
        if (position==0){
            return null;
        }
        return list.get(position-1);
    }
}
