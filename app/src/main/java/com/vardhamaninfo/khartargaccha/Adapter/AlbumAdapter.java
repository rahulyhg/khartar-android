package com.vardhamaninfo.khartargaccha.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vardhamaninfo.khartargaccha.Model.Album;
import com.vardhamaninfo.khartargaccha.Model.Gallery;
import com.vardhamaninfo.khartargaccha.R;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private Context context;
    private List<Album> albumList;

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        public ImageView sdvAlbumPhoto;

        public AlbumViewHolder(View view) {
            super(view);
            sdvAlbumPhoto = (ImageView) view.findViewById(R.id.recyclerview_album_photo_item);
        }
    }

    public AlbumAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_album_photo, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {

        Album album = albumList.get(position);

        Log.d("Image", album.getAlbumImageURL() + "");

//        holder.sdvAlbumPhoto.setImageURI(album.getAlbumImageURL());

        Glide.with(context).load(album.getAlbumImageURL())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.sdvAlbumPhoto);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
