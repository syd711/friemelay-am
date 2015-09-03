package de.friemelay.am.ui.imageeditor;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 *
 */
public class ImageTab extends Tab {
  private ImageVariant variant;

  public ImageTab(ImageVariant variant) {
    super(variant.toString());
    this.variant = variant;
    setClosable(false);

    ImageView imageView = new ImageView(variant.getImage());
    BorderPane main = new BorderPane();
    main.setPadding(new Insets(5, 5, 5, 5));
    main.setCenter(imageView);
    setContent(main);
  }
}
