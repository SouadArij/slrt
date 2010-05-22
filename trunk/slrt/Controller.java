package slrt;


import fxgui.GUIInterface;
import java.util.Random;
import javafx.reflect.FXClassType;
import javafx.reflect.FXLocal;
import javafx.reflect.FXLocal.Context;
import javafx.reflect.FXLocal.ObjectValue;
/**
 *
 * @author Nick
 */
public class Controller {
    private OpticalModel model;
    //private GUI view;
    private Thread opticalModelThread;
    private Thread viewThread;
   // private FXByInterface fxByInterface;
    private Context context = FXLocal.getContext();
    private FXClassType instance = context.findClass("fxgui.GUINewTest");
    private ObjectValue obj = (ObjectValue)instance.newInstance();
    public GUIInterface gi = (GUIInterface)obj.asObject();

    public Controller() {

        this.model = new OpticalModel(this);
        //this.view = new GUI(model);
        gi.GUI(model);
        //this.view.setVisible(true);
       this.opticalModelThread = new Thread(this.model);
        //this.viewThread = new Thread(this.view);
        gi.run();
        model.addObserver(gi);
        this.opticalModelThread.start();
        //this.viewThread.start();
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

    public void getNextImage(){
     //view.setWordDrawn(model.getImages().get(generateRandomInteger(0,model.getImagesNumber()-1)));
    }
}



