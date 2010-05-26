package Data;

import java.io.File;

public class Word {

    private String wordName;
    private File imagePath;

    /**
     * Class contructor. The name argument is a specifier.
     * The url argument must specify an relative adress on the pshysical memory.
     *
     * @param  path  an relative adress on the pshysical memory giving the base location of the image
     * @param  name the name of the image
     */
    public Word(String name, File path) {
        this.wordName = name;
        this.imagePath = path;
    }

    /**
     * Returns the name of this picture in use
     * @return      Returns an String object.
     *
     */
    public String getName() {
        return this.wordName;
    }


    public File getImagePath() {
        return this.imagePath;
    }
}
