package dynheurset.update.remove;

/**
 *
 * @author User
 */
public class NoRemove extends Remove{

    @Override
    public boolean canRemove() {
        return false;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
