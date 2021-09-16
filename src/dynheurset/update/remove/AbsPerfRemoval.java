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
        if(removedHeurSet.size() < heurList.size()-1 
                && runStat.elpTime/runStat.maxTime > ratio){
            //Remove heuristics only ONCE during the lifespan of the hyper-heuristic
            ratio = 2; //any number greator than 1 will do
            return true;
        } 
        return false;
    }

    @Override
    public void remove() {       
        //Get the absolute performance of each heuristic. 
        //`indPerf[idx]` is the absolute performance for the heuristic at index
        //`idx` in the universal set. 
        double[] indPerf = getIndPerf();
        double mean = util.mean(indPerf);
        //Remove heuristics with performance lower than the mean reduced by a factor        
        for(int idx=0; idx < indPerf.length; idx++){
            if(indPerf[idx] < aspiration*mean) removedHeurSet.add(idx);
        }       
    }
   
    
}
