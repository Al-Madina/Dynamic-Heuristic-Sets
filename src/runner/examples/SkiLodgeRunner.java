package runner.examples;

import dynheurset.DynHeurSet;
import dynheurset.RunStat;
import dynheurset.measure.ImprFreqMeasure;
import dynheurset.measure.Measure;
import dynheurset.update.PhaseDominanceUpdate;
import dynheurset.update.Update;
import dynheurset.update.remove.FreqWorstRemoval;
import dynheurset.update.remove.Remove;
import dynheurset.update.reset.NoReset;
import dynheurset.update.reset.Reset;
import problem.Problem;
import problem.skilodge.SkiLodge;
import hyperheuristic.HyperHeuristicIntrf;
import hyperheuristic.examples.ExampleHyperHeuristic1;
import runner.GenericRunner;

/**
 * A subclass of <code>GenericRunner</code> that is used to create:
 * <ul>
 * <li>A hyper-heuristic
 * <li>A problem that will be solved by the hyper-heuristic
 * <li>A dynamic set to integrate into the hyper-heuristic.
 * </ul>
 * <p>
 * The class creates one dynamic set to manage all low-level
 * heuristics regardless of their type. 
 * This class solves the Ski-Lode problem which is a toy problem.
 * In {@link SkiLodgeRunner2}, two dynamic sets are used. One for managing the 
 * perturbative heuristics and the other one for managing the local searches.
 * 
 * @see SkiLodgeRunner2
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class SkiLodgeRunner extends GenericRunner{
    
    protected final long timeLimit;
    
    public SkiLodgeRunner(long seed, long timeLimit){
        super(seed);
        this.timeLimit = timeLimit;
    }
    
    @Override
    protected Problem createProblem() {
        Problem problem = new SkiLodge(rng.nextLong());
        //Bad! Hard-coded
        String path = "D:\\NetBeansProjects\\DynHeurSet\\Data\\Ski_Lodge\\problem-01.txt";
        problem.loadInstance(path);
        return problem;
    }
    
    @Override
    protected HyperHeuristicIntrf createHyperHeuristic() {
        HyperHeuristicIntrf hyperHeur = new ExampleHyperHeuristic1(rng.nextLong());
        hyperHeur.setTimeLimit(timeLimit);
        return hyperHeur;
    }
    
    @Override
    protected DynHeurSet createDynHeurSet(){
        RunStat runStat = new RunStat();
        DynHeurSet dynSet = new DynHeurSet();
        dynSet.setRunStat(runStat);      
        
        Update update = new PhaseDominanceUpdate(100);   
        update.setRunStat(runStat);
        Measure measure = new ImprFreqMeasure();
        update.setMeasure(measure);
        Remove remove = new FreqWorstRemoval(0.2);  
        update.setRemove(remove);        
        Reset reset = new NoReset();
        update.setReset(reset);
        
        //Now, our update strategy is all set, load it into the dynamic set
        dynSet.setUpdate(update);
        return dynSet;
    }
}
