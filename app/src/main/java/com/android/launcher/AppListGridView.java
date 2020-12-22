package com.android.launcher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by grant on 18-11-30.
 */

public class AppListGridView extends GridView {
    public AppListGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AppListGridView(Context context) {
        super(context);
    }
    public AppListGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
