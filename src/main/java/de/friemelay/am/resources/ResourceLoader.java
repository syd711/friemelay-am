package de.friemelay.am.resources;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.log4j.Logger;

/**
 * Used for load resources and stuff.
 */
public class ResourceLoader {
  
  public static String getResource(String s) {
    try {
      return ResourceLoader.class.getResource(s).toExternalForm();
    } catch (Exception e) {
      Logger.getLogger(ResourceLoader.class.getName()).error("Resource not found: " + s + ": " + e.getMessage());
    }
    return null;
  }
  
  public static ImageView getImageView(String s) {
    return new ImageView(new Image(ResourceLoader.getResource(s)));
  }

  public static ImageView getWebImageView(String s) {
    Image image = new Image(s);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(100);
    imageView.setFitHeight(80);
    return imageView;
  }
}
