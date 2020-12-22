package com.android.launcher.livemonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.launcher.R;
import com.android.launcher.livemonitor.bean.PicBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2020/12/3.
 *
 * @author Simon
 */
public class PicAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<PicBean> list;

    public PicAdapter(Context context,List<PicBean> list)
    {
        this.context=context;
        this.list=list;
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
         }else {

         }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView image;
        private ImageView reduce;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            reduce=itemView.findViewById(R.id.im_reduce);
        }
    }
}
