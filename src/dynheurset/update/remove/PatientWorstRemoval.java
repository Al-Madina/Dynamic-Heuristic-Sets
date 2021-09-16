package dynheurset.update.remove;

import util.Utility;
import java.util.List;

/**
 * A patient Worst Removal Strategy.
 * <p>
 * This removal strategy wait for a number of iterations that are more/less than
 * the maximum number of iterations elapsed before the best solution is updated.
 * Then, the worst heuristics that is not removed yet is permanently removed.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PatientWorstRemoval extends Remove{
    private final double patience;    
    protected final Utility util;
    
        
    public PatientWorstRemoval(double patience){
        this.patience = patience;
        util = new Utility();
    }
    
    
    @Override
    public boolean canRemove() {
        if(runStat.waitRemove > patience*runStat.maxWait
                && removedHeurSet.size() < heurList.size()-1){
            //Reset the wait count to start over
            runStat.waitRemove = 0; 
            return true;
        }
        return false;
    }

    
    @Override
    public void remove() {        
        //Measure the performance of all heuristics
        List<Double> heurValues = measure.measure();        
        //Find the worst heuristic that is not currently in the set of permenantly 
        //removed heuristic 'removedHeurSet'
        int heurIdx = util.getIndexOfMinValue(heurValues, removedHeurSet);
        //Removes it
        removedHeurSet.add(heurIdx);
    }
    
}
