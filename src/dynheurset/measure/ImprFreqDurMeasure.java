package dynheurset.measure;

/**
 * Measure the performance by the ratio between the frequency of improvement 
 * of the heuristic and its duration.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class ImprFreqDurMeasure extends Measure{

    @Override
    public double measure(int heurIdx) {
        //If heuristic is not used, returns a big value to give it a chance to
        //be used at least once
        if(runStat.durations[heurIdx] == 0) return MAX;
        
        //Larger is better
        return runStat.numImpr[heurIdx]/runStat.durations[heurIdx];
    }   
    
}
