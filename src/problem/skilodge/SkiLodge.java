package problem.skilodge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import problem.Problem;

/**
 *  The Ski-Lodge Problem.
 * 
 * <p>
 * This class implements the Ski-Lodge problem which is involves assigning weeks
 * to customers in a ski-lodge such that hard constraints regarding the ski-lodge
 * capacity and fire regulation is not violated while all customers are accommodated
 * in a way that reduces the total compensation paid by the ski-lodge to the 
 * customers that have their preferences not met.
 * 
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class SkiLodge implements Problem{
    
    private final int[][] OWNER_CHOICES;
    private final int[] OWNER_PARTY;
    private final int UNASSIGNED;
    private final int MAX_OWNERS;
    private final int MAX_WEEKS;
    private final int MAX_CAPACITY;
    private final int MAX_APART;
    
    
    private double AVG_PARTY;
    private List<Integer> smallFamilies;
    private List<Integer> largeFamilies;
    
    private final int[] MU = new int[]{0,1,2,3,4,5};
    private final int[] RR = new int[]{6};
    private final int[] LS = new int[]{7,8,9,10};
    private final int[] CO =  null; //has not implemented crossover yet
    
    private double muIntensity; //did not use intensity of mutation
    //as perturbative heuristics are applied once
    private double searchDepth;
    
    private SkiLodgeSolution[] memory;
    private double bestSolutionValue;
    private int bestSolIndex;
    
    private final Random rng;

    public SkiLodge(long seed) {
        rng = new Random(seed);
        MAX_OWNERS = 64;        
        OWNER_CHOICES = new int[MAX_OWNERS][3];
        OWNER_PARTY = new int[MAX_OWNERS];
        MAX_WEEKS = 16;
        UNASSIGNED = -1;        
        MAX_CAPACITY = 22;
        MAX_APART = 4;
        memory = new SkiLodgeSolution[2];
        bestSolutionValue = Double.MAX_VALUE;
        bestSolIndex = -1;        
        muIntensity = 0.2;
        searchDepth = 0.2;
    }
    
    
    @Override
    public double applyHeuristic(int heuristicID, int srcIndex, int desIndex) {
        //First copy the solution if needed
        if(srcIndex != desIndex){
            memory[desIndex] = memory[srcIndex].copySolution();
            //solutionValues[desIndex] = solutionValues[srcIndex];
        }

        //Apply heuristic
        switch(heuristicID){
            //Mutational heuristics
            case 0:
                swapTwoOwnersAtRandom(desIndex);
                break;
            case 1:
                swapWorstOwner(desIndex);
                break;
            case 2:
                swapWorstOwnerToBestWeek(desIndex);
                break;                
            case 3:
                swapSmallLarge(desIndex);
                break;
            case 4:
                swapSmall(desIndex);
                break;
            case 5:
                swapLarge(desIndex);
                break;                  
            //Ruin and recreate heuristics
            case 6:
                ruinAndRecreate(desIndex);
                break;                
            //Local search heuristics
            case 7:
                swapLocalSearch(desIndex);
                break;
            case 8:
                swapSmallLargeLocalSearch(desIndex);
                break;   
            case 9:
                swapSmallLocalSearch(desIndex);
                break;  
            case 10:
                swapLargeLocalSearch(desIndex);
                break; 
            default:
                throw new IllegalArgumentException("Unknown heuristic ID");
        }
        //Update the best solution value
        if(memory[desIndex].getSolutionValue() < bestSolutionValue){
            bestSolutionValue = memory[desIndex].getSolutionValue();
            bestSolIndex = desIndex;
            //System.out.println("best solution updated: " + bestSolutionValue);
        }
        if(!isValid(desIndex)){
            System.out.println("Not valid");
            System.exit(4);
        }
        //The solution value is changed using delta calculation when the above 
        //heuristics are applied
        return memory[desIndex].getSolutionValue();      
    }
    
    @Override
    public double applyHeuristic(int heuristicID, int srcIndex1, int srcIndex2, int desIndex) {
        //Crossover not implemented yet. I do not want to throw an exception just do nothing
        throw new UnsupportedOperationException("Not supported yet.");
    }    

    @Override
    public String bestSolutionToString(){
        return memory[bestSolIndex].toString();
    }
        
    @Override
    public boolean compareSolutions(int solutionIndex1, int solutionIndex2) {
        return memory[solutionIndex1].equals(solutionIndex2);
    }
    
    @Override
    public void copySolution(int srcIndex, int desIndex) {
        memory[desIndex] = memory[srcIndex].copySolution();
    }
    
    @Override
    public double getBestSolutionValue() {
        return bestSolutionValue;
    }    
    
    @Override
    public double getDepthOfSearch() {
        return searchDepth;
    }
    
    @Override
    public double getFunctionValue(int solutionIndex) {
        return memory[solutionIndex].getSolutionValue(); //solutionValues[solutionIndex];
    }
    
    @Override
    public int[] getHeuristicsOfType(HeuristicType heuristicType) {
        switch(heuristicType){
            case MUTATION:
                return MU;
            case RUIN_RECREATE:
                return RR;
            case LOCAL_SEARCH:
                return LS;
            case CROSSOVER:
                return CO;
            default:
                return null;
        }
    }

    @Override
    public double getIntensityOfMutation() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    
    @Override
    public int getNumberOfHeuristics() {
        return MU.length + RR.length + LS.length;
        //CO.length is not included as no crossover is implemented yet
    }
    
    @Override
    public void loadInstance(String path) {
        File file = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            String line;
            while((line = reader.readLine()) != null){
                //Skip comments
                if(line.charAt(0) == '#') continue;
                StringTokenizer tokens = new StringTokenizer(line);
                while(tokens.hasMoreTokens()){
                    try{
                        int ownerID = Integer.parseInt(tokens.nextToken());
                        int partySize = Integer.parseInt(tokens.nextToken());
                        int choice1 = Integer.parseInt(tokens.nextToken());
                        int choice2 = Integer.parseInt(tokens.nextToken());
                        int choice3 = Integer.parseInt(tokens.nextToken());
                        OWNER_PARTY[ownerID] = partySize;
                        OWNER_CHOICES[ownerID] = new int[]{choice1, choice2, choice3};   
                        AVG_PARTY += partySize;
                    } catch(NumberFormatException ex){//do nothing, just move to the next line
                    }              
                }                
            }
            //Done reading the instance file
            
            //Compute the average family size
            AVG_PARTY /= MAX_OWNERS;            
            //Find owners with small and large families
            findSmallAndLargeFamilies();            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SkiLodge.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SkiLodge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    public void initialiseSolution(int index) {        
        SkiLodgeSolution solution = new SkiLodgeSolution();
        memory[index] = solution;
        int[] capacity = new int[MAX_WEEKS];
        int[] apartments = new int[MAX_WEEKS];        
        
        //Try to assign a week to each owner taking into account their prefernces
        for(int owner=0; owner<MAX_OWNERS; owner++){
            //If the owner booking cannot be satisifed, we return -1 to signify that
            int week = findWeekForOwner(owner, capacity, apartments);
            solution.assignWeekToOwner(owner, week, assignmentCost(owner, week));
            //If no week can be assigned to this owner, do not alter the capacity and apartments
            if(week == UNASSIGNED) continue;
            capacity[week] += OWNER_PARTY[owner];
            if(capacity[week] > MAX_CAPACITY) {
                System.err.println("Maximum capacity exceeded");
                System.exit(2);
            }
            apartments[week]++;
            if(apartments[week] > MAX_APART) {
                System.err.println("Maximum number of apartements exceeded");
                System.exit(3);
            }            
 
        }        
        
        //Try to find a week for 'losers', i.e. owners that cannot be given any week at all
        fixLosers(index, capacity, apartments);
        //Check if it is a valid solution. This is optional and may slow the 
        //performance in big problems
        if(!isValid(index)){
            System.err.println("Invalid solution");
            System.exit(4);
        }
        if(memory[index].getSolutionValue() < bestSolutionValue){
            bestSolutionValue = memory[index].getSolutionValue();
            bestSolIndex = index;
        }
    }
        
    @Override
    public boolean isValid(int solutionIndex){
        SkiLodgeSolution solution = memory[solutionIndex];
        int[] capacity = new int[MAX_WEEKS];
        int[] apartments = new int[MAX_WEEKS];
        
        for(int owner=0; owner < MAX_OWNERS; owner++){
            int week = solution.getOwnerAssignment(owner);
            if(week == UNASSIGNED) return false;
            capacity[week] += OWNER_PARTY[owner];
            apartments[week]++;
            if(capacity[week] > MAX_CAPACITY || apartments[week] > MAX_APART){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void setDepthOfSearch(double searchDepth) {
        if(searchDepth >= 0 && searchDepth <= 1){
            this.searchDepth = searchDepth;        
        }
    }
    
    @Override
    public void setIntensityOfMutation(double mutationIntensity) {
        if(mutationIntensity >= 0 && mutationIntensity <= 1){
            this.muIntensity = mutationIntensity;
        }
    }    

    @Override
    public void setMemorySize(int size) {
        if(size == memory.length) return;
        
        if(size < memory.length){
            //Free some space
            SkiLodgeSolution[] newMemory = new SkiLodgeSolution[size];
            //Copy the first elements from 0..size-1 inclusive
            for(int i=0; i < size; i++) {
                newMemory[i] = memory[i];
            }
            memory = newMemory;     
        }
        else{
            SkiLodgeSolution[] newMemory = new SkiLodgeSolution[size];
            //Copy the entire memory
            for(int i=0; i < memory.length; i++) {
                newMemory[i] = memory[i];
            }
            memory = newMemory;
        }
        
    }

    @Override
    public String solutionToString(int solutionIndex) {
        return memory[solutionIndex].toString();
    }
        
    private double assignmentCost(int owner, int week){
        int partySize = OWNER_PARTY[owner];
        if(OWNER_CHOICES[owner][0] == week) return 0;
        if(OWNER_CHOICES[owner][1] == week) return 2*partySize;
        if(OWNER_CHOICES[owner][2] == week) return 4*partySize;
        if(week == UNASSIGNED) return 1000;
        return 7*partySize + 50;
    }
        
//    private double getAssignmentDelta(int owner, int currentWeek, int newWeek){
//        double oldCost = assignmentCost(owner, currentWeek);
//        double newCost = assignmentCost(owner, newWeek); 
//        return newCost - oldCost;
//    }
    
    private int getNumberOfPeople(int solutionIndex, int week){
        SkiLodgeSolution solution = memory[solutionIndex];
        int people = 0;
        for(int ow=0; ow < MAX_OWNERS; ow++){
            if(solution.getOwnerAssignment(ow) == week){
                people += OWNER_PARTY[ow];
            }
        }
        return people;
    }
    
    private boolean canAssignWeekToOwner(SkiLodgeSolution solution, int owner, int week){
        if(solution.getOwnerAssignment(owner) == week) return false; //cannot reassign
        int people = MAX_CAPACITY;
        int apartments = MAX_APART;
        for(int ow=0; ow<MAX_OWNERS; ow++){
            if(ow == owner) continue;
            if(solution.getOwnerAssignment(ow) == week){
                people -= OWNER_PARTY[ow];
                apartments--;
            }
        }
        return apartments >= 1 && people >= OWNER_PARTY[owner];
    }
    
    private boolean canSwapOwners(int solutionIndex, int owner1, int owner2){
        if(owner1 == owner2) return false;
        int week1 = memory[solutionIndex].getOwnerAssignment(owner1);
        int week2 = memory[solutionIndex].getOwnerAssignment(owner2);
        if(week1 == week2 || week1 == UNASSIGNED || week2 == UNASSIGNED) {
            return false;
        }
        int people1 = getNumberOfPeople(solutionIndex, week1);
        int people2 = getNumberOfPeople(solutionIndex, week2);
        int party1 = OWNER_PARTY[owner1];
        int party2 = OWNER_PARTY[owner2];        
        int week1Space = MAX_CAPACITY - (people1 - party1); 
        int week2Space = MAX_CAPACITY - (people2 - party2);   
        return week1Space >= party2 && week2Space >= party1;
    }
    
    private int findWeekForOwner(int owner, int[] capacity, int[] apartments){
        int choice1 = OWNER_CHOICES[owner][0];
        int choice2 = OWNER_CHOICES[owner][1];
        int choice3 = OWNER_CHOICES[owner][2];
        int partySize = OWNER_PARTY[owner];
        if(MAX_CAPACITY - capacity[choice1] >= partySize && apartments[choice1] < MAX_APART){
            return choice1;
        }
        else if(MAX_CAPACITY - capacity[choice2] >= partySize && apartments[choice2] < MAX_APART){
            return choice2;
        }
        else if(MAX_CAPACITY - capacity[choice3] >= partySize && apartments[choice3] < MAX_APART){
            return choice3;
        }           
        else{
            int bestWeek = UNASSIGNED;
            int leftOver = MAX_CAPACITY;
            for(int week=0; week < 16; week++){
                if(MAX_CAPACITY - capacity[week] >= partySize && apartments[week] < MAX_APART){
                    if(MAX_CAPACITY - (capacity[week] + partySize) < leftOver){
                        bestWeek = week;
                        leftOver = MAX_CAPACITY - (capacity[week] + partySize);
                    }
                }
            }
            //Cannot find a week for the current owner, return -1 to indicate that
            return bestWeek;
        }
        
    }
    
    private void fixLosers(int solutionIndex, int[] capacity, int[] apartments){
        SkiLodgeSolution solution = memory[solutionIndex];

        //find owners that were not assigned to any week
        Set<Integer> losers = new HashSet<>();
        for(int owner=0; owner < MAX_OWNERS; owner++){
            if(solution.getOwnerAssignment(owner) == UNASSIGNED) losers.add(owner);
        }
        
        //Try to find a week for each loser by swapping owners to create a room for the losers
        while(losers.size() > 0){
            int owner1 = rng.nextInt(MAX_OWNERS);
            int owner2 = rng.nextInt(MAX_OWNERS);
            while(losers.contains(owner1)){
                owner1 = rng.nextInt(MAX_OWNERS);
            }
            while(losers.contains(owner2) || owner1 == owner2){
                owner2 = rng.nextInt(MAX_OWNERS);
            }
            
            int week1 = solution.getOwnerAssignment(owner1);
            int week2 = solution.getOwnerAssignment(owner2);
            
            //Swapping owners in the same week will not lead to anything
            if(week1 == week2) continue;
            
            //Check if at least there is a vacant apartment in either of the two weeks
            if(apartments[week1] == MAX_APART && apartments[week2] == MAX_APART) continue;
            
            int party1 = OWNER_PARTY[owner1];
            int party2 = OWNER_PARTY[owner2];
            
            //check if we can swap these two owners
            int leftOver1 = MAX_CAPACITY - (capacity[week1] + party2 - party1); 
            int leftOver2 = MAX_CAPACITY - (capacity[week2] + party1 - party2);
            //If can swap the two owners, do it
            if(leftOver1 >= 0 && leftOver2 >= 0){
                //Swap
                solution.assignWeekToOwner(owner1, week2, assignmentCost(owner1, week2));
                solution.assignWeekToOwner(owner2, week1, assignmentCost(owner2, week1));  
                capacity[week1] += party2 - party1;
                capacity[week2] += party1 - party2;
                
                //check if we can acccommodate any of the 'losers'
                //Use iterators if the set is modified in-place
                Iterator<Integer> itr = losers.iterator();
                while(itr.hasNext()){
                    int loser = itr.next();
                    //Check whether we can fit this loser in week1
                    if(OWNER_PARTY[loser] <= leftOver1 && apartments[week1] < MAX_APART){
                        solution.assignWeekToOwner(loser, week1, assignmentCost(loser, week1));
                        capacity[week1] += OWNER_PARTY[loser];
                        apartments[week1]++;
                        if(capacity[week1] > MAX_CAPACITY){
                            System.err.println("Maximum capacity is exceeded");
                            System.exit(2);
                        }
                        //Remove the current loser
                        itr.remove();
                        break;
                    }
                    //Check whether we can fit this loser in week2
                    else if(OWNER_PARTY[loser] <= leftOver2 && apartments[week2] < MAX_APART){
                        solution.assignWeekToOwner(loser, week2, assignmentCost(loser, week2));                   
                        capacity[week2] += OWNER_PARTY[loser];
                        apartments[week2]++;
                        if(capacity[week2] > MAX_CAPACITY){
                            System.err.println("Maximum capacity is exceeded");
                            System.exit(2);
                        }                        
                        itr.remove();
                        break;
                    }
                }
            }
        }   
        
    }                
    
    private boolean swapTwoOwners(int solutionIndex, int owner1, int owner2){
        
        if(canSwapOwners(solutionIndex, owner1, owner2)){
            SkiLodgeSolution solution = memory[solutionIndex];
            int week1 = solution.getOwnerAssignment(owner1);
            int week2 = solution.getOwnerAssignment(owner2);
            double cost1 = assignmentCost(owner1, week2);
            solution.assignWeekToOwner(owner1, week2, cost1);
            double cost2 = assignmentCost(owner2, week1);
            solution.assignWeekToOwner(owner2, week1, cost2);            
            return true;
        }  
        return false;
    }
    
    private void swapTwoOwnersAtRandom(int solutionIndex){
        int owner1 = rng.nextInt(MAX_OWNERS);
        int owner2 = rng.nextInt(MAX_OWNERS);
        while(owner1 == owner2) owner2 = rng.nextInt(MAX_OWNERS);
        swapTwoOwners(solutionIndex, owner1, owner2);
    }
    
    private boolean swapTwoOwnersIfBetter(int solutionIndex, int owner1, int owner2){
        SkiLodgeSolution solution = memory[solutionIndex];
        int week1 = solution.getOwnerAssignment(owner1);
        int week2 = solution.getOwnerAssignment(owner2);
        double oldCost = solution.getOwnerCompensation(owner1) + solution.getOwnerCompensation(owner2);
        double newCost = assignmentCost(owner1, week2) + assignmentCost(owner2, week1);
        if(newCost < oldCost){
            return swapTwoOwners(solutionIndex, owner1, owner2);
        }
        return false;
    }

    private void swapSmallLarge(int solutionIndex){
        int smallOwner = smallFamilies.get(rng.nextInt(smallFamilies.size()));
        int largeOwner = largeFamilies.get(rng.nextInt(largeFamilies.size()));
        swapTwoOwners(solutionIndex, smallOwner, largeOwner);
    }
    
    private void swapSmall(int solutionIndex){
        int owner1 = smallFamilies.get(rng.nextInt(smallFamilies.size()));
        int owner2 = smallFamilies.get(rng.nextInt(smallFamilies.size()));
        while(owner1 == owner2){
            owner2 = smallFamilies.get(rng.nextInt(smallFamilies.size()));
        }
        swapTwoOwners(solutionIndex, owner1, owner2);
    }    
        
    private void swapLarge(int solutionIndex){
        int owner1 = largeFamilies.get(rng.nextInt(largeFamilies.size()));
        int owner2 = largeFamilies.get(rng.nextInt(largeFamilies.size()));
        while(owner1 == owner2){
            owner2 = largeFamilies.get(rng.nextInt(largeFamilies.size()));
        }
        swapTwoOwners(solutionIndex, owner1, owner2);
    }    
    
    private void swapLocalSearch(int solutionIndex){
        
        SkiLodgeSolution solution = memory[solutionIndex];
        int steps = (int)(searchDepth*solution.size() + 0.5)/2; //each step involves two owners
        Set<Integer> seen = new HashSet<>(2*steps);
        for(int i=0; i < steps; i++){           
            int owner1 = rng.nextInt(MAX_OWNERS);  
            while(seen.contains(owner1)) owner1 = rng.nextInt(MAX_OWNERS);            
            int owner2 = rng.nextInt(MAX_OWNERS);  
            while(seen.contains(owner2) || owner1 == owner2) owner2 = rng.nextInt(MAX_OWNERS);
            swapTwoOwnersIfBetter(solutionIndex, owner1, owner2);
        }
    }
    
    private void swapSmallLargeLocalSearch(int solutionIndex){        
        SkiLodgeSolution solution = memory[solutionIndex];
        int steps = (int)(searchDepth*solution.size() + 0.5)/2; //each step involves two owners
        Set<Integer> seen = new HashSet<>(2*steps);
        for(int i=0; i < steps; i++){           
            int owner1 = smallFamilies.get(rng.nextInt(smallFamilies.size()));            
            while(seen.contains(owner1)){
                owner1 = smallFamilies.get(rng.nextInt(smallFamilies.size()));
            }       
            int owner2 = largeFamilies.get(rng.nextInt(largeFamilies.size()));
            while(seen.contains(owner2)) {
                owner2 = largeFamilies.get(rng.nextInt(largeFamilies.size()));
            }
            swapTwoOwnersIfBetter(solutionIndex, owner1, owner2);
        }
    }    
    
    private void swapSmallLocalSearch(int solutionIndex){        
        SkiLodgeSolution solution = memory[solutionIndex];
        int steps = (int)(searchDepth*solution.size() + 0.5)/2; //each step involves two owners
        Set<Integer> seen = new HashSet<>(2*steps);
        for(int i=0; i < steps; i++){           
            int owner1 = smallFamilies.get(rng.nextInt(smallFamilies.size()));            
            while(seen.contains(owner1)){
                owner1 = smallFamilies.get(rng.nextInt(smallFamilies.size()));
            }       
            int owner2 = smallFamilies.get(rng.nextInt(smallFamilies.size()));
            while(seen.contains(owner2) || owner1 == owner2) {
                owner2 = smallFamilies.get(rng.nextInt(smallFamilies.size()));
            }
            swapTwoOwnersIfBetter(solutionIndex, owner1, owner2);
        }
    }    
        
    private void swapLargeLocalSearch(int solutionIndex){        
        SkiLodgeSolution solution = memory[solutionIndex];
        int steps = (int)(searchDepth*solution.size() + 0.5)/2; //each step involves two owners
        Set<Integer> seen = new HashSet<>(2*steps);
        for(int i=0; i < steps; i++){           
            int owner1 = largeFamilies.get(rng.nextInt(largeFamilies.size()));            
            while(seen.contains(owner1)){
                owner1 = largeFamilies.get(rng.nextInt(largeFamilies.size()));
            }       
            int owner2 = largeFamilies.get(rng.nextInt(largeFamilies.size()));
            while(seen.contains(owner2)) {
                owner2 = largeFamilies.get(rng.nextInt(largeFamilies.size()));
            }
            swapTwoOwnersIfBetter(solutionIndex, owner1, owner2);
        }
    }
    
    private void swapWorstOwner(int solutionIndex){
        SkiLodgeSolution solution = memory[solutionIndex];
        int worstOwner = solution.getWorstOwner();
        int choice1 = OWNER_CHOICES[worstOwner][0];
        int choice2 = OWNER_CHOICES[worstOwner][1];
        int choice3 = OWNER_CHOICES[worstOwner][2];
        List<Integer> week1Owners = solution.getOwnersAssignedToWeek(choice1);
        List<Integer> week2Owners = solution.getOwnersAssignedToWeek(choice2);
        List<Integer> week3Owners = solution.getOwnersAssignedToWeek(choice3);
        
        List<Integer> targetOwners = new ArrayList<>(week1Owners);
        targetOwners.addAll(week2Owners);
        targetOwners.addAll(week3Owners);
        Set<Integer> tried = new HashSet<>(targetOwners.size());
        while(tried.size() < targetOwners.size()){
            int owner = targetOwners.get(rng.nextInt(targetOwners.size()));
            while(tried.contains(owner)){
                owner = targetOwners.get(rng.nextInt(targetOwners.size()));
            }
            if(swapTwoOwners(solutionIndex, worstOwner, owner)){
                return; 
            }
            else{
                tried.add(owner);
            }
        }
    }
    
    private void swapWorstOwnerToBestWeek(int solutionIndex){
        swapTwoOwnersAtRandom(solutionIndex);
        SkiLodgeSolution solution = memory[solutionIndex];
        int worstOwner = solution.getWorstOwner();
        int choice1 = OWNER_CHOICES[worstOwner][0];
        int choice2 = OWNER_CHOICES[worstOwner][1];
        int choice3 = OWNER_CHOICES[worstOwner][2];
        List<Integer> week1Owners = solution.getOwnersAssignedToWeek(choice1);
        List<Integer> week2Owners = solution.getOwnersAssignedToWeek(choice2);
        List<Integer> week3Owners = solution.getOwnersAssignedToWeek(choice3);
        
        int bestOwner = UNASSIGNED;
        double bestCost = Double.MAX_VALUE;
        double cost;
        for(int owner : week1Owners){//consider choice1
            //If can swap the two owners
            if(canSwapOwners(solutionIndex, worstOwner, owner)){
                cost = assignmentCost(worstOwner, choice1) 
                        + assignmentCost(owner, solution.getOwnerAssignment(worstOwner));
                if(cost < bestCost){
                    bestOwner = owner;
                    bestCost = cost;
                }
            }
        }        
        //Cannot give the first choice to worstOwner, check if can be given the second choice
        for(int owner : week2Owners){//consider choice2
            //If can swap the two owners
            if(canSwapOwners(solutionIndex, worstOwner, owner)){
                cost = assignmentCost(worstOwner, choice2) 
                        + assignmentCost(owner, solution.getOwnerAssignment(worstOwner));
                if(cost < bestCost){
                    bestOwner = owner;
                    bestCost = cost;
                }
            }
        }        
        //Cannot give either of the first and second choice to worstOwner, 
        //check if can be given the third choice
        for(int owner : week3Owners){//consider choice3
            //If can swap the two owners
            if(canSwapOwners(solutionIndex, worstOwner, owner)){
                cost = assignmentCost(worstOwner, choice3) 
                        + assignmentCost(owner, solution.getOwnerAssignment(worstOwner));
                if(cost < bestCost){
                    bestOwner = owner;
                    bestCost = cost;
                }
            }
        }        
        //Do the assignment
        if(bestOwner != UNASSIGNED) {
            swapTwoOwners(solutionIndex, worstOwner, bestOwner);
        }
    }    
    
    private void ruinAndRecreate(int solutionIndex){
        SkiLodgeSolution solution = memory[solutionIndex];
        int[] capacity = new int[MAX_WEEKS];
        int[] apartments = new int[MAX_WEEKS];
        
        for(int ow=0; ow < MAX_OWNERS; ow++){
            int week = solution.getOwnerAssignment(ow);
            capacity[week] += OWNER_PARTY[ow];
            apartments[week]++;
        }
        
        int fr = (int)(muIntensity*solution.size() + 0.5);
        List<Integer> seen = new ArrayList<>(fr);
        for(int i=0; i < fr; i++){
            int owner = rng.nextInt(MAX_OWNERS);
            while(seen.contains(owner)) owner = rng.nextInt(MAX_OWNERS);
            int week = solution.getOwnerAssignment(owner);
            capacity[week] -= OWNER_PARTY[owner];
            apartments[week]--;
            //Unassign the owner
            solution.assignWeekToOwner(owner, UNASSIGNED, assignmentCost(owner, UNASSIGNED));
            seen.add(owner);            
        }
        
        //Shuffle the list at random
        Collections.shuffle(seen, rng);
        
        for(int owner : seen){
            int week = findWeekForOwner(owner, capacity, apartments);
            solution.assignWeekToOwner(owner, week, assignmentCost(owner, week));
            if(week == UNASSIGNED) continue;
            capacity[week] += OWNER_PARTY[owner];
            apartments[week]++;            
        }
        
        //Accommodate losers
        fixLosers(solutionIndex, capacity, apartments);
    }

    private void findSmallAndLargeFamilies(){
        smallFamilies = new ArrayList<>(MAX_OWNERS);
        largeFamilies = new ArrayList<>(MAX_OWNERS);
        for(int ow=0; ow < MAX_OWNERS; ow++){
            if(OWNER_PARTY[ow] <= AVG_PARTY){
                smallFamilies.add(ow);
            }
            else{
                largeFamilies.add(ow);
            }
        }
    }    
}
