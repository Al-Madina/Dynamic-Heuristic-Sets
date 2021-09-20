package dynheurset.update.remove;

/**
 * A Conservative removal strategy.
 * <p>
 * This removal strategy removes heuristics that perform lower than the average 
 * performance reduced by a factor. A heuristic performance is measured "absolutely" 
 * without considering other heuristics as well as "relatively" by considering
 * other heuristics.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class ConservativeRemoval extends GroupRemoval{
    
    public ConservativeRemoval(double ratio, double aspiration,
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
            ratio = 2; //any number sufficiently greator than 1 will do            
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
        double indMean = util.mean(indPerf);
        
        //Get the relative performance of each heuristic. 
        //`groupPerf[idx]` is the relative performance for the heuristic at index
        //`idx` in the universal set. 
        double[] groupPerf = getGroupPerf();
        double gpMean = util.mean(groupPerf);
        //Needs to handle the aspiration of the individual and group performance 
        //since of these may contain negative values only
        double indAsp = aspiration;
        double gpAsp = aspiration;
        //If the mean is negative, we need to adjust `aspiration` if all values
        //are negative
        if(indMean < 0){
            boolean allNegatives = true;
            for(int idx=0; idx < indPerf.length; idx++){
                if(indPerf[idx] >= 0){
                    allNegatives = false;
                    break;
                }
            }
            //If all values are negative, adjust `aspiration` to avoid all the
            //possible case of removing all heuristics
            if(allNegatives) indAsp = 1/aspiration;
        }
        //Do the same for the group performance
        if(gpMean < 0){
            boolean allNegatives = true;
            for(int idx=0; idx < groupPerf.length; idx++){
                if(groupPerf[idx] >= 0){
                    allNegatives = false;
                    break;
                }
            }
            //If all values are negative, adjust `aspiration` to avoid all the
            //possible case of removing all heuristics
            if(allNegatives) gpAsp = 1/aspiration;
        }
        //Remove heuristics with performance lower than the mean reduced by a factor 
        for(int idx=0; idx < groupPerf.length; idx++){
            if(indPerf[idx] < indAsp*indMean && groupPerf[idx] < gpAsp*gpMean){
                removedHeurSet.add(idx);
            }
        }
    }
}
