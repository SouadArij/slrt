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
import xmlparser.XMLWrite;

public class SLRTModel extends Observable implements Runnable {

    private EyeWebcam eye;
    private Brain brain;
    private MovementBrain movementBrain;

    private Database database;
    
    private Vector<Object> data;
    private BufferedImage currentImage;       
    private boolean imageChanged;
    //private Vector<Letter> letters = new Vector<Letter>();
    //private Vector<MyImage> images = new Vector<MyImage>();
    private boolean newResultFromBrain;
    private boolean newResultFromMovementBrain;    
    private Thread brainThread;
    private Thread movementBrainThread;
    private int resultFromBrain;
    private Boolean[] resultFromMovementBrain;
    private String displayedWord="JeG";
    private String currentWord="JeGaaaa";
    private BufferedImage wordImage;    
    private boolean  displayedWordCorrect;
    private File XMLName;

    public SLRTModel() {
        data = new Vector<Object>();

        this.database = new Database();
        this.loadPreprocessedImages2DB();
        this.loadData();
        /* Don't get this, but I'm sure this doesn't fit here in the constructor.
         * Do this in a separate function. And do this only on specific request.
         *
       
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
        
        this.wordImage = this.getNextWordImage();
        //this.controller.gi.setWordImage(this.wordImage);
        
       // importDataFromXML();
        currentImage = getCapturedImageFromEye();
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

    public Boolean[] getResultFromMovementBrain() {
        return this.resultFromMovementBrain;
    }

    public int getResultFromBrain() {
        return this.brain.getResult();
    }

    /* Huni: Why this if we have getCapturedImageFromEye() ?
     *
    public BufferedImage getCurrentImage() {
        return this.currentImage;
    }
     */

    /* Load all .dbim images in the <database>
     * Usually used when starting the program.
     */
    public void loadPreprocessedImages2DB() {
        this.database.process();
    }

    /* Processes all .bmp/.jpg until they become .dbim and all in the <database>.
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

    public boolean imageHasChanged(){
        return this.imageChanged;
    }

    public void setBrainResultChanged() {
        this.newResultFromBrain = true;
        this.resultFromBrain = brain.getResult();
        
    }

    public void setNewResultFromMovementBrain(boolean b) {
        this.newResultFromMovementBrain = b;
        resultFromMovementBrain = movementBrain.getHighlightedButtons();
    }

     public int generateRandomInteger(int startRange, int stopRange)
    {
    Random random = new Random();
    {if ( startRange > stopRange ) {
            throw new IllegalArgumentException("Start cannot exceed End.");
                 }
    long range = (long)stopRange - (long)startRange + 1;
    long fraction = (long)(range * random.nextDouble());
    return   (int)(fraction + startRange);}
     }

    public BufferedImage getNextWordImage() {
        Vector<Word> words = this.database.getWords();
        File path = words.get(generateRandomInteger(0, words.size() - 1)).getImagePath();
        BufferedImage buffIm = null;
        try {
            buffIm = ImageIO.read(path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return buffIm;
    }

//    public void setWordImage()
//    {
//        this.controller.gi.setWordImage(this.wordImage);
//    }

    public String getDisplayedWord()
    {
        return this.displayedWord;
    }

    public boolean getDisplayedWordCorrect()
    {
        return this.displayedWordCorrect;
    }

    public BufferedImage getWordImage() {
        return this.wordImage;
    }

    @Override
    public void run() {
        byte typeOfAction;
        while (true) {
            typeOfAction = 0;
            if (imageChanged) {
                this.setChanged();
                typeOfAction |= 1;
                this.imageChanged = false;
                }

            if (newResultFromBrain) {
                 this.displayedWord += (new Integer(this.resultFromBrain)).toString();//(this.letters.get(this.resultFromBrain+1)).getName;
                
               /*   // need to make gui take the string and the boolean for current char
                if((this.currentWord).contains(this.displayedWord))
                     //true if the letter is ok
                    this.controller.gi.setDisplayedString(this.displayedWord,true);
                else
                    //false if the letter is wtong
                    this.controller.gi.setDisplayedString(this.displayedWord,false);
                */

                 if((this.currentWord).contains(this.displayedWord))
                     this.displayedWordCorrect = true;
                 else
                     this.displayedWordCorrect = false;


                typeOfAction |= 2;
                this.newResultFromBrain = false;
            }

            if (newResultFromMovementBrain) {
                this.setChanged();
                typeOfAction |= 4;
                this.newResultFromMovementBrain = false;
            }
            if (typeOfAction != 0)
            this.notifyObservers(typeOfAction);
        }
    }

    private void loadData() {
        this.XMLName = new File("src/db/letters/letters.xml");
        if (XMLName.exists()) {
        } else {
            XMLWrite xmlWriter = new XMLWrite();
            xmlWriter.generateXMLLetters();
            xmlWriter.generateXMLDictionary();
        }
    }
}




