package dynheurset;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import runner.GenericRunner;
import runner.Result;
import runner.examples.HyFlexTestRunner;
import runner.examples.HyFlexTestRunner2;
import runner.examples.SkiLodgeRunner;
import runner.examples.SkiLodgeRunner2;

/**
 * The Entry point of running the simulation.
 * 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //A random number generator to generate the seeds for all runs
        Random rng = new Random();
        //The time limit of the simulation
        long timeLimit = 20000;
        //Number of runs
        int numRuns = 5;

        //Executes many runs using randomly created dynamic sets.
        //If there is a bug, it is likely to surface. 
        //Alternatively, you can do "proper" tests.
        //Let us get rolling now!
        int iter = 10;
        for(int r=1; r < iter+1; r++){
            System.out.println("Starting round No: " + r); 
            //Solve a HyFlex problem (SAT in our case) by a hyper-heuristic that
            //applies a perturbative heuristics followed by a local search heuristic
            //and uses two dynamic sets to manage each type of heuristics separately
            solveHyFlexProblem2(rng, timeLimit, numRuns);
            System.out.println("All tests in round No: " + r + " were successful");
        }
        //If we reach this line, we passed all tests
        int numTests = iter*numRuns;
        //These are not tests in the technical terms; however, this is sort of
        //"proof by demonstration" if thousands of dynamic sets work gracefully
        //it is likely that integration bugs do not exist        
        System.out.println(numTests + " tests were passed");
        
        //This a hyper-heuristic that does not distinguish between heuristics of
        //different types and uses one dynamic set to manage them all.
        //solveHyFlexProblem(rng, timeLimit, numRuns);
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
        //Uncomment the following line if you want to use a predetermined dynamicset
        //GenericRunner hyflexRunner = new HyFlexRunner(rng.nextLong(), timeLimit);
        
        //Create dynamic set at random
        GenericRunner hyflexRunner = new HyFlexTestRunner(rng.nextLong(), timeLimit);
        Result res = hyflexRunner.run(numRuns);
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
        //Uncomment the following line if you want to use a predetermined dynamicset
        //GenericRunner hyflexRunner = new HyFlexRunner2(rng.nextLong(), timeLimit);
        
        //Create dynamic set at random
        GenericRunner hyflexRunner = new HyFlexTestRunner2(rng.nextLong(), timeLimit);
        List<Result> resList= hyflexRunner.run2(numRuns);
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
