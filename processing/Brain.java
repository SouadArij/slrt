package processing;

import Data.DBImage;
import Data.GrayImageAndHistogram;
import Data.Letter;
import Data.Point2D;
import Data.Shape;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import SLRTr.SLRTModel;
import java.awt.Color;
import java.util.Random;
import java.util.Vector;

public class Brain implements Runnable {

    private static final double MINIMUM_RATE_NEEDED_TO_MATCH = 0.62;
    private static final int MINIMUM_NUMBER_NEEDED_TO_MATCH = 0;
    /* Synchronization purposes.
     * Can not write <capturedImageChanged> by two different
     * threads at the same time. Yes I'm smart :>
     */
    private final Object lockObject = new Object();
    private SLRTModel parentModel;

    /* Having the sibling eye makes us able to get the captured image
     * and process it.
     */
    private EyeWebcam siblingEye;

    /* Shows whether the eye captured a new image or not
     * If yes, we will process and compare that soon.
     */
    private boolean capturedImageChanged;
    private Boolean algorithmRunning;
    private int result;

    private BufferedImage processedImage;

    /**
     * Class contructor. The om argument specifies the SLRTModel that controlls this Brain.
     *
     * @param  model  The refrence of the OpticalModel that this Brain communicates with
     *
     */
    public Brain(SLRTModel model) {
        this.parentModel = model;

        this.capturedImageChanged = false;
        this.algorithmRunning = false;

        this.result = -1;
    }

    /**
     * Sets the EyeWebcam that gives a BufferedImage to brain for analysys
     * @param eye
     *
     */
    public void setSiblingEye(EyeWebcam e) {
        this.siblingEye = e;
    }

    /**
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

    /**
     * Returns immediatly, whether or not the int is null.
     * @return      the int corresponding in Sign Language(Romanian Sign Language) to the letter shown in analyzed image
     *
     */
    public int getResult() {
        return this.result;
    }

    public BufferedImage getProcessedImage() {
        if (this.processedImage == null) {
            System.out.format("Brain: null processedImage was get.\n");
        }
        return this.processedImage;
    }

    /**
     * Returns true if this Brain is running, else false
     * @return true if this Brain is running, else false
     */
    public Boolean checkAlgorithmRunning() {
        return this.algorithmRunning;
    }

    /**
     * Commands this Brain to resume running
     */
    public void startAlgorithmRunning() {
        this.algorithmRunning = true;
    }

    /**
     * Commands this Brain to suspend 
     */
    public void stopAlgorithmRunning() {
        this.algorithmRunning = false;
    }

