package hyperheuristic;

import dynheurset.DynHeurSet;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import problem.Problem;

/**
 * This class is the base class for all hyper-heuristics that use dynamic sets.
 * 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public abstract class GenericHyperHeuristic implements HyperHeuristicIntrf{
    /**
     * Random number generator.
     */
    protected final Random rng;
    /**
     * The problem to be solved by this hyper-heuristic.
     */
    protected Problem problem;
    /**
     * A dynamic set.
     */
    protected DynHeurSet dynSet;
    /**
     * A dynamic set for managing perturbative heuristics separately.
     */
    protected DynHeurSet pertDynSet;
    /**
     * A dynamic set for managing local search heuristics separately.
     */
    protected DynHeurSet lsDynSet;
    /**
     * Best solution value
     */
    private double bestSolutionValue;
    /**
     * The maximum computational time allocated to this hyper-heuristic
     */
    private long timeLimit;
    /**
     * The time when the search started
     */
    private long start;
    
    //I decided against the use of ThreadMXbean and revert to System.
    //It gives me a bit of inconsistency.
    //ThreadMXBean bean;
    
    
    public GenericHyperHeuristic(long seed) {
        this.rng = new Random(seed);
        bestSolutionValue = Double.MAX_VALUE;
        timeLimit = 0;
        start = 0;
    }
    

    @Override
    public double getBestSolutionValue() {
        if(bestSolutionValue == Double.MAX_VALUE){
            throw new IllegalStateException("Please call 'hasTimeExpired' before calling this method");
        }
        return bestSolutionValue;
    }

    @Override
    public long getElapsedTime() {
        if(start == 0) return 0;
        return System.currentTimeMillis() - start;
        
        //Previously I used bean. I left this commented if you want to use bean instead
        /*if(this.bean == null){
            return 0;
        }
        return (long)((bean.getCurrentThreadCpuTime() - start) / 1000000.0D);*/
    }

    @Override
    public long getTimeLimit() {
        return timeLimit;
        //return timeLimit / 1000000L;
    }

    public boolean hasTimeExpired() {
        long elpTime = System.currentTimeMillis() - start;
        if(elpTime >= timeLimit){
            return true;
        }
        bestSolutionValue = problem.getBestSolutionValue();
        return false;
        
        //Previously I used bean. I left this commented if you want to use bean instead
        /*long elpTime = bean.getCurrentThreadCpuTime() - start;
        if(elpTime >= timeLimit){
            return true;
        }
        bestSolutionValue = problem.getBestSolutionValue();
        return false;*/
    }
    
    @Override
    public void loadProblem(Problem problem) {
        this.problem = problem;
    }

    @Override
    public void run() {
        if(problem == null){
            throw new IllegalStateException("Please load the problem domain "
                    + "using 'loadProblem' before calling this method");
        }
        if(timeLimit == 0){
            throw new IllegalStateException("Please set the time limit using "
                    + "'setTimeLimit' before calling this method");
        }
        start = System.currentTimeMillis();
        
        //Previously I used bean. I left this commented if you want to use bean instead
        /*bean = ManagementFactory.getThreadMXBean();
        start = bean.getCurrentThreadCpuTime();*/
        
        //In your hyper-heuristic, override the 'solve' method to implement your
        //logic for solving the problem
        solve();        
    }

    @Override
    public DynHeurSet getDySet(){
        return dynSet;
    }
    
    @Override
    public DynHeurSet getPertDynSet(){
        return pertDynSet;
    }
    
    @Override
    public DynHeurSet getLsDynSet(){
        return lsDynSet;
    }
    
    @Override
    public void setDynSet(DynHeurSet dynSet){
        this.dynSet = dynSet;
    }
    
    @Override
    public void setPertDynSet(DynHeurSet pertDynSet){
        this.pertDynSet = pertDynSet;
    }
    
    @Override
    public void setLsDynSet(DynHeurSet lsDynSet){
        this.lsDynSet = lsDynSet;
    }
    
    @Override
    public void setTimeLimit(long time) {
        timeLimit = time;
        //Previously I used bean. I left this commented if you want to use bean instead
        /*if(timeLimit == 0){
            //Convert time to nanoseconds since we are using 'bean' to measure time
            timeLimit = time * 1000000L;
        }
        else{
            throw new IllegalStateException("Cannot set the time limit twice");
        }*/
    }
    
    protected abstract void solve();
    
}
