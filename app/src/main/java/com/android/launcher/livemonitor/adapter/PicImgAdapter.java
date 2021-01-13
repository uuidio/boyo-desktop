package com.android.launcher.livemonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.R;
import com.android.launcher.livemonitor.api.entity.PicImgRsp;
import com.android.launcher.livemonitor.api.entity.TagListRsp;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created on 2020/12/3.
 *贴纸图片
 */
public class PicImgAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<PicImgRsp.Data> list;


    public PicImgAdapter(Context context, List<PicImgRsp.Data> list)
    {
        this.context=context;
        this.list=list;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_pic_img,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder= (MyViewHolder) holder;
        viewHolder.tv_name.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getImg()).into(viewHolder.image);

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
        private RelativeLayout rl_img_select;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            tv_name=itemView.findViewById(R.id.tv_name);
            rl_img_select=itemView.findViewById(R.id.rl_img_select);
        }
    }
}
