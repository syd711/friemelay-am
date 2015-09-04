package de.friemelay.am.ui.imageeditor;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Matthias on 03.09.2015.
 */
public class ImageVariant {
  private Image image;
  private String name;

  public ImageVariant(String name, Image image) {
    this.image = image;
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public void save(OutputStream out) {
    try {
      ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Image getImage() {
    return image;
  }

  @Override
  public String toString() {
    if(image == null && name == null) {
      return "Default Bild";
    }
    return name + " (" + (int)image.getWidth() + " x " + (int)image.getHeight() + ")";
  }
}
