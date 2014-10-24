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
import git.force.push.lvivrouter.ui.model.HistoryItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements HistoryAdapter.OnHistoryItemClick, HistoryAdapter.OnRoutesButtonClick{

    public static interface OnHistoryItemSelected{
        public void onHistoryItemSelected(HistoryItem item);
    }

    private List<HistoryItem> mHistoryItems;
    private OnHistoryItemSelected mItemListener;

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryItems = new ArrayList<HistoryItem>();
        for(int i = 0; i < 36; i++){
            String fromString = "Початкова";
            String toString = "Кiнцева";
            mHistoryItems.add(new HistoryItem(fromString, Integer.toString(i), toString, Integer.toString(i)));
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
        HistoryAdapter adapter = new HistoryAdapter(getActivity(), mHistoryItems);
        adapter.setHistoryItemClickListener(this);
        adapter.setRoutesButtonListener(this);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setHistoryItemsListener(OnHistoryItemSelected listener){
        mItemListener = listener;
    }

    @Override
    public void onHistoryItemClick(HistoryItem item) {
        if(mItemListener != null){
            mItemListener.onHistoryItemSelected(item);
        }
    }

    @Override
    public void onRoutesButtonClick(HistoryItem item) {

    }
}
