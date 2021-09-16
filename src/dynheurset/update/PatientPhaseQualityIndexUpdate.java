package dynheurset.update;

/**
 * This class implements a patient phase-based quality index update strategy.
 * <p>
 * The active set is updated every <code>phaseLen</code> iterations. A patience
 * factor is used to control  how long the update strategy waits for the best 
 * solution to improve before updating the active set.
 * The performance value of each heuristic is converted to indexes (ranks).
 * Every heuristic that has its ranks below the average value multiplied by
 * <code>aspiration</code> factor is not included in the active set.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PatientPhaseQualityIndexUpdate extends PhaseQualityIndexUpdate{
    /**
     * A patience factor controlling how long the update strategy waits for the
     * best solution to improve before updating the active set.
     */
    protected final double patience;

    public PatientPhaseQualityIndexUpdate(double patience, int phaseLen, double aspiration) {
        super(phaseLen, aspiration);
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
