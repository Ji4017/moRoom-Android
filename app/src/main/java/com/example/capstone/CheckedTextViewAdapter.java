package com.example.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CheckedTextViewAdapter extends RecyclerView.Adapter<CheckedTextViewAdapter.ChdContentsViewHolder> {

    final ArrayList<CheckedTextViewData> arrayList;
    final Context context;
    private WriteActivity writeActivity;

//    추후 DB에 true false 값 넣으려면
//    public boolean isSelected;


    public CheckedTextViewAdapter(ArrayList<CheckedTextViewData> arrayList, Context context, WriteActivity writeActivity) {
        this.arrayList = arrayList;
        this.context = context;
        this.writeActivity = writeActivity;
    }


    @NonNull
    @Override
    public ChdContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_checked_item, parent, false);
        return new ChdContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChdContentsViewHolder holder, int position) {
        holder.checkedTextView.setText(String.valueOf(arrayList.get(position).getListText()));
//        holder.checkedTextView.setChecked(arrayList.get(position).isSelected());

        holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CheckedTextView checkBox = (CheckedTextView) view;
                checkBox.setChecked(!checkBox.isChecked());

                // 체크 여부에 따라 리스트에 텍스트를 추가하거나 제거
                String text = String.valueOf(arrayList.get(holder.getAdapterPosition()).getListText());
                if (checkBox.isChecked()) {
                    // 체크되었을 때 리스트에 추가
                    writeActivity.addTextToList(text);
                } else {
                    // 체크 해제되었을 때 리스트에서 제거
                    writeActivity.removeTextFromList(text);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public static class ChdContentsViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView checkedTextView;

        public ChdContentsViewHolder(@NonNull View itemView) {
            super(itemView);
            checkedTextView = itemView.findViewById(R.id.checkedTextView);
        }
    }
}