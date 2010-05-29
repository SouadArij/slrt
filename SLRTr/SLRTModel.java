package SLRTr;

import Data.Database;
import Data.Letter;
import Data.Word;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import processing.Brain;
import processing.EyeWebcam;
import java.util.Observable;
import java.util.Random;
import processing.MovementBrain;

public class SLRTModel extends Observable implements Runnable {

    private final Object lockObject1 = new Object();
    private EyeWebcam eye;
    private Brain brain;
    private MovementBrain movementBrain;
    private Thread brainThread;
    private Thread movementBrainThread;
    private Database database;
    private boolean imageChanged;
    private boolean brainResultChanged;
    private boolean movementResultChanged;
    private Boolean[] highlightsFromMovementBrain;
    private Boolean[] pressedFromMovementBrain;
    private Word requieredWord;
    private String buildingWord;
    private String currentLetter;
    private Boolean guiResultChanged;
    private Boolean[] clickedButtonsFromGui;

    /**
     * Class contructor
     */
    public SLRTModel() {

        if (!Database.LETTERS_XML_FILE.exists()) {
            Database.generateXMLLetters();
        }
        if (!Database.WORDS_XML_FILE.exists()) {
            Database.generateXMLDictionary();
        }
        this.database = new Database();
        this.loadPreprocessedImages2DB();
        
        /* for debugging
        Iterator<Letter> itlt = this.database.getLetters().iterator();
        while (itlt.hasNext()) {
            Letter lt = itlt.next();
            System.out.format("letter name: %s, path: %s\n", lt.getName(), lt.getDirPath());
        }
        Iterator<Word> itwd = this.database.getWords().iterator();
        while (itwd.hasNext()) {
            Word wd = itwd.next();
            System.out.format("word name: %s\n", wd.getName());
        }
         */

        this.brain = new Brain(this);
        this.eye = new EyeWebcam(this);
        this.brain.setSiblingEye(this.eye);
        this.eye.setSiblingBrain(this.brain);
        this.movementBrain = new MovementBrain(this);

        this.eye.start();
        this.brainThread = new Thread(this.brain);
        this.brainThread.start();
        this.movementBrainThread = new Thread(this.movementBrain);
        this.movementBrainThread.start();

        this.takeNextWordImage();

        this.buildingWord = "";
        this.currentLetter = "";
        this.guiResultChanged=false;

    }

    public Vector<Word> getWords() {
        return this.database.getWords();
    }

    public Vector<Letter> getLetters() {
        return this.database.getLetters();
    }

    /**
     * Gets the BufferedImage caputred by EyeWebcam
     * @return the BufferedImage caputred by EyeWebcam
     */
    public BufferedImage getCapturedImageFromEye() {
        return eye.getImage();
    }

    /**
     * Gets an Array from MovementBrain that indicates the highligted buttons
     * @return an boolean array from MovementBrain
     */
    public Boolean[] getHighlightsFromMovementBrain() {
        return this.highlightsFromMovementBrain;
    }


    /**
     * Gets an Array from MovementBrain that indicates the pressed buttons
     * @return an boolean array from MovementBrain
     */
    public Boolean[] getPressedFromMovementBrain() {
        return this.pressedFromMovementBrain;
    }

    /**
     * Called by the EyeWebcam to notify there was a new image captured
     * @param bool true if it was captured a new image, else false
     */
    public int getResultFromBrain() {
        return this.brain.getResult();
    }

    /**
     * Load all .dbim images in the <database>
     * Usually used when starting the program.
     */
    public void loadPreprocessedImages2DB() {
        this.database.load();
    }

    /**
     * Processes all .bmp/.jpg until they become .dbim and all in the <database>.
     * Then those .dbim are saved to the disk so next time you won't have to
     * process again, just load.
     * Use only if really the pictures have changed.
     */
    public void reProcessImagesAndLoad2DB() {
        this.database.process();
        this.database.save();
    }

    public void setChanged(boolean b) {
        this.imageChanged = b;
    }

    /**
     * Returns  true if the image from EyeWebcam was changed, else false
     * @return Returns  true if the image from EyeWebcam was changed, else false
     */
    public boolean imageHasChanged() {
        return this.imageChanged;
    }

