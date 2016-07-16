package ru.yandex.yamblz.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.midi.MidiDevice;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kostya on 16.07.16.
 */
public class CustomLayout extends ViewGroup{

    int height = 0;
    int width = 0;

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bot) {
        int numOfChildren = getChildCount();
        int current_left = left;
        for (int i = 0; i < numOfChildren; i++) {
            View child = getChildAt(i);
            int current_right = current_left + child.getMeasuredWidth();
            int current_bot = top + child.getMeasuredHeight();
            child.layout(current_left, top, current_right, current_bot);
            current_left = current_right;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        View childWithMatchParent = null;
        int sumOfChildrenWidth = 0;
        int maxOfChildrenHeight = 0;

        int numOfChildren = getChildCount();
        for (int i = 0; i < numOfChildren; i++) {
            View child = getChildAt(i);
            LayoutParams childLayoutParams = child.getLayoutParams();
            if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
                childWithMatchParent = child;
                continue;
            }
            int widthSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.width, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY);
            measureChild(child, widthSpec, heightSpec);
            sumOfChildrenWidth += child.getMeasuredWidth();
            maxOfChildrenHeight = Math.max(child.getMeasuredHeight(), maxOfChildrenHeight);
        }
        if (childWithMatchParent != null) {
            int widthOfMatchParentChild = widthSize - sumOfChildrenWidth;

            if (widthOfMatchParentChild < 0) {
                widthOfMatchParentChild = 0;
            }

            LayoutParams childLayoutParams = childWithMatchParent.getLayoutParams();

            int widthSpec = MeasureSpec.makeMeasureSpec(widthOfMatchParentChild, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY);

            measureChild(childWithMatchParent, widthSpec, heightSpec);
            sumOfChildrenWidth += childWithMatchParent.getMeasuredWidth();
            maxOfChildrenHeight = Math.max(childWithMatchParent.getMeasuredHeight(), maxOfChildrenHeight);
        }

        int newWidth = resolveSize(sumOfChildrenWidth, widthMeasureSpec);
        int newHeight = resolveSize(maxOfChildrenHeight, heightMeasureSpec);
        setMeasuredDimension(newWidth, newHeight);
    }
}
