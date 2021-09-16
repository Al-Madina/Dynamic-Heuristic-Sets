package problem.skilodge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class SkiLodgeSolution{
    
    private final List<Integer> choices;
    private final List<Double> compensations;
    private final int UNASSIGNED = -1;
    private final double MAX_COST = 1000.0;
    
    private double value;
    private int worstOwner;
    private double worstCost;

    public SkiLodgeSolution() {
        choices = new ArrayList<>(64);
        compensations = new ArrayList<>(64);
        for(int i=0; i < 64; i++){
            choices.add(UNASSIGNED);
            //If an owner cannot be given any week, the compensation is 1000
            compensations.add(0.0); //for delta calc
        }
        value = 0;
        worstOwner = -1;
        worstCost = -1;
        
    }
    
    public void assignWeekToOwner(int owner, int week, double compensation){
        value += compensation - compensations.get(owner);
        
        //We consider the worst cost ONLY among owners that have assigned week.
        //This is because we work with "complete" solutions
        if(compensation < MAX_COST && compensation > worstCost){
            worstOwner = owner;
            worstCost = compensation;            
        }
        else if(compensation < MAX_COST && owner == worstOwner){
            //We need to find the new worst owner if the current owner was the worst one
            //and its compensation is lower than the previous compensation
            findWorstOwner();
        }     
        choices.set(owner, week);
        compensations.set(owner, compensation);
    }
    
    public List<Integer> getAssignment(){
        return choices;
    }
    
    public int getOwnerAssignment(int owner){
        return choices.get(owner);
    }    
    
    public List<Double> getCompensations(){
        return compensations;
    }
    
    public double getOwnerCompensation(int owner){
        return compensations.get(owner);
    }
    
    public int getWorstOwner(){
        return worstOwner;
    }
    
    public List<Integer> getOwnersAssignedToWeek(int week){
        //You could use a dict for this to improve the performance
        List<Integer> list = new ArrayList<>(4); //at any week no more than 4 owners can book
        for(int ow=0; ow < 64; ow++){
            if(choices.get(ow) == week){
                list.add(ow);
            }
        }
        return list;
    }
        
    public double getSolutionValue(){
        return value;
    }
     
    public SkiLodgeSolution copySolution(){//Alternatively, you can implement the Cloneable intrf
        SkiLodgeSolution solution = new SkiLodgeSolution();
        for(int ow=0; ow < 64; ow++){            
            solution.choices.set(ow, Integer.valueOf(this.choices.get(ow))); //Did the boxing to leave no doubt!!
            solution.compensations.set(ow, Double.valueOf(this.compensations.get(ow)));            
        }
        solution.worstCost = this.worstCost;
        solution.worstOwner = this.worstOwner;
        solution.value = this.value;
        return solution;
    }    
    
    public int size(){
        return choices.size();
    }
    
    private void findWorstOwner(){
        worstCost = -1;
        for(int ow=0; ow < choices.size(); ow++){
            if(compensations.get(ow) > worstCost){
                worstCost = compensations.get(ow);
                worstOwner = ow;
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.choices);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj){
        //Delegates the 'equals' method of 'List'
        return this.choices.equals(obj);
    }
    
    @Override
    public String toString() {
        return choices.toString() + "\n" + compensations.toString();
    }    
}
