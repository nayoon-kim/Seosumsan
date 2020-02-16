package com.bgp.seosumsan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bgp.seosumsan.DTO.SightsData;
import com.bgp.seosumsan.R;

import java.util.ArrayList;

public class SightsAdapter extends RecyclerView.Adapter<SightsAdapter.CustomViewHolder> {
    private ArrayList<SightsData> mList;

    public SightsAdapter(ArrayList<SightsData> list) {
        this.mList = list;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected ImageView image;

        public CustomViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.name_listitem);
            this.image = (ImageView) view.findViewById(R.id.image_listitem);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sights_list, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.name.setText(mList.get(position).getMember_name());
        if(mList.get(position).getMember_image()!=null)
            viewholder.image.setImageBitmap(mList.get(position).getMember_image());
    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
