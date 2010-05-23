package Data;

public class GrayImageAndHistogram {

    private int[][] grayImage;
    private int[] histogram;

    public GrayImageAndHistogram(int[][] grayIm, int[] hist) {
        this.grayImage = grayIm;
        this.histogram = hist;
    }

    public int[][] getGrayImage() {
        return this.grayImage;
    }

    public int[] getHistogram() {
        return this.histogram;
    }
}
