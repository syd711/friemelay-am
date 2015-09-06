package de.friemelay.am.ui.imageeditor;

import java.awt.image.BufferedImage;

/**
 * Created by Matthias on 03.09.2015.
 */
public class ImageVariant {
  private BufferedImage image;
  private String name;

  public ImageVariant(String name, BufferedImage image) {
    this.image = image;
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }

  public BufferedImage getImage() {
    return image;
  }

  @Override
  public String toString() {
    if(name == null) {
      name = "Bild";
    }

    if(image == null) {
      return name;
    }

    return name + " (" + (int)image.getWidth() + " x " + (int)image.getHeight() + ")";
  }
}
