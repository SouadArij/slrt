package processing;



import slrt.OpticalModel;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nick
 */
public class Brain implements Runnable {

    private boolean changed;
    private OpticalModel parentOpticalModel;
    private String result="";
    private Random r = new Random();

    public Brain(OpticalModel om) {
        this.parentOpticalModel = om;
    }

    @Override
    public void run() {

        try {
            //this is where the Algorithm method will be called (possibly other classes involded)
            //this.wait(30);
            Thread.sleep(30);
            
            this.result = Long.toString(Math.abs(r.nextLong()), 36);
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
