package com.td.framework.ui.appbar;

import android.support.design.widget.AppBarLayout;

/**APP bar 状态变更*/
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
 
    public enum State {
        /**展开*/
        EXPANDED,
        /**关闭*/
        COLLAPSED,
        /**中间*/
        IDLE
    }
 
    private State mCurrentState = State.IDLE;
 
    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }
 
    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}