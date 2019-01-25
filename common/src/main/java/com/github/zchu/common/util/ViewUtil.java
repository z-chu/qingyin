package com.github.zchu.common.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.IdRes;

/**
 * Created by Chu on 2017/8/9.
 */

public class ViewUtil {

    public static void setVisibility(View rootView, int visibility, @IdRes int... ids) {
        for (int id : ids) {
            View view = rootView.findViewById(id);
            if (view != null && view.getVisibility() != visibility) {
                view.setVisibility(visibility);
            }
        }
    }

    public static void setVisibility(Activity activity, int visibility, @IdRes int... ids) {
        for (int id : ids) {
            View view = activity.findViewById(id);
            if (view != null && view.getVisibility() != visibility) {
                view.setVisibility(visibility);
            }
        }
    }

    public static void setVisibility(int visibility, View... views) {
        for (View view : views) {
            if (view.getVisibility() != visibility)
                view.setVisibility(visibility);
        }
    }


    /**
     * Request layout parent.
     *
     * @param view  the view
     * @param isAll the is all
     */
    public static void requestLayoutParent(View view, boolean isAll) {
        ViewParent parent = view.getParent();
        while (parent instanceof View) {
            if (!parent.isLayoutRequested()) {
                parent.requestLayout();
                if (!isAll) {
                    break;
                }
            }
            parent = parent.getParent();
        }
    }


    /**
     * 测量view
     *
     * @param view the view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 获取view的宽度
     *
     * @param view the view
     * @return the view width
     */
    public static int getViewWidth(View view) {
        measureView(view);
        return view.getMeasuredWidth();
    }

    /**
     * 获取view的高度
     *
     * @param view the view
     * @return the view height
     */
    public static int getViewHeight(View view) {
        measureView(view);
        return view.getMeasuredHeight();
    }


    /**
     * 截图
     *
     * @param v the v
     * @return the bitmap
     */
    public static Bitmap captureView(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return v.getDrawingCache();
    }


    /**
     * 截图
     *
     * @param v the v
     * @return the bitmap
     */
    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }


    /**
     * 截图
     *
     * @param view the view
     * @return the bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }


    /**
     * 获取Activity的截图
     *
     * @param activity the activity
     * @return the activity bitmap
     */
    public static Bitmap getActivityBitmap(Activity activity) {
        View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }


}
