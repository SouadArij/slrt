package processing;

import Data.GrayImageAndHistogram;
import Data.Point2D;
import Data.Shape;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Random;

public class ImageAlgorithms {

    public static int[][] buffIm2GrayResizedIntIm(BufferedImage buffIm, int destinationWidth, int destinationHeight) {

        if ((buffIm == null) || (destinationWidth <= 0) || (destinationHeight <= 0)) {
            return null;
        }

        int swi = buffIm.getWidth(), she = buffIm.getHeight();
        int[][] dIm = new int[destinationHeight][destinationWidth];
        int sRGB, dColor;
        Color sColor;
        int sxi, syi, dxi, dyi;
        double dx, dy, sxr, syr;

        dx = (double) swi / destinationWidth;
        dy = (double) she / destinationHeight;

        syr = 0;
        syi = 0;
        dyi = 0;
        while (dyi < destinationHeight) {
            sxr = 0;
            sxi = 0;
            dxi = 0;
            while (dxi < destinationWidth) {
                sRGB = buffIm.getRGB(sxi, syi);
                sColor = new Color(sRGB);
                dColor = (sColor.getRed() + sColor.getGreen() + sColor.getBlue()) / 3;
                dIm[dyi][dxi] = dColor;

                sxr += dx;
                sxi = (int) sxr;
                dxi++;
            }
            syr += dy;
            syi = (int) syr;
            dyi++;
        }

        return dIm;
    }

    public static int[][] buffIm2CutGrayResizedIntIm(BufferedImage buffIm, int sx1, int sy1, int sx2, int sy2, int destinationWidth, int destinationHeight) {

        if ((buffIm == null) || (destinationWidth <= 0) || (destinationHeight <= 0)) {
            return null;
        }

        int swi = buffIm.getWidth(), she = buffIm.getHeight();
        if ((sx1 >= swi) || (sx1 < 0) || (sy1 < 0) || (sy1 >= she)
                    || (sx2 >= swi) || (sx2 < 0) || (sy2 >= she) || (sy2 < 0)
                    || (sx1 >= sx2) || (sy1 >= sy2)) {
            return null;
        }

        int[][] dIm = new int[destinationHeight][destinationWidth];
        int sRGB, dColor;
        Color sColor;
        int sxi, syi, dxi, dyi;
        double dx, dy, sxr, syr;

        dx = (double) sx2 - sx1 / destinationWidth;
        dy = (double) sy2 - sy1 / destinationHeight;

        syr = 0;
        syi = 0;
        dyi = 0;
        while (dyi < destinationHeight) {
            sxr = sx1;
            sxi = sx1;
            dxi = 0;
            while (dxi < destinationWidth) {
                sRGB = buffIm.getRGB(sxi, syi);
                sColor = new Color(sRGB);
                dColor = (sColor.getRed() + sColor.getGreen() + sColor.getBlue()) / 3;
                dIm[dyi][dxi] = dColor;

                sxr += dx;
                sxi = (int) sxr;
                dxi++;
            }
            syr += dy;
            syi = (int) syr;
            dyi++;
        }

        return dIm;
    }

    public static GrayImageAndHistogram grayIntIm2ContourImAndHistogram(int[][] grayIntIm, int radius) {

        int wi = grayIntIm[0].length, he = grayIntIm.length;
        int[][] contourIm = new int[he][wi];
        int[] histogram = new int[256];
        int[] neighX = new int[2 * (radius * (radius + 1))], neighY = new int[2 * (radius * (radius + 1))];
        int sGray, nGray;
        int x, y, nx, ny, i, j, diff;
        int diam = radius * 2 + 1, dist;

        j = 0;
        for (y = 0; y < diam; y++) {
            for (x = 0; x < diam; x++) {
                dist = Math.abs(radius - x) + Math.abs(radius - y);
                if ((dist > 0) && (dist <= radius)) {
                    neighX[j] = x - radius;
                    neighY[j] = y - radius;
                    j++;
                }
            }
        }

        for (i = 0; i < 255; i++) {
            histogram[i] = 0;
        }

        for (y = 0; y < he; y++) {
            for (x = 0; x < wi; x++) {
                sGray = grayIntIm[y][x];
                diff = 0;
                j = 0;
                for (i = 0; i < neighX.length; i++) {
                    nx = x + neighX[i];
                    ny = y + neighY[i];
                    if ((nx >= 0) && (nx < wi) && (ny < he) && (ny >= 0)) {
                        nGray = grayIntIm[ny][nx];
                        diff += (int) Math.abs(sGray - nGray);
                        j++;
                    }
                }
                diff /= j;

                contourIm[y][x] = diff;
                histogram[diff]++;
            }
        }

        GrayImageAndHistogram result = new GrayImageAndHistogram(contourIm, histogram);
        return result;
    }

