package dynheurset.measure;

/**
 * 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class WtdBestRunbestPerfFreqMeasure extends Measure{
    private final double w1;
    private final double w2;
    private final double w3;
    private final double w4;

    public WtdBestRunbestPerfFreqMeasure(double w1, double w2, double w3, double w4) {
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.w4 = w4;
    }

    @Override
    public double measure(int heurIdx) {
        //If heuristic is not used, returns a big value to give it a chance to
        //be used at least once
        if(runStat.durations[heurIdx] == 0) return MAX;
        
        //Larger is better
        return w1*runStat.numBestImpr[heurIdx] + w2*runStat.numRunBestImpr[heurIdx]
                + w3*runStat.numImpr[heurIdx] - w4*runStat.numDisimpr[heurIdx];    
    }    
}
