package com.android.launcher.livemonitor.adapter;

import android.content.Context;
import android.util.Log;
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
import com.android.launcher.livemonitor.api.entity.AutocueClassifyRsp;
import com.android.launcher.livemonitor.api.entity.TagListRsp;

import java.util.List;

/**
 * Created on 2020/12/3.
 *题词分类
 */
public class AutocueClassifyAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<AutocueClassifyRsp.Data> list;
    private AdapterView.OnItemClickListener listener;
    private int curryIndex=-1;

    public AutocueClassifyAdapter(Context context, List<AutocueClassifyRsp.Data> list, AdapterView.OnItemClickListener listener)
    {
        this.context=context;
        this.list=list;
        this.listener=listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_autocue_classify,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder= (MyViewHolder) holder;
        viewHolder.tv_name.setText(list.get(position).getClassify_name());
        viewHolder.tv_date.setText(list.get(position).getUpdated_at());

        viewHolder.rl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curryIndex=position;
                notifyDataSetChanged();
                if (listener!=null){
                    listener.onItemClick(null,viewHolder.rl_title,position,getItemId(position));
                }
            }
        });


        if (curryIndex==position){
            viewHolder.rl_title.setBackgroundResource(R.drawable.bg_autocue_sel);
        }else{
            viewHolder.rl_title.setBackgroundResource(R.drawable.bg_autocue);
        }
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

        private TextView tv_date;
        private TextView tv_name;
        private RelativeLayout rl_title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_date=itemView.findViewById(R.id.tv_date);
            rl_title=itemView.findViewById(R.id.rl_title);
        }
    }

    public List<AutocueClassifyRsp.Data> getList(){
        return this.list;
    }
}
