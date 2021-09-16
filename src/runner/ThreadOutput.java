package runner;

import dynheurset.RunStat;
import java.util.ArrayList;
import java.util.List;
import util.Utility;

/**
 *
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class ThreadOutput {
    private final RunStat runStat;
    private final Utility util;    
    public final double bestValue;    
    
    //Traces
    public final List<Double> valuesTrace;
    public final List<Double> uniqueValuesTrace;
    public final List<Double> bestValuesTrace;
    
    // Individuals scores (absolute scores) of heuristics over the entire run
    public double[] imprScores;
    public double[] disimprScores;
    public double[] numImprScores;
    public double[] numDisimprScores;

    // Group scores (relative scores) of heuristics over the entire run
    public double[] groupImprScores;
    public double[] groupDisimprScores;
    public double[] groupNumImprScores;
    public double[] groupNumDisimprScores;

    // Relative duration of each heuristic in the entire run
    // How long each heuristic has been utilized
    public double[] groupDurations; 
    
    //Scores of heuristics over several periods to see how effective each heuristic is
    //at different times during the search.
    //A list of arrays where List.get(p)[h] gives the performance score of heuristic 'h' in period 'p'
    //E.g. List.get(0)[0] gives the performance of the first heuristic in the first period
    public List<double[]> imprScoresList;
    public List<double[]> groupImprScoresList;
    public List<double[]> disimprScoresList;
    public List<double[]> groupDisimprScoresList;
    public List<double[]> numImprScoresList;
    public List<double[]> groupNumImprScoresList;
    public List<double[]> numDisimprScoresList;
    public List<double[]> groupNumDisimprScoresList;  
    
    public ThreadOutput(RunStat runStat){
        this.runStat = runStat;
        util = new Utility();
        this.bestValue = runStat.bestValue;
        this.valuesTrace = runStat.valuesTrace;
        this.uniqueValuesTrace = runStat.uniqueValuesTrace;
        this.bestValuesTrace = runStat.bestValuesTrace; 
        
        imprScoresList = new ArrayList<>(runStat.imprList.size());
        groupImprScoresList = new ArrayList<>(runStat.imprList.size());
        disimprScoresList = new ArrayList<>(runStat.imprList.size());
        groupDisimprScoresList = new ArrayList<>(runStat.imprList.size());
        numImprScoresList = new ArrayList<>(runStat.imprList.size());
        groupNumImprScoresList = new ArrayList<>(runStat.imprList.size());
        numDisimprScoresList = new ArrayList<>(runStat.imprList.size());
        groupNumDisimprScoresList = new ArrayList<>(runStat.imprList.size());
    }
    
    /**
     * Compute the performance statistics from all runs for each heuristic.
     */
    public void collectPerfStatistics(){
        // First, calc individual scores
        imprScores = util.calcAbsolutePerf(runStat.impr, runStat.disimpr);
        disimprScores = util.calcAbsolutePerf(runStat.disimpr, runStat.impr);
        numImprScores = util.calcAbsolutePerf(runStat.numImpr, runStat.numDisimpr);
        numDisimprScores = util.calcAbsolutePerf(runStat.numDisimpr, runStat.numImpr);
        
        // Second, calc group scores
        groupImprScores = util.calcRelativePerf(runStat.impr);
        groupDisimprScores = util.calcRelativePerf(runStat.disimpr);
        groupNumImprScores = util.calcRelativePerf(runStat.numImpr);
        groupNumDisimprScores = util.calcRelativePerf(runStat.numDisimpr);
        
        // Third calc duration
        groupDurations = util.calcRelativePerf(runStat.durations);    
        
        // Fourth, record the performance over periods of time
        for(int period=0; period < runStat.imprList.size(); period++){
            //TODO: include absolute performance overperiods
            imprScoresList.add(util.calcAbsolutePerf(runStat.imprList.get(period), runStat.disimprList.get(period)));
            groupImprScoresList.add(util.calcRelativePerf(runStat.imprList.get(period)));
            
            disimprScoresList.add(util.calcAbsolutePerf(runStat.disimprList.get(period), runStat.imprList.get(period)));
            groupDisimprScoresList.add(util.calcRelativePerf(runStat.disimprList.get(period)));
                        
            numImprScoresList.add(util.calcAbsolutePerf(runStat.numImprList.get(period), runStat.numDisimprList.get(period)));
            groupNumImprScoresList.add(util.calcRelativePerf(runStat.numImprList.get(period)));
            
            numDisimprScoresList.add(util.calcAbsolutePerf(runStat.numDisimprList.get(period), runStat.numImprList.get(period)));
            groupNumDisimprScoresList.add(util.calcRelativePerf(runStat.numDisimprList.get(period)));
        }
    }    
    
}
