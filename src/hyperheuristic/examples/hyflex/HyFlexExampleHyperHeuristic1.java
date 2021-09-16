package hyperheuristic.examples.hyflex;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import dynheurset.DynHeurSet;
import hyperheuristic.HyperHeuristicIntrf;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;

/**
 * This is an example class for using a dynamic set in a hyper-heuristic.
 * <p>
 * This class extends the <code>GenericHyperHeuristic</code> class. It
 * implements a simple random hyper-heuristic that chooses a heuristic 
 * to apply at random and accepts better moves.
 * @author Ahmed (ahmedhassan@aims.ac.za)
 * <p>
 * This hyper-heuristic uses the HyFlex framework.
 */
public class HyFlexExampleHyperHeuristic1 extends HyperHeuristic implements HyperHeuristicIntrf{
    /**
     * A dynamic set to be used with this hyper-heuristic
     */
    private DynHeurSet dynSet;
    private ThreadMXBean bean;
    
  
    public HyFlexExampleHyperHeuristic1(long seed){
        super(seed);
    }

    //This method should contain your hyper-heuristic logic
    @Override
    public void solve(ProblemDomain problem) {
               
        //Set the maximum computational time
        //This will help the dynamic set to make time-related decisions
        //Call this method BEFORE calling the 'init' of `DynHeurSet`
        dynSet.setMaxTime(getTimeLimit()); 
                
        bean = ManagementFactory.getThreadMXBean();
        
        //Get the mutational heuristics
        int[] muHeurs = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.MUTATION);
        //Get the ruin-recreate heuristics
        int[] rrHeurs = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.RUIN_RECREATE);
        //Get the local search heuristics
        int[] lsHeurs = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH);

        //Create the "universal set" which contains all heuristics
        int[] univList = new int[muHeurs.length + rrHeurs.length + lsHeurs.length];
        for(int i=0; i < muHeurs.length; i++){
            univList[i] = muHeurs[i];
        }        
        for(int i=0; i < rrHeurs.length; i++){
            univList[muHeurs.length + i] = rrHeurs[i];
        }         
        for(int i=0; i < lsHeurs.length; i++){
            univList[muHeurs.length + rrHeurs.length + i] = lsHeurs[i];
        }     
        
        //Set the universal set of the dynamic set
        dynSet.setHeurList(univList); 
        
        //Initialize a solution
        problem.initialiseSolution(0);
        //Get the solution value
        double currentValue = problem.getFunctionValue(0);            
       
        //Set the initial value for the dynamic set.
        dynSet.init(currentValue);
        //Now, the dynamic set is all set and ready to go
        
        //Entering the optimization loop
        while(!hasTimeExpired()){
            //Get the active set
            List<Integer> activeList = dynSet.updateActiveList();
            //Choose an index of a heuristic at random
            int heurIndex = activeList.get(rng.nextInt(activeList.size()));
            //Choose the heuristic from the universal set
            int heurToApply = univList[heurIndex];
            
            //Measure the execution time for the current heuristic
            long before = bean.getCurrentThreadCpuTime();
            //Apply the heuristic to the solution at index 0 in the solution memory
            //and store the new solution at index 1 in the solution memory
            double newValue = problem.applyHeuristic(heurToApply, 0, 1);
            
            //Measure the execution time of the heuristics in millisecond. 
            long duration = 1 + (bean.getCurrentThreadCpuTime() - before)/1000000L;

            
            //Update the heuristic performance in the dynamic set
            dynSet.updateHeurValue(heurIndex, currentValue, newValue, duration);
            
            //Accept the move if it leads to a better solution
            if(newValue < currentValue){                
                problem.copySolution(1, 0);
                currentValue = newValue;
            }
        }
    }

    @Override
    public String toString() {
        return "Example Hyper Heuristic 1";
    }
    
    //You need to implement this method of the HyperHeuristicIntrf interface
    //to set the dynamic set for this hyper-heuristic
    @Override
    public void setDynSet(DynHeurSet dynSet){
        this.dynSet = dynSet;
    }

}

