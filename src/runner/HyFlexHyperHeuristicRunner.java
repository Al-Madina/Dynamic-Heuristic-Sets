package runner;

import AbstractClasses.ProblemDomain;
import dynheurset.DynHeurSet;
import dynheurset.RunStat;
import hyperheuristic.HyperHeuristicIntrf;
import java.util.concurrent.Callable;

/**
 *
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class HyFlexHyperHeuristicRunner implements Callable<ThreadOutput>{
    private final ProblemDomain problem;
    private final HyperHeuristicIntrf hyperHeur;
    private final DynHeurSet dynSet;
    

    public HyFlexHyperHeuristicRunner(ProblemDomain problem, HyperHeuristicIntrf hyperHeur, DynHeurSet dynSet) {
        this.problem = problem;
        this.hyperHeur = hyperHeur;
        this.dynSet = dynSet;
    }
    
    
    @Override
    public ThreadOutput call() throws Exception {              
        //Load the problem instance
        hyperHeur.loadProblemDomain(problem);
        
        //Create dynamic set
        hyperHeur.setDynSet(dynSet);
        
        //Run hyper-heuristic
        hyperHeur.run();
        
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
