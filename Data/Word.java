package Data;

import java.awt.image.BufferedImage;
import java.io.File;

public class Word {

    private String wordName;
    private BufferedImage image;

    /**
     * Class contructor. The name argument is a specifier.
     * The url argument must specify an relative adress on the pshysical memory.
     *
     * @param  path  an relative adress on the pshysical memory giving the base location of the image
     * @param  name the name of the image
     */
    public Word(String name, BufferedImage img) {
        this.wordName = name;
        this.image = img;
    }

    /**
     * Returns the name of this picture in use
     * @return      Returns an String object.
     *
     */
    public String getName() {
        return this.wordName;
    }

    /**
     * Gets the path of this Word's image
     * @return the path of this Word's image
     */
    public BufferedImage getImage() {
        return this.image;
    }
}
