package git.force.push.lvivrouter.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import git.force.push.lvivrouter.R;
import git.force.push.lvivrouter.ui.drawer.NavItems;

public class ContentFragment extends Fragment {
    public static final String ARG_CONTENT_NUMBER = "content_number";

    public ContentFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);
        int i = getArguments().getInt(ARG_CONTENT_NUMBER);

        ((TextView) rootView.findViewById(R.id.text)).setText(NavItems.values()[i].getTitleRes());
        getActivity().setTitle(NavItems.values()[i].getTitleRes());
        return rootView;
    }
}