    public static int computeNecessaryThreshold(double aim, int[] histogram, int imArea) {
        int i = 256;
        double sum = 0;

        int limit = (int) Math.round(imArea * aim);
        do {
            i--;
            sum += histogram[i];
        } while ((i > 0) && (sum <= limit));

        return i;
    }

    public static boolean[][] grayIntIm2BoolIm(int[][] grayIntIm, int treshold) {
        int wi = grayIntIm[0].length, he = grayIntIm.length;
        boolean[][] boolIm = new boolean[he][wi];
        int sGray;
        int x, y;

        for (y = 0; y < he; y++) {
            for (x = 0; x < wi; x++) {
                sGray = grayIntIm[y][x];

                if (sGray < treshold) {
                    boolIm[y][x] = false;
                } else {
                    boolIm[y][x] = true;
                }
            }
        }

        return boolIm;
    }

    public static Shape findGreatestShape(boolean[][] boolIm) {
        int wi = boolIm[0].length, he = boolIm.length;
        boolean[][] workingIm = new boolean[he][wi];
        int x, y;

        for (y = 0; y < he; y++) {
            for (x = 0; x < wi; x++) {
                workingIm[y][x] = boolIm[y][x];
            }
        }

        Shape greatestShape = null;
        int maxArea = 0;
        for (y = 0; y < he; y++) {
            for (x = 0; x < wi; x++) {
                if (workingIm[y][x]) {
                    Shape currentShape = ImageAlgorithms.breadthShapeExtendAndCut(workingIm, new Point2D(x, y));
                    int currentArea = currentShape.getArea();
                    if (maxArea < currentArea) {
                        maxArea = currentArea;
                        greatestShape = currentShape;
                    }
                }
            }
        }

        return greatestShape;
    }

    public static Shape breadthShapeExtendAndCut(boolean[][] boolImage, Point2D firstPoint) {
        int i;
        int wi = boolImage[0].length, he = boolImage.length;
        int px, py, nx, ny;
        Point2D head, neighbour;
        ArrayDeque<Point2D> queue;
        int neighx[] = {-1, 0, 1, -1, 1, -1, 0, 1}, neighy[] = {-1, -1, 1, 0, 0, 1, 1, 1};

        Shape shape = new Shape();
        queue = new ArrayDeque();
        queue.offer(firstPoint);
        while (!queue.isEmpty()) {
            head = queue.poll();
            shape.add(head);
            px = head.getX();
            py = head.getY();
            boolImage[py][px] = false;
            for (i = 0; i < 8; i++) {
                nx = px + neighx[i];
                ny = py + neighy[i];
                neighbour = new Point2D(nx, ny);
                if ((nx >= 0) && (nx < wi) && (ny >= 0) && (ny < he)
                        && (boolImage[ny][nx])
                        && (!shape.contains(neighbour)) && (!queue.contains(neighbour))) {
                    queue.offer(neighbour);
                    boolImage[ny][nx] = false;
                }
            }
        }

        return shape;
    }

    public static Shape reduceShapeHVAreaLimit(Shape originalShape, Point2D firstPoint, int sizeLimit, boolean horizontal, int areaLimit) {
        int px, py, nx, ny;
        int neighx[] = {-1, 0, 1, -1, 1, -1, 0, 1}, neighy[] = {-1, -1, 1, 0, 0, 1, 1, 1};

        Shape reducedShape = new Shape();
        ArrayDeque<Point2D> queue = new ArrayDeque();
        if (originalShape == null) {
            return null;
        }
        if (!originalShape.contains(firstPoint)) {
            return null;
        }
        queue.offer(firstPoint);
        while (true) {
            Point2D head = queue.poll();
            reducedShape.add(head);
            int reducedArea = reducedShape.getArea();
            Point2D shapeCenter = reducedShape.getCenter();
            px = head.getX();
            py = head.getY();
            for (int i = 0; i < 8; i++) {
                nx = px + neighx[i];
                ny = py + neighy[i];
                Point2D neighbour = new Point2D(nx, ny);
                if ((originalShape.contains(neighbour))
                        && (!reducedShape.contains(neighbour)) && (!queue.contains(neighbour))
                        && (queue.size() + reducedArea < areaLimit)
                        && (((horizontal) && (Math.abs(nx - shapeCenter.getX()) < sizeLimit))
                        || ((!horizontal) && (Math.abs(ny - shapeCenter.getY()) < sizeLimit)))
                        ) {
                    queue.offer(neighbour);
                }
            }

            if ((reducedArea > areaLimit) || (queue.isEmpty())) {
                break;
            }
        }

        return reducedShape;
    }

