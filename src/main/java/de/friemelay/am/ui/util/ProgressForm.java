package de.friemelay.am.ui.util;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressForm {
  private final Stage dialogStage;
  private final ProgressBar pb = new ProgressBar();

  public ProgressForm(Scene parent, String title) {
    dialogStage = new Stage();
    dialogStage.setTitle(title);
    dialogStage.setMinWidth(300);
    dialogStage.initStyle(StageStyle.UTILITY);
    dialogStage.setResizable(false);
    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.initOwner(parent.getWindow());

    pb.setProgress(-1F);

    final VBox hb = new VBox();
    hb.setMinWidth(250);
    pb.setMinWidth(250);
    hb.setSpacing(5);
    hb.setPadding(new Insets(10,10,10,10));
    hb.setAlignment(Pos.CENTER);
    hb.getChildren().addAll(pb);


    Scene scene = new Scene(hb);
    dialogStage.setScene(scene);
  }

  public void activateProgressBar(final Task<?> task)  {
    pb.progressProperty().bind(task.progressProperty());
    dialogStage.show();
  }

  public Stage getDialogStage() {
    return dialogStage;
  }
}