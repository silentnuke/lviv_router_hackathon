package git.force.push.lvivrouter.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import git.force.push.lvivrouter.ui.fragments.RoutinesListFragment;

/**
 * Created by admin on 10/24/2014.
 */
public class RoutinesPagerAdapter extends FragmentPagerAdapter {//implements PagerSlidingTabStrip.TabCustomViewProvider {

    int[] mIcons;
    public RoutinesPagerAdapter(FragmentManager fm, int[] icons) {
        super(fm);
        mIcons = icons;
    }

    @Override
    public Fragment getItem(int i) {
        return new RoutinesListFragment();
    }

    @Override
    public int getCount() {
        return mIcons.length;
    }


}
