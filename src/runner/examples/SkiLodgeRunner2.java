package runner.examples;

import hyperheuristic.HyperHeuristicIntrf;
import hyperheuristic.examples.ExampleHyperHeuristic2;

/**
 * A subclass of <code>SkiLodgeRunner</code> that is used to create:
 * <ul>
 * <li>A hyper-heuristic that will work with two dynamic sets
 * <li>A problem that will be solved by the hyper-heuristic
 * <li>A dynamic set to integrate into the hyper-heuristic.
 * </ul>
 * <p>
 * The class creates two dynamic sets to manage perturbative heuristics and
 * lcoal search heuristics separately. 
 * This class solves the Ski-Lode problem which is a toy problem.
 * In {@link SkiLodgeRunner}, one dynamic set is used for managing all heuristics
 * regardless of their types.
 * 
 * @see SkiLodgeRunner
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class SkiLodgeRunner2 extends SkiLodgeRunner{

    
    public SkiLodgeRunner2(long seed, long timeLimit) {
        super(seed, timeLimit);
    }
    
    
    /**
     * Creates a hyper-heuristic that uses two dynamic sets to manage perturbative
     * and local search heuristics separately.
     * @return a hyper-heuristic that uses two dynamic sets
     */
    @Override
    protected HyperHeuristicIntrf createHyperHeuristic() {
        //dynamic sets for perturbative and local search heuristics
        HyperHeuristicIntrf hyperHeur = new ExampleHyperHeuristic2(rng.nextLong());
        hyperHeur.setTimeLimit(timeLimit);
        return hyperHeur;
    }    
}
