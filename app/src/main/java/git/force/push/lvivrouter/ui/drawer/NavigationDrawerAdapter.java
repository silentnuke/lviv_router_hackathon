package git.force.push.lvivrouter.ui.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import git.force.push.lvivrouter.R;

public class NavigationDrawerAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;

    public NavigationDrawerAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return NavItems.values().length;
    }

    @Override
    public NavItems getItem(int position) {
        return NavItems.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.drawer_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.build(getItem(position));

        return convertView;
    }

    private static class ViewHolder {
        TextView title;

        public ViewHolder(View convertView) {
            title = (TextView) convertView;
        }

        public void build(NavItems item) {
            title.setText(item.getTitleRes());
        }
    }
}
