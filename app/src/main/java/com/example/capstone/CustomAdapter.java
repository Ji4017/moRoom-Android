package com.example.capstone;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.databinding.ListItemBinding;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    final ArrayList<User> arrayList;
    final Context context;

    public CustomAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // 객체 담아서 리스트 컬럼 여러 개 뿌려줌
        holder.tv_id.setText(String.valueOf(arrayList.get(position).getId()));
        holder.tv_pw.setText(String.valueOf(arrayList.get(position).getPw()));
        holder.tv_userName.setText(String.valueOf(arrayList.get(position).getUserName()));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        ListItemBinding listItemBinding;
        TextView tv_id;
        TextView tv_pw;
        TextView tv_userName;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemBinding = ListItemBinding.bind(itemView);
            if (listItemBinding != null && listItemBinding.tvId != null) {
                tv_id = listItemBinding.tvId;
                tv_pw = listItemBinding.tvPw;
                tv_userName = listItemBinding.tvUserName;
            } else {
                // tvID가 null인 경우
                Log.e(TAG, "널 에러 발생");
            }
//            tv_id = listItemBinding.tvId;
//            tv_pw = listItemBinding.tvPw;
//            tv_userName = listItemBinding.tvUserName;
        }
    }
}
