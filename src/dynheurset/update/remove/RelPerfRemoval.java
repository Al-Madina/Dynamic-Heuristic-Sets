package dynheurset.update.remove;

/**
 * Relative performance removal strategy.
 * <p>
 * This removal strategy removes heuristics that perform lower than the average 
 * performance reduced by a factor. A heuristic performance is measured relative to other heuristics.
 * For instance, how much improvement a heuristic contributed to the total 
 * improvement achieved by all heuristics.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class RelPerfRemoval extends GroupRemoval{
    
    public RelPerfRemoval(double ratio, double aspiration,
            double alpha1, double alpha2, double alpha3, double alpha4, 
            double beta1, double beta2, double beta3, double beta4)
    {
        super(ratio, aspiration, alpha1, alpha2, alpha3, alpha3, beta1, beta2, beta3, beta4);
    }
    
    @Override
    public boolean canRemove() {
        //Perform the removal only if ratio% of the computational time was used up
        if(runStat.elpTime/runStat.maxTime > ratio){
            //Remove heuristics only ONCE during the lifespan of the hyper-heuristic          
            ratio = 2; //any number sufficiently larger than 1 will do            
            return true;
        } 
        return false;
    }

    @Override
    public void remove() {
        //Cannot remove heuristics anymore if there is only one heuristic left
        if(removedHeurSet.size() == heurList.size()-1) return;
        
        //Get the relative performance of each heuristic. 
        //`groupPerf[idx]` is the relative performance for the heuristic at index
        //`idx` in the universal set. 
        double[] groupPerf = getGroupPerf();
        double mean = util.mean(groupPerf);
        //Remove heuristics with performance lower than the mean reduced by a factor
        for(int idx=0; idx < groupPerf.length; idx++){
            if(groupPerf[idx] < aspiration*mean) removedHeurSet.add(idx);
        }
    }    
}
