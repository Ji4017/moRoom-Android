package com.moroom.android.ui.nav.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ViewToBitmap {
    public static Bitmap createBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Drawable background = view.getBackground();
        if (background != null) {
            background.draw(canvas);
            view.draw(canvas);
        }
        return bitmap;
    }
}
