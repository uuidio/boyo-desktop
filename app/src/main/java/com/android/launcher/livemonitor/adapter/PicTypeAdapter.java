package com.android.launcher.livemonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.R;
import com.android.launcher.livemonitor.api.entity.TagListRsp;
import com.android.launcher.livemonitor.bean.PicBean;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created on 2020/12/3.
 *贴纸分类
 */
public class PicTypeAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<TagListRsp.Data> list;
    private AdapterView.OnItemClickListener listener;

    public PicTypeAdapter(Context context, List<TagListRsp.Data> list, AdapterView.OnItemClickListener listener)
    {
        this.context=context;
        this.list=list;
        this.listener=listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_pic_type,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder= (MyViewHolder) holder;
        viewHolder.tv_name.setText(list.get(position).getName());

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(null,viewHolder.image,position,getItemId(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView image;
        private TextView tv_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            tv_name=itemView.findViewById(R.id.tv_name);
        }
    }

    public List<TagListRsp.Data> getList(){
        return this.list;
    }
}
