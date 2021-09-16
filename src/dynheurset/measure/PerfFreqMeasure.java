package dynheurset.measure;

/**
 * Considers the frequency improvement, and the frequency disimprovement of the 
 * heuristic to measure its performance.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PerfFreqMeasure extends Measure{

    @Override
    public double measure(int heurIdx) {
        //If heuristic is not used, returns a big value to give it a chance to
        //be used at least once
        if(runStat.durations[heurIdx] == 0) return MAX;
        
        //Larger is better
        return (runStat.numImpr[heurIdx] - runStat.numDisimpr[heurIdx]);
    }
    
}
