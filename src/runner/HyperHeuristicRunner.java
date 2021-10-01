package runner;

import dynheurset.DynHeurSet;
import dynheurset.RunStat;
import java.util.concurrent.Callable;
import problem.Problem;
import hyperheuristic.HyperHeuristicIntrf;

/**
 * This class represents an instance of a hyper-heuristic that would be executed
 * by a thread.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class HyperHeuristicRunner implements Callable<ThreadOutput>{
    
    private final Problem problem;
    private final HyperHeuristicIntrf hyperHeur;
    private final DynHeurSet dynSet;
    

    public HyperHeuristicRunner(Problem problem, HyperHeuristicIntrf hyperHeur, DynHeurSet dynSet) {
        this.problem = problem;
        this.hyperHeur = hyperHeur;
        this.dynSet = dynSet;
    }
        
    @Override
    public ThreadOutput call(){        
        //Load the problem instance
        hyperHeur.loadProblem(problem);
        //Load dynamic set
        hyperHeur.setDynSet(dynSet);
        
        //Run hyper-heuristic
        try{
            hyperHeur.run();
        }catch(Exception ex){
            ex.printStackTrace();
            System.err.println("Dynamic set that may cause the exception: ");
            System.err.println(dynSet);
            System.exit(2);
        }
        
        //Get runStat objects which holds different run statistics
        RunStat runStat = dynSet.getRunStatistics();
        
        //Pass the run statistics to the ThreadOutput to do some processing
        ThreadOutput output = new ThreadOutput(runStat);
        //Needs to call to actually starts the processing of the statistics
        //You can override this method if you want different performance info
        output.collectPerfStatistics();
        
        return output;
    }
    
}
