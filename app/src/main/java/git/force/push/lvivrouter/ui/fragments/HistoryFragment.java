package git.force.push.lvivrouter.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import git.force.push.lvivrouter.R;
import git.force.push.lvivrouter.ui.adapter.HistoryAdapter;
import git.force.push.lvivrouter.ui.model.HistoryItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private List<HistoryItem> mHistoryItems;

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryItems = new ArrayList<HistoryItem>();
        for(int i = 0; i < 36; i++){
            String fromString = "Початкова " + i;
            String toString = "Кiнцева " + i;
            mHistoryItems.add(new HistoryItem(fromString, toString));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final RecyclerView historyRecyclerView = (RecyclerView)getView().findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setAdapter(new HistoryAdapter(getActivity(), mHistoryItems));
    }
}
