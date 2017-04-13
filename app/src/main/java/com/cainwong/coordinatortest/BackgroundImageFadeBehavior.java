package com.cainwong.coordinatortest;

import android.content.Context;
import android.content.res.TypedArray;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

public class BackgroundImageFadeBehavior  extends CoordinatorLayout.Behavior<View> {

    private final static float MIN_AVATAR_PERCENTAGE_SIZE   = 0.3f;
    private final static int EXTRA_FINAL_AVATAR_PADDING     = 80;

    private final static String TAG = "behavior";
    private Context mContext;

    private final float mTopOffset;
    private float mStartToolbarPosition = -1;

    public BackgroundImageFadeBehavior(Context context, int topOffsetDp) {
        mContext = context;
        mTopOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, topOffsetDp, context.getResources().getDisplayMetrics());
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof ViewPager;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        float pos = dependency.getY() - mTopOffset;
        if (mStartToolbarPosition == -1) {
            mStartToolbarPosition = pos;
        }

        child.setAlpha( pos / mStartToolbarPosition );

        return true;
    }

}