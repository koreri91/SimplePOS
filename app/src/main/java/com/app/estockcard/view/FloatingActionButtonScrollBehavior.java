package com.app.estockcard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FloatingActionButtonScrollBehavior extends FloatingActionButton.Behavior {

    /* Must provide this constructer method, otherwise app will throw Could not inflate Behavior subclass error message. */
    public FloatingActionButtonScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes, int type) {
        boolean ret;
        if(nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL)
        {
            ret = true;
        }else {
            ret = super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes, type);
        }
        return ret;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0) {
            if(child.getVisibility() == View.VISIBLE) {
                child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton floatingActionButton) {
                        super.onHidden(floatingActionButton);
                        floatingActionButton.hide();
                    }
                });
            }
        }
        // Means recyclerview scroll up.
        else if (dyConsumed < 0 ) {
            child.show(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onShown(FloatingActionButton fab) {
                    super.onShown(fab);
                    fab.show();
                }
            });
        }
    }

}