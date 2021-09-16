package dynheurset.measure;

/**
 * Considers the percentage improvement, the percentage disimprovement, and the 
 * duration of the a heuristic to measure its performance.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PerfDurMeasure extends Measure{

    @Override
    public double measure(int heurIdx) {
        //If heuristic is not used, returns a big value to give it a chance to
        //be used at least once
        if(runStat.durations[heurIdx] == 0) return MAX;
        
        //Larger is better
        return (runStat.impr[heurIdx] - runStat.disimpr[heurIdx])/runStat.durations[heurIdx]; 
    }
    
}
