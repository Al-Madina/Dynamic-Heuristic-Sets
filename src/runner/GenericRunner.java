package runner;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import dynheurset.DynHeurSet;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import problem.Problem;
import hyperheuristic.HyperHeuristicIntrf;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * This class is the base class for all runners that are used to run the simulation.
 * 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public abstract class GenericRunner {
    protected final Random rng;
    protected Result result;
    protected Result pertResult;
    protected Result lsResult;
    
    public GenericRunner(long seed){
        rng = new Random(seed);
    }
    
    protected Problem createProblem(){
        throw new UnsupportedOperationException("Please implement this method.");
    }
    
    protected ProblemDomain createHyFlexProblem(){
        throw new UnsupportedOperationException("Please implement this method.");        
    }
    
    protected HyperHeuristicIntrf createHyperHeuristic(){
        throw new UnsupportedOperationException("Please implement this method.");
    }
    
    protected DynHeurSet createDynHeurSet(){
        throw new UnsupportedOperationException("Please implement this method.");
    }
    
    private List<ThreadOutput> execute(int numRuns) 
            throws InterruptedException, ExecutionException{        
        //Gets number of available cores
        int cores = Runtime.getRuntime().availableProcessors() - 1; //excludes the main thread
        int qtn = numRuns/cores; //quotient
        int rem = numRuns%cores; //remainder
        //Stores the results for all runs
        Future<ThreadOutput>[] future = new Future[numRuns];
        //Stores the result for the current "round"
        Future<ThreadOutput>[] roundFuture;
        for(int r=0; r < qtn; r++){
            int start = r*cores;
            int end = (r+1)*cores;
            roundFuture = submit(start, end);
            for(int idx=0; idx < cores; idx++){
                future[start + idx] = roundFuture[idx];
            }
        }
        int start = numRuns - rem;
        int end = numRuns;
        roundFuture = submit(start, end);        
        for(int idx=0; idx < rem; idx++){
            future[start + idx] = roundFuture[idx];
        }
        
        List<ThreadOutput> outputList = new ArrayList<>(numRuns);
        for(int i=0; i < numRuns; i++){
            outputList.add((ThreadOutput) future[i].get());
        }
        return outputList;
    }
    
    private Future<ThreadOutput>[] submit(int start, int end) throws InterruptedException{
        if(start >= end) return null;
        int numRuns = end - start;
        Future<ThreadOutput>[] future = new Future[numRuns];
        ExecutorService exec = Executors.newFixedThreadPool(numRuns);
        
        for(int i=0; i < numRuns; i++){
            
            //Create a hyper-heuristic
            HyperHeuristicIntrf hyperHeur = createHyperHeuristic();  
            
            //Create a dynamic set
            DynHeurSet dynSet = createDynHeurSet();            
            
            Callable<ThreadOutput> hyperRunner;
            
            // Create a problem
            //Decide whether we will create a HyFlex problem or not
            if(hyperHeur instanceof HyperHeuristic){
                ProblemDomain problem = createHyFlexProblem();
                hyperRunner = new HyFlexHyperHeuristicRunner(problem, hyperHeur, dynSet);
            }
            else{ //This problem does not belong to HyFlex framework
                Problem problem = createProblem();
                hyperRunner = new HyperHeuristicRunner(problem, hyperHeur, dynSet);
            }
            
            //Run the thread
            future[i] = exec.submit(hyperRunner);
        } //Done submitting tasks
        
        //Shut down executor
        exec.shutdown();
        
        //Wait for all threads to finish running
        exec.awaitTermination(1, TimeUnit.DAYS);
        return future;
    }
    
    /**
     * The suffix indicates that the method works with two dynamic sets: one for
     * perturbative heuristics and one for local search heuristics.
     * @param numRuns
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InterruptedException
     * @throws ExecutionException 
     */
    private List<ThreadOutput[]> execute2(int numRuns) 
            throws InterruptedException, ExecutionException {        
        //Gets number of available cores
        int cores = Runtime.getRuntime().availableProcessors() - 1; //excludes the main thread
        int qtn = numRuns/cores; //quotient
        int rem = numRuns%cores; //remainder
        //Stores the results for all runs
        Future<ThreadOutput[]>[] future = new Future[numRuns];
        //Stores the result for the current "round"
        Future<ThreadOutput[]>[] roundFuture;
        for(int r=0; r < qtn; r++){
            int start = r*cores;
            int end = (r+1)*cores;
            roundFuture = submit2(start, end); //submit2
            for(int idx=0; idx < cores; idx++){
                future[start + idx] = roundFuture[idx];
            }
        }
        int start = numRuns - rem;
        int end = numRuns;
        roundFuture = submit2(start, end);  //submit2      
        for(int idx=0; idx < rem; idx++){
            future[start + idx] = roundFuture[idx];
        }
        
        List<ThreadOutput[]> outputList = new ArrayList<>(numRuns);
        for(int i=0; i < numRuns; i++){
            outputList.add((ThreadOutput[]) future[i].get());
        }
        return outputList;
    }
    
    private Future<ThreadOutput[]>[] submit2(int start, int end) throws InterruptedException{
        if(start >= end) return null;
        int numRuns = end - start;
        Future<ThreadOutput[]>[] future = new Future[numRuns];
        ExecutorService exec = Executors.newFixedThreadPool(numRuns);
        
        for(int i=0; i < numRuns; i++){
            
            //Create a hyper-heuristic
            HyperHeuristicIntrf hyperHeur = createHyperHeuristic();  
            
            //Create a dynamic set
            DynHeurSet pertDynSet = createDynHeurSet(); 
            DynHeurSet lsDynSet = createDynHeurSet();
            
            Callable<ThreadOutput[]> hyperRunner = null;
            
            /* Create a problem */
            //Decide whether we will create a HyFlex problem or not
            if(hyperHeur instanceof HyperHeuristic){
                ProblemDomain problem = createHyFlexProblem();
                hyperRunner = new HyFlexHyperHeuristicRunner2(problem, hyperHeur, 
                        pertDynSet, lsDynSet);
            }
            else{ //This problem does not belong to HyFlex framework
                Problem problem = createProblem();
                hyperRunner = new HyperHeuristicRunner2(problem, hyperHeur, 
                        pertDynSet, lsDynSet);
            }
            
            //Run the thread
            future[i] = exec.submit(hyperRunner);
        } //Done submitting tasks
        
        //Shut down executor
        exec.shutdown();
        
        //Wait for all threads to finish running

        exec.awaitTermination(1, TimeUnit.DAYS);
        return future;
    }
    
    public Result run(int numRuns) throws InterruptedException, ExecutionException {
        List<ThreadOutput> outputList = new ArrayList<>(execute(numRuns));
        
        result = new Result(outputList);
        return result;
    }

    public List<Result> run2(int numRuns) 
            throws InterruptedException, ExecutionException{
        List<ThreadOutput[]> outputList = new ArrayList<>(execute2(numRuns));
        
        List<ThreadOutput> pertOutputList = new ArrayList<>(numRuns);
        List<ThreadOutput> lsOutputList = new ArrayList<>(numRuns);
        for(int thread=0; thread < numRuns; thread++){
            pertOutputList.add(outputList.get(thread)[0]);
            lsOutputList.add(outputList.get(thread)[1]);
        }
        
        pertResult = new Result(pertOutputList);
        lsResult = new Result(lsOutputList);        
        return new ArrayList<>(Arrays.asList(pertResult, lsResult));
    }    
    
    protected Result getResult(){
        if(result == null && pertResult == null && lsResult == null){
            throw new IllegalStateException("Run the simulation first");
        }
        return result;
    }    
   
}
