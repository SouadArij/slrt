package SLRTr;


import fxgui.GUIInterface;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javafx.reflect.FXClassType;
import javafx.reflect.FXLocal;
import javafx.reflect.FXLocal.Context;
import javafx.reflect.FXLocal.ObjectValue;
/**
 *
 * DEPRECATED
 */

public class Controller {
    private SLRTModel model;
    //private GUI view;
    private Thread opticalModelThread;
    private Thread viewThread;
   // private FXByInterface fxByInterface;
    private Context context = FXLocal.getContext();
    private FXClassType instance = context.findClass("fxgui.GUINewTest");
    private ObjectValue obj = (ObjectValue)instance.newInstance();
    public GUIInterface gi = (GUIInterface)obj.asObject();

 /**
 * Class contructor.
 */
    public Controller() {

        //this.model = new SLRTModel(this);

        //this.view = new GUI(model);
        gi.GUI(model);
        //this.view.setVisible(true);
       this.opticalModelThread = new Thread(this.model);
        //this.viewThread = new Thread(this.view);
        gi.myRun();
      //  model.addObserver(gi);
      
        this.opticalModelThread.start();
        //this.viewThread.start();
    }

  

 
}



