package fi.group4.project.view;

import fi.group4.project.controller.SimulatorController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;

public class SimulatorView extends Application {

    private SimulatorController controller;

    private HashMap<String, TextField> queueCounters;
    private HashMap<String, TextField> taskCounters;

    public SimulatorView(){
        this.controller = new SimulatorController(this);
        this.queueCounters = new HashMap<>();
        this.taskCounters = new HashMap<>();
    }
    @Override
    public void start(Stage stage) {
        Pane pane = new FlowPane();
        Display d = new Display(600,200, Color.BLANCHEDALMOND);
        d.setX(500);
        d.setY(300);
        d.drawBall();

        d.addBall(30,50,4,Color.BLANCHEDALMOND);
        d.addBall(500,300,2,Color.AZURE);

        Scene scene = new Scene(pane,600,500);
        pane.getChildren().add(d);
        pane.getChildren().add(createFormGrid());
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20;");

        String[] labels = {"Planning","Implementation","Testing", "Review", "Presentation"};
        int i = 0;
        for (String s : labels) {
            Label label = new Label(s);
            TextField textField1 = new TextField();
            TextField textField2 = new TextField();

            grid.add(label, 0, i);
            grid.add(textField1, 1, i);
            grid.add(textField2, 2, i);
            i++;
        }

        Label sliderLabel = new Label("Adjust Value:");
        Slider slider = new Slider(0, 100, 50);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(25);
        slider.setMinorTickCount(4);
        slider.setBlockIncrement(1);

        int sliderRow = 4;
        grid.add(sliderLabel, 0, sliderRow);
        grid.add(slider, 1, sliderRow, 2, 1);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.controller.setSpeed(slider.getValue());
        });
        return grid;
    }

}
