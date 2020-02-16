package com.bgp.seosumsan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bgp.seosumsan.DTO.PathData;
import com.bgp.seosumsan.R;

import java.util.ArrayList;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.CustomViewHolder> {
    private ArrayList<PathData> mList;

    public PathAdapter(ArrayList<PathData> list){this.mList = list; }
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView name;
        protected TextView start;
        protected TextView end;
        protected TextView txtTest;
        protected PathData data;
        protected int position;


        public CustomViewHolder(View view){
            super(view);
            this.txtTest = (TextView)view.findViewById(R.id.tag_click);
            this.name = (TextView)view.findViewById(R.id.nameText);
            this.start = (TextView)view.findViewById(R.id.startText);
            this.end = (TextView)view.findViewById(R.id.endText);
        }
        void onBind(PathData data, int position){
            this.data = data;
            this.position = position;

            itemView.setOnClickListener(this);
            this.txtTest.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.tag_click:
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                    break;
            }
        }

    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.path_list, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position){
        viewholder.onBind(mList.get(position),position);
        viewholder.name.setText(mList.get(position).getMember_name());
        viewholder.start.setText(mList.get(position).getMember_start());
        viewholder.end.setText(mList.get(position).getMember_end());
    }

    @Override
    public int getItemCount(){return (null!=mList ? mList.size() : 0); }
}
