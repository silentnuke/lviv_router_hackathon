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

    public static interface OnHistoryItemClick{
        public void onHistoryItemClick(HistoryItem item);
    }
    public static interface OnRoutesButtonClick{
        public void onRoutesButtonClick(HistoryItem item);
    }

    private List<HistoryItem> mItems;
    private LayoutInflater mInflater;

    private OnHistoryItemClick mHistoryItemClickListener;
    private OnRoutesButtonClick mRoutesButtonListener;

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
        holder.root = view;
        holder.button = view.findViewById(R.id.buttonRoutes);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int i) {
        HistoryItem item = mItems.get(i);
        viewHolder.item = item;
        viewHolder.fromTextView.setText(item.getFromStreet() + " " + item.getFromNumber());
        viewHolder.toTextView.setText(item.getToStreet() + " " + item.getToNumber());
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        HistoryItem item;
        View root;
        View button;
        TextView fromTextView;
        TextView toTextView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setHistoryItemClickListener(OnHistoryItemClick historyItemClickListener) {
        this.mHistoryItemClickListener = historyItemClickListener;
    }

    public void setRoutesButtonListener(OnRoutesButtonClick routesButtonListener) {
        this.mRoutesButtonListener = routesButtonListener;
    }
}
