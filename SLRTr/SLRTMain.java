package SLRTr;

import fxgui.GUIInterface;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javafx.reflect.FXClassType;
import javafx.reflect.FXLocal;
import javafx.reflect.FXLocal.Context;
import javafx.reflect.FXLocal.ObjectValue;

public class SLRTMain {

    public SLRTMain() {
        SLRTModel model = new SLRTModel();
        
        Context context = FXLocal.getContext();
        FXClassType classType = context.findClass("fxgui.GUINewTest");
        ObjectValue objectValue = (ObjectValue) classType.newInstance();
        GUIInterface guiInterface = (GUIInterface) objectValue.asObject();
        guiInterface.GUI(model);        

        Thread modelThread = new Thread(model);
        modelThread.start();
        guiInterface.myRun();
    }
}
