package processing;

import slrt.OpticalModel;
import java.util.Random;

public class Brain implements Runnable {
    
    private final Object lockObject = new Object();

    private OpticalModel parentOpticalModel;
    private boolean changed;
    private int result;
    

    public Brain(OpticalModel om) {
        this.parentOpticalModel = om;
        
        this.changed = false;
        this.result = -1;        
    }

    private void notifyOpticalModel() {
        this.parentOpticalModel.setNewResultFromBrain(true);
    }

    public int getResult() {
        synchronized (this.lockObject) {
            this.changed = false;
        }
        return this.result;
    }

    @Override
    public void run() {
        Random r = new Random();
        int currentResult = this.result;

        /*
         * This is where the Algorithm method will be called (possibly other classes involded).
         * For now, instead a simple sleep will be called for simulation purposes.
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        currentResult = r.nextInt(100);
        if (currentResult != this.result) {
            synchronized (this.lockObject){
                this.changed = true;
            }
            this.notifyOpticalModel();
        }        
    }    
}
