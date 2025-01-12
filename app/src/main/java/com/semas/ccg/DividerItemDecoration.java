package com.semas.ccg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint paint;
    private final int width;

    public DividerItemDecoration(int color, int width) {
        paint = new Paint();
        paint.setColor(color);
        this.width = width;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int top, bottom;

        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child = parent.getChildAt(i);

            int left = child.getRight();
            int right = left + width;


            top = child.getTop();
            bottom = child.getBottom();


            c.drawRect(left, top, right, bottom, paint);
        }
    }
}