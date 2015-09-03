package de.friemelay.am.ui.imageeditor;

import de.friemelay.am.ui.util.WidgetFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ImageEditor extends BorderPane implements EventHandler<ActionEvent> {
  private static File lastLocation;
  private Button pasteButton;

  private ImageTabPane imageTabs;
  private Stage stage;
  private List<ImageEditorChangeListener> changeListeners = new ArrayList<>();
  private boolean dirty = false;
  private int maxTabs = 1;
  private Button addButton;
  private Button removeButton;

  public ImageEditor(Stage stage, int height) {
    this.stage = stage;
    imageTabs = new ImageTabPane();
    setCenter(imageTabs);
    setMinHeight(height);
    setStyle("-fx-border-color:#DDD; -fx-border-width:1px;");

    HBox actionBox = new HBox(10);
    actionBox.setStyle("-fx-border-color:#DDD; -fx-border-width:1px;");
    actionBox.setPadding(new Insets(5, 5, 5, 5));
    addButton = WidgetFactory.createButton(actionBox, "Bild auswählen...", this);
    pasteButton = WidgetFactory.createButton(actionBox, "Aus Zwischenablage einfügen", this);
    removeButton = WidgetFactory.createButton(actionBox, "Bild entfernen", this);
    removeButton.setDisable(true);
    setBottom(actionBox);
  }

  public void setImageTabCount(int max) {
    this.maxTabs = max;
  }

  public void addChangeListener(ImageEditorChangeListener listener) {
    this.changeListeners.add(listener);
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
    imageTabs.getTabs().addAll(new ImageTab(new ImageVariant(name, image)));
    if(imageTabs.getTabs().size() == maxTabs) {
      addButton.setDisable(true);
    }
    removeButton.setDisable(false);
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
      fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Dateitype", "*.png", ".jpg"));
      if(lastLocation != null) {
        fileChooser.setInitialDirectory(lastLocation);
      }
      File file = fileChooser.showOpenDialog(stage);
      if(file != null) {
        setDirty(true);
        lastLocation = file.getParentFile();
        addImageVariant(file);
      }
    }
    else if(event.getSource() == removeButton) {
      addButton.setDisable(false);
    }
    else if(event.getSource() == pasteButton) {
      Clipboard clipboard = Clipboard.getSystemClipboard();
      Image image = clipboard.getImage();
      if(image != null) {
        addImageVariant("?", image);
      }
    }
  }

  protected void setDirty(boolean b) {
    this.dirty = b;
    if(dirty) {
      for(ImageEditorChangeListener changeListener : changeListeners) {
        changeListener.editorChanged();
      }

    }
  }
}
