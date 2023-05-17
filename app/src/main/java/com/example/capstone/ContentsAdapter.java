package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.databinding.ListItemBinding;

import java.util.ArrayList;

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder> {

    final ArrayList<Contents> arrayList;
//    final Context context;

    public ContentsAdapter(ArrayList<Contents> arrayList) {
        this.arrayList = arrayList;
//        this.context = context;
    }

    @NonNull
    @Override
    public ContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentsViewHolder holder, int position) {
        // 객체 담아서 리스트 컬럼 여러 개 뿌려줌
        holder.tv_title.setText(String.valueOf(arrayList.get(position).getTitle()));
        holder.tv_cbxList.setText(String.valueOf(arrayList.get(position).getCbxList()));
        holder.tv_opinion.setText(String.valueOf(arrayList.get(position).getOpinion()));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public static class ContentsViewHolder extends RecyclerView.ViewHolder {

        ListItemBinding listItemBinding;
        TextView tv_title;
        TextView tv_cbxList;
        TextView tv_opinion;

        public ContentsViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemBinding = ListItemBinding.bind(itemView);
            tv_title = listItemBinding.tvTitle;
            tv_cbxList = listItemBinding.tvCbxList;
            tv_opinion = listItemBinding.tvOpinion;
        }
    }
}
