package dynheurset.update;

import java.util.List;

/**
 * This class implements a phase-based dominance update strategy.
 * <p>
 * The active set is updated every <code>phaseLen</code> iterations. All dominant
 * heuristics are included in the active set. A heuristic <code>j</code> is
 * dominated by a heuristic <code>i</code> if <code>i</code> achieves better
 * performance and in a shorter amount of computational time than <code>j</code> 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PhaseDominanceUpdate extends Update{
    /**
     * The phase length which is the maximum number of iterations that can elapse
     * before updating the active set
     */
    protected final int phaseLen;

    public PhaseDominanceUpdate(int phaseLen) {
        //Deal with in appropriate argument
        if(phaseLen <= 0) phaseLen = 1; //update every iteration
        this.phaseLen = phaseLen;
    }
    
    
    @Override
    public boolean canUpdate() {
        return runStat.iter % phaseLen == 0;
    }

    @Override
    protected void performUpdate() {
        //Starts from an empty active set
        activeList.clear();        
        //Compute the heuristic values
        List<Double> heurValues = measure.measure();
        for(int idx1=0; idx1 < heurValues.size(); idx1++){
            if(remove.removedHeurSet.contains(idx1)) continue;
            boolean dominated = false;
            for(int idx2=0; idx2 < heurValues.size(); idx2++){
                if(remove.removedHeurSet.contains(idx2)) continue;
                //Heuristic idx2 is better than idx1
                if(heurValues.get(idx2) > heurValues.get(idx1) 
                        //and heuristic idx2 used less time so far
                        && runStat.durations[idx2] < runStat.durations[idx1]){
                    dominated = true;
                    break;
                }
            }
            if(!dominated){
                activeList.add(idx1);
            }            
        }        
    }
    
}
