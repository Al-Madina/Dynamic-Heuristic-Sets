package dynheurset.measure;

/**
 * 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class WtdPerfDurMeasure extends Measure{
    
    private final double w1;
    private final double w2;

    public WtdPerfDurMeasure(double w1, double w2) {
        this.w1 = w1;
        this.w2 = w2;
    }
        

    @Override
    public double measure(int heurIdx) {
        //If heuristic is not used, returns a big value to give it a chance to
        //be used at least once
        if(runStat.durations[heurIdx] == 0) return MAX;
        
        //Larger is better
        return (w1*runStat.impr[heurIdx] - w2*runStat.disimpr[heurIdx])/runStat.durations[heurIdx]; 
    }
    
}
