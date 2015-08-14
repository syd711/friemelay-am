package de.friemelay.am.images;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.log4j.Logger;

/**
 * Used for load images and stuff.
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
}
