package problem;

/**
 * This interface should be implemented by all problems.
 * <p>
 * You do not have to implement all methods of this interface. You can only implement
 * the methods that your hyper-heuristic will need.
 * 
 * @see problem.skilodge.SkiLodge
 * 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public interface Problem {
    
    public enum HeuristicType {MUTATION, RUIN_RECREATE, LOCAL_SEARCH, CROSSOVER};   
    
    public double applyHeuristic(int heuristicID, int solutionSourceIndex, int solutionDestinationIndex);
    
    public double applyHeuristic(int heuristicID, int solutionSourceIndex1, int solutionSourceIndex2, int solutionDestinationIndex);
    
    public String bestSolutionToString();
    
    public boolean compareSolutions(int solutionIndex1, int solutionIndex2);
    
    public void copySolution(int solutionSourceIndex, int solutionDestinationIndex);        
    
    public double getBestSolutionValue();
    
    public double getDepthOfSearch();
    
    public double getFunctionValue(int solutionIndex);
    
    public int[] getHeuristicsOfType(HeuristicType heuristicType);
    
    public double getIntensityOfMutation();
    
    public int getNumberOfHeuristics();       
    
    public void initialiseSolution(int index);
    
    public boolean isValid(int index);
        
    public void loadInstance(String path);       
    
    public void setDepthOfSearch(double searchDepth);
    
    public void setIntensityOfMutation(double mutationIntensity);        
    
    public void setMemorySize(int size);
    
    public String solutionToString(int solutionIndex);
}
