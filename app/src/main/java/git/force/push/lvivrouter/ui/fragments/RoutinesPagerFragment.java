package git.force.push.lvivrouter.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import git.force.push.lvivrouter.R;
import git.force.push.lvivrouter.ui.adapter.RoutinesPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutinesPagerFragment extends android.support.v4.app.Fragment {


    public RoutinesPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routines_pager, container, false);
        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) view.findViewById(R.id.viewPager);
        int[] icons = new int[]{R.drawable.ic_directions_grey600_24dp, R.drawable.ic_directions_grey600_24dp, R.drawable.ic_directions_grey600_24dp};
//        pager.setAdapter(new RoutinesPagerAdapter(getFragmentManager()), icons);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        return view;
    }


}
