package dynheurset;

import java.util.List;
import dynheurset.update.Update;

/**
 * DynHeurSet implements basic functionalities of dynamic sets.
 * <p>
 * Dynamic sets can be used within hyper-heuristics to manage the set of low-level 
 * heuristics available for the hyper-heuristic to select from. This set is called 
 * the <i>active set</i> which is typically a subset of the <i>universal set</i> 
 * that represents the set of all low-level heuristics in a problem domain.
 * <p>
 * A dynamic set changes during the search allowing different heuristics to enter 
 * and leave the active set at different phases during the search. 
 * <p>
 * This dynamic set has a {@link RunStat} field to keep track of run-related 
 * statistics/variables such as the elapsed time, the best solution value, 
 * the improvement (deterioration) achieved (caused) by each heuristic and many more. 
 * <p>
 * This dynamic set has a {@link dynheurset.update.Update} field which implements 
 * the update strategy that defines the <i>update condition</i> (when
 * to update) and the <i>update criterion</i> (which heuristic to include in the active
 * set).
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class DynHeurSet {
    /**
     * The run statistics.
     */
    protected RunStat runStat;
    /**
     * The update strategy used to decide when and how to update the active set.
     */
    protected Update update;
        
    
    /**
     * Sets the <code>runStat</code> field of this dynamic set.
     * @param runStat records run-related information
     */
    public void setRunStat(RunStat runStat){
        this.runStat = runStat;
    }
    
    /**
     * Sets the update strategy for this dynamic set.
     * @param update the update strategy used by this dynamic set
     */
    public void setUpdate(Update update){
        this.update = update;       
    }
    
    
    /**
     * Sets the universal set for this dynamic heuristic set.
     * <p>
     * The universal set should contain all heuristics represented in an array of
     * integers where each integer identifies a unique heuristic. 
     * @param heurArray an integer array where each integer uniquely identifies a heuristic
     */
    public void setHeurList(int[] heurArray){                          
        runStat.setHeurList(heurArray);
        // Needs to setup heuristic list in update which will set it in the inner components
        update.setHeurList(heurArray);
    }
    
    /**
     * Sets the maximum computational time for this dynamic heuristic set.
     * <p>
     * The maximum time should be the same as the maximum time for the hyper-heuristic
     * using this dynamic heuristic set.
     * @param maxTime the time limit in millisecond for the hyper-heuristic using this dynamic set
     */
    public void setMaxTime(long maxTime){
        this.runStat.setMaxTime(maxTime);
    }
    
    /**
     * Initializes this dynamic heuristic set.
     * <p>
     * This method should be called immediately after the hyper-heuristic has created
     * an initial solution and evaluates it.
     * @param initValue the initial solution value
     */
    public void init(double initValue){
        runStat.init(initValue);
    }
    
    /**
     * Updates the active set from which the hyper-heuristic is allowed to choose
     * if an update condition is satisfied and returns the currently active set.
     * <p>
     * Note that the returned list contains <strong>the indexes of the heuristics
     * in the universal set (not their unique identifiers)</strong>. The universal
     * set should be passed to this dynamic set using the method <code>setHeurList</code>.
     * <p>
     * The update condition and the update criterion are implemented in the 
     * <code>update</code> field of this dynamic set. This method calls 
     * <code>updateActiveList</code> of the <code>update</code> field to update
     * the active set. Therefore, your logic for updating the active set should
     * go in <code>updateActiveList</code> of the <code>Update</code> class.
     * @return a list containing the integer indexes of the heuristics in the 
     * currently active set
     * @see dynheurset.update.Update#updateActiveList()
     */
    public List<Integer> updateActiveList(){       
        return update.updateActiveList();
    }
    
    /**
     * Updates the performance-related information of the heuristic stored at index
     * <code>idx</code> in the universal set of this dynamic set.
     * <p>
     * Note that the universal set of this dynamic set is set using
     * <code>setHeurList</code>. Therefore, <code>heurList.get(idx)</code>
     * returns the heuristic unique identifier.
     * <p>
     * The update includes the percentage and frequency of improvement (disimprovement) 
     * achieved (caused) by this heuristic, the total execution time used by this heuristic,
     * the percentage and frequency of improvement over the best solution and the run-best
     * solution, etc.
     * <p>
     * @param idx          the heuristic identifier
     * @param currentValue the solution value before applying the heuristic
     * @param newValue     the solution value after applying the heuristic
     * @param duration     the time (in millisecond) taken by the heuristic to execute the move
     */
    public void updateHeurValue(int idx, double currentValue, double newValue, long duration){
        this.runStat.updateHeurValue(idx, currentValue, newValue, duration);
    }
    
    /**
     * Indicates that a restart strategy is triggered in the hyper-heuristic.
     * <p>
     * This method is relevant to hyper-heuristics that implement a restart strategy.
     * This method is used by this dynamic set to reset run-specific statistics such
     * as the percentage improvement over the run-best solution.
     * @param initValue the solution value of the solution from which the search 
     * is initialized 
     */
    public void restart(double initValue){
        runStat.restart(initValue);
    }
    
    /**
     * Returns the currently active set.
     * @return a list containing the integer identifiers of the currently active set
     */
    public List<Integer> getActiveList(){
        return update.getActiveList();
    }
    
    /**
     * Returns the <code>runStat</code> field of this dynamic set.
     * @return the observer that records the run statistics of heuristics
     */
    public RunStat getRunStatistics(){   
        return runStat;
    }
    
    /**
     * Returns the number of updates performed by this dynamic sets.
     * @return the number of updates performed by this dynamic sets
     */
    public int getNumberOfUpdates(){
        return update.numUpdates;
    }
    
    /**
     * Returns the number of times a removal strategy is triggered.
     * <p>
     * The removal strategy implements the removal condition (when to remove some
     * heuristic permanently) and the removal criterion (which heuristics to remove)
     * @return the number of times a removal strategy is triggered.
     * @see dynheurset.update.remove.Remove
     */
    public int getNumberOfRemove(){
        return update.numRemove;
    }
    
    /**
     * Returns the number of times the active set is <i>reset</i> to include all
     * heuristics except for permanently removed heuristics.
     * <p>
     * The reset is performed by a <i>reset strategy</i> which decides when to trigger
     * the reset.
     * @return the number of times the active set is reset
     */
    public int getNumberOfResets(){
        return update.numReset;
    }
    
    @Override
    public String toString(){
        return update.toString();
    }
}
