package xmlparser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
import javax.imageio.ImageIO;

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

        BufferedImage dog = null;
        BufferedImage cat = null;

        try {
            dog = ImageIO.read(new File("src/db/dictionary/dog.jpg"));
            cat = ImageIO.read(new File("src/db/dictionary/cat.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyImage i = new MyImage("dog", dog);
        MyImage i2 = new MyImage("cat", cat);
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
