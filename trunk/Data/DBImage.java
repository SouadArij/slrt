package Data;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import processing.ImageAlgorithms;

public class DBImage {

    public static String SIGNATURE = "<Sign Language Recognition and Translation database image file>";
    public static int N_ANGLES = 4;
    public static int DB_IMAGE_WIDTH = 320;
    public static int DB_IMAGE_HEIGHT = 240;
    public static int CONTOUR_POWER = 2;
    public static double WHITE_PROPORTION = 0.04;
    public static int HV_AREA_LIMIT = 200;
    public static int HV_THINNESS = 5;
    public static double RATIO_THINNESS = 8.0;
    public static int OUTER_TRIES = 10;
    public static int INNER_TRIES = 10;
    public static int THIN_AREA_LOWER_LIMIT = 150;
    public static int THIN_AREA_HIGHER_LIMIT = 250;

    public static final int AAA = 10;

    private boolean[][] raster;
    private double[] angles;
    private int shapeHeight;
    private int shapeWidth;
    private int leftShapeCenter;
    private int topShapeCenter;

    private Shape greatestShape;
    private Shape leftShape;
    private Shape rightShape;
    private Shape topShape;
    private Shape bottomShape;
    private Shape[] thinShapes;
    private double[] thinRatios;

    public DBImage(boolean[][] raster, int shapeWidth, int shapeHeight, int leftShapeCenter, int topShapeCenter, double[] angles) {
        this.raster = raster;
        this.angles = angles;
        this.shapeHeight = shapeHeight;
        this.shapeWidth = shapeWidth;
        this.leftShapeCenter = leftShapeCenter;
        this.topShapeCenter = topShapeCenter;
    }

    public boolean[][] getRaster() {
        return this.raster;
    }

    public double[] getAngles() {
        return this.angles;
    }

    public int getLeftShapeCenter() {
        return this.leftShapeCenter;
    }

    public int getTopShapeCenter() {
        return this.topShapeCenter;
    }

    public int getShapeWidth() {
        return this.shapeWidth;
    }

    public int getShapeHeight() {
        return this.shapeHeight;
    }

    public Shape getGreatestShape() {
        return this.greatestShape;
    }

    public Shape getLeftShape() {
        return this.leftShape;
    }

    public Shape getRightShape() {
        return this.rightShape;
    }

    public Shape getTopShape() {
        return this.topShape;
    }

    public Shape getBottomShape() {
        return this.bottomShape;
    }

    public Shape[] getThinShapes() {
        return this.thinShapes;
    }

    public double[] getThinRatios() {
        return this.thinRatios;
    }

    public void setGreatestShape(Shape s) {
        this.greatestShape = s;
    }

    public void setLeftShape(Shape s) {
        this.leftShape = s;
    }

    public void setRightShape(Shape s) {
        this.rightShape = s;
    }

    public void setTopShape(Shape s) {
        this.topShape = s;
    }

    public void setBottomShape(Shape s) {
        this.bottomShape = s;
    }

    public void setThinShapes(Shape[] s) {
        this.thinShapes = s;
    }

    public void setThinRatios(double[] r) {
        this.thinRatios = r;
    }

