package runner.examples;

import dynheurset.DynHeurSet;
import dynheurset.RunStat;
import dynheurset.measure.DisimprDurMeasure;
import dynheurset.measure.DisimprFreqDurMeasure;
import dynheurset.measure.DisimprFreqMeasure;
import dynheurset.measure.DisimprMeasure;
import dynheurset.measure.ImprDurMeasure;
import dynheurset.measure.ImprFreqDurMeasure;
import dynheurset.measure.ImprFreqMeasure;
import dynheurset.measure.ImprMeasure;
import dynheurset.measure.Measure;
import dynheurset.measure.PerfDurMeasure;
import dynheurset.measure.PerfFreqDurMeasure;
import dynheurset.measure.PerfFreqMeasure;
import dynheurset.measure.PerfMeasure;
import dynheurset.measure.WtdBestPerfDurMeasure;
import dynheurset.measure.WtdBestPerfFreqDurMeasure;
import dynheurset.measure.WtdBestPerfFreqMeasure;
import dynheurset.measure.WtdBestPerfMeasure;
import dynheurset.measure.WtdBestRunbestPerfDurMeasure;
import dynheurset.measure.WtdBestRunbestPerfFreqDurMeasure;
import dynheurset.measure.WtdBestRunbestPerfFreqMeasure;
import dynheurset.measure.WtdBestRunbestPerfMeasure;
import dynheurset.measure.WtdPerfDurMeasure;
import dynheurset.measure.WtdPerfFreqDurMeasure;
import dynheurset.measure.WtdPerfFreqMeasure;
import dynheurset.measure.WtdPerfMeasure;
import dynheurset.update.PatientPhaseDominanceUpdate;
import dynheurset.update.PatientPhaseFairGreedyUpdate;
import dynheurset.update.PatientPhaseGreedyUpdate;
import dynheurset.update.PatientPhaseQualityIndexUpdate;
import dynheurset.update.PhaseDominanceUpdate;
import dynheurset.update.PhaseFairGreedyUpdate;
import dynheurset.update.PhaseGreedyUpdate;
import dynheurset.update.PhaseQualityIndexUpdate;
import dynheurset.update.Update;
import dynheurset.update.remove.AbsPerfRemoval;
import dynheurset.update.remove.ConservativeRemoval;
import dynheurset.update.remove.FreqWorstRemoval;
import dynheurset.update.remove.NoRemove;
import dynheurset.update.remove.PatientWorstRemoval;
import dynheurset.update.remove.RelPerfRemoval;
import dynheurset.update.remove.Remove;
import dynheurset.update.reset.FreqReset;
import dynheurset.update.reset.NoReset;
import dynheurset.update.reset.PatientReset;
import dynheurset.update.reset.Reset;

/**
 *
 * @author Ahmed Hassan
 */
public class HyFlexTestRunner extends HyFlexRunner{

    public HyFlexTestRunner(long seed, long timeLimit) {
        super(seed, timeLimit);
    }
    
    @Override
    protected DynHeurSet createDynHeurSet() {
        RunStat runStat = new RunStat();
        DynHeurSet dynSet = new DynHeurSet();
        dynSet.setRunStat(runStat);      
        
        //Create an update strategy
        Update update = createUpdate();   
        update.setRunStat(runStat);
        Measure measure = createMeasure();
        update.setMeasure(measure);
        Remove remove = createRemove();     
        update.setRemove(remove);        
        Reset reset = createReset();
        update.setReset(reset);
        
        //Now, our update strategy is all set, load it into the dynamic set
        dynSet.setUpdate(update);
        //To see the dynamic set, uncomment the following lines
        /*System.out.println(update.getClass().getSimpleName());
        System.out.println(remove.getClass().getSimpleName());
        System.out.println(reset.getClass().getSimpleName());
        System.out.println(measure.getClass().getSimpleName());*/
        return dynSet;
    }    
    
