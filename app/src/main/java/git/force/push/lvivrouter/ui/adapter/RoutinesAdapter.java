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
import git.force.push.lvivrouter.ui.model.RoutineItem;

/**
 * Created by admin on 10/24/2014.
 */
public class RoutinesAdapter extends RecyclerView.Adapter<RoutinesAdapter.ViewHolder> {

    private List<RoutineItem> mItems;
    private LayoutInflater mInflater;

    public RoutinesAdapter(Context context, List<RoutineItem> items){
        mItems = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RoutinesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_history, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.fromTextView = (TextView)view.findViewById(R.id.fromTextView);
        holder.toTextView = (TextView)view.findViewById(R.id.toTextView);
        holder.routineNumber = (TextView)view.findViewById(R.id.routineNumberTextView);
        holder.button = view.findViewById(R.id.buttonRoutes);
        return holder;
    }

    @Override
    public void onBindViewHolder(RoutinesAdapter.ViewHolder viewHolder, int i) {
        final RoutineItem item = mItems.get(i);
        viewHolder.fromTextView.setText(item.getRoutineStart());
        viewHolder.toTextView.setText(item.getRoutineEnd());
        viewHolder.routineNumber.setText(Integer.toString(item.getNumber()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView routineNumber;
        TextView fromTextView;
        TextView toTextView;
        View button;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
