package processing;

import slrt.OpticalModel;
import com.lti.civil.CaptureDeviceInfo;
import com.lti.civil.CaptureException;
import com.lti.civil.CaptureObserver;
import com.lti.civil.CaptureStream;
import com.lti.civil.CaptureSystem;
import com.lti.civil.CaptureSystemFactory;
import com.lti.civil.DefaultCaptureSystemFactorySingleton;
import com.lti.civil.Image;
import com.lti.civil.awt.AWTImageConverter;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImagingOpException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nick
 */
public class EyeWebcam {

    private OpticalModel parentOpticalModel;
    private BufferedImage image;
    private Brain siblingBrain;
    private CaptureStream captureStream;
    private CaptureSystem captureSystem;

    public EyeWebcam(OpticalModel om, Brain br) {
        this.siblingBrain = br;
        this.parentOpticalModel = om;
        this.image = null;

        CaptureSystemFactory captureFactory = DefaultCaptureSystemFactorySingleton.instance();
        try {
            this.captureSystem = captureFactory.createCaptureSystem();
            this.captureSystem.init();
            List deviceList = captureSystem.getCaptureDeviceInfoList();
            Iterator<CaptureDeviceInfo> dlit = deviceList.iterator();
            System.out.println("Connected capture device:");
            if (dlit.hasNext()) {
                //dlit.next();
                CaptureDeviceInfo info = (CaptureDeviceInfo) dlit.next();
                System.out.println(info.getDescription());
                CaptureStream cs = captureSystem.openCaptureDeviceStream(info.getDeviceID());
                cs.setObserver(new MyCaptureObserver(this));
                this.captureStream = cs;
                this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            }
            System.out.println();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        try {
            if (this.captureStream != null) {
                this.captureStream.start();
            }
        } catch (CaptureException ex) {
            Logger.getLogger(EyeWebcam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        try {
            this.captureStream.stop();
        } catch (CaptureException ex) {
            Logger.getLogger(EyeWebcam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage newImage) {
        this.image = newImage;
    }

    public void notifyWaitingComponents() {
        siblingBrain.setCapturedImageChanged();
        parentOpticalModel.setChanged(true);

    }

    private class MyCaptureObserver implements CaptureObserver {

        public MyCaptureObserver(EyeWebcam e) {
        }

        @Override
        public void onNewImage(CaptureStream arg0, Image arg1) {
            BufferedImageOp op = new AffineTransformOp(
                    AffineTransform.getScaleInstance(0.5, 0.5),
                    new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC));

            BufferedImage bi = AWTImageConverter.toBufferedImage(arg1);
            if (bi != null) {
                setImage(bi);
                notifyWaitingComponents();
            }

        }

        @Override
        public void onError(CaptureStream arg0, CaptureException arg1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}

