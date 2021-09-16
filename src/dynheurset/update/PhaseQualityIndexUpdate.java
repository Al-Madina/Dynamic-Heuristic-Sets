package dynheurset.update;

import java.util.List;
import util.Utility;

/**
 * This class implements a phase-based quality index update strategy.
 * <p>
 * The active set is updated every <code>phaseLen</code> iterations and
 * the performance value of each heuristic is converted to indexes (ranks).
 * Every heuristic that has its ranks below the average value multiplied by
 * <code>aspiration</code> factor is not included in the active set.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PhaseQualityIndexUpdate extends Update{
    /**
     * The phase length which is the maximum number of iterations that can elapse
     * before updating the active set
     */
    protected final int phaseLen;   
    /**
     * Aspiration factor to permit the heuristics that perform slightly below 
     * the average to be included in the active set
     */
    protected double aspiration;    
    /**
     * A utility object containing several helper methods
     */
    protected Utility util;

    public PhaseQualityIndexUpdate(int phaseLen, double aspiration) {
        this.phaseLen = phaseLen;
        this.aspiration = aspiration;
        //Aspiration of values higher than 1 may lead to an empty active list 
        //which will cause an exception.
        //Values less than 0 will not be a problem since it will result in 
        //including all heuristics in the active list
        if(this.aspiration > 1) this.aspiration = 1;
        util = new Utility();
    }

    
    @Override
    public boolean canUpdate() {        
        return runStat.iter % phaseLen == 0;
    }

    @Override
    protected void performUpdate() {
        //Starts from an empty active set
        activeList.clear();
        //Measure the performance of all heuristics
        List<Double> heurValues = measure.measure();
        //Compute the ranks for all heuristics
        double[] ranks = rank(heurValues);
        //Compute the average rank
        double mean = util.mean(ranks);        
        //Exclude heurisitcs that are either permanently removed or have ranks
        //below the average rank reduced by the aspiration factor
        for(int idx=0; idx < heurValues.size(); idx++){
            if(remove.removedHeurSet.contains(idx) || ranks[idx] < aspiration*mean) continue;
            //Current heuristic has good quality index, add it to the active set
            activeList.add(idx);
        }
    }
    
    /**
     * Converts the heuristic performance into ranks.
     * <p>
     * The best heuristic will have the highest and the worst heuristic will have
     * a rank of 1.
     * <p>
     * This method is typically needed by quality index update strategies.
     * @param heurValues the heuristic values
     * @return the ranks of heuristics
     */
    protected double[] rank(List<Double> heurValues){
        double[] ranks = new double[heurValues.size()];
        for(int idx1=0; idx1 < heurValues.size(); idx1++){
            if(remove.removedHeurSet.contains(idx1)) continue;
            ranks[idx1]++;
            for(int idx2=0; idx2 < heurValues.size(); idx2++){
                if(remove.removedHeurSet.contains(idx2)) continue;
                if(heurValues.get(idx1) > heurValues.get(idx2)) ranks[idx1]++;
            }
        }
        return ranks;
    }
    
}
