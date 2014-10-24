package git.force.push.lvivrouter.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.PagerSlidingTabStrip.TabCustomViewProvider;

import git.force.push.lvivrouter.R;
import git.force.push.lvivrouter.ui.fragments.RoutinesListFragment;

/**
 * Created by admin on 10/24/2014.
 */
public class RoutinesPagerAdapter extends FragmentPagerAdapter implements TabCustomViewProvider{

    int[] mIcons;
    private LayoutInflater mInflater;
    public RoutinesPagerAdapter(Context context, FragmentManager fm, int[] icons) {
        super(fm);
        mIcons = icons;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Fragment getItem(int i) {
        return new RoutinesListFragment();
    }

    @Override
    public int getCount() {
        return mIcons.length;
    }

    @Override
    public View getPageTabCustomView(int position) {
        View view =  mInflater.inflate(R.layout.item_pager_header, null, false);
        ((ImageView)view.findViewById(R.id.image)).setImageResource(mIcons[position]);
        return view;
    }
}
