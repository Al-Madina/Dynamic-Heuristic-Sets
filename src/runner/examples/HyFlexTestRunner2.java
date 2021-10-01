/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runner.examples;

import hyperheuristic.HyperHeuristicIntrf;
import hyperheuristic.examples.hyflex.HyFlexExampleHyperHeuristic2;

/**
 *
 * @author User
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
