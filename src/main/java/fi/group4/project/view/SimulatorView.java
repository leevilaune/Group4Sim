package fi.group4.project.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SimulatorView extends Application {
    @Override
    public void start(Stage stage) {
        Pane pane = new FlowPane();
        Scene scene = new Scene(pane,600,400);

        stage.setScene(scene);
        stage.show();
    }

}