    public static boolean[][] shapes2BoolIm(Shape[] shapes, int wi, int he) {
        boolean[][] boolImage = new boolean[he][wi];
        for (int shapeIndex = 0; shapeIndex < shapes.length; shapeIndex++) {
            if (shapes[shapeIndex] == null) {
                continue;
            }
            Iterator<Point2D> itpt = shapes[shapeIndex].iterator();
            while (itpt.hasNext()) {
                Point2D pt = itpt.next();
                boolImage[pt.getY()][pt.getX()] = true;
            }
        }

        return boolImage;
    }

    public static BufferedImage boolIm2BuffIm(boolean[][] boolImage, int backgroundRGB, int foregroundRGB) {
        int x, y;
        int wi = boolImage[0].length, he = boolImage.length;
        int dRGB;

        BufferedImage buffImage = new BufferedImage(wi, he, BufferedImage.TYPE_INT_RGB);
        for (y = 0; y < he; y++) {
            for (x = 0; x < wi; x++) {
                if (boolImage[y][x]) {
                    dRGB = foregroundRGB;
                } else {
                    dRGB = backgroundRGB;
                }
                buffImage.setRGB(x, y, dRGB);
            }
        }

        return buffImage;
    }

    public static Shape[] findThinShapes(Shape shape, int n, int outerTryLimit, int innerTryLimit, double thinnessLimit, int areaLowerLimit, int areaHigherLimit) {
        Random r = new Random();
        Shape workingShape = (Shape) shape.clone();
        Shape[] thinShapes = new Shape[n];

        int outerTries = 0;
        int maxFound = 0;
        int i = 0;
        while ((i < n) && (outerTries < outerTryLimit)) {
            Shape thinShape = null;
            int innerTries = 0;
            while ((thinShape == null) && (innerTries < innerTryLimit)) {
                // this may be real slow
                Point2D randomPoint = workingShape.getPoint(r.nextInt(shape.getArea()));
                thinShape = ImageAlgorithms.reduceShapeToAreaAndRatio(workingShape, randomPoint, thinnessLimit, areaLowerLimit, areaHigherLimit);
                innerTries++;
            }
            if (thinShape == null) {
                outerTries++;
                i = 0;
                workingShape = (Shape) shape.clone();
                continue;
            }
            thinShapes[i] = thinShape;
            workingShape.subtract(thinShape);
            i++;
            if (i > maxFound) {
                maxFound = i;
            }
        }

        System.out.format("most thinshapes found: %d\n", maxFound);
        if (outerTries == outerTryLimit) {
            //return null;
            return thinShapes;
        }
        return thinShapes;
    }

