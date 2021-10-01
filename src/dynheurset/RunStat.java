/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynheurset;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import util.Utility;
import java.util.ArrayList;
import java.util.List;

/**
 * RunStat calculates and stores all run statistics/variables information that 
 * can be used by dynamic set to update the active set. 
 * <p>
 * The statistics/variables which are kept track of include:
 * <ul>
 * <li>The elapsed time
 * <li>The percentage improvement (disimprovement) achieved (caused) by each heuristic
 * <li>The frequency of improvement (disimprovement) achieved (caused) by each heuristic
 * <li>The percentage improvement over the best/run-best solution achieved by each heuristic
 * <li>The maximum number of iterations we waited so far before the best solution is updated
 * </ul>
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class RunStat {
    
    //I used a bean but I decided against it!
    //ThreadMXBean bean;
    
    /**
     * The universal set which contains the heuristic unique identifiers
     */
    public int[] heurList;
    /**
     * The percentage improvement of each heuristic
     */
    public double[] impr;
    /**
     * The percentage improvement of each heuristic for the <i>current run</i> (after the
     * last restart is invoked)
     */
    public double[] runImpr;
    /**
     * The percentage disimprovement of each heuristic
     */
    public double[] disimpr;
    /**
     * The percentage disimprovement of each heuristic for the <i>current run</i> 
     * (when a <i>restart strategy</i> is invoked, a new <i>run</i> will be started)
     */
    public double[] runDisimpr;
    /**
     * The percentage improvement of each heuristic over the best solution
     */
    public double[] bestImpr;
    /**
     * The percentage improvement of each heuristic over the <i>run-best</i> solution
     */
    public double[] runBestImpr;
    /**
     * The number of improvement of each heuristic (frequency of improvement)
     */ 
    public double[] numImpr;
    /**
     * The number of improvement of each heuristic in the current run
     */
    public double[] numRunImpr;
    /**
     * The number of disimprovement of each heuristic (frequency of disimprovement)
     */
    public double[] numDisimpr;
    /**
     * The number of disimprovement of each heuristic in the current run
     */
    public double[] numRunDisimpr;
    /**
     * The number of improvement of each heuristic over the best solution
     */
    public double[] numBestImpr;
    /**
     * The number of improvement of each heuristic over the run-best solution
     */
    public double[] numRunBestImpr;
    /**
     * The total cumulative execution time for each heuristic (utilization)
     */    
    public double[] durations;
    /**
     * The total number of heuristics
     */
    public int numHeurs;
    /**
     * The current iteration (starts from 1)
     */
    public int iter;
    /**
     * The time limit in millisecond
     */
    public double maxTime = 0;
    /**
     * The elapsed time so far
     */
    public double elpTime;
    /**
     * The best solution value
     */
    public double bestValue = Double.POSITIVE_INFINITY; //this will help us check if dynamic set is set up right
    /**
     * The best solution value in the current run
     */
    public double runBestValue;
    /**
     * Tracks the number of iterations we waited before the best solution is updated
     */
    private int wait;
    /**
     * Tracks the number of iterations we waited before the best solution is updated
     * in the current run
     */
    private int runWait;
    public int waitRemove;
    public int runWaitRemove;
    public int waitReset;
    public int runWaitReset;
    public int waitUpdate;
    public int runWaitUpdate;
    /**
     * The maximum number of iterations we waited before the best solution is updated
     */
    public int maxWait;
    /**
     *The maximum number of iterations we waited before the best solution is updated
     * in the current run
     */
    public int runMaxWait;
    /**
     * The time when the search started
     */
    private double start;
    
    
    //Traces
    /**
     * The fitness trace of the hyper-heuristic (the objective values of the 
     * current solution)
     */
    public List<Double> valuesTrace;
    /**
     * The unique fitness trace which does not store duplicate solution values
     */
    public List<Double> uniqueValuesTrace;
    /**
     * The fitness trace of the best solution
     */
    public List<Double> bestValuesTrace;
    /**
     * A small constant
     */
    private final double eps = 1E-06;
    

    //Performance over several periods defined by 'numPeriods'
    /**
     * The number of peroids over which the heuristic performance is observed.
     * This helps in determining which heuristic is useful at which period during
     * the search.
     */
    int numPeriods = 10;
    private double[] tmpImpr;
    private double[] tmpDisimpr;
    private double[] tmpNumImpr;
    private double[] tmpNumDisimpr;
    /**
     * The percentage improvement for each heuristic over several periods during 
     * the search.
     * <p>
     * The total computational time is divided into several periods of the same
     * time (default is <code>10</code> periods). The improvement achieved
     * by each heuristic in that period is recorded.
     * <p>
     * The array at index <code>idx</code> is the percentage improvement for all
     * heuristics at period <code>idx</code> starting counting from <code>0</code>.
     * To access the performance of the heuristic indexed by <code>idx</code> in 
     * the first period: <code>imprList.get(0)[idx]</code>.
     * Note that <code>idx</code> is the index of the heuristic in the universal
     * set.
     */
    public List<double[]> imprList;
    /**
     * The percentage disimprovement for each heuristic over several periods during 
     * the search.
     * <p>
     * The total computational time is divided into several periods of the same
     * time (default is <code>10</code> periods). The disimprovement caused
     * by each heuristic in that period is recorded.
     * <p>
     * The array at index <code>idx</code> is the percentage disimprovement for all
     * heuristics at period <code>idx</code> starting counting from <code>0</code>.
     * To access the performance of the heuristic indexed by <code>idx</code> in 
     * the first period: <code>imprList.get(0)[idx]</code>.
     * Note that <code>idx</code> is the index of the heuristic in the universal
     * set.
     */
    public List<double[]> disimprList;
    /**
     * The number of improvement for each heuristic over several periods during 
     * the search.
     * <p>
     * The total computational time is divided into several periods of the same
     * time (default is <code>10</code> periods) and the number of improvement achieved
     * by each heuristic in that period is recorded.
     * <p>
     * The array at index <code>idx</code> is the number of improvement for all
     * heuristics at period <code>idx</code> starting counting from <code>0</code>.
     * To access the performance of the heuristic indexed by <code>idx</code> in 
     * the first period: <code>imprList.get(0)[idx]</code>.
     * Note that <code>idx</code> is the index of the heuristic in the universal
     * set.
     */
    public List<double[]> numImprList;
    /**
     * The number of disimprovement for each heuristic over several periods during 
     * the search.
     * <p>
     * The total computational time is divided into several periods of the same
     * time (default is <code>10</code> periods) and the number of disimprovement achieved
     * by each heuristic in that period is recorded.
     * <p>
     * The array at index <code>idx</code> is the number of disimprovement for all
     * heuristics at period <code>idx</code> starting counting from <code>0</code>.
     * To access the performance of the heuristic indexed by <code>idx</code> in 
     * the first period: <code>imprList.get(0)[idx]</code>.
     * Note that <code>idx</code> is the index of the heuristic in the universal
     * set.
     */
    public List<double[]> numDisimprList;
    /**
     * A utility object that offers helper methods
     */
    private final Utility utility;

    
    public RunStat() {
        //bean = ManagementFactory.getThreadMXBean();
        utility = new Utility();
    }
        
    /**
     * Sets the universal set for this <code>RunStat</code> object.
     * <p>
     * The universal set should contain all heuristics represented in an array of
     * integers where each integer identifies a unique heuristic. 
     * @param heurList an integer array where each integer uniquely identifies a heuristic
     */
    public void setHeurList(int[] heurList){
        this.heurList = heurList;
        numHeurs = heurList.length;
        
        //Percentage improvement (PI) where impr[idx] stores the PI for
        //the heuristic at position (index) idx in 'heurList'
        impr = new double[numHeurs];        
        runImpr = new double[numHeurs];        
        //Ppercentage disimprovement
        disimpr = new double[numHeurs];        
        runDisimpr = new double[numHeurs];        
        //percentage improvement 'over the best solution' accross the entire 
        //life span of the hyper-heuristic
        bestImpr = new double[numHeurs];                
        //Percentage improvement 'over the best solution' made during 
        //the current 'run' where a run is defined as the period of time between two
        //consecutive restarts (if the hyper-heuristic uses a restart strategy)
        runBestImpr = new double[numHeurs];               
        //Number of improvements where 'numImpr[idx]' stores the number of times 
        //the heuristic at position idx in 'heurList' has improved the solution
        numImpr = new double[numHeurs];        
        numRunImpr = new double[numHeurs];        
        //Number of disimprovement
        numDisimpr = new double[numHeurs];        
        numRunDisimpr = new double[numHeurs];        
        numBestImpr = new double[numHeurs];
        //Number of improvement over the best solution
        numRunBestImpr = new double[numHeurs];                
        //Duration where duration[idx] stores the total cumulative execution time
        //for the heuristic at index idx in 'heurList' 
        durations = new double[numHeurs];          
        
        // Track performance over several periods
        tmpImpr = new double[numHeurs];
        tmpNumImpr = new double[numHeurs];
        tmpDisimpr = new double[numHeurs];        
        tmpNumDisimpr = new double[numHeurs];        
        imprList = new ArrayList<>(numPeriods + 1); 
        disimprList = new ArrayList<>(numPeriods + 1); 
        numImprList = new ArrayList<>(numPeriods + 1);
        numDisimprList = new ArrayList<>(numPeriods + 1);                   
    }
    
    /**
     * Initializes the fields of this <code>RunStat</code> object.
     * @param initValue the value of the initial solution from which the hyper-heuristic
     * starts the search
     */
    public void init(double initValue){
        if(maxTime == 0){
            throw new ArithmeticException("Please set 'maxTime' using 'setMaxTime' method "
                        + "before calling this method");
        }
                
        iter = 1; // Starting from 1        
        bestValue = initValue;
        runBestValue = initValue;        
        wait = 0;   
        runWait = 0;
        waitRemove = 0;
        runWaitRemove = 0;
        waitReset = 0; 
        runWaitReset = 0;
        waitUpdate = 0;
        runWaitUpdate = 0;
        maxWait = 1;
        runMaxWait = 1;        
        elpTime = 0;        
        valuesTrace = new ArrayList<>(100);
        uniqueValuesTrace = new ArrayList<>(100);
        bestValuesTrace = new ArrayList<>(100);
        valuesTrace.add(initValue); 
        uniqueValuesTrace.add(initValue); 
        bestValuesTrace.add(initValue);
    }
    
    /**
     * Updates the performance-related information of the heuristic stored at index
     * <code>idx</code> in the universal set of this dynamic set.
     * <p>
     * Note that the universal set of this dynamic set is set using
     * <code>setHeurList</code>. Therefore, <code>heurList.get(idx)</code>
     * returns the heuristic unique identifier.
     * <p>
     * The update includes the percentage and frequency of improvement (disimprovement) 
     * achieved (caused) by this heuristic, the total execution time used by this heuristic,
     * the percentage and frequency of improvement over the best solution and the run-best
     * solution, etc.
     * <p>
     * @param idx          the heuristic identifier
     * @param currentValue the solution value before applying the heuristic
     * @param newValue     the solution value after applying the heuristic
     * @param duration     the time (in millisecond) taken by the heuristic to execute the move
     */
    public void updateHeurValue(int idx, double currentValue, double newValue, long duration){ 
        
        if(bestValue == Double.POSITIVE_INFINITY){
            //Terminate the current thread and throws an exception
            throw new IllegalStateException("Please initialize the 'RunStat' object using "
                    + "'init' method before calling this method");
            
            //If number of threads are running and this exception is caught by a thread 
            //the thread will die but the program continues running. Stop this behavior
        }
        
        //Update the elapsed time
        elpTime = System.currentTimeMillis() - start;
        //elpTime = bean.getCurrentThreadCpuTime()/1000000D - start;        
        //Update the iteration count
        iter++;        
        //Percentage deviation
        double delta = utility.percDiff(currentValue, newValue);        
        // The current solution is improved
        if(delta > 0){   
            //Percentage improvement
            impr[idx] += delta;
            runImpr[idx] += delta;
            tmpImpr[idx] += delta;
            //Frequency of improvement
            numImpr[idx]++;
            numRunImpr[idx]++;
            tmpNumImpr[idx]++;                        
            //Update run-specific information 
            delta = utility.percDiff(runBestValue, newValue);              
            if(delta > 0){//The run-best value has improved
                //Update
                runBestValue = newValue;                
                runBestImpr[idx] += delta;
                numRunBestImpr[idx]++;
                //Update the maximum iterations that we waited until the run-best value has improved
                runMaxWait = Math.max(runWait, runMaxWait);
                //Reinitialize
                runWait = 0;
                runWaitRemove = 0;
                runWaitReset = 0;
                runWaitUpdate = 0;                
            }
            //The run-best value is not improved
            else{
                runWait++;
                runWaitRemove++;
                runWaitReset++;
                runWaitUpdate++;
            }             
            // Update the information relating to the best solution
            delta = utility.percDiff(bestValue, newValue); 
            if(delta > 0){//The best solution has improved    
                //Update
                bestValue = newValue;                
                bestImpr[idx] += delta;
                numBestImpr[idx]++;
                //Update the maximum iterations that we waited until the best 
                //value has improved
                maxWait = Math.max(wait, maxWait);
                //Reinitialize
                wait = 0;
                waitRemove = 0;
                waitReset = 0;
                waitUpdate = 0;                
            }
            //The best value is not improved
            else{                
                wait++;
                waitRemove++;
                waitReset++;
                waitUpdate++;
            }            
        }
        //The current solution is not improved
        else{
            //Percentage disimprovement
            disimpr[idx] += -delta; //Store a positive percentage deviation
            runDisimpr[idx] += -delta;
            tmpDisimpr[idx] += -delta;
            //Frequency of disimprovement
            numDisimpr[idx]++;
            numRunDisimpr[idx]++;            
            tmpNumDisimpr[idx]++;
            //Run-best variables
            runWait++;
            runWaitRemove++;
            runWaitReset++;
            runWaitUpdate++;
            //global variables
            wait++;
            waitRemove++;            
            waitReset++;
            waitUpdate++;                        
        }        
        
        //Update the total execution time of the current heuristic
        durations[idx] += duration;            
        
        //Fitness trace
        valuesTrace.add(currentValue);
        delta = Math.abs(currentValue - uniqueValuesTrace.get(uniqueValuesTrace.size()-1));
        if(delta > eps) uniqueValuesTrace.add(currentValue);
        delta = Math.abs(bestValue - bestValuesTrace.get(bestValuesTrace.size()-1));
        if(delta > eps) bestValuesTrace.add(bestValue);        
        
        // Record the performance if the current 'period' is over
        recordPerfOverPeriod();        
    }
    
    /**
     * Record the performance over the current period if it ends.
     */
    private void recordPerfOverPeriod(){
        double slice = maxTime/numPeriods;
        elpTime = System.currentTimeMillis() - start;
        //elpTime =bean.getCurrentThreadCpuTime()/1000000D - start;
        
        // If the period is not over yet, return
        if(elpTime < (imprList.size()+1)*slice) return;
        // Otherwise, record performance over the last period
        imprList.add(tmpImpr);
        disimprList.add(tmpDisimpr);
        numImprList.add(tmpNumImpr);
        numDisimprList.add(tmpNumDisimpr);
        
        // Re-initialize the trackers
        tmpImpr = new double[numHeurs];
        tmpDisimpr = new double[numHeurs];
        tmpNumImpr = new double[numHeurs];
        tmpNumDisimpr = new double[numHeurs];
    }
        
    /**
     * Indicates that a restart strategy is triggered in the hyper-heuristic.
     * <p>
     * This method is relevant to hyper-heuristics that implement a restart strategy.
     * This method is used by this dynamic set to reset run-specific statistics such
     * as the percentage improvement over the run-best solution.
     * @param initValue the solution value of the solution from which the search 
     * is initialized 
     */
    public void restart(double initValue){
        runBestValue = initValue;
        
        //Initialize run-specific arrays and variables
        runImpr = new double[numHeurs];
        runDisimpr = new double[numHeurs];
        runBestImpr = new double[numHeurs];        
        numRunImpr = new double[numHeurs];
        numRunDisimpr = new double[numHeurs];
        numRunBestImpr = new double[numHeurs];
        
        runWait = 0;
        runWaitRemove = 0;
        waitReset = 0;
        runWaitUpdate = 0;
    }
    
    /**
     * Sets the maximum computational time for this dynamic heuristic set.
     * <p>
     * The maximum time should be the same as the maximum time for the hyper-heuristic
     * using this dynamic heuristic set.
     * @param maxTime the time limit in millisecond for the hyper-heuristic using this dynamic set
     */
    public void setMaxTime(long maxTime){
        this.maxTime = maxTime;
        start = System.currentTimeMillis();
        //start = bean.getCurrentThreadCpuTime()/1000000D; //Convert to milliseconds
    }    
}
