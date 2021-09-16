package runner.examples;

import hyperheuristic.HyperHeuristicIntrf;
import hyperheuristic.examples.hyflex.HyFlexExampleHyperHeuristic2;

/**
 * A subclass of <code>HyFlexRunner</code> that is used to create:
 * <ul>
 * <li>A hyper-heuristic that will work with two dynamic sets
 * <li>A problem that will be solved by the hyper-heuristic
 * <li>Two dynamic sets to integrate into the hyper-heuristic
 * </ul>
 * <p>
 * The class uses HyFlex and creates two dynamic set to manage perturbative 
 * heuristics and local search heuristics separately.
 * The parent class {@link HyFlexRunner} uses one dynamic sets to managing all
 * heuristics regardless of their types.
 * 
 * @see HyFlexRunner
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class HyFlexRunner2 extends HyFlexRunner{

    public HyFlexRunner2(long seed, long timeLimit) {
        super(seed, timeLimit);
    }
    
    /**
     * Creates a hyper-heuristic that uses two dynamic sets to manage perturbative
     * and local search heuristics separately.
     * @return a hyper-heuristic that uses two dynamic sets
     */
    @Override
    protected HyperHeuristicIntrf createHyperHeuristic() {
        HyperHeuristicIntrf hyperHeur = new HyFlexExampleHyperHeuristic2(rng.nextLong());
        hyperHeur.setTimeLimit(timeLimit);
        return hyperHeur;
    }    
}
