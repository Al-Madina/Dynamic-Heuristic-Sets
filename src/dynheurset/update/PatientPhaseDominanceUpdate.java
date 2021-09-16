package dynheurset.update;

/**
 * This class implements a patient phase-based dominance update strategy.
 * <p>
 * The active set is updated every <code>phaseLen</code> iterations. A patience
 * factor is used to control  how long the update strategy waits for the best 
 * solution to improve before updating the active set.
 * All dominant heuristics are included in the active set. A heuristic 
 * <code>j</code> is dominated by a heuristic <code>i</code> if <code>i</code> 
 * achieves better performance and in a shorter amount of computational time 
 * than <code>j</code>.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PatientPhaseDominanceUpdate extends PhaseDominanceUpdate{
    /**
     * A patience factor controlling how long the update strategy waits for the
     * best solution to improve before updating the active set.
     */
    protected final double patience;

    public PatientPhaseDominanceUpdate(double patience, int phaseLen) {
        super(phaseLen);
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
