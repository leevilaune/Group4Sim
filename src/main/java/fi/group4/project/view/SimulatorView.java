package fi.group4.project.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SimulatorView extends Application {
    @Override
    public void start(Stage stage) {
        Pane pane = new FlowPane();
        Display d = new Display(600,400, Color.BLANCHEDALMOND);
        d.setX(500);
        d.setY(300);
        d.drawBall();

        d.addBall(30,50,4,Color.BLANCHEDALMOND);
        d.addBall(500,300,2,Color.AZURE);

        Scene scene = new Scene(pane,600,400);
        pane.getChildren().add(d);
        stage.setScene(scene);
        stage.show();
    }

}
