package runner;

import java.util.ArrayList;
import java.util.List;
import util.Utility;

/**
 *
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class Result {

    private final List<ThreadOutput> outputList;
    private final List<Double> results;
    private final Utility util;
    private int minPeriods;

    public Result(List<ThreadOutput> outputList) {
        this.outputList = outputList;
        results = new ArrayList<>(outputList.size());
        minPeriods = Integer.MAX_VALUE;
        for(ThreadOutput output : outputList) {
            results.add(output.bestValue);
            if(output.imprScoresList.size() < minPeriods){
                minPeriods = output.imprScoresList.size();
            }
        }
        util = new Utility();
    }
    
    /**
     * Returns the minimum value of all runs.
     * @return the minimum value of all runs
     */
    public double min(){
        return util.min(results);
    }
    
    /**
     * Returns the mean of all runs.
     * @return the mean of all runs
     */
    public double mean(){
        return util.mean(results);
    }    
    
    /**
     * Returns the standard deviation.
     * @return the standard deviation
     */
    public double std(){
        return util.std(results);
    }
    
    /**
     * Returns the median of all runs.
     * @return the median of all runs
     */
    public double median(){
        return util.median(results);
    }
    
    /**
     * Returns the maximum value of all runs.
     * @return the maximum value of all runs
     */
    public double max(){
        return util.max(results);
    }
    
    /**
     * Returns the average percentage improvement for each heuristic across all runs.
     * <p>
     * The value at index <code>idx</code> of the returned array represents the 
     * average percentage improvement of the heuristic at index <code>idx</code> in 
     * the universal set.
     * @return the average percentage improvement for each heuristic across all runs
     */
    public double[] avgImprovement(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).imprScores);
        }
        return util.meanHeurPerf(perfList);
    }
    
     /**
     * Returns the average percentage disimprovement for each heuristic across all runs.
     * <p>
     * The value at index <code>idx</code> of the returned array represents the 
     * average percentage disimprovement of the heuristic at index <code>idx</code> in 
     * the universal set.
     * @return the average percentage disimprovement for each heuristic across all runs
     */
    public double[] avgDisimprovement(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).disimprScores);
        }
        return util.meanHeurPerf(perfList);
    }  
    
     /**
     * Returns the average number of improvements for each heuristic across all runs.
     * <p>
     * The value at index <code>idx</code> of the returned array represents the 
     * average number of improvements of the heuristic at index <code>idx</code> in 
     * the universal set.
     * @return the average number of improvements for each heuristic across all runs
     */
    public double[] avgNumberOfImprovements(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).numImprScores);
        }
        return util.meanHeurPerf(perfList);
    }    
    
     /**
     * Returns the average number of disimprovements for each heuristic across all runs.
     * <p>
     * The value at index <code>idx</code> of the returned array represents the 
     * average number of disimprovements of the heuristic at index <code>idx</code> in 
     * the universal set.
     * @return the average number of disimprovements for each heuristic across all runs
     */
    public double[] avgNumberOfDisimprovements(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).numDisimprScores);
        }
        return util.meanHeurPerf(perfList);
    }  
    
    /**
     * Returns the ratios of the contribution of all heuristics in the total
     * percentage improvement made by all heuristics.
     * <p>
     * The value at index <code>idx</code> is the ratio of percentage improvement
     * that the the heuristic at index <code>idx</code> in the universal set has
     * contributed to the total percentage improvement.
     * @return the ratios of the contribution of all heuristic in the total
     * percentage improvement made by all heuristics.
     */
    public double[] avgGroupImprovement(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).groupImprScores);
        }
        return util.meanHeurPerf(perfList);
    }
    
    /**
     * Returns the ratios of the contribution of all heuristics in the total
     * percentage disimprovement made by all heuristics.
     * <p>
     * The value at index <code>idx</code> is the ratio of percentage disimprovement
     * that the the heuristic at index <code>idx</code> in the universal set has
     * contributed to the total percentage disimprovement.
     * @return the ratios of the contribution of all heuristic in the total
     * percentage disimprovement made by all heuristics.
     */
    public double[] avgGroupDisimprovement(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).groupDisimprScores);
        }
        return util.meanHeurPerf(perfList);
    }  
    
    /**
     * Returns the ratios of the contribution of all heuristics in the total
     * number of improvements made by all heuristics.
     * <p>
     * The value at index <code>idx</code> is the ratio of number of improvements
     * that the the heuristic at index <code>idx</code> in the universal set has
     * contributed to the total number improvements.
     * @return the ratios of the contribution of all heuristic in the total
     * number of improvements made by all heuristics.
     */
    public double[] avgGroupNumberOfImprovements(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).groupNumImprScores);
        }
        return util.meanHeurPerf(perfList);
    }    
    
    /**
     * Returns the ratios of the contribution of all heuristics in the total
     * number of disimprovements made by all heuristics.
     * <p>
     * The value at index <code>idx</code> is the ratio of number of disimprovements
     * that the the heuristic at index <code>idx</code> in the universal set has
     * contributed to the total number disimprovements.
     * @return the ratios of the contribution of all heuristic in the total
     * number of disimprovements made by all heuristics.
     */
    public double[] avgGroupNumberOfDisimprovements(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).groupNumDisimprScores);
        }
        return util.meanHeurPerf(perfList);
    }     
    
    /**
     * Returns the ratios of the share of all heuristics in the total computational 
     * time used by all heuristics.
     * <p>
     * The value at index <code>idx</code> is the share in the computational time
     * that the the heuristic at index <code>idx</code> in the universal set has
     * used up.
     * @return the ratios of the share of all heuristics in the total computational 
     * time used by all heuristics.
     */
    public double[] avgDurations(){
        List<double[]> perfList = new ArrayList<>(outputList.size());
        for(int thread=0; thread < outputList.size(); thread++){
            perfList.add(outputList.get(thread).groupDurations);
        }
        return util.meanHeurPerf(perfList);        
    }
    
    /**
     * Returns the average percentage improvement for each heuristic across all 
     * runs over several periods.
     * <p>
     * The search is made of several periods of equal computational time. The
     * performance of each heuristic is observed in each period.
     * <p>
     * The performance of each heuristic in a period <code>pd</code> is an array
     * at index <code>pd-1</code> in the returned list. The value at index 
     * <code>idx</code> in the returned array represents the average percentage 
     * improvement of the heuristic at index <code>idx</code> in the universal 
     * set in the period <code>pd</code>.
     * @return the average percentage improvement for each heuristic across all 
     * runs over several periods
     */
    public List<double[]> avgImprovementOverPeriods(){
        //A list to store average performance over several periods
        List<double[]> perfOverPeriods = new ArrayList<>(minPeriods);
        for(int period=0; period < minPeriods; period++){
            //A list to store the performance for the current period obtained
            //by several threads
            List<double[]> perfList = new ArrayList<>(outputList.size());
            for(int thread=0; thread < outputList.size(); thread++){
                perfList.add(outputList.get(thread).imprScoresList.get(period));
            }
            //Calculate the average performance for the current period
            perfOverPeriods.add(util.meanHeurPerf(perfList));
        }        
        return perfOverPeriods;
    }
    
    /**
     * Returns the average percentage disimprovement for each heuristic across all 
     * runs over several periods.
     * <p>
     * The search is made of several periods of equal computational time. The
     * performance of each heuristic is observed in each period.
     * <p>
     * The performance of each heuristic in a period <code>pd</code> is an array
     * at index <code>pd-1</code> in the returned list. The value at index 
     * <code>idx</code> in the returned array represents the average percentage 
     * disimprovement of the heuristic at index <code>idx</code> in the universal 
     * set in the period <code>pd</code>.
     * @return the average percentage disimprovement for each heuristic across all 
     * runs over several periods
     */
    public List<double[]> avgDisimprovementOverPeriods(){
        //A list to store average performance over several periods
        List<double[]> perfOverPeriods = new ArrayList<>(minPeriods);
        for(int period=0; period < minPeriods; period++){
            //A list to store the performance for the current period obtained
            //by several threads
            List<double[]> perfList = new ArrayList<>(outputList.size());
            for(int thread=0; thread < outputList.size(); thread++){
                perfList.add(outputList.get(thread).disimprScoresList.get(period));
            }
            //Calculate the average performance for the current period
            perfOverPeriods.add(util.meanHeurPerf(perfList));
        }        
        return perfOverPeriods;
    }  
    
    /**
     * Returns the average number of improvements for each heuristic across all 
     * runs over several periods.
     * <p>
     * The search is made of several periods of equal computational time. The
     * performance of each heuristic is observed in each period.
     * <p>
     * The performance of each heuristic in a period <code>pd</code> is an array
     * at index <code>pd-1</code> in the returned list. The value at index 
     * <code>idx</code> in the returned array represents the average number of 
     * improvements of the heuristic at index <code>idx</code> in the universal 
     * set in the period <code>pd</code>.
     * @return the average number of improvements for each heuristic across all 
     * runs over several periods
     */
    public List<double[]> avgNumberOfImprovementsOverPeriods(){
        //A list to store average performance over several periods
        List<double[]> perfOverPeriods = new ArrayList<>(minPeriods);
        for(int period=0; period < minPeriods; period++){
            //A list to store the performance for the current period obtained
            //by several threads
            List<double[]> perfList = new ArrayList<>(outputList.size());
            for(int thread=0; thread < outputList.size(); thread++){
                perfList.add(outputList.get(thread).numImprScoresList.get(period));
            }
            //Calculate the average performance for the current period
            perfOverPeriods.add(util.meanHeurPerf(perfList));
        }        
        return perfOverPeriods;
    }    
    
    /**
     * Returns the average number of disimprovements for each heuristic across all 
     * runs over several periods.
     * <p>
     * The search is made of several periods of equal computational time. The
     * performance of each heuristic is observed in each period.
     * <p>
     * The performance of each heuristic in a period <code>pd</code> is an array
     * at index <code>pd-1</code> in the returned list. The value at index 
     * <code>idx</code> in the returned array represents the average number of 
     * disimprovements of the heuristic at index <code>idx</code> in the universal 
     * set in the period <code>pd</code>.
     * @return the average number of disimprovements for each heuristic across all 
     * runs over several periods
     */
    public List<double[]> avgNumberOfDisimprovementsOverPeriods(){
        //A list to store average performance over several periods
        List<double[]> perfOverPeriods = new ArrayList<>(minPeriods);
        for(int period=0; period < minPeriods; period++){
            //A list to store the performance for the current period obtained
            //by several threads
            List<double[]> perfList = new ArrayList<>(outputList.size());
            for(int thread=0; thread < outputList.size(); thread++){
                perfList.add(outputList.get(thread).numDisimprScoresList.get(period));
            }
            //Calculate the average performance for the current period
            perfOverPeriods.add(util.meanHeurPerf(perfList));
        }        
        return perfOverPeriods;
    }  
    
    /**
     * Returns the ratios of percentage improvement that each heuristic has
     * contributed in the total percentage improvement across all runs over 
     * several periods.
     * <p>
     * The search is made of several periods of equal computational time. The
     * performance of each heuristic is observed in each period.
     * <p>
     * The performance of each heuristic in a period <code>pd</code> is an array
     * at index <code>pd-1</code> in the returned list. The value at index 
     * <code>idx</code> in the returned array represents the average ratio of 
     * percentage improvement that the heuristic at index <code>idx</code> in 
     * the universal set in the period <code>pd</code> has contributed to the
     * total percentage improvement.
     * @return the ratios of percentage improvement that each heuristic has
     * contributed in the total percentage improvement across all runs over 
     * several periods
     */
    public List<double[]> avgGroupImprovementOverPeriods(){
        //A list to store average performance over several periods
        List<double[]> perfOverPeriods = new ArrayList<>(minPeriods);
        for(int period=0; period < minPeriods; period++){
            //A list to store the performance for the current period obtained
            //by several threads
            List<double[]> perfList = new ArrayList<>(outputList.size());
            for(int thread=0; thread < outputList.size(); thread++){
                perfList.add(outputList.get(thread).groupImprScoresList.get(period));
            }
            //Calculate the average performance for the current period
            perfOverPeriods.add(util.meanHeurPerf(perfList));
        }        
        return perfOverPeriods;
    }
    
    /**
     * Returns the ratios of percentage disimprovement that each heuristic has
     * contributed in the total percentage disimprovement across all runs over 
     * several periods.
     * <p>
     * The search is made of several periods of equal computational time. The
     * performance of each heuristic is observed in each period.
     * <p>
     * The performance of each heuristic in a period <code>pd</code> is an array
     * at index <code>pd-1</code> in the returned list. The value at index 
     * <code>idx</code> in the returned array represents the average ratio of 
     * percentage disimprovement that the heuristic at index <code>idx</code> in 
     * the universal set in the period <code>pd</code> has contributed to the
     * total percentage disimprovement.
     * @return the ratios of percentage disimprovement that each heuristic has
     * contributed in the total percentage disimprovement across all runs over 
     * several periods
     */
    public List<double[]> avgGroupDisimprovementOverPeriods(){
        //A list to store average performance over several periods
        List<double[]> perfOverPeriods = new ArrayList<>(minPeriods);
        for(int period=0; period < minPeriods; period++){
            //A list to store the performance for the current period obtained
            //by several threads
            List<double[]> perfList = new ArrayList<>(outputList.size());
            for(int thread=0; thread < outputList.size(); thread++){
                perfList.add(outputList.get(thread).groupDisimprScoresList.get(period));
            }
            //Calculate the average performance for the current period
            perfOverPeriods.add(util.meanHeurPerf(perfList));
        }        
        return perfOverPeriods;
    }  
    
    /**
     * Returns the ratios of number of improvements that each heuristic has
     * contributed in the total number of improvements across all runs over 
     * several periods.
     * <p>
     * The search is made of several periods of equal computational time. The
     * performance of each heuristic is observed in each period.
     * <p>
     * The performance of each heuristic in a period <code>pd</code> is an array
     * at index <code>pd-1</code> in the returned list. The value at index 
     * <code>idx</code> in the returned array represents the average ratio of 
     * number of improvements that the heuristic at index <code>idx</code> in 
     * the universal set in the period <code>pd</code> has contributed to the
     * total number of improvements.
     * @return the ratios of number of improvements that each heuristic has
     * contributed in the total number of improvements across all runs over 
     * several periods
     */
    public List<double[]> avgGroupNumberOfImprovementsOverPeriod(){
        //A list to store average performance over several periods
        List<double[]> perfOverPeriods = new ArrayList<>(minPeriods);
        for(int period=0; period < minPeriods; period++){
            //A list to store the performance for the current period obtained
            //by several threads
            List<double[]> perfList = new ArrayList<>(outputList.size());
            for(int thread=0; thread < outputList.size(); thread++){
                perfList.add(outputList.get(thread).groupNumImprScoresList.get(period));
            }
            //Calculate the average performance for the current period
            perfOverPeriods.add(util.meanHeurPerf(perfList));
        }        
        return perfOverPeriods;
    }    
    
    /**
     * Returns the ratios of number of disimprovements that each heuristic has
     * contributed in the total number of disimprovements across all runs over 
     * several periods.
     * <p>
     * The search is made of several periods of equal computational time. The
     * performance of each heuristic is observed in each period.
     * <p>
     * The performance of each heuristic in a period <code>pd</code> is an array
     * at index <code>pd-1</code> in the returned list. The value at index 
     * <code>idx</code> in the returned array represents the average ratio of 
     * number of disimprovements that the heuristic at index <code>idx</code> in 
     * the universal set in the period <code>pd</code> has contributed to the
     * total number of disimprovements.
     * @return the ratios of number of disimprovements that each heuristic has
     * contributed in the total number of disimprovements across all runs over 
     * several periods
     */
    public List<double[]> avgGroupNumberOfDisimprovementsOverPeriods(){
        //A list to store average performance over several periods
        List<double[]> perfOverPeriods = new ArrayList<>(minPeriods);
        for(int period=0; period < minPeriods; period++){
            //A list to store the performance for the current period obtained
            //by several threads
            List<double[]> perfList = new ArrayList<>(outputList.size());
            for(int thread=0; thread < outputList.size(); thread++){
                perfList.add(outputList.get(thread).groupNumDisimprScoresList.get(period));
            }
            //Calculate the average performance for the current period
            perfOverPeriods.add(util.meanHeurPerf(perfList));
        }        
        return perfOverPeriods;
    }    
}

