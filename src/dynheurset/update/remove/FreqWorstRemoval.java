package dynheurset.update.remove;

import java.util.List;
import util.Utility;

/**
 *  Frequency Worst Removal Strategy
 * <p>
 * This removal strategy is performed whenever a <code>freq</code>% of the computational
 * is used. For instance a value of <code>0.25</code> for <code>freq</code> will
 * cause the removal operation to be executed <code>4</code> times.
 * <p>
 * Every time the worst heuristic that is not removed so far is removed.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class FreqWorstRemoval extends Remove{
    
    protected double freq;
    private int incr;
    protected Utility util;
    
    public FreqWorstRemoval(double freq) {
        this.freq = freq;
        incr = 1;
        util = new Utility();
    }
       
    
    @Override
    public boolean canRemove() {        
        if(removedHeurSet.size() < heurList.size()-1 
                && runStat.elpTime/runStat.maxTime > incr*freq){
            incr++;
            return true;
        }
        return false;
    }

    @Override
    public void remove() {
        //Measure the performance for all heuristics
        List<Double> heurValues = measure.measure();        
        //Find the worst heuristic that is not currently in the set of permenantly 
        //removed heuristic 'removedHeurSet'
        int heurIdx = util.getIndexOfMinValue(heurValues, removedHeurSet);        
        removedHeurSet.add(heurIdx);
    }
    
}
