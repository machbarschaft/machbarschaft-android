package com.ks.einanrufhilft.view.login;

import com.ks.einanrufhilft.R;

/**
 * Model Object for the Slideshow during the Login/Register Process to inform new Users about the intention of the app.
 */
public enum ModelObject {

    intro1(R.string.app_name, R.layout.intro1),
    intro2(R.string.app_name, R.layout.intro2),
    intro3(R.string.app_name, R.layout.intro3),
    intro4(R.string.app_name, R.layout.intro4);

    private int mTitlteResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitlteResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitlteResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
