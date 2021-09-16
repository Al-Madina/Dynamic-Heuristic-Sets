package hyperheuristic.examples.hyflex;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import dynheurset.DynHeurSet;
import hyperheuristic.HyperHeuristicIntrf;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;

/**
 * This is an example class for using two dynamic sets in a hyper-heuristic.
 * <p>
 * This class extends the <code>GenericHyperHeuristic</code> class. It
 * implements a simple iterated local search hyper-heuristic that chooses 
 * a perturbative heuristic at random and applies it followed by applying 
 * a local search heuristic that is also chosen at random. It accepts better moves.
 * <p>
 * In this example, we use two dynamic sets to manage the perturbative heuristics
 * and local search heuristic separately.
 * <p>
 * This example uses the HyFlex framework.
 * @author Ahmed (ahmedhassan@aims.ac.za)
 */
public class HyFlexExampleHyperHeuristic2 extends HyperHeuristic implements HyperHeuristicIntrf{
    /**
     * A dynamic set to manage perturbative heuristics
     */
    private DynHeurSet pertDynSet;
    /**
     * A dynamic set to manage local search heuristics
     */
    private DynHeurSet lsDynSet;  
    private ThreadMXBean bean; 

    
    public HyFlexExampleHyperHeuristic2(long seed) {
        super(seed);
    }

    
    //This is the only method you need to override from GenericHyperHeuristic.
    //It should contain your hyper-heuristic logic
    @Override
    public void solve(ProblemDomain problem) {
       
        //Set the maximum computational time
        //This will help the dynamic set to make time-related decisions
        //Call this method BEFORE calling the 'init' of `DynHeurSet`
        pertDynSet.setMaxTime(getTimeLimit()); 
        lsDynSet.setMaxTime(getTimeLimit()); 
        
        bean = ManagementFactory.getThreadMXBean();
        
        //Get the mutational heuristics
        int[] muHeurs = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.MUTATION);
        //Get the ruin-recreate heuristics
        int[] rrHeurs = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.RUIN_RECREATE);
        //Create the "universal set" for perturbative heuristic
        int[] pertUnivList = new int[muHeurs.length + rrHeurs.length];
        for(int i=0; i < muHeurs.length; i++){
            pertUnivList[i] = muHeurs[i];
        }        
        for(int i=0; i < rrHeurs.length; i++){
            pertUnivList[muHeurs.length + i] = rrHeurs[i];
        }
        //Set the universal set of the dynamic set for perturbative heuristics
        pertDynSet.setHeurList(pertUnivList); 
        //Get the "universal set" for local search heuristics
        int[] lsUnivList = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH); 
        //Set the universal set of the dynamic set for local search heuristics
        lsDynSet.setHeurList(lsUnivList);
       
        //Initialize a solution
        problem.initialiseSolution(0);
        //Get the solution value
        double currentValue = problem.getFunctionValue(0);

        //Set the initial value for the dynamic set.
        pertDynSet.init(currentValue);
        lsDynSet.init(currentValue);
        //Now, the dynamic set is all set and ready to go

        //Entering the optimization loop
        while(!hasTimeExpired()){
            /* First apply a perturbative heuristic */
            //Get the active heuristic set for perturbative heuristics
            List<Integer> pertActiveList = pertDynSet.updateActiveList();            
            //Choose an index of a heuristic to apply at random
            int pertHeurIndex = pertActiveList.get(rng.nextInt(pertActiveList.size()));
            //Choose the heuristic from the universal set
            int heurToApply = pertUnivList[pertHeurIndex];
            
            //Measure the execution time for the current heuristic
            long pertBefore = bean.getCurrentThreadCpuTime();
            //Apply the heuristic to the solution at index 0 in the solution memory
            //and store the new solution at index 1 in the solution memory
            problem.applyHeuristic(heurToApply, 0, 1);                        
            
            //Do not update the value for the current perturbative heuristic yet.
            //We evaluate the "value" of a perturbative heuristic by the solution
            //value generated AFTER applying the local search heuristic            
            
            /* Apply a local search heuristic */
            //Get the active heuristic set for local searches
            List<Integer> lsActiveList = lsDynSet.updateActiveList();
            //Choose an index of a heuristic to apply at random
            int lsHeurIndex = lsActiveList.get(rng.nextInt(lsActiveList.size()));
            //Choose the heuristic from the universal set
            heurToApply = lsUnivList[lsHeurIndex];
            
            //Measure the execution time for the current heuristic
            long before = bean.getCurrentThreadCpuTime();
            //Apply the heuristic to the solution at index 1 in the solution memory
            //and store the new solution at index 1 in the solution memory
            double newValue = problem.applyHeuristic(heurToApply, 1, 1);
            
            long duration = 1 + (bean.getCurrentThreadCpuTime() - before)/1000000L;
            
            //Update the heuristic performance in the dynamic set
            lsDynSet.updateHeurValue(lsHeurIndex, currentValue, newValue, duration);
            
            //AFTER applying the local search, we can now update the performance
            //information for the perturbative heuristic. This makes sense since
            //good perturbative heuristics should help local searches to escape
            //local optima
            
            duration = 1 + (bean.getCurrentThreadCpuTime() - pertBefore)/1000000L;
            pertDynSet.updateHeurValue(pertHeurIndex, currentValue, newValue, duration);            
            
            //Accept the move if it leads to a better solution
            if(newValue < currentValue){
                problem.copySolution(1, 0);
                currentValue = newValue;
            }                         
        }
    }
    
    //You need to implement this method of the HyperHeuristicIntrf interface
    //to set the dynamic set for this hyper-heuristic
    @Override
    public void setPertDynSet(DynHeurSet pertDynSet){
        this.pertDynSet = pertDynSet;
    }
    
    //You need to implement this method of the HyperHeuristicIntrf interface
    //to set the dynamic set for this hyper-heuristic
    @Override
    public void setLsDynSet(DynHeurSet lsDynSet){
        this.lsDynSet = lsDynSet;
    }

    @Override
    public String toString() {
        return "Example Hyper Heuristic 2";
    }
}