    /*
     * Structure:
     * - signature
     * - width
     * - height
     * - shapeWidth
     * - shapeHeight
     * - leftShapeCenter
     * - topShapeCenter
     * - 5 angles
     * - raster image
     */
    public void saveToDisk(File file) {
        int wi = this.raster[0].length, he = this.raster.length;
        DataOutputStream dos;
        int x, y;
        int power = 0;
        int b = 0;

        try {
            dos = new DataOutputStream(new FileOutputStream(file));
            dos.writeBytes(SIGNATURE);
            dos.writeInt(wi);
            dos.writeInt(he);
            dos.writeInt(this.shapeWidth);
            dos.writeInt(this.shapeHeight);
            dos.writeInt(this.leftShapeCenter);
            dos.writeInt(this.topShapeCenter);
            for (int i = 0; i < this.angles.length; i++) {
                dos.writeDouble(this.angles[0]);
            }

            y = 0;
            while (y < he) {
                x = 0;
                while (x < wi) {
                    switch (power) {
                        case 0:
                            if (this.raster[y][x]) {
                                b |= 1;
                            }
                            break;
                        case 1:
                            if (this.raster[y][x]) {
                                b |= 2;
                            }
                            break;
                        case 2:
                            if (this.raster[y][x]) {
                                b |= 4;
                            }
                            break;
                        case 3:
                            if (this.raster[y][x]) {
                                b |= 8;
                            }
                            break;
                        case 4:
                            if (this.raster[y][x]) {
                                b |= 16;
                            }
                            break;
                        case 5:
                            if (this.raster[y][x]) {
                                b |= 32;
                            }
                            break;
                        case 6:
                            if (this.raster[y][x]) {
                                b |= 64;
                            }
                            break;
                        case 7:
                            if (this.raster[y][x]) {
                                b |= 128;
                            }
                            break;
                    }

                    power++;
                    if (power > 7) {
                        dos.writeByte(b);

                        power = 0;
                        b = 0;
                    }
                    x++;
                }
                y++;
            }

            dos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static DBImage loadFromDisk(File file) {
        int wi, he;
        boolean[][] boolIm = null;
        byte[] bSignature = new byte[SIGNATURE.length()];
        DataInputStream dis;
        int x, y;
        int power = 0;
        int b = 0, q = 0;

        int shapeWidth = 0;
        int shapeHeight = 0;
        int leftShapeCenter = 0;
        int topShapeCenter = 0;
        double[] angles = new double[N_ANGLES];

        try {
            dis = new DataInputStream(new FileInputStream(file));
            dis.read(bSignature, 0, SIGNATURE.length());
            if (!(new String(bSignature)).equals(SIGNATURE)) {
                return null;
            }

            wi = dis.readInt();
            he = dis.readInt();
            shapeWidth = dis.readInt();
            shapeHeight = dis.readInt();
            leftShapeCenter = dis.readInt();
            topShapeCenter = dis.readInt();
            for (int i = 0; i < N_ANGLES; i++) {
                angles[i]= dis.readDouble();
            }
            boolIm = new boolean[he][wi];

            b = dis.readByte();
            y = 0;
            while (y < he) {
                x = 0;
                while (x < wi) {

                    if (power > 7) {
                        power = 0;
                        b = dis.read();
                    }

                    switch (power) {
                        case 0:
                            q = (b & 1);
                            break;
                        case 1:
                            q = (b & 2);
                            break;
                        case 2:
                            q = (b & 4);
                            break;
                        case 3:
                            q = (b & 8);
                            break;
                        case 4:
                            q = (b & 16);
                            break;
                        case 5:
                            q = (b & 32);
                            break;
                        case 6:
                            q = (b & 64);
                            break;
                        case 7:
                            q = (b & 128);
                            break;
                    }

                    if (q > 0) {
                        boolIm[y][x] = true;
                    } else {
                        boolIm[y][x] = false;
                    }

                    power++;
                    x++;
                }
                y++;
            }

            dis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        DBImage dbIm = new DBImage(boolIm, shapeWidth, shapeHeight, leftShapeCenter, topShapeCenter, angles);
        return dbIm;
    }

    public static DBImage loadRawFromDisk(File file) {
        BufferedImage rawIm = null;
        DBImage dbIm = null;
        int leftMostCoord;
        int topMostCoord;
        int leftShapeCenter;
        int topShapeCenter;
        int shapeWidth;
        int shapeHeight;
        Shape leftShape;
        Shape rightShape;
        Shape topShape;
        Shape bottomShape;
        Point2D leftMostPoint;
        Point2D rightMostPoint;
        Point2D bottomMostPoint;
        Point2D topMostPoint;
        int[][] resizedGrayIntIm;
        GrayImageAndHistogram contourIntImAndHistogram;
        int[][] contourIntIm;
        int[] histogram;
        int treshold;
        boolean[][] boolIm;
        boolean[][] boolIm2;
        Shape greatestShape;
        int dbImWidth, dbImHeight;
        double ar;

        try {
            rawIm = ImageIO.read(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ar = (double) rawIm.getWidth() / rawIm.getHeight();
        if (ar < 1.333) {
            dbImWidth = (int) Math.round(DB_IMAGE_WIDTH * ar);
            dbImHeight = DB_IMAGE_HEIGHT;
        } else {
            dbImWidth = DB_IMAGE_WIDTH;
            dbImHeight = (int) Math.round(DB_IMAGE_HEIGHT / ar);
        }

        resizedGrayIntIm = ImageAlgorithms.buffIm2GrayResizedIntIm(rawIm, dbImWidth, dbImHeight);
        contourIntImAndHistogram = ImageAlgorithms.grayIntIm2ContourImAndHistogram(resizedGrayIntIm, CONTOUR_POWER);
        contourIntIm = contourIntImAndHistogram.getGrayImage();
        histogram = contourIntImAndHistogram.getHistogram();
        treshold = ImageAlgorithms.computeNecessaryThreshold(WHITE_PROPORTION, histogram, dbImWidth * dbImHeight);
        boolIm = ImageAlgorithms.grayIntIm2BoolIm(contourIntIm, treshold);
        boolIm2 = boolIm.clone();
        greatestShape = ImageAlgorithms.findGreatestShape(boolIm2);

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

        dbIm = new DBImage(boolIm, shapeWidth, shapeHeight, leftShapeCenter, topShapeCenter, null);
        return dbIm;
    }

    public static DBImage getInstanceFromRawMemory(BufferedImage rawIm) {
        DBImage dbIm = null;
        int leftMostCoord;
        int topMostCoord;
        int leftShapeCenter;
        int topShapeCenter;
        int shapeWidth;
        int shapeHeight;
        Shape leftShape;
        Shape rightShape;
        Shape topShape;
        Shape bottomShape;
        Point2D leftMostPoint;
        Point2D rightMostPoint;
        Point2D bottomMostPoint;
        Point2D topMostPoint;
        int[][] resizedGrayIntIm;
        GrayImageAndHistogram contourIntImAndHistogram;
        int[][] contourIntIm;
        int[] histogram;
        int treshold;
        boolean[][] boolIm;
        Shape greatestShape;
        int dbImWidth, dbImHeight;
        double ar;

        ar = (double) rawIm.getWidth() / rawIm.getHeight();
        if (ar < 1.333) {
            dbImWidth = (int) Math.round(DB_IMAGE_HEIGHT * ar);
            dbImHeight = DB_IMAGE_HEIGHT;
        } else {
            dbImWidth = DB_IMAGE_WIDTH;
            dbImHeight = (int) Math.round(DB_IMAGE_WIDTH / ar);
        }

        resizedGrayIntIm = ImageAlgorithms.buffIm2GrayResizedIntIm(rawIm, dbImWidth, dbImHeight);
        contourIntImAndHistogram = ImageAlgorithms.grayIntIm2ContourImAndHistogram(resizedGrayIntIm, CONTOUR_POWER);
        contourIntIm = contourIntImAndHistogram.getGrayImage();
        histogram = contourIntImAndHistogram.getHistogram();
        treshold = ImageAlgorithms.computeNecessaryThreshold(WHITE_PROPORTION, histogram, dbImWidth * dbImHeight);
        boolIm = ImageAlgorithms.grayIntIm2BoolIm(contourIntIm, treshold);
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

        /*
         * Not necessary, but there.
         */
        dbIm.setGreatestShape(greatestShape);
        dbIm.setLeftShape(leftShape);
        dbIm.setRightShape(rightShape);
        dbIm.setTopShape(topShape);
        dbIm.setBottomShape(bottomShape);
        dbIm.setThinShapes(thinShapes);
        dbIm.setThinRatios(thinRatios);
        return dbIm;
    }

}
