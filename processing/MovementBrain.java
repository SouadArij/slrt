package processing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import SLRTr.SLRTModel;

public class MovementBrain implements Runnable {

    private static int IMAGE_SIZE_SETTING = 1;
    private static int THRESHOLD_DIFFERENCE = 120000 / IMAGE_SIZE_SETTING;
    private static int PLAY_BUTTON_X = 295 * IMAGE_SIZE_SETTING;
    private static int PLAY_BUTTON_Y = 25 * IMAGE_SIZE_SETTING;
    private static int STOP_BUTTON_X = 25 * IMAGE_SIZE_SETTING;
    private static int STOP_BUTTON_Y = 25 * IMAGE_SIZE_SETTING;
    private static int BACK_BUTTON_X = 108 * IMAGE_SIZE_SETTING;
    private static int BACK_BUTTON_Y = 25 * IMAGE_SIZE_SETTING;
    private static int NEXTWORD_BUTTON_X = 201 * IMAGE_SIZE_SETTING;
    private static int NEXTWORD_BUTTON_Y = 25 * IMAGE_SIZE_SETTING;
    private static int BUTTON_RADIUS = 20 * IMAGE_SIZE_SETTING;

    private static int BUTTON_SENSITIVITY = 2;
    
    SLRTModel parentModel;
    
    private BufferedImage previousImageForDetection;
    private BufferedImage currentImageForDetection;

    private int[] motionCounter = new int[4];
    
    private Boolean[] buttonHighlighted = new Boolean[4];
    private Boolean[] buttonPressed = new Boolean[4];

    private boolean keepFirstImage;

    public MovementBrain(SLRTModel m) {
        this.parentModel = m;

        for (int i = 0; i < 4; i++) {
            this.buttonHighlighted[i] = false;
            this.buttonPressed[i] = false;
            this.motionCounter[i] = 0;
        }
        
        this.buttonHighlighted[3] = true;
        this.buttonPressed[3] = true;
        
        this.keepFirstImage = false;
    }

    private boolean movementDetected(int buttonX, int buttonY) {
        int x = 0;
        int y = 0;
        
        if ((this.previousImageForDetection == null) || (this.currentImageForDetection == null)) {
            return false;
        }

        if (this.previousImageForDetection.getHeight() == 480 && this.currentImageForDetection.getWidth() == 640) {
            MovementBrain.IMAGE_SIZE_SETTING = 2;
        }
        
        int btnDiff = 0;
        for (y = buttonY - BUTTON_RADIUS; y < buttonY + BUTTON_RADIUS; y++) {
            for (x = buttonX - BUTTON_RADIUS; x < buttonX + BUTTON_RADIUS; x++) {
                int colorA = (new Color(currentImageForDetection.getRGB(x, y))).getRed();
                int colorB = (new Color(previousImageForDetection.getRGB(x, y))).getRed();
                btnDiff += Math.abs(colorB - colorA);
                colorA = (new Color(currentImageForDetection.getRGB(x, y))).getBlue();
                colorB = (new Color(previousImageForDetection.getRGB(x, y))).getBlue();
                btnDiff += Math.abs(colorB - colorA);
                colorA = (new Color(currentImageForDetection.getRGB(x, y))).getGreen();
                colorB = (new Color(previousImageForDetection.getRGB(x, y))).getGreen();
                btnDiff += Math.abs(colorB - colorA);
            }
        }
        
        if (btnDiff > MovementBrain.THRESHOLD_DIFFERENCE) {
            System.out.println("MOVEMENT! " + btnDiff);
            //this.keepFirstImage = true;
            return true;
        }
        
        return false;
    }

    private void checkStaticButton(int index, int btnX, int btnY) {
        int otherIndex = Math.abs(index - 3);
        
        if (!(this.buttonPressed[index]) && this.buttonPressed[otherIndex]) {
            boolean movement = movementDetected(btnX, btnY);

            if (movement) {
                if (this.motionCounter[index] == 0) {
                    this.keepFirstImage = true;
                }
                this.motionCounter[index]++;
            }

            if (!movement && this.motionCounter[index] < BUTTON_SENSITIVITY) {
                this.motionCounter[index] = 0;
            }

            if (this.motionCounter[index] >= BUTTON_SENSITIVITY) {
                this.buttonHighlighted[index] = true;
                this.buttonHighlighted[otherIndex] = false;
                System.out.println("Button highlighted!" + System.currentTimeMillis());

                if (!movement) {
                    System.out.println("Button pressed!" + System.currentTimeMillis());
                    this.buttonPressed[index] = true;
                    this.buttonPressed[otherIndex] = false;
                    this.keepFirstImage = false;
                    this.motionCounter[index] = 0;
                }
            }
        }
    }

    private void checkDynamicButton(int index, int btnX, int btnY) {
        boolean movement = movementDetected(btnX, btnY);
        int otherIndex = Math.abs(index - 3);
        
        if (this.buttonPressed[0] && !this.buttonHighlighted[otherIndex] && !this.buttonPressed[otherIndex]) {            
            if (movement) {
                if (this.motionCounter[index] == 0) {
                    this.keepFirstImage = true;
                }
                this.motionCounter[index]++;
            }

            if (this.motionCounter[index] < BUTTON_SENSITIVITY && !movement) {
                this.motionCounter[index] = 0;

            }

            if (this.motionCounter[index] >= BUTTON_SENSITIVITY) {
                this.buttonHighlighted[index] = true;
                System.out.println("Button highlighted!" + System.currentTimeMillis());

                if (!movement) {
                    System.out.println("Button pressed!" + System.currentTimeMillis());
                    this.buttonHighlighted[index] = false;
                    this.buttonPressed[index] = true;
                    this.keepFirstImage = false;
                    this.motionCounter[index] = 0;
                }
            }
        }
    }

    public Boolean[] getHighlightedButtons() {
        return this.buttonHighlighted;
    }

    public Boolean[] getPressedButtons() {
        return this.buttonPressed;
    }
    
    @Override
    public void run() {

        while (true) {
            // set the two images needed for comparison
            if (!this.keepFirstImage) {
                this.previousImageForDetection = this.currentImageForDetection;
            }
            this.currentImageForDetection = this.parentModel.getCapturedImageFromEye();

            // check if button has been pressed or highlighted and notify OpticalModel
            if (this.previousImageForDetection != null && this.currentImageForDetection != null) {
                Boolean[] prevButtonHighlighted = this.buttonHighlighted.clone();
                Boolean[] prevButtonPressed = this.buttonPressed.clone();

                this.checkStaticButton(0, PLAY_BUTTON_X, PLAY_BUTTON_Y);
                this.checkDynamicButton(1, NEXTWORD_BUTTON_X, NEXTWORD_BUTTON_Y);
                this.checkDynamicButton(2, BACK_BUTTON_X, BACK_BUTTON_Y);
                this.checkStaticButton(3, STOP_BUTTON_X, STOP_BUTTON_Y);

                boolean changed = false;
                for (int i = 0; i < prevButtonHighlighted.length; i++) {
                    if (this.buttonHighlighted[i] != prevButtonHighlighted[i]) {
                        changed = true;
                        break;
                    }
                    if (this.buttonPressed[i] != prevButtonPressed[i]) {
                        changed = true;
                        break;
                    }
                }
                if (changed) {
                    parentModel.setMovementBrainResultChanged();
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(MovementBrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}