    public static Shape reduceShapeToAreaAndRatio(Shape originalShape, Point2D firstPoint, double ratioLimit, int areaLowerLimit, int areaHigherLimit) {
        int neighx[] = {-1, 0, 1, -1, 1, -1, 0, 1};
        int neighy[] = {-1, -1, 1, 0, 0, 1, 1, 1};
        if (ratioLimit <= 0) {
            return null;
        }
        double ratioLimitInv = 1 / ratioLimit;
        int px, py, nx, ny;
        Point2D head, neighbour;



        Shape reducedShape = new Shape();
        int reducedArea = 0;
        double reducedRatio = 1.0;
        ArrayDeque<Point2D> queue = new ArrayDeque();
        if (originalShape == null) {
            return null;
        }
        if (!originalShape.contains(firstPoint)) {
            return null;
        }
        queue.offer(firstPoint);
        while (true) {
            head = queue.poll();

            reducedShape.add(head);
            reducedRatio = reducedShape.getAspectRatio();
            reducedArea = reducedShape.getArea();
            px = head.getX();
            py = head.getY();
            for (int i = 0; i < 8; i++) {
                nx = px + neighx[i];
                ny = py + neighy[i];
                neighbour = new Point2D(nx, ny);
                if ((originalShape.contains(neighbour))
                        && (!reducedShape.contains(neighbour)) && (!queue.contains(neighbour))
                        && (reducedArea + queue.size() < areaHigherLimit)) {
                    queue.offer(neighbour);
                }
            }

            /*
             * Stop condition. It's weird but it's faster
             */
            if (queue.isEmpty()) {
                break;
            } else {
                if (reducedArea < areaLowerLimit) {
                    continue;
                } else if (reducedArea > areaHigherLimit) {
                    break;
                } else {
                    if ((ratioLimitInv < reducedRatio) && (reducedRatio < ratioLimit)) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
        }

        if ((reducedShape.getArea() < areaLowerLimit) || ((ratioLimitInv < reducedRatio) && (reducedRatio < ratioLimit))) {
            return null;
        }
        return reducedShape;
    }

    public static boolean[][] shape2BoolIm(Shape shape, int wi, int he) {
        Iterator<Point2D> itsh = shape.iterator();
        Point2D pt;

        boolean[][] boolImage = new boolean[he][wi];
        while (itsh.hasNext()) {
            pt = itsh.next();
            boolImage[pt.getY()][pt.getX()] = true;
        }

        return boolImage;
    }

    public static BufferedImage boolImOverBuffIm(boolean[][] boolIm, BufferedImage buffIm, int foregroundRGB) {
        int wi = boolIm[0].length, he = boolIm.length;
        if ((wi > buffIm.getWidth()) || (he > buffIm.getHeight())) {
            return null;
        }
        int x, y;

        for (y = 0; y < he; y++) {
            for (x = 0; x < wi; x++) {
                if (boolIm[y][x]) {
                    buffIm.setRGB(x, y, foregroundRGB);
                }
            }
        }

        return buffIm;
    }

    public static boolean[][] transZoomBoolIm(boolean[][] sIm, int tx, int ty, double zx, double zy, int cx, int cy) {

        int wi = sIm[0].length, he = sIm.length;
        boolean[][] dIm = new boolean[he][wi];
        int sxi, syi, dxi, dyi;
        double dx, dy, sxr, syr;

        zx = 1 / zx;
        zy = 1 / zy;

        // down and right
        syr = cy;
        syi = cy;
        dyi = cy + ty;
        while ((dyi >= 0) && (dyi < he)) {
            sxr = cx;
            sxi = cx;
            dxi = cx + tx;
            while ((dxi >= 0) && (dxi < wi)) {
                if ((sxi >= 0) && (sxi < wi) && (syi >= 0) && (syi < he)) {
                    dIm[dyi][dxi] = sIm[syi][sxi];
                } else {
                    dIm[dyi][dxi] = false;
                }

                sxr += zx;
                sxi = (int) sxr;
                dxi++;
            }
            syr += zy;
            syi = (int) syr;
            dyi++;
        }

        // down and left
        syr = cy;
        syi = cy;
        dyi = cy + ty;
        while ((dyi >= 0) && (dyi < he)) {
            sxr = cx;
            sxi = cx;
            dxi = cx + tx;
            while ((dxi >= 0) && (dxi < wi)) {
                if ((sxi >= 0) && (sxi < wi) && (syi >= 0) && (syi < he)) {
                    dIm[dyi][dxi] = sIm[syi][sxi];
                } else {
                    dIm[dyi][dxi] = false;
                }

                sxr -= zx;
                sxi = (int) (sxr + 1.0);
                dxi--;
            }
            syr += zy;
            syi = (int) syr;
            dyi++;
        }

        // up and right
        syr = cy;
        syi = cy;
        dyi = cy + ty;
        while ((dyi >= 0) && (dyi < he)) {
            sxr = cx;
            sxi = cx;
            dxi = cx + tx;
            while ((dxi >= 0) && (dxi < wi)) {
                if ((sxi >= 0) && (sxi < wi) && (syi >= 0) && (syi < he)) {
                    dIm[dyi][dxi] = sIm[syi][sxi];
                } else {
                    dIm[dyi][dxi] = false;
                }

                sxr += zx;
                sxi = (int) sxr;
                dxi++;
            }
            syr -= zy;
            syi = (int) (syr + 1.0);
            dyi--;
        }

        // up and left
        syr = cy;
        syi = cy;
        dyi = cy + ty;
        while ((dyi >= 0) && (dyi < he)) {
            sxr = cx;
            sxi = cx;
            dxi = cx + tx;
            while ((dxi >= 0) && (dxi < wi)) {
                if ((sxi >= 0) && (sxi < wi) && (syi >= 0) && (syi < he)) {
                    dIm[dyi][dxi] = sIm[syi][sxi];
                } else {
                    dIm[dyi][dxi] = false;
                }

                sxr -= zx;
                sxi = (int) (sxr + 1.0);
                dxi--;
            }
            syr -= zy;
            syi = (int) (syr + 1.0);
            dyi--;
        }

        return dIm;
    }

    public static double compareTwoBoolIms(boolean[][] aIm, boolean[][] bIm) {
        if ((aIm == null) || (bIm == null)) {
            return 0.0;
        }
        if ((aIm[0] == null) || (bIm[0] == null)) {
            return 0.0;
        }
        if ((aIm.length != bIm.length) || (aIm[0].length != bIm[0].length)) {
            return 0.0;
        }
        
        int wi = aIm[0].length, he = aIm.length;
        int x, y;
        int commonArea = 0;
        int aArea = 0;

        for (y = 0; y < he; y++) {
            for (x = 0; x < wi; x++) {
                boolean a = aIm[y][x];
                boolean b = bIm[y][x];
                if (a) {
                    aArea++;
                }
                if (a && b) {                    
                    commonArea++;
                }
            }
        }

        double match = (double) commonArea / aArea;
        return match;
    }

}
