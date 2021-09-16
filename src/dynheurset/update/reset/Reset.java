package dynheurset.update.reset;

import dynheurset.RunStat;
import dynheurset.measure.Measure;
import java.util.List;

/**
 * This class defines the reset strategy that is used by the update strategy.
 * <p>
 * The reset strategy determines the reset condition (when to reset the active set
 * such that it includes all heuristics except for permanently removed heuristics).
 * The reset operation is performed in <code>updateActiveList</code> method of the
 * <code>Update</code> class since the update strategy have access to the set of
 * permanently removed heuristics.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 * @see dynheurset.update.Update#updateActiveList()
 */
public abstract class Reset {
    public RunStat runStat;
    public Measure measure;
    
    List<Integer> heurList;
        
    public void setup(RunStat runStat, Measure measure){
        this.runStat = runStat;
        this.measure = measure;
    }
    
    public void setHeurList(List<Integer> heurList){
        this.heurList = heurList;
    }
    
    /**
     * Returns <code>true</code> if the active set should be reset and returns 
     * <code>false</code> otherwise.
     * <p>
     * The reset forces the active set to include all heuristics except for permanently
     * removed heuristics.
     * @return <code>true</code> if the active set should be reset and returns 
     * <code>false</code> otherwise.
     */
    public abstract boolean canReset();
    
//    public abstract List<Integer> resetHeurList();
}
