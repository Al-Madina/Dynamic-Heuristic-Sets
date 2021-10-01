package dynheurset.update.remove;

/**
 * Absolute performance removal strategy.
 * <p>
 * This removal strategy removes heuristics that perform lower than the average 
 * performance reduced by a factor. A heuristic performance is measured "absolutely" 
 * without considering other heuristics. For instance, the ratio between the 
 * percentage improvement and percentage disimprovement that the heuristic achieves
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class AbsPerfRemoval extends GroupRemoval{
    
    public AbsPerfRemoval(double ratio, double aspiration,
            double alpha1, double alpha2, double alpha3, double alpha4, 
            double beta1, double beta2, double beta3, double beta4)
    {
        super(ratio, aspiration, alpha1, alpha2, alpha3, alpha3, beta1, beta2, beta3, beta4);
    }
    
    @Override
    public boolean canRemove() {
        //Perform the removal only if ratio% of the computational time was used up
        //and do that once
        return removedHeurSet.isEmpty() && runStat.elpTime/runStat.maxTime > ratio;
    }

    @Override
    public void remove() {       
        //Get the absolute performance of each heuristic. 
        //`indPerf[idx]` is the absolute performance for the heuristic at index
        //`idx` in the universal set. 
        double[] indPerf = getIndPerf();
        double mean = util.mean(indPerf);
        //If the mean is negative, we need to adjust `aspiration` if all values
        //are negative
        if(mean < 0){
            boolean allNegatives = true;
            for(int idx=0; idx < indPerf.length; idx++){
                if(indPerf[idx] >= 0){
                    allNegatives = false;
                    break;
                }
            }
            //If all values are negative, adjust `aspiration` to avoid all the
            //possible case of removing all heuristics
            if(allNegatives) aspiration = 1/aspiration;
        }
        //Remove heuristics with performance lower than the mean reduced by a factor        
        for(int idx=0; idx < indPerf.length; idx++){
            if(indPerf[idx] < aspiration*mean) removedHeurSet.add(idx);
        }            
        //If aspiration is adjusted, reverts back to its original value
        if(aspiration > 1) aspiration = 1/aspiration;
    }
   
    
}
