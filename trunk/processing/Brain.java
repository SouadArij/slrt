package processing;

import slrt.OpticalModel;
import java.util.Random;

public class Brain implements Runnable {
    
    private final Object lockObject = new Object();

    private OpticalModel parentOpticalModel;
    private boolean capturedImageChanged;
    private int result;
    

    public Brain(OpticalModel om) {
        this.parentOpticalModel = om;
        
        this.capturedImageChanged = false;
        this.result = -1;        
    }

    /*
     * This is called from the sibling Eye.
     * Whenever a new image was captured by the cam, this is set.
     * It means that at the next iteration, the brain _will have to_
     * process a new image. That then can be get from the eye.
     */
    public void setCapturedImageChanged() {
        synchronized (this.lockObject) {
            this.capturedImageChanged = true;
        }
    }

    public int getResult() {
        return this.result;
    }
    
 
    @Override
    public void run() {
        Random r = new Random();
        int currentResult = this.result;

        while (true) {

            if (this.capturedImageChanged) {
                synchronized (this.lockObject) {
                    this.capturedImageChanged = false;
                }


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
            }
            
            if (currentResult != this.result) {
                this.result = currentResult;
                this.parentOpticalModel.setBrainResultChanged();
            }

            /*
             * This is the normal sleep that relaxes the CPU a bit and
             * lets other threads process fluently.
             */
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }    
}
