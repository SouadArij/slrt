package Data;

//import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;

public class Shape implements Cloneable {

    HashSet<Point2D> set;
    private int sumX, sumY;
    private Point2D leftMostPoint, rightMostPoint, topMostPoint, bottomMostPoint;

    public Shape() {        
        this.set = new HashSet();
        this.sumX = 0;
        this.sumY = 0;
        this.leftMostPoint = null;
        this.rightMostPoint = null;
        this.topMostPoint = null;
        this.bottomMostPoint = null;
    }

    public Shape(HashSet<Point2D> set, int sumX, int sumY, Point2D leftMostPoint, Point2D rightMostPoint, Point2D topMostPoint, Point2D bottomMostPoint) {
        this.set = set;
        this.sumX = sumX;
        this.sumY = sumY;
        this.leftMostPoint = leftMostPoint;
        this.rightMostPoint = rightMostPoint;
        this.topMostPoint = topMostPoint;
        this.bottomMostPoint = bottomMostPoint;
    }
    
    public void add(Point2D p) {
        int x = p.getX(), y = p.getY();
        
        this.set.add(p);

        this.sumX += x;
        this.sumY += y;

        if (this.leftMostPoint == null) {
            this.leftMostPoint = p;
        } else {
            if (x < this.leftMostPoint.getX()) {
                this.leftMostPoint = p;
            }
        }
        if (this.rightMostPoint == null) {
            this.rightMostPoint = p;
        } else {
            if (x > this.rightMostPoint.getX()) {
                this.rightMostPoint = p;
            }
        }
        if (this.topMostPoint == null) {
            this.topMostPoint = p;
        } else {
            if (y < this.topMostPoint.getY()) {
                this.topMostPoint = p;
            }
        }
        if (this.bottomMostPoint == null) {
            this.bottomMostPoint = p;
        } else {
            if (y > this.bottomMostPoint.getY()) {
                this.bottomMostPoint = p;
            }
        }        
    }

    public void remove(Point2D p) {
        int x = p.getX(), y = p.getY();
        
        this.set.remove(p);

        this.sumX -= x;
        this.sumY -= y;

        // BIG WARNING! Margin points and coords are not moved back.
    }

    public void subtract(Shape s) {
        int area = s.getArea();
        Point2D center = s.getCenter();
        int cx = center.getX(), cy = center.getY();

        this.set.removeAll(s.getSet());

        this.sumX -= area * cx;
        this.sumY -= area * cy;

        // BIG WARNING! Margin points and coords are not moved back.
    }

    public Point2D getCenter() {
        int s = this.set.size();
        if (s != 0) {
            return new Point2D(Math.round((float)sumX / s), Math.round((float)sumY / s));
        }

        return null;
    }

    public Point2D getLeftMostPoint() {
        return this.leftMostPoint;
    }

    public Point2D getRightMostPoint() {
        return this.rightMostPoint;
    }

    public Point2D getTopMostPoint() {
        return this.topMostPoint;
    }

    public Point2D getBottomMostPoint() {
        return this.bottomMostPoint;
    }

    public int getArea() {
        return this.set.size();
    }

    /*
     * This can be immensely optimized. From O(n*n) to O(n).
     * And really, this is the part where we loose time.
     * Just a little mathematics. Later on...
     */ 
    public double getElongation() {
        Point2D pt, ce = this.getCenter();
        int px, py, dx, dy, cx = ce.getX(), cy = ce.getY();
        int nom = 0, den = 0;
        double result = 0.0;

        Iterator<Point2D> itsh = this.set.iterator();
        while (itsh.hasNext()) {
            pt = itsh.next();
            px = pt.getX();
            py = pt.getY();
            dy = (cy - py);
            dx = (cx - px);
            nom += dx * dy;
            den += dx * dx - dy * dy;
        }                
        
        if (den != 0) {
            result = Math.atan(2.0 * nom / den);
            result /= 2;
            if (result > 0) {
                result -= Math.PI / 2;
            } else {
                result += Math.PI / 2;
            }
        }

        return result;
    }

    public double getAspectRatio() {
        double el = this.getElongation();
        double le = this.getElongation() + Math.PI / 2;
        double tanel = Math.tan(el), tanle = Math.tan(le), cosel = Math.cos(el);
        double minX, maxX, minY, maxY;
        double width, height, ratio = 0.0;
        double x, y;
        Point2D pt;

        Iterator<Point2D> itsh = this.set.iterator();
        pt = itsh.next();
        minY = maxY = pt.getY() - tanel * pt.getX();
        minX = maxX = (pt.getX() - pt.getY()) / tanle;
        while (itsh.hasNext()) {
            pt = itsh.next();
            
            // elongation parallel intersects the OY axis in:
            y = pt.getY() - tanel * pt.getX();

            // elongation perpendicular intersects the OX axis in:
            x = (pt.getX() - pt.getY()) / tanle;

            if (y > maxY) {
                maxY = y;
            }
            if (y < minY) {
                minY = y;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (x < minX) {
                minX = x;
            }
        }

        // length by perpendicular
        width = Math.abs((maxY - minY) * cosel);
        
        // length by elongation
        height = Math.abs((maxX - minX) * cosel);

        if ((height != 0) && (width != 0)) {
            if (width > height) {
                ratio = width / height;
            } else {
                ratio = height / width;
            }
        }

        return ratio;
    }

    public Point2D getPoint(int k) {
        Iterator<Point2D> itp = this.set.iterator();
        Point2D p = null;

        int i = 0;
        while ((itp.hasNext()) && (i < k)) {
            p = itp.next();
            i++;
        }

        return p;
    }

    public Iterator<Point2D> iterator() {
        return this.set.iterator();
    }

    public boolean contains(Point2D p) {
        return this.set.contains(p);
    }

    public HashSet<Point2D> getSet() {
        return this.set;
    }

    @Override
    public Object clone() {        

        HashSet<Point2D> clonedSet = (HashSet<Point2D>) this.set.clone();
        Shape clonedShape = new Shape(clonedSet, this.sumX, this.sumY, this.leftMostPoint, this.rightMostPoint, this.topMostPoint, this.bottomMostPoint);

        return clonedShape;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Shape) {
            Shape equalShape = (Shape) o;
            if (equalShape.getSet().equals(this.set)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.set.hashCode();
    }
}