    private Update createUpdate(){
        int phase = 1 + rng.nextInt(1000);
        double patience = 0.1 + 10*rng.nextDouble();
        double asp = rng.nextDouble();
        int top = 1 + rng.nextInt(10);
        int max = 1 + rng.nextInt(10);
        
        int choice = rng.nextInt(8);
        switch(choice){
            case 0:                
                return new PhaseDominanceUpdate(phase);
            case 1:
                return new PhaseQualityIndexUpdate(phase, asp);
            case 2:
                return new PhaseGreedyUpdate(phase, top);
            case 3:
                return new PhaseFairGreedyUpdate(max, phase, top);
            case 4:
                return new PatientPhaseDominanceUpdate(patience, phase);
            case 5:
                return new PatientPhaseQualityIndexUpdate(patience, phase, asp);
            case 6:
                return new PatientPhaseGreedyUpdate(patience, phase, top);
            case 7:
                return new PatientPhaseFairGreedyUpdate(patience, max, phase, top);
        }
        return null;
    }
    
    private Remove createRemove(){        
        double patience = 0.1 + 10*rng.nextDouble();
        double freq = 0.01 + rng.nextDouble();
        double ratio = 0.05 + rng.nextDouble();
        double asp = rng.nextDouble();
        double a1 = rng.nextDouble();
        double a2 = rng.nextDouble();
        double a3 = rng.nextDouble();
        double a4 = rng.nextDouble();
        double b1 = rng.nextDouble();
        double b2 = rng.nextDouble();
        double b3 = rng.nextDouble();
        double b4 = rng.nextDouble();
        
        int choice = rng.nextInt(6);
        switch(choice){
            case 0:
                return new NoRemove();
            case 1:
                return new PatientWorstRemoval(patience);
            case 2:
                return new FreqWorstRemoval(freq);
            case 3:
                return new AbsPerfRemoval(ratio, asp, a1, a2, a3, a4, b1, b2, b3, b4);
            case 4:
                return new RelPerfRemoval(ratio, asp, a1, a2, a3, a4, b1, b2, b3, b4);
            case 5:
                return new ConservativeRemoval(ratio, asp, a1, a2, a3, a4, b1, b2, b3, b4);
        }                
        return null;
    }
    
    private Reset createReset(){        
        double patience = 0.1 + 10*rng.nextDouble();
        double freq = rng.nextDouble();
        
        int choice = rng.nextInt(3);
        switch(choice){
            case 0:
                return new NoReset();
            case 1:
                return new PatientReset(patience);
            case 2:
                return new FreqReset(freq);
        }
        return null;
    }
    
    private Measure createMeasure(){
        double w1 = rng.nextDouble();
        double w2 = rng.nextDouble();
        double w3 = rng.nextDouble();
        double w4 = rng.nextDouble();
        
        int choice = rng.nextInt(24);
        switch(choice){
            case 0:
                return new DisimprDurMeasure();   
            case 1:
                return new DisimprFreqDurMeasure();  
            case 2:
                return new DisimprFreqMeasure();
            case 3:
                return new DisimprMeasure();
            case 4:
                return new ImprDurMeasure();
            case 5:
                return new ImprFreqDurMeasure();
            case 6:
                return new ImprFreqMeasure();
            case 7:
                return new ImprMeasure();
            case 8:
                return new PerfDurMeasure();
            case 9:
                return new PerfFreqDurMeasure();
            case 10:
                return new PerfFreqMeasure();
            case 11:
                return new PerfMeasure();
            case 12:
                return new WtdBestPerfDurMeasure(w1, w2, w3);
            case 13:
                return new WtdBestPerfFreqDurMeasure(w1, w2, w3);
            case 14:
                return new WtdBestPerfFreqMeasure(w1, w2, w3);
            case 15:
                return new WtdBestPerfMeasure(w1, w2, w3);
            case 16:
                return new WtdBestRunbestPerfDurMeasure(w1, w2, w3, w4);
            case 17:
                return new WtdBestRunbestPerfFreqDurMeasure(w1, w2, w3, w4);
            case 18:
                return new WtdBestRunbestPerfFreqMeasure(w1, w2, w3, w4);
            case 19:
                return new WtdBestRunbestPerfMeasure(w1, w2, w3, w4);
            case 20:
                return new WtdPerfDurMeasure(w1, w2);
            case 21:
                return new WtdPerfFreqDurMeasure(w1, w2);
            case 22:
                return new WtdPerfFreqMeasure(w1, w2);
            case 23:
                return new WtdPerfMeasure(w1, w2);
        }
        return null;
    }
    
}
