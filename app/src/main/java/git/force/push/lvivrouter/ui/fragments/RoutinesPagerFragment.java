package git.force.push.lvivrouter.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewPager pager = (ViewPager) getView().findViewById(R.id.viewPager);
        int[] icons = new int[]{R.drawable.ic_directions_bus_white, R.drawable.ic_directions_troll_white, R.drawable.ic_directions_tram_white};
        pager.setAdapter(new RoutinesPagerAdapter(getActivity(), getFragmentManager(), icons));
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) getView().findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        tabs.setIndicatorColor(0xFFFFC006);
    }
}
