package processing;

import SLRTr.SLRTModel;
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

public class EyeWebcam {

    public static final int HAND_CUT_X1 = 13;
    public static final int HAND_CUT_Y1 = 55;
    public static final int HAND_CUT_X2 = 173;
    public static final int HAND_CUT_Y2 = 155;

    private SLRTModel parentModel;
    private BufferedImage image;
    private Brain siblingBrain;
    private CaptureStream captureStream;
    private CaptureSystem captureSystem;

    /**
     * Class contructor.The om argument specifies the SLRTModel that controlls data from this EyeWebcam.
     * @param om the SLRTModel that controlls data from this object
     *
     */
    public EyeWebcam(SLRTModel om) {        
        this.parentModel = om;
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
                //this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            }
            System.out.println();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setSiblingBrain(Brain b) {
        this.siblingBrain = b;
    }

    /**
     * Launches this EyeWebcam and it starts to capture images.
     */
    public void start() {
        try {
            if (this.captureStream != null) {
                this.captureStream.start();
            }
        } catch (CaptureException ex) {
            Logger.getLogger(EyeWebcam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Stops this EyeWebcam
     */
    public void stop() {
        try {
            this.captureStream.stop();
        } catch (CaptureException ex) {
            Logger.getLogger(EyeWebcam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets the BufferedImage captured by this EyeWebcam
     * @return the BufferedImage captured by this object
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Sets the BufferedImage captured by this EyeWebcam
     * @param newImage the BufferedImaged to be associated
     */
    public void setImage(BufferedImage newImage) {
        this.image = newImage;
    }

    /**
     * Notifies the Brain and the OpticalModel that it has a new BufferedImage
     */
    public void notifyWaitingComponents() {
        siblingBrain.setCapturedImageChanged();
        parentModel.setChanged(true);        
    }

    private class MyCaptureObserver implements CaptureObserver {

         /**
         * Class contructor.The e argument specifies the EyeWebcam captures the images for this MyCaptureObserver.
         * @param e the EyeWebcam captures the images for this object.
         */
        public MyCaptureObserver(EyeWebcam e) {
        }

         /**
         * Overided method. When a new image is captured by the EyeWebcam
         * @param arg0 the current stream
         * @param arg1 the captured image
         */
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

