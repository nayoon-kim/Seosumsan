package com.bgp.seosumsan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bgp.seosumsan.R;

import java.util.ArrayList;

public class PageAdapter extends PagerAdapter {
    private ArrayList<Bitmap> images;
    private LayoutInflater inflater;
    private Context context;

    public PageAdapter(ArrayList<Bitmap> bitmaps, Context context){
        this.images = bitmaps;
        this.context = context;
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.slider_image);
        imageView.setImageBitmap(images.get(position));
        container.addView(v);
        return v;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.invalidate();
    }
}
