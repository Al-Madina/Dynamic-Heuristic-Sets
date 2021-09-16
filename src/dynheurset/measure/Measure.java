package dynheurset.measure;

import dynheurset.RunStat;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>Measure</code> is the base class for all performance measures that 
 * aimed at measuring the quality of low-level heuristics.
 * <p>
 * Performance measures are should be used to decide which heuristic to include
 * in the active set and which heuristic to permanently removed if needed.
 * <p>
 * All subclasses of this class should implement the <code>measure(int heurIdx)</code>
 * abstract method.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public abstract class Measure {
    /**
     * The run statistics to record the heuristic performance.
     */
    public RunStat runStat;
    
    /**
     * A constant used to assign a high value for heuristic that have not been used
     * yet to give them the chance to be used at least once
     */
    protected final static double MAX = 1e6;
    
    /**
     * Sets the <code>runStat</code> field of this measure.
     * @param runStat the observer that record the performance of heuristics
     */
    public void setRunStat(RunStat runStat){
        this.runStat = runStat;
    }
    
    /**
     * Measure the performance of the heuristic indexed by <code>heurIdx</code> in
     * the universal set.
     * @param heurIdx the index of the heuristic in the universal set
     * @return the value of heuristic representing its quality
     */
    public abstract double measure(int heurIdx);
    
    /**
     * Loops through all heuristic indexes and returns a <code>List</code>
     * containing the heuristic values as measured by this measure.
     * <p>
     * The value at index <code>idx</code> in the returned list is the value of 
     * the heuristic at index <code>idx</code> in the universal set.
     * @return the values of all heuristics as an array list
     */
    public List<Double> measure(){
        List<Double> heurValues = new ArrayList<>(runStat.heurList.length);
        for(int heurIdx=0; heurIdx < runStat.heurList.length; heurIdx++){
            heurValues.add(measure(heurIdx));
        }
        return heurValues;
    }
}
