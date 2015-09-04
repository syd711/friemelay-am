package de.friemelay.am.ui.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class ImageUtil {

  public static InputStream getFileInputStream(Image image) {
    try {
      BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ImageIO.write(bImage, "jpg", os);
      return new ByteArrayInputStream(os.toByteArray());
    } catch (IOException e) {
      Logger.getLogger(ImageUtil.class).error("Failed to create input stream for image: " + e.getMessage(), e);
    }
    return null;
  }
}
