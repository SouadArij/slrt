package xmlparser;



import java.util.Vector;

/*
 * This is just a replacement class for the future implementation
 * of the XML creating and parsing.
 */
/**
 *
 * @author Nick
 */
public class XMLParser {

    private Vector<Letter> letters = new Vector<Letter>();
    private Vector<MyImage> images = new Vector<MyImage>();

    public XMLParser() {
        Letter l = new Letter("A", "D:/letters/letterA/");
        Letter l2 = new Letter("B", "D:/letters/letterB/");
        letters.add(l);
        letters.add(l2);
        MyImage i = new MyImage("dog", "D:/images/dog.bmp");
        MyImage i2 = new MyImage("cat", "D:/images/cat.bmp");
        images.add(i);
        images.add(i2);
    }

    public Vector<Letter> getLetters() {
        return letters;
    }

    public Vector<MyImage> getMyImages() {
        return images;
    }
}
