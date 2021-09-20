package dynheurset.update;

/**
 * This class implements a patient phase-based greedy update strategy.
 * <p>
 * The active set is updated every <code>phaseLen</code> iterations. A patience
 * factor is used to control  how long the update strategy waits for the best 
 * solution to improve before updating the active set.
 * The best <code>top</code> are included in the active set where <code>top</code>
 * is a field for the number of heuristics to include in the active set.
 * <p>
 * The greediness selection is fair. Every heuristic will be part of the active
 * set for a maximum number of times controlled by <code>max</code>. If a heuristic
 * is chosen <code>max</code> times, it cannot be part of the active set until
 * all other heuristics are selected <code>max</code> times. In this case, the 
 * frequency count for all heuristics is reset to <code>0</code>.
 * The selection is greedy considering the heuristics that are available (those
 * that are not permanently removed or hits <code>max</code>.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PatientPhaseFairGreedyUpdate extends PhaseFairGreedyUpdate{
    /**
     * A patience factor controlling how long the update strategy waits for the
     * best solution to improve before updating the active set.
     */
    protected final double patience;

    public PatientPhaseFairGreedyUpdate(double patience, int max, int phaseLen, int top) {
        super(max, phaseLen, top);
        //Deal with inappropriate patience
        if(patience <= 0) patience = 1; //Wait as long as it the longest we waited so far
        this.patience = patience;
    }
    
    @Override
    public boolean canUpdate(){
        int basePhase = Math.min(phaseLen, (int)(1 + patience*runStat.maxWait));
        if(runStat.waitUpdate > basePhase){
            runStat.waitUpdate = 0;
            return true;
        }
        return false; 
    }
}
