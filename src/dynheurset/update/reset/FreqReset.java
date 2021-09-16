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
public class FreqReset extends Reset{
    
    protected double freq;
    private int incr;
    
    public FreqReset(double freq){
        this.freq = freq;
        incr = 1;
    }

    @Override
    public boolean canReset() {
        if(runStat.elpTime/runStat.maxTime > incr*freq){
            incr++;
            return true;
        }
        return false;        
    }
    
}
