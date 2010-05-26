package fxgui;

import java.awt.image.BufferedImage;
import java.util.Observer;
import SLRTr.SLRTModel;


public interface GUIInterface extends Observer {
    //public void getImage();
    public void myRun();
    public void GUI(SLRTModel m);

   // public void setDisplayedString(String displayedWord, boolean b);

   // public void setWordImage(BufferedImage wordImage);

}
