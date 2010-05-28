package Data;

import java.awt.image.BufferedImage;
import java.io.File;

public class Word {

    private String wordName;
    private BufferedImage image;

    /**
     * Class contructor. The name argument is a specifier.
     * The img argument must specify the image corresponding to this word.
     *
     * @param  path  an relative adress on the pshysical memory giving the base location of the image
     * @param  img   the image corresponding to this Word
     */
    public Word(String name, BufferedImage img) {
        this.wordName = name;
        this.image = img;
    }

    /**
     * Returns the name of this Word
     * @return      Returns an String object.
     *
     */
    public String getName() {
        return this.wordName;
    }

    /**
     * Gets the BufferedImage of this Word
     * @return the BufferedImage of this Word
     */
    public BufferedImage getImage() {
        return this.image;
    }
}
