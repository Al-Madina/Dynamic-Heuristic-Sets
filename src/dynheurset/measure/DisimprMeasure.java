package dynheurset.measure;

/**
 * Measure the performance by the percentage disimprovement of the heuristic.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class DisimprMeasure extends Measure{

    @Override
    public double measure(int heurIdx) {
        //If heuristic is not used, returns a big value to give it a chance to
        //be used at least once
        if(runStat.durations[heurIdx] == 0) return MAX;
        
        //Larger is better
        return -runStat.disimpr[heurIdx];
    }
    
}
