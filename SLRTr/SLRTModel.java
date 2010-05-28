package SLRTr;

import Data.Database;
import Data.Letter;
import Data.Word;
import java.awt.image.BufferedImage;
import java.io.File;
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

    public SLRTModel() {

        if (!Database.LETTERS_XML_FILE.exists()) {
            Database.generateXMLLetters();
        }
        if (!Database.WORDS_XML_FILE.exists()) {
            Database.generateXMLDictionary();
        }
        this.database = new Database();
        this.loadPreprocessedImages2DB();

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

        this.currentLetter = "";

    }

    public Vector<Word> getWords() {
        return this.database.getWords();
    }

    public Vector<Letter> getLetters() {
        return this.database.getLetters();
    }

    public BufferedImage getCapturedImageFromEye() {
        return eye.getImage();
    }

    public Boolean[] getHighlightsFromMovementBrain() {
        return this.highlightsFromMovementBrain;
    }

    public Boolean[] getPressedFromMovementBrain() {
        return this.pressedFromMovementBrain;
    }

    public int getResultFromBrain() {
        return this.brain.getResult();
    }

    /**
     * Load all .dbim images in the <database>
     * Usually used when starting the program.
     */
    public void loadPreprocessedImages2DB() {
        this.database.process();
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

    public boolean imageHasChanged() {
        return this.imageChanged;
    }

    public void setBrainResultChanged() {
        synchronized (lockObject1) {
            this.brainResultChanged = true;
        }
        //take the correspongind letter and concat it to displayed string in gui
        // this.displayedWord += (this.database.getLetters().get(this.resultFromBrain - 1)).getName();
        //if the currentWord contains the displayedWord the the result is good
        //  this.displayedWordCorrect = this.currentWord.getName().contains(this.displayedWord);


        /* Huni: this won't be needed, we'll get the
         * result in the run when we get there.
         * 
        this.resultFromBrain = brain.getResult();
         */
    }

    public void setNewResultFromMovementBrain(boolean b) {
        this.movementResultChanged = b;
        this.highlightsFromMovementBrain = movementBrain.getHighlightedButtons();
        this.pressedFromMovementBrain = movementBrain.getPressedButtons();
    }

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

    public void takeNextWordImage() {
        Vector<Word> words = this.database.getWords();
        this.requieredWord = words.get(generateRandomInteger(0, words.size() - 1));

        //this.wordImage = newWord.getImage();
    }

    public String getBuildingWord() {
        return this.buildingWord;
    }

    public BufferedImage getWordImage() {
        return this.requieredWord.getImage();
    }

    public String getCurrentLetter() {
        return this.currentLetter;
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


                if (this.movementResultChanged) {
                    synchronized (lockObject1) {
                        this.movementResultChanged = false;

                    }

                    if (this.pressedFromMovementBrain[0]) {
                        if (this.brain.checkAlgorithmRunning()){
                         this.brain.startAlgorithmRunning();
                        }
                        if (this.pressedFromMovementBrain[1]) {
                            this.takeNextWordImage();
                            this.buildingWord = "";
                        }
                        if (this.pressedFromMovementBrain[2]) {
                            this.buildingWord = this.buildingWord.substring(0, this.buildingWord.length() - 2);
                        }

                        if (this.pressedFromMovementBrain[3] == true) {
                            this.brain.stopAlgorithmRunning();
                        }


                    }
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



