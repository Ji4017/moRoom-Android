package com.moroom.android.ui.adapter.write;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moroom.android.data.model.CheckItem;
import com.moroom.android.R;
import com.moroom.android.ui.write.WriteActivity;

import java.util.ArrayList;

public class CheckItemAdapter extends RecyclerView.Adapter<CheckItemAdapter.ChdContentsViewHolder> {

    final ArrayList<CheckItem> arrayList;
    final Context context;
    private WriteActivity writeActivity;


    public CheckItemAdapter(ArrayList<CheckItem> arrayList, Context context, WriteActivity writeActivity) {
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
        CheckItem checkItem = arrayList.get(position);
        holder.checkedTextView.setText(checkItem.getListText());
        holder.checkedTextView.setChecked(checkItem.isChecked());

        holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CheckedTextView checkBox = (CheckedTextView) view;
                boolean isChecked = !checkBox.isChecked();

                // 체크 여부를 데이터 모델에 업데이트
                checkItem.setChecked(isChecked);

                // 체크 여부에 따라 리스트에 텍스트를 추가하거나 제거
                String text = checkItem.getListText();
                if (isChecked) {
                    // 체크되었을 때 리스트에 추가
                    writeActivity.addTextToList(text);
                } else {
                    // 체크 해제되었을 때 리스트에서 제거
                    writeActivity.removeTextFromList(text);
                }

                checkBox.setChecked(isChecked); // 체크박스 상태 업데이트
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