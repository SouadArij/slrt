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
import processing.MovementBrain;
import xmlparser.XMLWrite;
import java.util.Random;
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
    private String resultFromBrain;
    private String displayedWord;
    private String currentWord;
  
    private int resultFromMovementBrain;
    File XMLName;

    public OpticalModel(Controller c) {
        this.controller = c;
        data = new Vector<Object>();
        this.xmlParser = new XMLParser();
        this.brain = new Brain(this);
        this.movementBrain = new MovementBrain(this);
        this.XMLName = new File("src/db/letters/letters.xml");
        if (XMLName.exists()) {
            importDataFromXML();
        } else {
            XMLWrite xmlWriter = new XMLWrite();
            xmlWriter.generateXMLLetters();
            xmlWriter.generateXMLDictionary();
        }
        this.eye = new EyeWebcam(this, brain);
        this.brainThread = new Thread(this.brain);
        this.movementBrainThread = new Thread(this.movementBrain);
        this.eye.start();
        this.brainThread.start();
        this.movementBrainThread.start();



        currentImage = getCapturedImageFromEye();
    }
 //nu mai fol
    public Vector<MyImage> getImages() {
        return images;
    }
//nu mai fol
    public int getImagesNumber() {
        return images.size();
    }

    public BufferedImage getCapturedImageFromEye() {
        return eye.getImage();
    }

    public int getIndexOfButtonFromMovementBrain() {
        return this.movementBrain.getIndexOfButtonPushed();
    }

    public BufferedImage getCurrentImage() {
        return this.currentImage;
    }

    public void importDataFromXML() {
        letters = xmlParser.getLetters();
        images = xmlParser.getMyImages();
    }

    public void setChanged(boolean b) {
        this.movementBrain.setNewImage(true);
        this.changedImage = b;
    }

    public boolean imageHasChanged() {
        return this.changedImage;
    }

    //gets the resulting letter
    public void setNewResultFromBrain(boolean b) {
        this.newResultFromBrain = b;
        this.resultFromBrain = brain.getResult();

    }

    public void setNewResultFromMovementBrain(boolean b) {
        this.newResultFromMovementBrain = b;
        resultFromMovementBrain = movementBrain.getIndexOfButtonPushed();
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

    public String getNextImage(){
     return (images.get(generateRandomInteger(0,images.size()-1))).getURL();
    }

    public void clearLetter(){
     String buffer=new String();
     buffer.copyValueOf(this.displayedWord.toCharArray(), 0, (this.displayedWord.length())-1);
     this.displayedWord=buffer;
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
                // this.setChanged();
                this.displayedWord+=this.resultFromBrain;
                if((this.currentWord).contains(this.displayedWord))
                    this.controller.gi.setDisplayedString(this.displayedWord,true);
                else
                    this.controller.gi.setDisplayedString(this.displayedWord,false);
                typeOfAction |= 2;
                this.newResultFromBrain = false;

            }

            if (newResultFromMovementBrain) {
                this.setChanged();
                typeOfAction |= 4;
                this.newResultFromMovementBrain = false;
            }


          if (typeOfAction != 0) {
//                this.notifyObservers(typeOfAction);
            }
        }
    }
}




