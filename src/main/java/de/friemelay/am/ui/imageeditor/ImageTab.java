package de.friemelay.am.ui.imageeditor;

import de.friemelay.am.ui.util.WidgetFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class ImageTab extends Tab implements EventHandler<ActionEvent> {
  private ImageVariant variant;

  private Button addButton;
  private Button removeButton;
  private Button pasteButton;

  private ImageEditor imageEditor;
  private ImageView imageView;

  public ImageTab(ImageEditor imageEditor, ImageVariant variant) {
    super(variant.toString());
    this.imageEditor = imageEditor;
    this.variant = variant;
    setClosable(false);

    imageView = new ImageView();
    if(variant.getImage() != null) {
      imageView.setImage(variant.getImage());
    }

    ScrollPane centerScroller = new ScrollPane();
    centerScroller.setFitToWidth(true);
    BorderPane scrollRoot = new BorderPane();
    scrollRoot.setPadding(new Insets(5,5,5,5));
    scrollRoot.setCenter(imageView);
    centerScroller.setContent(scrollRoot);

    BorderPane main = new BorderPane();
    main.setCenter(centerScroller);
    setContent(main);

    ToolBar toolBar = new ToolBar();
    addButton = WidgetFactory.createButton(toolBar, "", "open.png", this);
    removeButton = WidgetFactory.createButton(toolBar, "", "remove.png", this);
    toolBar.getItems().add(new Separator());
    pasteButton = WidgetFactory.createButton(toolBar, "", "paste.png", this);
    main.setTop(toolBar);
  }


  public void addImageVariant(File variant) {
    try {
      FileInputStream fileInputStream = new FileInputStream(variant);
      addImageVariant(variant.getName(), fileInputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void addImageVariant(String name, Blob blob) {
    try {
      InputStream binaryStream = blob.getBinaryStream();
      addImageVariant(name, binaryStream);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void addImageVariant(String name, Image image) {
    variant.setName(name);
    variant.setImage(image);
    setText(variant.toString());
    imageView.setImage(image);

    imageEditor.setDirty(true, variant);
  }

  private void addImageVariant(String name, InputStream in) {
    Image image = new Image(in);
    addImageVariant(name, image);
  }

  @Override
  public void handle(ActionEvent event) {
    if(event.getSource() == addButton) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Bilddatei öffnen");
      fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Dateityp", "*.png", ".jpg"));
      if(ImageEditor.lastLocation != null) {
        fileChooser.setInitialDirectory(ImageEditor.lastLocation);
      }
      File file = fileChooser.showOpenDialog(imageEditor.getStage());
      if(file != null) {
        ImageEditor.lastLocation = file.getParentFile();
        addImageVariant(file);
      }
    }
    else if(event.getSource() == removeButton) {
      variant.setImage(null);
      variant.setName(null);
      imageView.setImage(null);
      imageEditor.setDirty(true, variant);
    }
    else if(event.getSource() == pasteButton) {
      Clipboard clipboard = Clipboard.getSystemClipboard();
      Image image = clipboard.getImage();
      if(image != null) {
        String name = "Aus Zwichenablage (" + imageEditor.getTabs().size() + ")";
        if(clipboard.getHtml() != null && clipboard.getHtml().startsWith("<img")) {
          String html = clipboard.getHtml();
          html = html.replaceAll("/>", "");
          name = html.substring(html.lastIndexOf("/")+1, html.lastIndexOf("\""));
        }
        addImageVariant(name, image);
      }
      List<File> files = clipboard.getFiles();
      if(files != null && !files.isEmpty()) {
        File file = files.get(0);
        if(file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpg")) {
          addImageVariant(file);
        }
      }
    }
  }

}
