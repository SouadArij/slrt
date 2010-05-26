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

    private static int imageSizeSetting = 1;
    private static int THRESHOLD_DIFFERENCE = 70000 / imageSizeSetting;
    private static int PLAY_BUTTON_X = 295 * imageSizeSetting;
    private static int PLAY_BUTTON_Y = 25 * imageSizeSetting;
    private static int STOP_BUTTON_X = 25 * imageSizeSetting;
    private static int STOP_BUTTON_Y = 25 * imageSizeSetting;
    private static int BACK_BUTTON_X = 25 * imageSizeSetting;
    private static int BACK_BUTTON_Y = 25 * imageSizeSetting;
    private static int NEXTWORD_BUTTON_X = 25 * imageSizeSetting;
    private static int NEXTWORD_BUTTON_Y = 25 * imageSizeSetting;
    private static int BUTTON_RADIUS = 20 * imageSizeSetting;
    private static int BUTTON_SENSITIVITY = 2;
    OpticalModel parentOpticalModel;
    private BufferedImage previousImageForDetection;
    private BufferedImage currentImageForDetection;
    private int indexOfButtonPressed;
    private boolean[] noiseMovementDetected = new boolean[4];
    private int[] noiseCounter = new int[4];
    private Boolean[] buttonHighlighted = new Boolean[4];
    private boolean[] buttonPressed = new boolean[4];
    private boolean change1;
    private boolean keepFirstImage;

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
        this.keepFirstImage = false;
    }

    private boolean movementDetected(int buttonX, int buttonY) {
        int x = 0;
        int y = 0;
        System.out.println(this.previousImageForDetection.getHeight());
        System.out.println(this.previousImageForDetection.getWidth());
        // skip the first image from the webcam that comes black and with width 1
        if (this.previousImageForDetection.getHeight() >= 240 && this.currentImageForDetection.getWidth() >= 320) {
            if (this.previousImageForDetection.getHeight() == 480 && this.currentImageForDetection.getWidth() == 640) {
                MovementBrain.imageSizeSetting = 2;
            }
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
            if (btn_diff > MovementBrain.THRESHOLD_DIFFERENCE) {
                System.out.println("MOVEMENT! " + btn_diff);
                this.keepFirstImage = true;
                return true;
            }
        }
        return false;
    }

    private void checkStaticButton(int index, int btnX, int btnY) {
        int otherIndex = Math.abs(index - 3);
        if (!(this.buttonPressed[index]) && this.buttonPressed[otherIndex]) {
            boolean movement = movementDetected(btnX, btnY);

            if (movement) {
                if (this.noiseCounter[index] == 0) {
                    this.keepFirstImage = true;
                }
                this.noiseCounter[index]++;
            }

            if (!movement && this.noiseCounter[index] < BUTTON_SENSITIVITY) {
                this.noiseCounter[index] = 0;
            }

            if (this.noiseCounter[index] >= BUTTON_SENSITIVITY) {
                this.buttonHighlighted[index] = true;
                this.buttonHighlighted[otherIndex] = false;
                System.out.println("Button highlighted!" + System.currentTimeMillis());

                if (!movement) {
                    System.out.println("Button pressed!" + System.currentTimeMillis());
                    this.buttonPressed[index] = true;
                    this.buttonPressed[otherIndex] = false;
                    this.keepFirstImage = false;
                    this.noiseCounter[index] = 0;
                }
            }
        }
    }

    private void checkDynamicButton(int index, int btnX, int btnY) {
        boolean movement = movementDetected(btnX, btnY);

        if (this.noiseCounter[index] < BUTTON_SENSITIVITY && !movement) {
            this.noiseCounter[index] = 0;

        }

        if (this.noiseCounter[index] >= BUTTON_SENSITIVITY) {
            this.buttonHighlighted[index] = true;
            System.out.println("Button highlighted!" + System.currentTimeMillis());

            if (!movement) {
                System.out.println("Button pressed!" + System.currentTimeMillis());
                this.buttonHighlighted[index] = false;
                this.buttonPressed[index] = true;
                this.keepFirstImage = false;
                this.noiseCounter[index] = 0;
            }
        }

        if (movement) {
            if (this.noiseCounter[index] == 0) {
                this.keepFirstImage = true;
            }
            this.noiseCounter[index]++;
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
            if (!this.keepFirstImage) {
                this.previousImageForDetection = this.currentImageForDetection;
            }

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

