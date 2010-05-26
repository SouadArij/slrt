
package processing;

import slrt.OpticalModel;
import java.util.Random;

public class Brain implements Runnable {
    
    private final Object lockObject = new Object();

    private OpticalModel parentOpticalModel;
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
               ar = (double) rawIm.getWidth() / rawIm.getHeight();
                if (ar < 1.333) {
                    dbImWidth = (int) Math.round(DB_IMAGE_HEIGHT * ar);
                    dbImHeight = DB_IMAGE_HEIGHT;
                } else {
                    dbImWidth = DB_IMAGE_WIDTH;
                    dbImHeight = (int) Math.round(DB_IMAGE_WIDTH / ar);
                }

                resizedGrayIntIm = ImageAlgorithms.buffIm2GrayResizedIntIm(rawIm, dbImWidth, dbImHeight);
                contourIntImAndHistogram = ImageAlgorithms.grayIntIm2contourImAndHistogram(resizedGrayIntIm, CONTOUR_POWER);
                contourIntIm = contourIntImAndHistogram.getGrayImage();
                histogram = contourIntImAndHistogram.getHistogram();
                treshold = ImageAlgorithms.computeNecessaryThreshold(WHITE_PROPORTION, histogram, dbImWidth * dbImHeight);
                boolIm = ImageAlgorithms.grayIntIm2boolIm(contourIntIm, treshold);
                greatestShape = ImageAlgorithms.findGreatestShape(boolIm);

                Shape[] thinShapes = ImageAlgorithms.findThinShapes(greatestShape, N_ANGLES, OUTER_TRIES, INNER_TRIES, RATIO_THINNESS, THIN_AREA_LOWER_LIMIT, THIN_AREA_HIGHER_LIMIT);
                double[] angles = {0.0, 0.0, 0.0, 0.0, 0.0};
                double[] thinRatios = {1.0, 1.0, 1.0, 1.0, 1.0};
                if ((thinShapes != null)) {
                    //angles = new double[5];
                    for (int i = 0; i < thinShapes.length; i++) {
                        if (thinShapes[i] != null) {
                            angles[i] = thinShapes[i].getElongation();
                            thinRatios[i] = thinShapes[i].getAspectRatio();
                        }
                    }
                }

                leftMostPoint = greatestShape.getLeftMostPoint();
                rightMostPoint = greatestShape.getRightMostPoint();
                bottomMostPoint = greatestShape.getBottomMostPoint();
                topMostPoint = greatestShape.getTopMostPoint();

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

