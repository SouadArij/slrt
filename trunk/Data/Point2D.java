package Data;

public class Point2D {
    private int x, y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point2D) {
            Point2D p = (Point2D)o;
            if ((p.getX() == this.x) && (p.getY() == this.y)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.x;
        hash = 43 * hash + this.y;
        return hash;
    }
}
