package SLRTr;

import Data.Database;
import Data.Letter;
import Data.Word;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
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
    private int resultFromBrain;
    private Boolean[] highlightsFromMovementBrain;
    private Boolean[] pressedFromMovementBrain;

    private int currentLetterId;
    private Word currentWord;
    private String displayedWord = "Test";
    private boolean displayedWordCorrect;

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
        this.displayedWord += (this.database.getLetters().get(this.resultFromBrain - 1)).getName();
        //if the currentWord contains the displayedWord the the result is good
        this.displayedWordCorrect = this.currentWord.getName().contains(this.displayedWord);


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
        this.currentWord = words.get(generateRandomInteger(0, words.size() - 1));
       
        //this.wordImage = newWord.getImage();
    }

    public String getDisplayedWord() {
        return this.displayedWord;
    }

    public BufferedImage getWordImage() {
        return this.currentWord.getImage();
    }

    public boolean getDisplayedWordCorrect() {
        return this.displayedWordCorrect;
    }

    @Override
    public void run() {
        while (true) {


            if (this.brainResultChanged) {
                synchronized (lockObject1) {
                    this.brainResultChanged = false;
                }

                /* Huni: Here we should have a buildingWord without
                 * this current result. The displayedWord should be
                 * buildingWord + convert(currentLetterId). This way
                 * we won't have to delete always the last character
                 * from the end and add the current. Also helps when
                 * there is no result (result is -1);
                 */
                
                this.currentLetterId = this.brain.getResult();
                /* Huni introduced end.
                 */

                this.displayedWord += (new Integer(this.resultFromBrain)).toString();//(this.letters.get(this.resultFromBrain+1)).getName;



                if ((this.currentWord).getName().contains(this.displayedWord)) {
                    this.displayedWordCorrect = true;

                } else {
                    this.displayedWordCorrect = false;




                }
            }

            if (this.movementResultChanged) {
                synchronized (lockObject1) {
                    this.movementResultChanged = false;
                }
                if (this.pressedFromMovementBrain[0]) {
                    if (this.pressedFromMovementBrain[1]) {
                        this.takeNextWordImage();
                        this.displayedWord = "";
                    }
                    if (this.pressedFromMovementBrain[2] == true) {
                        String copy = null;
                        copy = copy.copyValueOf(this.displayedWord.toCharArray(), 0, this.displayedWord.length() - 1);
                        this.displayedWord = copy;
                    }
                }
            }
        }
    }
}




