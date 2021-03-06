package com.android.launcher.livemonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.android.launcher.R;
import com.android.launcher.livemonitor.bean.AudioSelectBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2020/12/3.
 *
 * @author Simon
 */
public class AudioSelectAdapter  extends RecyclerView.Adapter {

   private Context context;
   private List<AudioSelectBean> list;

    public AudioSelectAdapter(Context context, List<AudioSelectBean> list)
    {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_audio_select,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myHolder= (MyViewHolder) holder;
        switch (position){
            case 0:
                myHolder.radioButton.setText("HDMI 1");
                break;
            case 1:
                myHolder.radioButton.setText("HDMI 2");
                break;
            case 2:
                myHolder.radioButton.setText("USB");
                break;
            case 3:
                myHolder.radioButton.setText("Line in");
                break;
            case 4:
                myHolder.radioButton.setText("Mic in");
                break;

        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private RadioButton radioButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.radio);
        }
    }
}
