package de.friemelay.am.ui.imageeditor;

import de.friemelay.am.ui.util.ImageUtil;
import de.friemelay.am.ui.util.WidgetFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class ImageTab extends Tab implements EventHandler<ActionEvent> {
  private ImageVariant variant;

  private Button addButton;
  private Button newButton;
  private Button removeButton;
  private Button pasteButton;

  private ImageEditor imageEditor;
  private ImageView imageView;

  public ImageTab(ImageEditor imageEditor, ImageVariant variant) {
    super(variant.toString());
    this.imageEditor = imageEditor;
    this.variant = variant;
    setClosable(imageEditor.getTabs().size() >= 1);

    setOnClosed(new EventHandler<Event>() {
      @Override
      public void handle(Event event) {
        variant.setName(null);
        variant.setImage(null);
        imageEditor.setDirty(true, variant);
      }
    });

    imageView = new ImageView();
    if(variant.getImage() != null) {
      imageView.setImage(ImageUtil.toImage(variant.getImage()));
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
    addButton = WidgetFactory.createButton(toolBar, "", "open.png", "Bilddatei öffnen", this);
    removeButton = WidgetFactory.createButton(toolBar, "", "remove.png", "Bild entfernen", this);
    toolBar.getItems().add(new Separator());
    newButton = WidgetFactory.createButton(toolBar, "", "plus.png", "Neues Bild hinzufügen", this);
    newButton.setDisable(imageEditor.getTabs().size()+1 == imageEditor.getMaxTabs());
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

  private void addImageVariant(String name, BufferedImage image) {
    variant.setName(name);
    variant.setImage(image);
    setText(variant.toString());
    imageView.setImage(ImageUtil.toImage(variant.getImage()));

    imageEditor.setDirty(true, variant);
  }

  private void addImageVariant(String name, InputStream in) {
    try {
      BufferedImage image = ImageIO.read(in);
      addImageVariant(name, image);
    } catch (IOException e) {
      e.printStackTrace();
    }

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
    else if(event.getSource() == newButton) {
      imageEditor.openTab("Bild", null);
    }
    else if(event.getSource() == removeButton) {
      variant.setImage(null);
      variant.setName(null);
      imageView.setImage(null);
      setText("Bild");
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
          html = html.substring(html.indexOf("src=")+5, html.length());
          name = html.substring(html.lastIndexOf("/")+1, html.indexOf("\""));
        }
        addImageVariant(name, ImageUtil.toBufferedImage(image));
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

  public ImageVariant getVariant() {
    return variant;
  }

}
