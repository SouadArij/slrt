
package processing;

import Data.DBImage;
import Data.GrayImageAndHistogram;
import Data.Point2D;
import Data.Shape;
import java.awt.image.BufferedImage;
import slrt.OpticalModel;
import java.util.Random;

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
                 */
                BufferedImage capturedIm = this.siblingEye.getImage();
                double ar = (double) GUINewTest.CUT_SIZE_SX2 - GUINewTest.CUT_SIZE_SX1 / CUT_SIZE_SY2 - CUT_SIZE_SY1;
                int camImWidth, camImHeight;
                if (ar < 1.333) {
                    camImWidth = (int) Math.round(DBImage.DB_IMAGE_HEIGHT * ar);
                    camImHeight = DBImage.DB_IMAGE_HEIGHT;
                } else {
                    camImWidth = DBImage.DB_IMAGE_WIDTH;
                    camImHeight = (int) Math.round(DBImage.DB_IMAGE_WIDTH / ar);
                }
                int[][] cutResizedGrayIntIm = ImageAlgorithms.buffIm2cutGrayResizedIntIm(capturedIm, GUINewTest.CUT_SIZE_SX1, GUINewTest.CUT_SIZE_SY1, GUINewTest.CUT_SIZE_SX2, GUINewTest.CUT_SIZE_SY2, camImWidth, camImHeight);              
                GrayImageAndHistogram contourIntImAndHistogram = ImageAlgorithms.grayIntIm2contourImAndHistogram(resizedGrayIntIm, CONTOUR_POWER);
                int[][] contourIntIm = contourIntImAndHistogram.getGrayImage();
                int[] histogram = contourIntImAndHistogram.getHistogram();
                int treshold = ImageAlgorithms.computeNecessaryThreshold(DBImage.WHITE_PROPORTION, histogram, camImWidth * camImHeight);
                boolean[][] boolIm = ImageAlgorithms.grayIntIm2boolIm(contourIntIm, treshold);
                Shape greatestShape = ImageAlgorithms.findGreatestShape(boolIm);

                Point2D leftMostPoint = greatestShape.getLeftMostPoint();
                Point2D rightMostPoint = greatestShape.getRightMostPoint();
                Point2D bottomMostPoint = greatestShape.getBottomMostPoint();
                Point2D topMostPoint = greatestShape.getTopMostPoint();

                leftShape = ImageAlgorithms.reduceShapeHVAreaLimit(greatestShape, leftMostPoint, HV_THINNESS, true, HV_AREA_LIMIT);
                rightShape = ImageAlgorithms.reduceShapeHVAreaLimit(greatestShape, rightMostPoint, HV_THINNESS, true, HV_AREA_LIMIT);
                topShape = ImageAlgorithms.reduceShapeHVAreaLimit(greatestShape, topMostPoint, HV_THINNESS, false, HV_AREA_LIMIT);
                bottomShape = ImageAlgorithms.reduceShapeHVAreaLimit(greatestShape, bottomMostPoint, HV_THINNESS, false, HV_AREA_LIMIT);

                leftMostCoord = leftMostPoint.getX();
                topMostCoord = topMostPoint.getY();
                leftShapeCenter = leftShape.getCenter().getX();
                topShapeCenter = topShape.getCenter().getY();

                shapeWidth = rightShape.getCenter().getX() - leftShapeCenter;
                shapeHeight = bottomShape.getCenter().getY() - topShapeCenter;

                dbIm = new DBImage(boolIm, shapeWidth, shapeHeight, leftShapeCenter, topShapeCenter, angles);
              
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

