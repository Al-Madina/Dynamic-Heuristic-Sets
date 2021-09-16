package dynheurset.update;

import dynheurset.RunStat;
import dynheurset.measure.Measure;
import java.util.ArrayList;
import java.util.List;
import dynheurset.update.remove.Remove;
import dynheurset.update.reset.Reset;

/**
 * This class defines the update strategy used by a dynamic set.
 * <p>
 * The update strategy should specify:
 * <ul>
 * <li>The update condition which determines when to update the active set, and
 * <li>The update criterion which determines how to update the active set (which 
 * heuristic to include in the active set)
 * </ul>
 * The logic for the update condition should be implemented in <code>canUpdate</code>
 * method of this class. The logic for the update criterion should be implemented in
 * <code>performUpdate</code> method of this class.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public abstract class Update {
    /**
     * The run statistics to record the heuristic performance.
     */
    protected RunStat runStat;
    /**
     * The performance measure that measures the value of low-level heuristics 
     */
    protected Measure measure;
    /**
     * The removal strategy
     * @see dynheurset.update.remove.Remove
     */
    protected Remove remove;
    /**
     * The reset strategy
     * @see dynheurset.update.reset.Reset
     */
    protected Reset reset;
    
    public int numUpdates = 0;
    public int numRemove = 0;
    public int numReset = 0;
    /**
     * The universal set which contains the heuristic unique identifiers
     */
    protected List<Integer> heurList;
    /**
     * The active set which contains the indexes of heuristics in the universal set.
     * <p>
     * It is very important to note the difference between <code>activeList</code>
     * (active set) and <code>heurList</code> (universal set). The universal set
     * contains <strong>integer identifiers</strong> where each element in the universal
     * set uniquely identifies one heuristic.
     * The active set contains the <strong>indexes (positions)</strong> of the heuristics as
     * appear in the universal set. For instance, the universal set may contain
     * <code>[3,5,7,9]</code> and if the heuristics identified by 3, 7 are in the 
     * active set, then the active set is <code>[0,2]</code>
     */
    protected List<Integer> activeList;

    /**
     * Sets the <code>runStat</code> field of this update strategy.
     * @param runStat the observer that record the performance of heuristics
     */
    public void setRunStat(RunStat runStat){
        this.runStat = runStat;
    }
    
    /**
     * Sets the performance measure for this update strategy.
     * @param measure the performance measure
     */
    public void setMeasure(Measure measure){
        if(this.runStat == null){
            throw new NullPointerException(
                    "Please set the 'RunStat' object first using 'this.setRunStat' "
                    + "before calling this method");            
        }               
        this.measure = measure;
        this.measure.setRunStat(runStat);
    }
    
    /**
     * Sets the removal strategy for this update strategy.
     * @param remove the removal strategy
     */
    public void setRemove(Remove remove){
        this.remove = remove;
        if(this.runStat == null){
            throw new NullPointerException(
                    "Please set the 'runStat' object first using 'this.setRunStat' "
                    + "before calling this method");
        }        
        if(this.measure == null){
            throw new NullPointerException(
                    "Please set the measure first using 'this.setMeasure' before "
                    + "calling this method");
        }
        this.remove.setup(runStat, measure);
    }
    
    /**
     * Sets the reset strategy for this update strategy.
     * @param reset the reset strategy
     */
    public void setReset(Reset reset){
        if(this.runStat == null){
            throw new NullPointerException(
                    "Please set the 'runStat' object first using 'this.setRunStat' "
                    + "before calling this method");
        }      
        if(this.measure == null){
            throw new NullPointerException(
                    "Please set the 'runStat' object first using 'this.setRunStat' "
                    + "before calling this method");
        }        
        this.reset = reset;
        this.reset.setup(runStat, measure);
    }
    
    /**
     * Sets the universal set for this dynamic heuristic set.
     * <p>
     * The universal set should contain all heuristics represented in an array of
     * integers where each integer identifies a unique heuristic. 
     * @param heurArray an integer array where each integer uniquely identifies a heuristic
     */
    public void setHeurList(int[] heurArray){
        heurList = new ArrayList<>(heurArray.length);
        activeList = new ArrayList<>(heurArray.length);
        for(int idx=0; idx < heurArray.length; idx++){
            heurList.add(heurArray[idx]);
            //The active list contains the "indexes" (not the actual heuristics)
            activeList.add(idx);
        }       
        
        if(this.remove == null){
            throw new NullPointerException(
                    "Please set the 'Remove' object first using 'this.setRemove' "
                    + "before calling this method");            
        }
        remove.setHeurList(heurList);
        
        if(this.reset == null){
            throw new NullPointerException(
                    "Please set the 'Reset' object first using 'this.setReset' "
                    + "before calling this method");            
        }      
        reset.setHeurList(heurList);                      
    }
    
    /**
     * Returns <code>true</code> if the active set is to be updated and <code>false</code>
     * otherwise.
     * <p>
     * In this method, you should use the <code>RunStat</code> field to access the run
     * information that you may need to decides whether to update the active set.
     * @return <code>true</code> if the active set is to be updated and <code>false</code>
     * otherwise.
     */
    protected abstract boolean canUpdate();
    
    
    /**
     * Updates and returns the active set.
     * <p>
     * This method should be used as this. To specify your own update strategy you
     * need to implement <code>canUpdate</code> (update condition) and <code>performUpdate</code>
     * (update criterion).
     * @return an array list containing the <strong>indexes</strong> of the heuristics
     * currently in the active set.
     */
    public List<Integer> updateActiveList(){
        //Returns the current active list if cannot update
        if(!canUpdate()) return activeList;
        
        //Otherwise, starts the update by checking if some heuristics should be
        //removed permanently
        if(remove.canRemove()){
            remove.remove();
            numRemove++;
        }
        
        if(reset.canReset()){
            //Need to reset the active set to include all heuristics except those
            //that are permenantly removed
            activeList = new ArrayList<>();
            for(int heurIdx=0; heurIdx < heurList.size(); heurIdx++){
                if(remove.removedHeurSet.contains(heurIdx)) continue;
                activeList.add(heurIdx);
            }
            
            numReset++;
            
            //If heuristic list is reset, no needs to update it. Just returns it
            return activeList;
        }
        
        //Check the update condition
        if(canUpdate()){            
            //Perform the update
            performUpdate();
            numUpdates++;
        }
        return activeList;
    }
    
    /**
     * Determines which heuristics to include in the active set.
     * <p>
     * This method should contain your logic for updating the active set. You can
     * access the <code>runStat</code> and <code>measure</code> fields in your
     * implementation to get the search-status information and measure the 
     * performance of heuristics.
     * regarding the search
     */
    protected abstract void performUpdate();
    
    /**
     * Returns an array list of the indexes of the heuristics currently in the active set.
     * <p>
     * Note that each element in the returned array list is an index of a heuristic in the
     * universal set. This is should not be confused by the integer identifiers contained
     * the universal set.
     * @return the active set
     */
    public List<Integer> getActiveList(){
        return activeList;
    }
}
