package dynheurset.update.remove;

import dynheurset.RunStat;
import dynheurset.measure.Measure;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class defines the removal strategy that is used by the update strategy.
 * <p>
 * The removal strategy determines:
 * <ul>
 * <li>The removal condition: when to trigger the removal operation
 * <li>The removal criterion: which heuristics to remove permanently
 * </ul>
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public abstract class Remove {
    
    public RunStat runStat;
    /**
     * The performance measure that measures the value of low-level heuristics. 
     */
    public Measure measure;
    /**
     * Universal set.
     */
    public List<Integer> heurList;
    /**
     * The set of permanently removed heuristics.
     */
    public Set<Integer> removedHeurSet;  
    
    
    public void setup(RunStat runStat, Measure measure){
        this.runStat = runStat;
        this.measure = measure;        
    }
    
    /**
     * Sets the universal set for this removal strategy.
     * <p>
     * The universal set should contain all heuristics represented as an array of
     * integers where each integer identifies a unique heuristic. 
     * @param heurList an array list of integers uniquely identifying the heuristics
     */
    public void setHeurList(List<Integer> heurList){
        this.heurList = heurList;
        removedHeurSet = new HashSet<>(heurList.size());
    }
    
    /**
     * Specifies the removal condition.
     * <p>
     * The removal condition determines when to remove some heuristics permanently.
     * @return <code>true</code>if a removal operation can be performed and <code>false</code>
     * otherwise.
     */
    public abstract boolean canRemove();
    
    /**
     * Specifies the removal criterion.
     * <p>
     * The removal criterion determines which heuristics to remove permanently.
     */
    public abstract void remove();
}
