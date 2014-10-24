package git.force.push.lvivrouter.ui.model;

/**
 * Created by admin on 10/24/2014.
 */
public class RoutineItem {

    private String mRoutineStart;
    private String mRoutineEnd;
    private int mNumber;

    public RoutineItem(int number, String routineStart, String routineEnd){
        mRoutineStart = routineStart;
        mRoutineEnd = routineEnd;
        mNumber = number;
    }

    public String getRoutineEnd() {
        return mRoutineEnd;
    }

    public String getRoutineStart() {
        return mRoutineStart;
    }

    public int getNumber() {
        return mNumber;
    }
}
