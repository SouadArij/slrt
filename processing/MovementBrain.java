/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import slrt.OpticalModel;

/**
 *
 * @author Trishk
 */
public class MovementBrain implements Runnable {

    OpticalModel parentOpticalModel;
    private BufferedImage previousImageForDetection;
    private BufferedImage currentImageForDetection;
    private int threshold_diff = 40000;
    private int nrRequiredFrames = 5;
    private int btnPlay_x = 295;
    private int btnPlay_y = 25;
    private int radius = 20;
    private int actionPlay;
    private boolean play;
    private boolean buttonStaticActionDetected;
    private boolean buttonDynamicActionDetected;
    private int indexOfButtonPressed;
    private boolean newImage;

    public MovementBrain(OpticalModel om) {
        this.parentOpticalModel = om;
        this.previousImageForDetection = this.parentOpticalModel.getCurrentImage();
        this.currentImageForDetection = this.parentOpticalModel.getCurrentImage();
        this.newImage = false;
        this.indexOfButtonPressed = 0;
    }

    private void checkButtons() {

        int y, x;
        //System.out.println("previousImageForDetection.getWidth():" + this.previousImageForDetection.getWidth());
        //System.out.println("previousImageForDetection.getHeight():" + this.previousImageForDetection.getHeight());
        //System.out.println("currentImageForDetection.getWidth():" + this.currentImageForDetection.getWidth());
        //System.out.println("currentImageForDetection.getHeight():" + this.currentImageForDetection.getHeight());
        if (this.previousImageForDetection.getWidth() > 1 && this.currentImageForDetection.getWidth() > 1) {
            int btnStart_diff = 0;
            for (y = btnPlay_y - radius; y < btnPlay_y + radius; y++) {
                for (x = btnPlay_x - radius; x < btnPlay_x + radius; x++) {
                    int colorA = (new Color(currentImageForDetection.getRGB(x, y))).getRed();
                    int colorB = (new Color(previousImageForDetection.getRGB(x, y))).getRed();
                    btnStart_diff += Math.abs(colorB - colorA);
                    colorA = (new Color(currentImageForDetection.getRGB(x, y))).getBlue();
                    colorB = (new Color(previousImageForDetection.getRGB(x, y))).getBlue();
                    btnStart_diff += Math.abs(colorB - colorA);
                    colorA = (new Color(currentImageForDetection.getRGB(x, y))).getGreen();
                    colorB = (new Color(previousImageForDetection.getRGB(x, y))).getGreen();
                    btnStart_diff += Math.abs(colorB - colorA);


                }
            }

            //if (btnStart_diff > threshold_diff) {
            // System.out.println("btnStart_diff" + btnStart_diff);
            // }

            if (btnStart_diff > threshold_diff && actionPlay == 0) {
                actionPlay++;
            } else if (actionPlay >= nrRequiredFrames) {
                this.play = true;
                actionPlay = 0;
            } else if (btnStart_diff > threshold_diff && actionPlay > 1) {
                actionPlay = 0;
            } else if (btnStart_diff < threshold_diff && actionPlay > 0) {
                actionPlay++;
            }
        }
        if (play) {
            this.buttonStaticActionDetected = true;
            this.indexOfButtonPressed = 1;
            System.out.println("PLAY button has been pushed!!!!");
            this.play = false;
        }
    }

    public void notifyOpticalModel() {
        parentOpticalModel.setNewResultFromMovementBrain(true);
    }

    public int getIndexOfButtonPushed() {
        return this.indexOfButtonPressed;
    }

    public void setNewImage(boolean b) {
        this.newImage = b;
    }

    @Override
    public void run() {
        while (true) {
            this.previousImageForDetection = this.currentImageForDetection;
            this.currentImageForDetection = this.parentOpticalModel.getCapturedImageFromEye();


            if (this.previousImageForDetection != null && this.currentImageForDetection != null && this.newImage) {
                this.checkButtons();
                if (this.buttonStaticActionDetected) {
                    this.notifyOpticalModel();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MovementBrain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (this.buttonDynamicActionDetected) {
                    this.notifyOpticalModel();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MovementBrain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.indexOfButtonPressed = 0;
                    this.notifyOpticalModel();
                }
                this.newImage = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MovementBrain.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}

