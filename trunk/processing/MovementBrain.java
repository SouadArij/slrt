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

    private static int THRESHOLD_DIFFERENCE = 70000;
    private static int PLAY_BUTTON_X = 295;
    private static int PLAY_BUTTON_Y = 25;
    private static int STOP_BUTTON_X = 25;
    private static int STOP_BUTTON_Y = 25;
    private static int BACK_BUTTON_X = 25;
    private static int BACK_BUTTON_Y = 25;
    private static int NEXTWORD_BUTTON_X = 25;
    private static int NEXTWORD_BUTTON_Y = 25;
    private static int BUTTON_RADIUS = 20;
    private static int BUTTON_SENSITIVITY = 4;
    OpticalModel parentOpticalModel;
    private BufferedImage previousImageForDetection;
    private BufferedImage currentImageForDetection;
    private int indexOfButtonPressed;
    private boolean[] noiseMovementDetected = new boolean[4];
    private int[] noiseCounter = new int[4];
    private Boolean[] buttonHighlighted = new Boolean[4];
    private boolean[] buttonPressed = new boolean[4];
    private boolean change1;

    public MovementBrain(OpticalModel om) {
        this.parentOpticalModel = om;
        this.previousImageForDetection = this.parentOpticalModel.getCurrentImage();
        this.currentImageForDetection = this.parentOpticalModel.getCurrentImage();
        this.indexOfButtonPressed = 0;
        for (int i = 0; i < 4; i++) {
            this.buttonHighlighted[i] = false;
            this.buttonPressed[i] = false;
            this.noiseMovementDetected[i] = false;
            this.noiseCounter[i] = 0;
        }
        this.buttonHighlighted[3] = true;
        this.buttonPressed[3] = true;
        change1 = false;
    }

    private boolean movementDetected(int buttonX, int buttonY) {
        int x = 0;
        int y = 0;
        System.out.println(this.previousImageForDetection.getHeight());
        System.out.println(this.previousImageForDetection.getWidth());
        // skip the first image from the webcam that comes black and with width 1
        if (this.previousImageForDetection.getHeight() == 240 && this.currentImageForDetection.getWidth() == 320) {
            int btn_diff = 0;
            for (y = buttonY - BUTTON_RADIUS; y < buttonY + BUTTON_RADIUS; y++) {
                for (x = buttonX - BUTTON_RADIUS; x < buttonX + BUTTON_RADIUS; x++) {
                    int colorA = (new Color(currentImageForDetection.getRGB(x, y))).getRed();
                    int colorB = (new Color(previousImageForDetection.getRGB(x, y))).getRed();
                    btn_diff += Math.abs(colorB - colorA);
                    colorA = (new Color(currentImageForDetection.getRGB(x, y))).getBlue();
                    colorB = (new Color(previousImageForDetection.getRGB(x, y))).getBlue();
                    btn_diff += Math.abs(colorB - colorA);
                    colorA = (new Color(currentImageForDetection.getRGB(x, y))).getGreen();
                    colorB = (new Color(previousImageForDetection.getRGB(x, y))).getGreen();
                    btn_diff += Math.abs(colorB - colorA);
                }
            }
            if (btn_diff > 0) {
                System.out.println("Difference: " + btn_diff);
            }
            if (btn_diff > MovementBrain.THRESHOLD_DIFFERENCE) {
                System.out.println("MOVEMENT! " + btn_diff);
                // this.first++;
                // if (this.first > 1) {
                return true;
                // }
            }
        }
        return false;
    }

    private void checkStaticButton(int index, int btnX, int btnY) {
        boolean movement = this.movementDetected(btnX, btnY);
        int otherIndex = Math.abs(index - 3);
        if (!(this.buttonPressed[index]) && this.buttonPressed[otherIndex]) {
            if (change1 && (this.noiseCounter[index] >= BUTTON_SENSITIVITY)) {
                if (movement) {
                    System.out.println("Button pressed!" + System.currentTimeMillis());
                    //this.buttonHighlighted[index] = false;
                    this.buttonPressed[index] = true;
                    this.buttonPressed[otherIndex] = false;
                    this.noiseCounter[index] = 0;
                    this.change1 = false;
                } else {
                    System.out.println("Highlight!" + System.currentTimeMillis());
                    this.buttonHighlighted[index] = true;
                    this.buttonHighlighted[otherIndex] = false;
                }
            } else if (change1 && (this.noiseCounter[index] < BUTTON_SENSITIVITY)) {
                if (movement) {
                    this.noiseCounter[index] = 0;
                    this.change1 = false;
                    System.out.println("Change1 was not good!" + System.currentTimeMillis());

                } else {
                    this.noiseCounter[index]++;
                    System.out.println("Counter increased!" + System.currentTimeMillis());

                }
            } else if (!change1 && movement) {
                System.out.println("Change1!" + System.currentTimeMillis());
                this.change1 = true;
            }
        }
    }

    private void checkDynamicButton(int index, int btnX, int btnY) {
        boolean movement = this.movementDetected(btnX, btnY);
        if (change1 && (this.noiseCounter[index] >= BUTTON_SENSITIVITY)) {
            if (movement) {
                System.out.println("Button pressed!" + System.currentTimeMillis());
                this.buttonHighlighted[index] = false;
                this.buttonPressed[index] = true;
                this.noiseCounter[index] = 0;
                this.change1 = false;
            } else {
                System.out.println("Highlight!" + System.currentTimeMillis());
                this.buttonHighlighted[index] = true;
            }
        } else if (change1 && (this.noiseCounter[index] < BUTTON_SENSITIVITY)) {
            if (movement) {
                this.noiseCounter[index] = 0;
                this.change1 = false;
                System.out.println("Change1 was not good!" + System.currentTimeMillis());

            } else {
                this.noiseCounter[index]++;
                System.out.println("Counter increased!" + System.currentTimeMillis());

            }
        } else if (!change1 && movement) {
            System.out.println("Change1!" + System.currentTimeMillis());
            this.change1 = true;
        }
    }

    public void notifyOpticalModel() {
        parentOpticalModel.setNewResultFromMovementBrain(true);
    }

    public int getIndexOfButtonPushed() {
        return this.indexOfButtonPressed;
    }

    public Boolean[] getHighlightedButtons() {
        return this.buttonHighlighted;
    }

    @Override
    public void run() {
        while (true) {
            // set the two images needed for comparison
            this.previousImageForDetection = this.currentImageForDetection;
            this.currentImageForDetection = this.parentOpticalModel.getCapturedImageFromEye();

            // check if button has been pressed or highlighted and notify OpticalModel
            if (this.previousImageForDetection != null && this.currentImageForDetection != null) {
                this.checkStaticButton(0, PLAY_BUTTON_X, PLAY_BUTTON_Y);
                this.checkStaticButton(3, STOP_BUTTON_X, STOP_BUTTON_Y);

                this.notifyOpticalModel();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                // skip if there is no image from webcam
                Logger.getLogger(MovementBrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

