package com.moroom.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moroom.android.databinding.ListItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder> {
    final ArrayList<Contents> arrayList;
    final Context context;

    public ContentsAdapter(ArrayList<Contents> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentsViewHolder holder, int position) {
        holder.tv_title.setText(String.valueOf(arrayList.get(position).getTitle()));
        holder.tv_goodThing.setText(String.valueOf(arrayList.get(position).getGoodThing()));
        holder.tv_badThing.setText(String.valueOf(arrayList.get(position).getBadThing()));

        HashMap<String, String> selectedList = arrayList.get(position).getSelectedList();
        if (selectedList != null) {
            StringBuilder selectedListText = new StringBuilder();
            for (String key : selectedList.keySet()) {
                String value = selectedList.get(key);
                selectedListText.append(value).append("\n");
            }
            holder.tv_selectedList.setText(selectedListText.toString());
        } else {
            holder.tv_selectedList.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public static class ContentsViewHolder extends RecyclerView.ViewHolder {

        ListItemBinding listItemBinding;
        TextView tv_title;
        TextView tv_selectedList;
        TextView tv_goodThing;
        TextView tv_badThing;

        public ContentsViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemBinding = ListItemBinding.bind(itemView);
            tv_title = listItemBinding.tvTitle;
            tv_selectedList = listItemBinding.tvSelectedList;
            tv_goodThing = listItemBinding.tvGoodThing;
            tv_badThing = listItemBinding.tvBadThing;
        }
    }
}
