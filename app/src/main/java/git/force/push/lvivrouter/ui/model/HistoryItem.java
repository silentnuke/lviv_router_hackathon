package git.force.push.lvivrouter.ui.model;

/**
 * Created by admin on 10/24/2014.
 */
public class HistoryItem {

    private String mFromStreet;
    private String mToStreet;
    private String mNumberFrom;
    private String mNumberTo;

    public HistoryItem(String fromStreet, String fromNumber, String toStreet, String toNumber){
        mFromStreet = fromStreet;
        mToStreet = toStreet;
        mNumberFrom = fromNumber;
        mNumberTo = toNumber;
    }

    public String getFromStreet() {
        return mFromStreet;
    }

    public String getToStreet() {
        return mToStreet;
    }

    public String getFromNumber() {
        return mNumberFrom;
    }

    public String getToNumber() {
        return mNumberTo;
    }
}
