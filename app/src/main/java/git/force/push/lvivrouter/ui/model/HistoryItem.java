package git.force.push.lvivrouter.ui.model;

/**
 * Created by admin on 10/24/2014.
 */
public class HistoryItem {

    private String mFrom;
    private String mTo;

    public HistoryItem(String from, String to){
        mFrom = from;
        mTo = to;
    }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }
}
