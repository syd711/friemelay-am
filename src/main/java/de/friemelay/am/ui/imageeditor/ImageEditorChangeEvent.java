package de.friemelay.am.ui.imageeditor;

/**
 *
 */
public class ImageEditorChangeEvent {

  private ImageVariant imageVariant;

  public ImageEditorChangeEvent(ImageVariant variant) {
    this.imageVariant = variant;
  }

  public ImageVariant getImageVariant() {
    return imageVariant;
  }
}
