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
    private Boolean[] resultFromMovementBrain;

    public OpticalModel(Controller c) {
        this.controller = c;
        data = new Vector<Object>();
        this.xmlParser = new XMLParser();
        this.brain = new Brain(this);
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
        this.movementBrain.setNewImage(true);
        this.changedImage = b;
    }

    public boolean imageHasChanged(){
        return this.changedImage;
    }

    public void setNewResultFromBrain(boolean b) {
        this.newResultFromBrain = b;
        resultFromBrain = brain.getResult();
    }

    public void setNewResultFromMovementBrain(boolean b) {
        this.newResultFromMovementBrain = b;
        resultFromMovementBrain = movementBrain.getHighlightedButtons();
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
                this.setChanged();
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




