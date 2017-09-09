package com.vardhamaninfo.khartargaccha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vardhamaninfo.khartargaccha.Model.Video;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Activity.YoutubeVideoActivity;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.Holder> {

    private Context context;
    private ArrayList<Video> videos;

    public VideoListAdapter(Context context, ArrayList<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    class Holder extends RecyclerView.ViewHolder {

        public ImageView ivThumb;
        public TextView tvTitle, tvDescription;
        public LinearLayout llMain;

        public Holder(View itemView) {
            super(itemView);

            ivThumb = (ImageView) itemView.findViewById(R.id.recyclerview_video_iv_thumb);
            tvTitle = (TextView) itemView.findViewById(R.id.recyclerview_video_tv_title);
            tvDescription = (TextView) itemView.findViewById(R.id.recyclerview_video_tv_description);
            llMain = (LinearLayout) itemView.findViewById(R.id.recyclerview_video_ll);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_video, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {

        if (videos.get(position).getTitle() != null && !videos.get(position).getTitle().equals("")) {
            holder.tvTitle.setText(videos.get(position).getTitle());
        }

        if (videos.get(position).getDescription() != null && !videos.get(position).getDescription().equals("")) {
            holder.tvDescription.setText(videos.get(position).getDescription());
        }

        if (videos.get(position).getImageURL() != null && !videos.get(position).getImageURL().equals("")) {

            Glide.with(context).load(videos.get(position).getImageURL())
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.mipmap.logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivThumb);
        }

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "called");

                Intent intent = new Intent(context, YoutubeVideoActivity.class);
                intent.putExtra("videoID", videos.get(position).getVideoID());
                intent.putExtra("title", videos.get(position).getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
