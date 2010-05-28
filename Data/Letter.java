package Data;

import java.io.File;
import java.util.Vector;

public class Letter {

    private String letterName;
    private File dirPath;
    private Vector<DBImage> dbImages;

    /**
     * Class contructor. The name argument is a specifier.
     * The url argument must specify an relative adress on the pshysical memory.
     *
     * @param  url  an relative adress on the pshysical memory giving the base location of the folder where all the images for the required letter are contained
     * @param  name the name of the letter
     */
    public Letter(String name, File path) {
        this.letterName = name;
        this.dirPath = path;
        this.dbImages = null;
    }

    public void setDBImages(Vector<DBImage> dbImages) {
        this.dbImages = dbImages;
    }

    /**
     * Returns the name of the this Letter
     * @return    Returns an String object
     *
     */
    public String getName() {
        return this.letterName;
    }
    
    /**
     * Returns the the relative path of this letter's images.
     * @return      Returns an String object.
     *
     */
    public File getDirPath() {
        return this.dirPath;
    }

    public Vector<DBImage> getDBImages() {
        return this.dbImages;
    }
}
