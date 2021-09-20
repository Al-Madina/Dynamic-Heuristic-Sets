package dynheurset.update.remove;

import util.Utility;

/**
 * The base class for removal strategies that remove multiple heuristics at once.
 * <p>
 * The removal is carried out by considering the individual performance
 * of heuristics, the relative performance of heuristics, and/or a combination
 * of both.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public abstract class GroupRemoval extends Remove{
    /**
     * The length of the preliminary run in which no heuristics is allowed to
     * be removed permanently.
     */
    protected double ratio;
    /**
     * An aspiration factor to prevent removing heuristics that have "close"
     * performance but not good enough.
     */
    protected double aspiration;    
    
    //Weights
    protected double alpha1;
    protected double alpha2;
    protected double alpha3;
    protected double alpha4;
    protected double beta1;
    protected double beta2;
    protected double beta3;
    protected double beta4;
    
    protected Utility util;

    
    public GroupRemoval(double ratio, double aspiration,
            double alpha1, double alpha2, double alpha3, double alpha4, 
            double beta1, double beta2, double beta3, double beta4) 
    {
        this.ratio = ratio;
        if(aspiration > 1) aspiration = 1;
        this.aspiration = aspiration;
        this.alpha1 = alpha1;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.alpha4 = alpha4;
        this.beta1 = beta1;
        this.beta2 = beta2;
        this.beta3 = beta3;
        this.beta4 = beta4;
        
        util = new Utility();
    }
    
    /**
     * Returns the individual (absolute) performance for each heuristic.
     * <p>
     * The individual performance of a heuristic is calculated based on the
     * heuristic performance history without considering other heuristics. For
     * instance, the ratio between the improving moves and the disimproving 
     * moves that the heuristic has made.
     * @return the individual performance for each heuristic
     */
    protected double[] getIndPerf(){
        double[] impr = util.calcAbsolutePerf(runStat.impr, runStat.disimpr);
        double[] disimpr = util.calcAbsolutePerf(runStat.disimpr, runStat.impr);
        double[] freqImpr = util.calcAbsolutePerf(runStat.numImpr, runStat.numDisimpr);
        double[] freqDisimpr = util.calcAbsolutePerf(runStat.numDisimpr, runStat.numImpr);
        double[] indPerf = new double[runStat.numHeurs];
        for(int idx=0; idx < runStat.numHeurs; idx++){
            indPerf[idx] = alpha1*impr[idx] - alpha2*disimpr[idx]
                            + alpha3*freqImpr[idx] - alpha4*freqDisimpr[idx];
        }
        return indPerf;
    }
    
    /**
     * Returns the performance of group (relative) performance of each heuristic.
     * <p>
     * The relative performance of a heuristic is calculated considering other
     * heuristics. For instance, ratio between the percentage of improvement the 
     * heuristic has contributed to the total percentage improvement made by all
     * heuristics.
     * @return the performance of group (relative) performance of each heuristic
     */
    protected double[] getGroupPerf(){
        double[] impr = util.calcRelativePerf(runStat.impr);
        double[] disimpr = util.calcRelativePerf(runStat.disimpr);
        double[] freqImpr = util.calcRelativePerf(runStat.numImpr);
        double[] freqDisimpr = util.calcRelativePerf(runStat.numDisimpr);
        double[] groupPerf = new double[runStat.numHeurs];
        for(int idx=0; idx < runStat.numHeurs; idx++){
            groupPerf[idx] = beta1*impr[idx] - beta2*disimpr[idx]
                            + beta3*freqImpr[idx] - beta4*freqDisimpr[idx];
        }
        return groupPerf;        
    }
}
