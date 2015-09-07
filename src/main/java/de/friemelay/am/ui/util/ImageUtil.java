package de.friemelay.am.ui.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;

/**
 *
 */
public class ImageUtil {

  public static InputStream getImageInputStream(BufferedImage image) {
    try {
      ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
      ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
      jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      jpgWriteParam.setCompressionQuality(1f);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ImageOutputStream imgOut = new MemoryCacheImageOutputStream(outputStream);
      jpgWriter.setOutput(imgOut);
      IIOImage outputImage = new IIOImage(image, null, null);
      jpgWriter.write(null, outputImage, jpgWriteParam);
      jpgWriter.dispose();

      return new ByteArrayInputStream(outputStream.toByteArray());
    } catch (IOException e) {
      Logger.getLogger(ImageUtil.class).error("Failed to create input stream for image: " + e.getMessage(), e);
    }
    return null;
  }

  public static BufferedImage readImage(String column, ResultSet resultSet) {
    try {
      Blob blob = resultSet.getBlob(column);
      if(blob == null) {
        return null;
      }
      InputStream is = blob.getBinaryStream();
      return ImageIO.read(is);
    } catch (Exception e) {
      Logger.getLogger(ImageUtil.class).error("Error reading image from database: " + e.getMessage(), e);
    }
    return null;
  }

  public static BufferedImage createThumbnail(BufferedImage image, int maxWidth, int maxHeight) {
    return Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, maxWidth, maxHeight);
  }

  public static Image toImage(BufferedImage image) {
    return SwingFXUtils.toFXImage(image, null);
  }

  public static BufferedImage toBufferedImage(Image image) {
    return SwingFXUtils.fromFXImage(image, null);
  }
}
