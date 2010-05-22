package processing;



import slrt.OpticalModel;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Brain implements Runnable {

    private boolean changed;
    private OpticalModel parentOpticalModel;
    private int result;
    

    public Brain(OpticalModel om) {
        this.parentOpticalModel = om;

        this.result = -1;        
    }

    @Override
    public void run() {
        Random r = new Random();

        try {
            //this is where the Algorithm method will be called (possibly other classes involded)            
            Thread.sleep(500);
            
            this.result = r.nextInt(100);
            this.setChanged(true);
            this.notifyOpticalModel();
            this.setChanged(false);
        } catch (InterruptedException ex) {
            Logger.getLogger(Brain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /*after processing is done, the OpticalModel is notified that image is ready*/

    public void notifyOpticalModel() {
        parentOpticalModel.setNewResultFromBrain(true);
    }

    public void setChanged(boolean chng) {
        this.changed = chng;
    }

    public String getResult() {
        return this.result;
    }
}
