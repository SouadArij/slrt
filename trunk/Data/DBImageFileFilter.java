package Data;

import java.io.File;
import java.io.FileFilter;

public class DBImageFileFilter implements FileFilter {

    public boolean accept(File file) {
        if (file.isFile() && file.getName().endsWith(".dbim")) {
            return true;
        }

        return false;
    }

    public String description(){
        return "DBImage file";
    }
}
