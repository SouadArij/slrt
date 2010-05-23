package Data;

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter {

    public boolean accept(File file){
        String fileName = file.getName();
        if (file.isFile()
                && (fileName.endsWith(".jpg") || fileName.endsWith("bmp"))) {
            return true;
        }

        return false;
    }

    public String description() {
        return "Image Type";
    }
}
