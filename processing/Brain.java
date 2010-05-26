
package processing;

import Data.DBImage;
import Data.GrayImageAndHistogram;
import Data.Letter;
import Data.Point2D;
import Data.Shape;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import slrt.OpticalModel;
import java.util.Random;
import java.util.Vector;

public class Brain implements Runnable {
    
    private final Object lockObject = new Object();

    private OpticalModel parentOpticalModel;
    private EyeWebcam siblingEye;
    private boolean capturedImageChanged;
    private int result;
    

    public Brain(OpticalModel om) {
        this.parentOpticalModel = om;
        
        this.capturedImageChanged = false;
        this.result = -1;        
    }

    /*
     * This is called from the sibling Eye.
     * Whenever a new image was captured by the cam, this is set.
     * It means that at the next iteration, the brain _will have to_
     * process a new image. That then can be get from the eye.
     */
    public void setCapturedImageChanged() {
        synchronized (this.lockObject) {
            this.capturedImageChanged = true;
        }
    }

    public int getResult() {
        return this.result;
    }
    
 
    @Override
    public void run() {
        Random r = new Random();
        int currentResult = this.result;

        while (true) {

            if (this.capturedImageChanged) {
                synchronized (this.lockObject) {
                    this.capturedImageChanged = false;
                }


                /*
                 * This is where the Algorithm method will be called (possibly other classes involded).
                 * For now, instead a simple sleep will be called for simulation purposes.
                 */
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                /*
                 * Let's see.
                 * Processing and information aquiring part.
                 */
                BufferedImage capturedIm = this.siblingEye.getImage();
                double ar = 0.0;
                //double ar = (double) EyeWebcam.HAND_CUT_X2 - EyeWebcam.HAND_CUT_X1 / EyeWebcam.HAND_CUT_Y2 - EyeWebcam.HAND_CUT_Y1;
                int camImWidth, camImHeight;
                if (ar < 1.333) {
                    camImWidth = (int) Math.round(DBImage.DB_IMAGE_HEIGHT * ar);
                    camImHeight = DBImage.DB_IMAGE_HEIGHT;
                } else {
                    camImWidth = DBImage.DB_IMAGE_WIDTH;
                    camImHeight = (int) Math.round(DBImage.DB_IMAGE_WIDTH / ar);
                }
                int[][] cutResizedGrayIntIm = ImageAlgorithms.buffIm2CutGrayResizedIntIm(capturedIm, EyeWebcam.HAND_CUT_X1, EyeWebcam.HAND_CUT_Y1, EyeWebcam.HAND_CUT_X2, EyeWebcam.HAND_CUT_Y2, camImWidth, camImHeight);
                GrayImageAndHistogram contourIntImAndHistogram = ImageAlgorithms.grayIntIm2ContourImAndHistogram(cutResizedGrayIntIm, DBImage.CONTOUR_POWER);
                int[][] contourIntIm = contourIntImAndHistogram.getGrayImage();
                int[] histogram = contourIntImAndHistogram.getHistogram();
                int treshold = ImageAlgorithms.computeNecessaryThreshold(DBImage.WHITE_PROPORTION, histogram, camImWidth * camImHeight);
                boolean[][] camBoolIm = ImageAlgorithms.grayIntIm2BoolIm(contourIntIm, treshold);
                Shape greatestShape = ImageAlgorithms.findGreatestShape(camBoolIm);

                Point2D leftMostPoint = greatestShape.getLeftMostPoint();
                Point2D rightMostPoint = greatestShape.getRightMostPoint();
                Point2D bottomMostPoint = greatestShape.getBottomMostPoint();
                Point2D topMostPoint = greatestShape.getTopMostPoint();

                Shape leftShape = ImageAlgorithms.reduceShapeHVAreaLimit(greatestShape, leftMostPoint, DBImage.HV_THINNESS, true, DBImage.HV_AREA_LIMIT);
                Shape rightShape = ImageAlgorithms.reduceShapeHVAreaLimit(greatestShape, rightMostPoint, DBImage.HV_THINNESS, true, DBImage.HV_AREA_LIMIT);
                Shape topShape = ImageAlgorithms.reduceShapeHVAreaLimit(greatestShape, topMostPoint, DBImage.HV_THINNESS, false, DBImage.HV_AREA_LIMIT);
                Shape bottomShape = ImageAlgorithms.reduceShapeHVAreaLimit(greatestShape, bottomMostPoint, DBImage.HV_THINNESS, false, DBImage.HV_AREA_LIMIT);

                int camLeftShapeCenter = leftShape.getCenter().getX();
                int camTopShapeCenter = topShape.getCenter().getY();
                int camShapeWidth = rightShape.getCenter().getX() - camLeftShapeCenter;
                int camShapeHeight = bottomShape.getCenter().getY() - camTopShapeCenter;

                /*
                 * Comparation part.
                 */
                //Vector<Letter> letters = this.parentOpticalModel.getLetters();
                Vector<Letter> letters = null;
                Iterator<Letter> itlt = letters.iterator();
                int letterIndex = 0;
                while (itlt.hasNext()) {
                    Letter letter = itlt.next();
                    Vector<DBImage> dbIms = letter.getDBImages();
                    Iterator<DBImage> itim = dbIms.iterator();
                    int imIndex = 0;
                    while (itim.hasNext()) {
                        DBImage dbIm = itim.next();

                        boolean[][] dbBoolIm = dbIm.getRaster();
                        int dbLeftShapeCenter = dbIm.getLeftShapeCenter();
                        int dbTopShapeCenter = dbIm.getTopShapeCenter();
                        int dbShapeWidth = dbIm.getShapeWidth();
                        int dbShapeHeight = dbIm.getShapeHeight();
                        boolean[][] transZoomedBoolIm = ImageAlgorithms.transZoomBoolIm(camBoolIm, dbLeftShapeCenter - camLeftShapeCenter, dbTopShapeCenter - camTopShapeCenter, (double) dbShapeWidth / camShapeWidth, (double) dbShapeHeight / camShapeHeight, camLeftShapeCenter, camTopShapeCenter);

                        double match = ImageAlgorithms.compareTwoBoolIms(camBoolIm, dbBoolIm);

                        imIndex++;
                    }
                    letterIndex++;
                }

                
                //dbIm = new DBImage(boolIm, shapeWidth, shapeHeight, camLeftShapeCenter, camTopShapeCenter, angles);
              
                currentResult = r.nextInt(100);
            }
            
            if (currentResult != this.result) {
                
                this.result = currentResult;
                this.parentOpticalModel.setBrainResultChanged();
            }

            /*
             * This is the normal sleep that relaxes the CPU a bit and
             * lets other threads process fluently.
             */
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }    
}

