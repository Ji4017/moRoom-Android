package com.moroom.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moroom.android.databinding.ListItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder> {

    final ArrayList<Contents> arrayList;
    final Context context;

//    public ContentsAdapter(ArrayList<Contents> arrayList) {
//        this.arrayList = arrayList;
//        this.context = null;
//    }

    public ContentsAdapter(ArrayList<Contents> arrayList, Context context){
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
        // 객체 담아서 리스트 컬럼 여러 개 뿌려줌
//        Contents contents = arrayList.get(position);
//
//        applyBlurEffect(holder.itemView);
//
//        if (contents.isBlur()) {
//            Log.d("ContentsAdapter : ", "if is blur True 진입 완료");
//            // 데이터가 블러 처리된 경우, 블러 효과 적용
//            applyBlurEffect(holder.itemView);
//        }else {
//            Log.d("ContentsAdapter : ", "else blur false 진입 완료");
//            // 일반적인 스타일로 표시
//            holder.itemView.setBackground(null);
//
//            // 기존의 데이터 표시 코드
//            holder.tv_title.setText(String.valueOf(contents.getTitle()));
//            holder.tv_goodThing.setText(String.valueOf(contents.getGoodThing()));
//            holder.tv_badThing.setText(String.valueOf(contents.getBadThing()));
//
//            // selectedList 값 처리
//            HashMap<String, String> selectedList = contents.getSelectedList();
//            StringBuilder selectedListText = new StringBuilder();
//
//            // 리스트의 각 데이터를 줄바꿈 해서 보여주기 위해 selectedListText에 값을 담아 보여줌
//            for (String key : selectedList.keySet()) {
//                String value = selectedList.get(key);
//                selectedListText.append(value).append("\n");
//            }
//
//            holder.tv_selectedList.setText(selectedListText.toString());
//        }

                // 객체 담아서 리스트 컬럼 여러 개 뿌려줌
        holder.tv_title.setText(String.valueOf(arrayList.get(position).getTitle()));
        holder.tv_goodThing.setText(String.valueOf(arrayList.get(position).getGoodThing()));
        holder.tv_badThing.setText(String.valueOf(arrayList.get(position).getBadThing()));

        // selectedList 값 처리
        HashMap<String, String> selectedList = arrayList.get(position).getSelectedList();
        StringBuilder selectedListText = new StringBuilder();

        // 리스트의 각 데이터를 줄바꿈 해서 보여주기 위해 selectedListText에 값을 담아 보여줌
        for (String key : selectedList.keySet()) {
            String value = selectedList.get(key);
            selectedListText.append(value).append("\n");
        }

        holder.tv_selectedList.setText(selectedListText.toString());

        // isBlur 값에 따라 리뷰를 블러 처리
        Contents contents = new Contents();
        // Log.d("이즈블러 : ", "review is - " + contents.isBlur());
        if (contents.isBlur()) {
            // 리뷰를 블러 처리하는 코드 작성
            holder.tv_title.setText("****");
            holder.tv_goodThing.setText("****");
            holder.tv_badThing.setText("****");
            holder.tv_selectedList.setText("****");
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

    private void applyBlurEffect(View view) {
        // view의 너비와 높이가 0인지 확인합니다.
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            // view가 준비되지 않았으므로 블러 효과를 지연시킵니다.
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // 여러 번 호출되는 것을 방지하기 위해 리스너를 제거합니다.
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // view가 측정된 후에 applyBlurEffect 메서드를 다시 호출합니다.
                    applyBlurEffect(view);
                }
            });
            return;
        }


        // RenderScript를 사용하여 블러 효과 적용
        RenderScript renderScript = RenderScript.create(context);

        // 블러 처리를 위한 스크립트 객체 생성
        ScriptIntrinsicBlur scriptBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        // 블러 처리할 비트맵 생성
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        // 비트맵을 RenderScript에 전달하여 블러 효과 적용
        Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation output = Allocation.createTyped(renderScript, input.getType());
        scriptBlur.setRadius(10f); // 블러 강도 설정
        scriptBlur.setInput(input);
        scriptBlur.forEach(output);

        // 블러 처리된 비트맵을 다시 View에 설정하여 블러 효과 적용
        output.copyTo(bitmap);
        BitmapDrawable blurredDrawable = new BitmapDrawable(context.getResources(), bitmap);
        view.setBackground(blurredDrawable);

        // RenderScript 사용 종료
        renderScript.destroy();
    }
}
