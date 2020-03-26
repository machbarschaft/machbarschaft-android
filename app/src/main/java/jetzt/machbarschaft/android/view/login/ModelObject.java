package jetzt.machbarschaft.android.view.login;

import jetzt.machbarschaft.android.R;

/**
 * Model Object for the Slideshow during the Login/Register Process to inform new Users about the intention of the app.
 */
public enum ModelObject {
    INTRO_1(R.string.app_name, R.layout.intro1),
    INTRO_2(R.string.app_name, R.layout.intro2),
    INTRO_3(R.string.app_name, R.layout.intro3),
    INTRO_4(R.string.app_name, R.layout.intro4);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
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
