package runner;

import dynheurset.DynHeurSet;
import dynheurset.RunStat;
import java.util.concurrent.Callable;
import problem.Problem;
import hyperheuristic.HyperHeuristicIntrf;

/**
 *
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class HyperHeuristicRunner2 implements Callable<ThreadOutput[]>{
    
    private final Problem problem;
    private final HyperHeuristicIntrf hyperHeur;
    private final DynHeurSet pertDynSet;
    private final DynHeurSet lsDynSet;    

    public HyperHeuristicRunner2(Problem problem, HyperHeuristicIntrf hyperHeur, 
            DynHeurSet pertDynSet, DynHeurSet lsDynSet) {
        this.problem = problem;
        this.hyperHeur = hyperHeur;
        this.pertDynSet = pertDynSet;
        this.lsDynSet = lsDynSet;
    }
    
    
    @Override
    public ThreadOutput[] call() throws Exception {
        //Load the problem instance
        hyperHeur.loadProblem(problem);
        //Load dynamic sets
        hyperHeur.setPertDynSet(pertDynSet);        
        hyperHeur.setLsDynSet(lsDynSet);
        
        //Run hyper-heuristic
        hyperHeur.run();
        
        //Get runStat objects which holds different run statistics
        RunStat pertRunStat = pertDynSet.getRunStatistics();
        RunStat lsRunStat = lsDynSet.getRunStatistics();        

        //Pass the run statistics to the ThreadOutput to do some processing
        ThreadOutput pertOutput = new ThreadOutput(pertRunStat);
        ThreadOutput lsOutput = new ThreadOutput(lsRunStat);
        //Needs to call to actually starts the processing of the statistics
        //You can override this method if you want different performance info
        pertOutput.collectPerfStatistics();
        lsOutput.collectPerfStatistics();
        
        return new ThreadOutput[]{pertOutput, lsOutput};
    }
    
}
