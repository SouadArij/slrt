package Data;

import java.io.File;
import java.util.Vector;

public class Letter {

    private String letterName;
    private File dirPath;
    private Vector<DBImage> dbImages;

    public Letter(String name, File path) {
        this.letterName = name;
        this.dirPath = path;
        this.dbImages = null;
    }

    public void setDBImages(Vector<DBImage> dbImages) {
        this.dbImages = dbImages;
    }

    public String getName() {
        return this.letterName;
    }

    public File getDirPath() {
        return this.dirPath;
    }

    public Vector<DBImage> getDBImages() {
        return this.dbImages;
    }
}
