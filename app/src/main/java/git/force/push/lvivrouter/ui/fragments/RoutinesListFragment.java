package git.force.push.lvivrouter.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import git.force.push.lvivrouter.R;
import git.force.push.lvivrouter.ui.adapter.HistoryAdapter;
import git.force.push.lvivrouter.ui.adapter.RoutinesAdapter;
import git.force.push.lvivrouter.ui.model.HistoryItem;
import git.force.push.lvivrouter.ui.model.RoutineItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutinesListFragment extends Fragment {

    private List<RoutineItem> mRoutinesList;
    public RoutinesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routines_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoutinesList = new ArrayList<RoutineItem>();
        for(int i = 0; i < 36; i++){
            String fromString = "Початкова маршруту " + i;
            String toString = "Кiнцева маршруту " + i;
            mRoutinesList.add(new RoutineItem(i, fromString,toString));
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final RecyclerView historyRecyclerView = (RecyclerView)getView().findViewById(R.id.historyRecyclerView);
        RoutinesAdapter adapter = new RoutinesAdapter(getActivity(), mRoutinesList);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
