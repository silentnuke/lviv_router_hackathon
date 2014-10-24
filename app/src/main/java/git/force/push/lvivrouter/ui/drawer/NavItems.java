package git.force.push.lvivrouter.ui.drawer;

import android.support.annotation.StringRes;

import git.force.push.lvivrouter.R;

public enum NavItems {
    SEARCH(R.string.nav_title_search),
    HISTORY(R.string.nav_title_history),
    ROUTES_LIST(R.string.nav_title_routes_list),
    ABOUT(R.string.nav_title_about);

    private final int mTitleRes;

    NavItems(@StringRes int titleRes) {
        mTitleRes = titleRes;
    }

    @StringRes
    public int getTitleRes() {
        return mTitleRes;
    }
}
