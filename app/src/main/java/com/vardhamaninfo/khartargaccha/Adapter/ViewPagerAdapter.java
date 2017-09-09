package com.vardhamaninfo.khartargaccha.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vardhamaninfo.khartargaccha.Custom.MyPhotoView;


import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Vardhaman Infotech 5 on 8/20/2015.
 */
public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    String imag;
    int count;
    LayoutInflater mLayoutInflater;
    PhotoViewAttacher mAttacher;
    MyPhotoView mAttacher1;
    private GestureDetector gestureDetector;

    public ViewPagerAdapter(Context context, String images) {
        mLayoutInflater=LayoutInflater.from(context);
        this.context = context;
        this.count=count;
        this.imag=images;
        //mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imageView = new ImageView(context);


      // imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"onClick",Toast.LENGTH_SHORT).show();
            }
        });*/

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show();
            }
        });

        //imageView.setImageResource();

        //Log.d("adapter view images",imag[position]);

        //Picasso.with(context).load(imag[position]).error(R.drawable.ic_action_cart).into(imageView);
        Glide.with(context).load(imag).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                mAttacher1 = new MyPhotoView(context,imageView);
                //mAttacher1.setScaleType(ImageView.ScaleType.FIT_CENTER );
                mAttacher1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                return false;
            }
        }).into(imageView);
        ((ViewPager) container).addView(imageView, 0);


        //---------------------------------parser--------------------------------------

        return imageView;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}