package dynheurset;

import dynheurset.measure.ImprFreqMeasure;
import dynheurset.measure.Measure;
import dynheurset.update.PhaseDominanceUpdate;
import dynheurset.update.Update;
import dynheurset.update.remove.FreqWorstRemoval;
import dynheurset.update.remove.Remove;
import dynheurset.update.reset.FreqReset;
import dynheurset.update.reset.Reset;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import runner.GenericRunner;
import runner.Result;
import runner.examples.HyFlexRunner;
import runner.examples.HyFlexRunner2;
import runner.examples.SkiLodgeRunner;
import runner.examples.SkiLodgeRunner2;

/**
 * The Entry point of running the simulation.
 * 
 * @author Ahmed
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //A random number generator to generate the seeds for all runs
        Random rng = new Random();
        //The time limit of the simulation
        long timeLimit = 60000;
        //Number of runs
        int numRuns = 1;
        
//        solveHyFlexProblem(rng, timeLimit, numRuns);
//        solveHyFlexProblem2(rng, timeLimit, numRuns);
//        solveSkiLodge(rng, timeLimit, numRuns);
        solveSkiLodge2(rng, timeLimit, numRuns);
        
    }
    
    /**
     * Run the simulation for the Ski-Lodge Problem using a hyper-heuristic 
     * that uses one dynamic set to manage all heuristics regardless of their
     * types.
     * @param rng random number generator
     * @param timeLimit maximum computational time
     * @param numRuns number of runs
     * @throws InterruptedException
     * @throws ExecutionException 
     */
    private static void solveSkiLodge(Random rng, long timeLimit, int numRuns) 
            throws InterruptedException, ExecutionException{
        GenericRunner runner = new SkiLodgeRunner(rng.nextLong(), timeLimit);
        
        Result res = runner.run(numRuns);
        
        printResults(res);        
    }
    
    /**
     * Run the simulation for the Ski-Lodge Problem using a hyper-heuristic 
     * that uses two dynamic sets to manage perturbative and local search heuristics
     * separately.
     * @param rng random number generator
     * @param timeLimit maximum computational time
     * @param numRuns number of runs
     * @throws InterruptedException
     * @throws ExecutionException 
     */
    private static void solveSkiLodge2(Random rng, long timeLimit, int numRuns) 
            throws InterruptedException, ExecutionException{
        GenericRunner runner = new SkiLodgeRunner2(rng.nextLong(), timeLimit);
        
        List<Result> resList = runner.run2(numRuns);
        
        System.out.println("Printing results for perturbative heuristic dynamic set");
        printResults(resList.get(0));
        System.out.println("\n");
        System.out.println("Printing results for local search heuristic dynamic set");
        printResults(resList.get(1));        
    }    
    
    /**
     * Run the simulation for a HyFlex problem using a hyper-heuristic 
     * that uses one dynamic set to manage all heuristics regardless of their
     * types.
     * @param rng random number generator
     * @param timeLimit maximum computational time
     * @param numRuns number of runs
     * @throws InterruptedException
     * @throws ExecutionException 
     */
    private static void solveHyFlexProblem(Random rng, long timeLimit, int numRuns) throws InterruptedException, ExecutionException{
        GenericRunner satRunner = new HyFlexRunner(rng.nextLong(), timeLimit);
        Result res = satRunner.run(numRuns);
        printResults(res);
    }
    
    /**
     * Run the simulation for a HyFlex problem using a hyper-heuristic 
     * that uses two dynamic sets to manage perturbative and local search heuristics
     * separately.
     * @param rng random number generator
     * @param timeLimit maximum computational time
     * @param numRuns number of runs
     * @throws InterruptedException
     * @throws ExecutionException 
     */
    private static void solveHyFlexProblem2(Random rng, long timeLimit, int numRuns) throws InterruptedException, ExecutionException{
        GenericRunner satRunner = new HyFlexRunner2(rng.nextLong(), timeLimit);
        List<Result> resList= satRunner.run2(numRuns);
        System.out.println("Printing results for perturbative heuristic dynamic set");
        printResults(resList.get(0));
        System.out.println("");
        System.out.println("Printing results for local search heuristic dynamic set");
        printResults(resList.get(1));
        
    }          
    
    /**
     * Prints the results.
     * @param result the result object encapsulating the results
     */
    private static void printResults(Result result){
        DecimalFormat df = new DecimalFormat("#.####");
        System.out.println("Run Statistics");
        System.out.println("min = " + df.format(result.min()));
        System.out.println("mean = " + df.format(result.mean()));
        System.out.println("standard deviation = " + df.format(result.std()));
        System.out.println("median = " + df.format(result.median()));
        System.out.println("max = " + df.format(result.max()));
        
        System.out.println("");
        System.out.println("Individual heuristic performance (absolute performance)");
        System.out.println("Precentage improvement for each heuristic");
        printArray(result.avgImprovement());
        System.out.println("Precentage disimprovement for each heuristic");
        printArray(result.avgDisimprovement());
        System.out.println("Number of improvement for each heuristic");
        printArray(result.avgNumberOfImprovements());
        System.out.println("Number of disimprovement for each heuristic");
        printArray(result.avgNumberOfDisimprovements());
        
        System.out.println("");
        System.out.println("Group heuristic performance (relative performance)");
        System.out.println("Percentage of improvement of each heuristic relative to other heuristics");
        printArray(result.avgGroupImprovement());
        System.out.println("Percentage of disimprovement of each heuristic relative to other heuristics");
        printArray(result.avgGroupDisimprovement());
        System.out.println("Number of improvement of each heuristic relative to other heuristics");
        printArray(result.avgGroupNumberOfImprovements());
        System.out.println("Number of disimprovement of each heuristic relative to other heuristics");
        printArray(result.avgGroupNumberOfDisimprovements()); 
        //Share of each heuristic in the total computational time
        System.out.println("Utilization of each heuristic relative to other heuristics");
        printArray(result.avgDurations());
    }
    
    /**
     * Prints an array
     * @param arr the array to be printed
     */
    private static void printArray(double[] arr){
        DecimalFormat df = new DecimalFormat("#.####");
        double s = 0;
        for(double v : arr) {
            System.out.print(df.format(v) + " ");
            s += v;
        }
        System.out.println("");
    }    
}
