package com.ks.einanrufhilft.view.login;

import com.ks.einanrufhilft.R;

public enum ModelObject {

    intro1(R.string.app_name, R.layout.intro1),
    intro2(R.string.app_name, R.layout.intro2),
    intro3(R.string.app_name, R.layout.intro3);

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
