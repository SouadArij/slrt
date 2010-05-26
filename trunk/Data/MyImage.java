package Data;

import java.awt.image.BufferedImage;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * DEPRECATED!!!
 */
public class MyImage {

    private String name;
    private BufferedImage image;

    public MyImage(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public BufferedImage getImage() {
        return this.image;
    }
}
