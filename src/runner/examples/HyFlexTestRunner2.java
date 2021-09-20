package runner.examples;

import hyperheuristic.HyperHeuristicIntrf;
import hyperheuristic.examples.hyflex.HyFlexExampleHyperHeuristic2;

/**
 *
 * @author Ahmed Hassan
 */
public class HyFlexTestRunner2 extends HyFlexTestRunner{

    public HyFlexTestRunner2(long seed, long timeLimit) {
        super(seed, timeLimit);
    }
    
    @Override
    protected HyperHeuristicIntrf createHyperHeuristic() {
        HyperHeuristicIntrf hyperHeur = new HyFlexExampleHyperHeuristic2(rng.nextLong());
        hyperHeur.setTimeLimit(timeLimit);
        return hyperHeur;
    }    
}
