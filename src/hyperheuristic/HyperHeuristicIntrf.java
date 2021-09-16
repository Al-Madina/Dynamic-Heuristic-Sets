package hyperheuristic;

import AbstractClasses.ProblemDomain;
import dynheurset.DynHeurSet;
import problem.Problem;

/**
 * An interface for hyper-heuristics that use dynamic sets.
 * 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public interface HyperHeuristicIntrf {
    
    public double getBestSolutionValue();
    
    public default DynHeurSet getDySet(){
        throw new UnsupportedOperationException("Not supported yet.");        
    }
    
    public default DynHeurSet getPertDynSet(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public default DynHeurSet getLsDynSet(){
        throw new UnsupportedOperationException("Not supported yet.");
    }  
    
    public default long getElapsedTime(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public default long getTimeLimit(){
        throw new UnsupportedOperationException("Not supported yet.");
    }    
    
    public default void loadProblem(Problem problem){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public default void loadProblemDomain(ProblemDomain problem){
        throw new UnsupportedOperationException("Not supported yet.");
    }    
    
    public default void run(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default void setDynSet(DynHeurSet dynSet){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public default void setPertDynSet(DynHeurSet pertDynSet){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public default void setLsDynSet(DynHeurSet lsDynSet){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public default void setTimeLimit(long time){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
