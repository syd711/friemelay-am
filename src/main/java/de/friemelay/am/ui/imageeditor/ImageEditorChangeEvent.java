package de.friemelay.am.ui.imageeditor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ImageEditorChangeEvent {

  private ImageVariant imageVariant;
  private List<ImageVariant> allVariants;
  private ImageEditor editor;

  public ImageEditorChangeEvent(ImageEditor editor, ImageVariant variant, List<ImageVariant> allVariants) {
    this.imageVariant = variant;
    this.allVariants = allVariants;
    this.editor = editor;
  }

  public ImageVariant getImageVariant() {
    return imageVariant;
  }

  public List<ImageVariant> getAllVariants() {
    return allVariants;
  }

  public List<BufferedImage> getAllImages() {
    List<BufferedImage> images = new ArrayList<>();
    for(ImageVariant v : allVariants) {
      images.add(v.getImage());
    }
    return images;
  }

  public BufferedImage getImage() {
    return imageVariant.getImage();
  }

  public ImageEditor getSource() {
    return editor;
  }
}
