package com.vardhamaninfo.khartargaccha.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vardhamaninfo.khartargaccha.Model.Gallery;
import com.vardhamaninfo.khartargaccha.R;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private Context context;
    private List<Gallery> galleryList;

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAlbumName;
        public TextView tvTotalPhotos;
        public ImageView ivGalleryItemImage;
        public RelativeLayout rlGalleryItem;

        public GalleryViewHolder(View view) {
            super(view);
            tvAlbumName = (TextView) view.findViewById(R.id.recyclerview_gallery_item_album_name);
            tvTotalPhotos = (TextView) view.findViewById(R.id.recyclerview_gallery_item_album_total_photos);
            ivGalleryItemImage = (ImageView) view.findViewById(R.id.recyclerview_gallery_item_image);
            rlGalleryItem = (RelativeLayout) view.findViewById(R.id.recyclerview_gallery_item_rl);
        }
    }

    public GalleryAdapter(Context context, List<Gallery> galleryList) {
        this.context = context;
        this.galleryList = galleryList;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_gallery_item, parent, false);
        return new GalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {

        Gallery galleryItem = galleryList.get(position);

        holder.tvAlbumName.setText(galleryItem.getTitle());
        holder.tvTotalPhotos.setText(galleryItem.getTotalPhotos() + "");

        Log.d("binding", "working");

        if (galleryItem.isHide() || galleryItem.getStatus() == "0") {
            Log.d("item", "gone");
            holder.rlGalleryItem.setVisibility(View.GONE);
        } else {
            Log.d("item", "set");
            Glide.with(context).load(galleryItem.getImageURL())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivGalleryItemImage);
        }
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }
}
