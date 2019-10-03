package com.zero.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//音乐列表适配器
public class MusicPlayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<String> musicNamePaths;
    //私有属性
    private OnItemClickListener onItemClickListener = null;

    public MusicPlayAdapter(Context context, List<String> paths) {
        this.context = context;
        this.musicNamePaths = paths;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(mLayoutInflater.inflate(R.layout.adapter_item_music, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) holder;
        if (musicNamePaths != null && musicNamePaths.size() > position) {
            myHolder.tvMusicName.setText(musicNamePaths.get(position));
            myHolder.layoutMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musicNamePaths == null ? 0 : musicNamePaths.size();
    }


    //set方法
    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private LinearLayout layoutMusic;
        private TextView tvMusicName;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            layoutMusic = itemView.findViewById(R.id.layoutMusic);
            tvMusicName = itemView.findViewById(R.id.tvMusicName);
        }
    }
}
