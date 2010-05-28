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

    private String displayedWord="JeG";
    private String currentWord="JeGaaaa";
    private BufferedImage wordImage;    
    private boolean  displayedWordCorrect;
    private File XMLName;

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
        
        this.wordImage = this.getNextWordImage();
        //this.controller.gi.setWordImage(this.wordImage);
        
       // importDataFromXML();        
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

    /* Huni: Why this if we have getCapturedImageFromEye() ?
     *
    public BufferedImage getCurrentImage() {
        return this.currentImage;
    }
     */

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

    public boolean imageHasChanged(){
        return this.imageChanged;
    }

    public void setBrainResultChanged() {
        this.brainResultChanged = true;
        this.resultFromBrain = brain.getResult();
        //take the correspongind letter and concat it to displayed string in gui
        this.displayedWord+=(this.database.getLetters().get(this.resultFromBrain-1)).getName();
        //if the currentWord contains the displayedWord the the result is good
        this.displayedWordCorrect=this.currentWord.contains(this.displayedWord);

        
    }

    public void setNewResultFromMovementBrain(boolean b) {
        this.movementResultChanged = b;
        this.highlightsFromMovementBrain = movementBrain.getHighlightedButtons();
        this.pressedFromMovementBrain = movementBrain.getPressedButtons();
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
        Word newWord=words.get(generateRandomInteger(0,words.size()-1));
        this.currentWord=newWord.getName();
        File path = newWord.getImagePath();//words.get(generateRandomInteger(0, words.size() - 1)).getImagePath();
        this.wordImage = null;
        try {
           this.wordImage  = ImageIO.read(path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return  this.wordImage;
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

            if (brainResultChanged) {
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
                this.brainResultChanged = false;
            }

            if (movementResultChanged) {
                this.setChanged();
                typeOfAction |= 4;
                if (this.pressedFromMovementBrain[0]==true)
                    {
                     if (this.pressedFromMovementBrain[1]==true)
                        {
                        this.wordImage=this.getNextWordImage();
                        this.displayedWord="";
                        }
                     if (this.pressedFromMovementBrain[2]==true)
                        {String copy=null;
                         copy=copy.copyValueOf(this.displayedWord.toCharArray(),0,this.displayedWord.length()-1);
                         this.displayedWord=copy;
                        }
                    }
                this.movementResultChanged = false;

            }
            if (typeOfAction != 0)
            this.notifyObservers(typeOfAction);
        }
    }

    private void generateMissingXML() {
        Database.generateXMLDictionary();
        Database.generateXMLLetters();
    }
}




