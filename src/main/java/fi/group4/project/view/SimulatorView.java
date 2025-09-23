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
        Display d = new Display(600,400);
        d.drawBall();
        Scene scene = new Scene(pane,600,400);
        pane.getChildren().add(d);
        BallThread ballThr = new BallThread(d);
        ballThr.start();
        stage.setScene(scene);
        stage.show();
    }

}
