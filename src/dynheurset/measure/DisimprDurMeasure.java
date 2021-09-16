package dynheurset.measure;

/**
 * Measure the performance by the ratio between the percentage disimprovement made 
 * by the heuristic and its duration.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class DisimprDurMeasure extends Measure{

    @Override
    public double measure(int heurIdx) {
        //If heuristic is not used, returns a big value to give it a chance to
        //be used at least once
        if(runStat.durations[heurIdx] == 0) return MAX;        
        //Larger heuristic values are better
        return -runStat.disimpr[heurIdx]/runStat.durations[heurIdx]; 
    }
   
}
