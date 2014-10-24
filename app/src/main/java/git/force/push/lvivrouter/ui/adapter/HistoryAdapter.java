package git.force.push.lvivrouter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import git.force.push.lvivrouter.R;
import git.force.push.lvivrouter.ui.model.HistoryItem;

/**
 * Created by admin on 10/24/2014.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryItem> mItems;
    private LayoutInflater mInflater;

    public HistoryAdapter(Context context, List<HistoryItem> items){
        mItems = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_history, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.fromTextView = (TextView)view.findViewById(R.id.fromTextView);
        holder.toTextView = (TextView)view.findViewById(R.id.toTextView);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int i) {
        HistoryItem item = mItems.get(i);
        viewHolder.fromTextView.setText(item.getFrom());
        viewHolder.toTextView.setText(item.getFrom());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView fromTextView;
        TextView toTextView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
