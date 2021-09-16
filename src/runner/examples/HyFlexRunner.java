package runner.examples;

import AbstractClasses.ProblemDomain;
import SAT.SAT;
import dynheurset.DynHeurSet;
import dynheurset.RunStat;
import dynheurset.measure.ImprMeasure;
import dynheurset.measure.Measure;
import dynheurset.update.PhaseDominanceUpdate;
import dynheurset.update.Update;
import dynheurset.update.remove.NoRemove;
import dynheurset.update.remove.Remove;
import dynheurset.update.reset.NoReset;
import dynheurset.update.reset.Reset;
import hyperheuristic.HyperHeuristicIntrf;
import hyperheuristic.examples.hyflex.HyFlexExampleHyperHeuristic1;
import runner.GenericRunner;

/**
 * A subclass of <code>GenericRunner</code> that is used to create:
 * <ul>
 * <li>A hyper-heuristic
 * <li>A problem that will be solved by the hyper-heuristic
 * <li>A dynamic set to integrate into the hyper-heuristic
 * </ul>
 * <p>
 * The class uses HyFlex and creates one dynamic set to manage all low-level
 * heuristics regardless of their type. 
 * In {@link HyFlexRunner2}, two dynamic sets are used. One for managing the 
 * perturbative heuristics and the other one for managing the local searches.
 * 
 * @see HyFlexRunner2
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class HyFlexRunner extends GenericRunner{
    
    protected final long timeLimit;
    
    public HyFlexRunner(long seed, long timeLimit){
        super(seed);
        this.timeLimit = timeLimit;
    }

    @Override
    protected ProblemDomain createHyFlexProblem() {
        //create a ProblemDomain object with a seed for the random number generator
        ProblemDomain problem = new SAT(rng.nextLong());  //boolean satisfiability
        
        /* Uncomment on of the following line if you want to play with another problem */
//        ProblemDomain problem = new BinPacking(rng.nextLong()); //bin packing
//        ProblemDomain problem = new PersonnelScheduling(rng.nextLong()); //personnel scheduling
//        ProblemDomain problem = new FlowShop(rng.nextLong()); //permutation flow shop
//        ProblemDomain problem = new TSP(rng.nextLong()); //traveling salesman
//        ProblemDomain problem = new VRP(rng.nextLong()); //vehicle routing

        //Load a problem instance to solve
        problem.loadInstance(5);
        return problem;
    }

    @Override
    protected HyperHeuristicIntrf createHyperHeuristic() {
        HyperHeuristicIntrf hyperHeur = new HyFlexExampleHyperHeuristic1(rng.nextLong());
        hyperHeur.setTimeLimit(timeLimit);
        return hyperHeur;
    }

    @Override
    protected DynHeurSet createDynHeurSet() {
        RunStat runStat = new RunStat();
        DynHeurSet dynSet = new DynHeurSet();
        dynSet.setRunStat(runStat);      
        
        //Create an update strategy
        Update update = new PhaseDominanceUpdate(100);   
        update.setRunStat(runStat);
        Measure measure = new ImprMeasure();
        update.setMeasure(measure);
        Remove remove = new NoRemove();        
        update.setRemove(remove);        
        Reset reset = new NoReset();
        update.setReset(reset);
        
        //Now, our update strategy is all set, load it into the dynamic set
        dynSet.setUpdate(update);
        return dynSet;
    }
    
}
