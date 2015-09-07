package de.friemelay.am.ui.imageeditor;

import de.friemelay.am.ui.util.ImageUtil;

import java.awt.image.BufferedImage;

/**
 * Created by Matthias on 03.09.2015.
 */
public class ImageVariant {
  private BufferedImage image;
  private String name;
  private int maxWidth;
  private int maxHeight;

  public ImageVariant(String name, BufferedImage image, int maxWidth, int maxHeight) {
    this.image = image;
    this.name = name;
    this.maxHeight = maxHeight;
    this.maxWidth = maxWidth;

    if(image != null && image.getWidth() > maxWidth) {
      this.image = ImageUtil.createThumbnail(image, maxWidth, maxHeight);
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setImage(BufferedImage image) {
    if(image != null && image.getWidth() > maxWidth) {
      this.image = ImageUtil.createThumbnail(image, maxWidth, maxHeight);
    }
    else {
      this.image = image;
    }
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
