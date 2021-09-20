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
public class HyFlexHyperHeuristicRunner2 implements Callable<ThreadOutput[]>{
    private final ProblemDomain problem;
    private final HyperHeuristicIntrf hyperHeur;
    private final DynHeurSet pertDynSet;
    private final DynHeurSet lsDynSet;
    

    public HyFlexHyperHeuristicRunner2(ProblemDomain problem, 
            HyperHeuristicIntrf hyperHeur, DynHeurSet pertDynSet, 
            DynHeurSet lsDynSet) {
        this.problem = problem;
        this.hyperHeur = hyperHeur;
        this.pertDynSet = pertDynSet;
        this.lsDynSet = lsDynSet;
    }
    
    
    @Override
    public ThreadOutput[] call() throws Exception {        
        //Load the problem instance
        hyperHeur.loadProblemDomain(problem);
        
        //Create dynamic sets
        hyperHeur.setPertDynSet(pertDynSet);
        hyperHeur.setLsDynSet(lsDynSet);
        
        //Run hyper-heuristic
        try{
            hyperHeur.run();
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Dynamic sets that may cause the problem: ");
            System.out.println(pertDynSet);
            System.out.println(lsDynSet);
            System.exit(3);
        }
        
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
