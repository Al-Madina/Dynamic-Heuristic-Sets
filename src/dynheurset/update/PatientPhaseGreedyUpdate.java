package dynheurset.update;

/**
 * This class implements a patient phase-based greedy update strategy.
 * <p>
 * The active set is updated every <code>phaseLen</code> iterations. A patience
 * factor is used to control  how long the update strategy waits for the best 
 * solution to improve before updating the active set. 
 * The best <code>top</code> are included in the active set where <code>top</code>
 * is a field for the number of heuristics to include in the active set
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PatientPhaseGreedyUpdate extends PhaseGreedyUpdate{
    /**
     * A patience factor controlling how long the update strategy waits for the
     * best solution to improve before updating the active set.
     */
    protected final double patience;

    public PatientPhaseGreedyUpdate(double patience, int phaseLen, int top) {
        super(phaseLen, top);
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
