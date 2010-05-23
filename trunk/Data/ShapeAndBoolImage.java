package Data;

public class ShapeAndBoolImage {

    private Shape shape;
    private boolean[][] boolImage;

    public ShapeAndBoolImage(Shape shape, boolean[][] boolIm) {
        this.shape = shape;
        this.boolImage = boolIm;
    }

    public Shape getShape() {
        return this.shape;
    }

    public boolean[][] getBoolImage() {
        return this.boolImage;
    }
}