    /**
     * This method is called by the Brain to notify that the analyze completed
     */
    public void setBrainResultChanged() {
        synchronized (lockObject1) {
            this.brainResultChanged = true;
        }
    }

    public void setMovementBrainResultChanged() {
        synchronized (this.lockObject1) {
            this.movementResultChanged = true;
        }
    }

    /**
     *  Generates a random integer between the startRange and stopRange
     * @param startRange is the start value of the desired range
     * @param stopRange is the stop value of the desired range
     * @return a random integer in the specified range
     */
    public int generateRandomInteger(int startRange, int stopRange) {
        Random random = new Random();
        {
            if (startRange > stopRange) {
                throw new IllegalArgumentException("Start cannot exceed End.");
            }
            long range = (long) stopRange - (long) startRange + 1;
            long fraction = (long) (range * random.nextDouble());
            return (int) (fraction + startRange);
        }
    }

    /**
     * Gets a new Word from the list of available words
     *
     */
    public void takeNextWordImage() {
        Vector<Word> words = this.database.getWords();
        this.requieredWord = words.get(generateRandomInteger(0, words.size() - 1));

        //this.wordImage = newWord.getImage();
    }

    /**
     * Gets the curent string displayed on the interface
     * @return the current string displayed on the interface
     */
    public String getBuildingWord() {
        return this.buildingWord;
    }

    /**
     * Gets the corresponding BufferedImage of this Word
     * @return BufferedImage
     */
    public BufferedImage getWordImage() {
        return this.requieredWord.getImage();
    }

    public String getCurrentLetter() {
        return this.currentLetter;
    }

    public BufferedImage getProcessedImageFromBrain() {
        return this.brain.getProcessedImage();
    }

    public void setNewResultFromGui(Boolean[] vec){
       this.clickedButtonsFromGui=vec;
       this.guiResultChanged=true;
    }

    @Override
    public void run() {
        while (true) {

            if (this.brainResultChanged) {
                synchronized (lockObject1) {
                    this.brainResultChanged = false;
                }

                int currentLetterId = this.brain.getResult();
                this.currentLetter = this.database.id2String(currentLetterId);
                String displayedWord = this.buildingWord + this.currentLetter;

                if (displayedWord.equals(this.requieredWord.getName().substring(0, displayedWord.length() - 1))) {
                    this.buildingWord += this.currentLetter;
                }
            }

// from movement brain
            if (this.movementResultChanged) {
                synchronized (lockObject1) {
                    this.movementResultChanged = false;
                }


                System.out.format("Model: movementResultChanged\n");

                this.highlightsFromMovementBrain = this.movementBrain.getHighlightedButtons();
                this.pressedFromMovementBrain = this.movementBrain.getPressedButtons();

                if (this.pressedFromMovementBrain[0]) {
                        this.brain.startAlgorithmRunning();
                        System.out.format("Model: called to start algorithm\n");
                }

                if (this.pressedFromMovementBrain[1]) {
                    this.takeNextWordImage();
                    this.buildingWord = "";
                }

                if (this.pressedFromMovementBrain[2]) {
                    if (this.buildingWord.length() > 0) {
                        this.buildingWord = this.buildingWord.substring(0, this.buildingWord.length());
                    }
                }


                if (this.pressedFromMovementBrain[3]) {
                    this.brain.stopAlgorithmRunning();
                    System.out.format("Model: called to stop algorithm\n");
                }
            }
//from gui
            if (this.guiResultChanged)
            {  
                this.guiResultChanged=false;
                if (this.clickedButtonsFromGui[0]) {
                        this.brain.startAlgorithmRunning();
                        System.out.format("Model: called to start algorithm\n");
                }

                if (this.clickedButtonsFromGui[1]) {
                    this.takeNextWordImage();
                    this.buildingWord = "";
                }

                if (this.clickedButtonsFromGui[2]) {
                    if (this.buildingWord.length() > 0) {
                        this.buildingWord = this.buildingWord.substring(0, this.buildingWord.length());
                    }
                }


                if (this.clickedButtonsFromGui[3]) {
                    this.brain.stopAlgorithmRunning();
                    System.out.format("Model: called to stop algorithm\n");
                }
            }


            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SLRTModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}




