package jetzt.machbarschaft.android.view.home;

import jetzt.machbarschaft.android.R;

public enum ModelObjectWarnings {
    WARNING_1(R.string.app_name, R.layout.warnings_slide1),
    WARNING_2(R.string.app_name, R.layout.warnings_slide2);


    private int mTitleResId;
    private int mLayoutResId;

    ModelObjectWarnings(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
