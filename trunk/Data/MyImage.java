package Data;

import java.awt.image.BufferedImage;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gicu
 */
public class MyImage {

    private String name;
    private BufferedImage image;
/**
 * Class contructor. The name argument is a specifier.
 * The url argument must specify an relative adress on the pshysical memory.
 *
 * @param  url  an relative adress on the pshysical memory giving the base location of the image
 * @param  name the name of the image
 */
    public MyImage(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
    }
/**
 * Returns the name of this picture in use
 * @return      Returns an String object.
 *
 */
    public String getName() {
        return this.name;
    }

    public BufferedImage getImage() {
        return this.image;
    }
}
