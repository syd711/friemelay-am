package de.friemelay.am.ui.catalog;

import de.friemelay.am.model.AbstractModel;
import de.friemelay.am.model.CatalogItem;
import de.friemelay.am.model.Product;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by Matthias on 07.09.2015.
 */
public class CatalogTreeCell extends TreeCell<CatalogItem> {

  private TextField textField;

  public CatalogTreeCell() {
  }

  @Override
  public void startEdit() {
    super.startEdit();

    if (textField == null) {
      createTextField();
    }
    setText(null);
    setGraphic(textField);
    textField.selectAll();
  }

  @Override
  public void cancelEdit() {
    super.cancelEdit();
    setText(getItem().toString());
    setGraphic(getTreeItem().getGraphic());
  }

  @Override
  public void updateItem(CatalogItem item, boolean empty) {
    super.updateItem(item, empty);

    if (empty) {
      setText(null);
      setGraphic(null);
    } else {
      if (isEditing()) {
        if (textField != null) {
          textField.setText(getString());
        }
        setText(null);
        setGraphic(textField);
      } else {
        setText(getString());
        if(item.getStatusValue() == 1) {
          setStyle("-fx-text-fill:#000;");
          if(item.getType() == AbstractModel.TYPE_VARIANT || item.getType() == AbstractModel.TYPE_PRODUCT) {
            Product product = (Product) item;
            if(!product.isOnStock()) {
              setStyle("-fx-text-fill:#cc4444;-fx-font-weight:bold;");
            }
            else {
              setStyle("-fx-text-fill:#000;");
            }
          }
        }
        else {
          setStyle("-fx-text-fill:#aaa;-fx-font-weight:normal;");
        }


        setGraphic(getTreeItem().getGraphic());
      }
    }
  }

  private void createTextField() {
    textField = new TextField(getString());
    textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

      @Override
      public void handle(KeyEvent t) {
        if(t.getCode() == KeyCode.ENTER) {
//          commitEdit(textField.getText());
        }
        else if(t.getCode() == KeyCode.ESCAPE) {
          cancelEdit();
        }
      }
    });
  }

  private String getString() {
    return getItem() == null ? "" : getItem().toString();
  }
}