    /**
     * Overrides the Runnable run method. Checks an image to see if it contains a Sign Language letter and
     * identifies the letter if true.
     */
    @Override
    public void run() {
        Random r = new Random();
        int currentResult = this.result;

        while (true) {

            /*
             * This is the normal sleep that relaxes the CPU a bit and
             * lets other threads run fluently.
             */
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            while (this.algorithmRunning) {

                if (this.capturedImageChanged) {
                    synchronized (this.lockObject) {
                        this.capturedImageChanged = false;
                    }

                    /*
                     * This is where the Algorithm method will be called (possibly other classes involded).
                     * For now, instead a simple sleep will be called as simulation.
                     *
                     *   try {
                     *       Thread.sleep(5000);
                     *   } catch (InterruptedException ex) {
                     *       ex.printStackTrace();
                     *   }
                     */

                    /*
                     * Let's see.
                     * Processing and information aquiring part.
                     */
                    if (this.siblingEye == null) {
                        continue;
                    }
                    BufferedImage capturedIm = this.siblingEye.getImage();
                    System.out.format("Brain: capIm's with is %d and height is %d\n", capturedIm.getWidth(), capturedIm.getHeight());
                    if (capturedIm == null) {
                        continue;
                    }
                    double ar = (double) (EyeWebcam.HAND_CUT_X2 - EyeWebcam.HAND_CUT_X1) / (EyeWebcam.HAND_CUT_Y2 - EyeWebcam.HAND_CUT_Y1);
                    System.out.format("Brain: aspect ratio = %.2f\n", ar);
                    int camImWidth, camImHeight;
                    int cutX1 = EyeWebcam.HAND_CUT_X1;
                    int cutX2 = EyeWebcam.HAND_CUT_X2;
                    int cutY1 = EyeWebcam.HAND_CUT_Y1;
                    int cutY2 = EyeWebcam.HAND_CUT_Y2;
                    if (ar < DBImage.DB_IMAGE_ASPECT_RATIO) {
                        camImWidth = (int) Math.round(DBImage.DB_IMAGE_HEIGHT * ar);
                        camImHeight = DBImage.DB_IMAGE_HEIGHT;
                        double cutHeight = (EyeWebcam.HAND_CUT_Y2 - EyeWebcam.HAND_CUT_Y1 - (EyeWebcam.HAND_CUT_X2 - EyeWebcam.HAND_CUT_X1) / DBImage.DB_IMAGE_ASPECT_RATIO) / 2;
                        cutY1 = (int) (EyeWebcam.HAND_CUT_Y1 + cutHeight);
                        cutY2 = (int) (EyeWebcam.HAND_CUT_Y2 - cutHeight);
                    } else {
                        camImWidth = DBImage.DB_IMAGE_WIDTH;
                        camImHeight = (int) Math.round(DBImage.DB_IMAGE_WIDTH / ar);
                        double cutWidth = (EyeWebcam.HAND_CUT_X2 - EyeWebcam.HAND_CUT_X1 - (EyeWebcam.HAND_CUT_Y2 - EyeWebcam.HAND_CUT_Y1) * DBImage.DB_IMAGE_ASPECT_RATIO) / 2;
                        cutX1 = (int) (EyeWebcam.HAND_CUT_X1 + cutWidth);
                        cutX2 = (int) (EyeWebcam.HAND_CUT_X2 - cutWidth);
                    }                    
                    System.out.format("Brain: camImWidth = %d, camImHeight = %d\n", camImWidth, camImHeight);
                    System.out.format("Brain: cutX2 = %d, cutX1 = %d, cutY2 = %d, cutY1 = %d\n", cutX2, cutX1, cutY2, cutY1);
                    System.out.format("Brain: cutX2 - cutX1 / cutY2 - cutY1 = %.3f\n", (double) (cutX2 - cutX1) / (cutY2 - cutY1));
                    int[][] cutResizedGrayIntIm = ImageAlgorithms.buffIm2CutGrayResizedIntIm(capturedIm, cutX1, cutY1, cutX2, cutY2, DBImage.DB_IMAGE_WIDTH, DBImage.DB_IMAGE_HEIGHT);
                    if (cutResizedGrayIntIm == null) {
                        continue;
                    }
                    GrayImageAndHistogram contourIntImAndHistogram = ImageAlgorithms.grayIntIm2ContourImAndHistogram(cutResizedGrayIntIm, DBImage.CONTOUR_POWER);
                    int[][] contourIntIm = contourIntImAndHistogram.getGrayImage();
                    int[] histogram = contourIntImAndHistogram.getHistogram();
                    int threshold = ImageAlgorithms.computeNecessaryThreshold(DBImage.WHITE_PROPORTION, histogram, camImWidth * camImHeight);
                    System.out.format("Brain: threshold = %d\n", threshold);
                    boolean[][] camBoolIm = ImageAlgorithms.grayIntIm2BoolIm(contourIntIm, threshold);
                    this.processedImage = ImageAlgorithms.boolIm2BuffIm(camBoolIm, Color.BLACK.getRGB(), Color.WHITE.getRGB());
                    Shape greatestShape = ImageAlgorithms.findGreatestShape(camBoolIm);
                    System.out.format("Brain: greatestShape's area is %d\n", greatestShape.getArea());

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
                    System.out.format("Brain: camLeftShapeCenter = %d, camTopShapeCenter = %d, camShapeWidth = %d, camShapeHeight = %d\n", camLeftShapeCenter, camTopShapeCenter, camShapeWidth, camShapeHeight);

                    /* Comparison part.
                     * Similar to that sucky algorithm Iulia showed me.
                     * (The algorithm is sucky not Iulia)
                     * BTW talkin'bout Iulia M., not Iulia P. cause Iulia P. _is_ forsure.
                     */
                    Vector<Letter> letters = this.parentModel.getLetters();
                    if (letters == null) {
                        continue;
                    }
                    Iterator<Letter> itlt = letters.iterator();
                    int letterIndex = 0;
                    int maxMatched = 0;
                    int maxMatchedIndex = 0;
                    while (itlt.hasNext()) {
                        Letter letter = itlt.next();
                        Vector<DBImage> dbIms = letter.getDBImages();
                        if (dbIms == null) {
                            continue;
                        }
                        Iterator<DBImage> itim = dbIms.iterator();
                        int imIndex = 0;
                        int nMatched = 0;
                        while (itim.hasNext()) {
                            DBImage dbIm = itim.next();

                            boolean[][] dbBoolIm = dbIm.getRaster();
                            System.out.format("dbBoolIm's width and heigth is %d and %d\n", dbBoolIm[0].length, dbBoolIm.length);
                            int dbLeftShapeCenter = dbIm.getLeftShapeCenter();
                            int dbTopShapeCenter = dbIm.getTopShapeCenter();
                            int dbShapeWidth = dbIm.getShapeWidth();
                            int dbShapeHeight = dbIm.getShapeHeight();
                            System.out.format("Brain: dbLeftShapeCenter = %d, dbTopShapeCenter = %d, dbShapeWidth = %d, dbShapeHeight = %d\n", dbLeftShapeCenter, dbTopShapeCenter, dbShapeWidth, dbShapeHeight);
                            boolean[][] transZoomedBoolIm = ImageAlgorithms.transZoomBoolIm(camBoolIm, dbLeftShapeCenter - camLeftShapeCenter, dbTopShapeCenter - camTopShapeCenter, (double) dbShapeWidth / camShapeWidth, (double) dbShapeHeight / camShapeHeight, camLeftShapeCenter, camTopShapeCenter);
                            System.out.format("transZoomedBoolIm's width and heigth is %d and %d\n", transZoomedBoolIm[0].length, transZoomedBoolIm.length);

                            double match = ImageAlgorithms.compareTwoBoolIms(transZoomedBoolIm, dbBoolIm);                            
                            if (match >= Brain.MINIMUM_RATE_NEEDED_TO_MATCH) {
                                nMatched++;
                            }

                            imIndex++;
                        }
                        if (nMatched > maxMatched) {
                            maxMatched = nMatched;
                            maxMatchedIndex = letterIndex;
                        }

                        letterIndex++;
                    }
                    System.out.format("Brain: compared with %d letters.\n", letterIndex);

                    /* Evaluating the comparison.
                     */
                    if (maxMatched > Brain.MINIMUM_NUMBER_NEEDED_TO_MATCH) {
                        currentResult = maxMatchedIndex;
                    } else {
                        currentResult = -1;
                    }
                    System.out.format("Brain: currentResult = %d.\n", currentResult);

                    /* For testing:
                     * currentResult = r.nextInt(100);
                     */
                }

                if (currentResult != this.result) {
                    this.result = currentResult;
                    this.parentModel.setBrainResultChanged();
                }
            }
        }
    }
}

