package fxgui;

import java.awt.image.BufferedImage;
import java.util.Observer;
import slrt.OpticalModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Trishk
 */
public interface GUIInterface extends Observer {
    //public void getImage();
    public void run();
    public void GUI(OpticalModel m);

   // public void setDisplayedString(String displayedWord, boolean b);

   // public void setWordImage(BufferedImage wordImage);

}
