package slrt;

import xmlparser.Letter;
import xmlparser.MyImage;
import xmlparser.XMLParser;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import processing.Brain;
import processing.EyeWebcam;
import java.awt.Color;
import java.util.Observable;
import java.util.Random;
import processing.MovementBrain;
import xmlparser.XMLWrite;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nick
 */
public class OpticalModel extends Observable implements Runnable {

    private EyeWebcam eye;
    private Brain brain;
    private MovementBrain movementBrain;
    private Vector<Object> data;
    private BufferedImage currentImage;
    private XMLParser xmlParser;
    private boolean changedImage;
    private Vector<Letter> letters = new Vector<Letter>();
    private Vector<MyImage> images = new Vector<MyImage>();
    private boolean newResultFromBrain;
    private boolean newResultFromMovementBrain;
    private Controller controller;
    private Thread brainThread;
    private Thread movementBrainThread;
    private int resultFromBrain;
    private Boolean[] resultFromMovementBrain;
    private String displayedWord="JeG";
    private String currentWord="JeGaaaa";
    private BufferedImage wordImage;
    private File XMLName;
    private boolean  displayedWordCorrect;

    public OpticalModel(Controller c) {
        this.controller = c;
        data = new Vector<Object>();
        this.xmlParser = new XMLParser();
        this.XMLName = new File("src/db/letters/letters.xml");
        if (XMLName.exists()) {
            importDataFromXML();
        } else {
            XMLWrite xmlWriter = new XMLWrite();
            xmlWriter.generateXMLLetters();
            xmlWriter.generateXMLDictionary();
        }
        this.brain = new Brain(this);
        this.wordImage = this.getNextImage();
        //this.controller.gi.setWordImage(this.wordImage);
        this.movementBrain = new MovementBrain(this);
        this.eye = new EyeWebcam(this, brain);
        this.brainThread = new Thread(this.brain);
        this.movementBrainThread = new Thread(this.movementBrain);
        this.eye.start();
        this.brainThread.start();
        this.movementBrainThread.start();
        importDataFromXML();
        currentImage = getCapturedImageFromEye();
    }

    public Vector<MyImage> getImages() {
        return images;
    }

    public int getImagesNumber() {
        return images.size();
    }

    public BufferedImage getCapturedImageFromEye() {
        return eye.getImage();
    }

    public Boolean[] getResultFromMovementBrain(){
        return this.resultFromMovementBrain;
    }

        public BufferedImage getCurrentImage() {
        return this.currentImage;
    }

    public void importDataFromXML() {
        letters = xmlParser.getLetters();
        images = xmlParser.getMyImages();
    }

    public void setChanged(boolean b) {
         this.changedImage = b;
    }

    public boolean imageHasChanged(){
        return this.changedImage;
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

    public BufferedImage getNextImage(){

     return (images.get(generateRandomInteger(0,images.size()-1))).getImage();
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
            if (changedImage) {
                this.setChanged();
                typeOfAction |= 1;
                this.changedImage = false;
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
}




