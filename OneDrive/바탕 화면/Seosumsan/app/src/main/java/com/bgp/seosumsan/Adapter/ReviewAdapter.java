package com.bgp.seosumsan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bgp.seosumsan.DTO.ReviewData;
import com.bgp.seosumsan.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder>{
    private ArrayList<ReviewData> mList;
    public ReviewAdapter(ArrayList<ReviewData> list) { this.mList = list;}
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected RatingBar rate;
        protected TextView review;
        public CustomViewHolder(@NonNull View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.review_userid);
            this.rate=(RatingBar) view.findViewById(R.id.review_ratingBar);
            this.review = (TextView) view.findViewById(R.id.review_text);
        }
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_list, viewGroup, false);
        CustomViewHolder viewHolder=  new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.name.setText(mList.get(position).getName());
        viewholder.rate.setRating(mList.get(position).getEvaluation());
        viewholder.review.setText(mList.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
