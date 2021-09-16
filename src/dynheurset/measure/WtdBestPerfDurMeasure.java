package dynheurset.measure;

/**
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class WtdBestPerfDurMeasure extends Measure{
    private final double w1;
    private final double w2;
    private final double w3;

    public WtdBestPerfDurMeasure(double w1, double w2, double w3) {
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
    }

    @Override
    public double measure(int heurIdx) {
        //If heuristic is not used, returns a big value to give it a chance to
        //be used at least once
        if(runStat.durations[heurIdx] == 0) return MAX;
        
        //Larger is better
        return (w1*runStat.bestImpr[heurIdx] + w2*runStat.impr[heurIdx] 
                - w3*runStat.disimpr[heurIdx])/runStat.durations[heurIdx];    
    }
    
}    
