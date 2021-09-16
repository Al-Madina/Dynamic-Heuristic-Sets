package dynheurset.update;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements a phase-based greedy update strategy.
 * <p>
 * The active set is updated every <code>phaseLen</code> iterations. 
 * The best <code>top</code> are included in the active set where <code>top</code>
 * is a field for the number of heuristics to include in the active set.
 * <p>
 * The greediness selection is fair. Every heuristic will be part of the active
 * set for a maximum number of times controlled by <code>max</code>. If a heuristic
 * is chosen <code>max</code> times, it cannot be part of the active set until
 * all other heuristics are selected <code>max</code> times. In this case, the 
 * frequency count for all heuristics is reset to <code>0</code>.
 * The selection is greedy considering the heuristics that are available (those
 * that are not permanently removed or hits <code>max</code>.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class PhaseFairGreedyUpdate extends PhaseGreedyUpdate{
    /**
     * The maximum number of consecutive iterations a heuristic can be in the 
     * active set.
     */
    protected final int max;
    /**
     * The frequencies of being in the active set.
     */
    protected List<Integer> freq;

    public PhaseFairGreedyUpdate(int max, int phaseLen, int top) {
        super(phaseLen, top);
        this.max = max;                
    }
    
    @Override
    public void performUpdate(){
        //Starts from an empty active set
        activeList.clear();
        if(freq == null){
            freq = new ArrayList<>(heurList.size());
            for(int idx=0; idx<heurList.size(); idx++) freq.add(0);
        }
        //Get the minimum frequency for the least-used heuristic
        int min = (int)(util.min(freq));
        //Reset frequency if all heuristics are used equally
        if(min >= max){
           for(int idx=0; idx<freq.size(); idx++) {
               if(remove.removedHeurSet.contains(idx)) {
                   freq.set(idx, max);
                   continue;
               }
               freq.set(idx, 0);
           }
        }        
        //Compute the number of heuristics available for selection
        int freeHeurs = heurList.size() - remove.removedHeurSet.size();
        if(top > freeHeurs){
            //Keep it greedy
            top = freeHeurs > 1 ? freeHeurs - 1 : freeHeurs;            
        }
         //Measure the performance of all heuristics       
        List<Double> values = measure.measure();
        //Heuristics that should not be selected
        Set<Integer> seen = new HashSet<>(freeHeurs);
        //Include those that are permanently removed
        seen.addAll(remove.removedHeurSet);
        for(int idx=0; idx < values.size(); idx++){
            //Get the index of the maximum value (index of the best heuristic
            //among all heuristics available for selection)
            int chosen = util.getIndexOfMaxValue(values, seen);      
            if(freq.get(chosen) >= max) {
                seen.add(chosen);
                continue;
            }
            activeList.add(chosen);
            //Make it unavailable for selection again
            seen.add(chosen);
            //Update the frequency of chosen heuristics
            freq.set(chosen, freq.get(chosen) + 1);
            if(activeList.size() == top) break;
        }
    }
}
