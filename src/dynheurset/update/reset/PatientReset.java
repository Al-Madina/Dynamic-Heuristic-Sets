/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynheurset.update.reset;

/**
 *
 * @author User
 */
public class PatientReset extends Reset{
    
    private final double patience;

    public PatientReset(double patience) {
        this.patience = patience;
    }
        

    @Override
    public boolean canReset() {
        if(runStat.waitReset > patience*runStat.maxWait){
            //Reset the count to start over
            runStat.waitReset = 0;
            return true;
        }
        return false;
    }
}
