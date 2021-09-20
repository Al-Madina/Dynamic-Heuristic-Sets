package dynheurset.update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.Utility;

/**
 * This class implements a phase-based greedy update strategy.
 * <p>
 * The active set is updated every <code>phaseLen</code> iterations. 
 * The best <code>top</code> are included in the active set where <code>top</code>
 * is the number of heuristics to include in the active set.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PhaseGreedyUpdate extends Update{
    /**
     * The phase length which is the maximum number of iterations that can elapse
     * before updating the active set
     */
    protected int phaseLen;
    /**
     * The number of heuristics to select greedily
     */
    protected int top;
    /**
     * A utility object containing several helper methods
     */
    protected Utility util;
    
    public PhaseGreedyUpdate(int phaseLen, int top){
        //Deal with in appropriate argument
        if(phaseLen <= 0) phaseLen = 1; //update every iteration
        this.phaseLen = phaseLen;        
        if(top <= 0) top = 1; //choose the best heuristic always
        this.top = top;        
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
        //Compute the number of heuristics available for selection
        int freeHeurs = heurList.size() - remove.removedHeurSet.size();
        if(top > freeHeurs){
            //Keep it greedy
            top = freeHeurs > 1 ? freeHeurs - 1 : freeHeurs;            
        }
        //Measure the performance of all heuristics
        List<Double> values = measure.measure();
        //Heuristics that should not be selected
        Set<Integer> seen = new HashSet<>(freeHeurs);
        //Include those that are permanently removed
        seen.addAll(remove.removedHeurSet);
        for(int idx=0; idx < values.size(); idx++){
            //Get the index of the maximum value (index of the best heuristic
            //among all heuristics available for selection)
            int chosen = util.getIndexOfMaxValue(values, seen);
            //Add it to the active set
            activeList.add(chosen);
            //Make it unavailable for selection again
            seen.add(chosen);           
            if(activeList.size() == top) break;
        }
    }
    
}
