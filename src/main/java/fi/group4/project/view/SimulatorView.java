package fi.group4.project.view;

import fi.group4.project.controller.SimulatorController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import simu.model.EventType;
import simu.model.ServicePointController;

import java.util.Arrays;
import java.util.HashMap;

public class SimulatorView extends Application {

    private SimulatorController controller;
    private Display d;

    private HashMap<EventType, TextField> queueCounters;
    private HashMap<EventType, TextField> reservedCounters;

    public SimulatorView(){
        this.controller = new SimulatorController(this);
        this.queueCounters = new HashMap<>();
        this.reservedCounters = new HashMap<>();
    }
    @Override
    public void start(Stage stage) {
        Pane pane = new FlowPane();
        /*
        this.d = new Display(600,200, Color.BLANCHEDALMOND);
        d.setX(500);
        d.setY(300);
        d.drawBall();

        d.addBall(30,50,4,Color.BLANCHEDALMOND);
        d.addBall(500,300,2,Color.AZURE);


         */
        Scene scene = new Scene(pane,600,500);
        //pane.getChildren().add(d);
        stage.setScene(createParameterScene(stage));
        stage.show();
        stage.setOnCloseRequest(event -> {
            //terminate();
            System.exit(0);
        });
    }

    private GridPane createCounterGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20;");

        grid.add(new Label("Event Type"), 0, 0);
        grid.add(new Label("Queue"), 1, 0);
        grid.add(new Label("Reserved"), 2, 0);

        EventType[] labels = {EventType.ARR1, EventType.DEP1, EventType.DEP2, EventType.DEP3,EventType.DEP4,EventType.DEP5};
        int i = 1;
        for (EventType s : labels) {
            Label label = new Label(s.name());
            TextField queueField = new TextField();
            TextField reservedField = new TextField();
            queueField.setEditable(false);
            reservedField.setEditable(false);
            this.queueCounters.putIfAbsent(s, queueField);
            this.reservedCounters.putIfAbsent(s, reservedField);

            grid.add(label, 0, i);
            grid.add(queueField, 1, i);
            grid.add(reservedField, 2, i);
            i++;
        }

        Label sliderLabel = new Label("Speed:");
        Slider slider = new Slider(0, 1000, 500);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(25);
        slider.setMinorTickCount(4);
        slider.setBlockIncrement(1);

        int sliderRow = i;
        grid.add(sliderLabel, 0, sliderRow);
        grid.add(slider, 1, sliderRow, 2, 1);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.controller.setSpeed(slider.getValue());
        });

        return grid;
    }

    private Scene createParameterScene(Stage stage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20;");

        Label title = new Label("Set Parameters (People Amounts)");
        grid.add(title, 0, 0, 2, 1);

        TextField[] fields = new TextField[5];
        for (int i = 0; i < 5; i++) {
            Label label = new Label("Parameter " + (i + 1) + ":");
            TextField textField = new TextField();
            textField.setPromptText("Enter number");
            textField.setText("1");

            fields[i] = textField;

            grid.add(label, 0, i + 1);
            grid.add(textField, 1, i + 1);
        }

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> {
            int[] params = new int[5];
            for (int i = 0; i < 5; i++) {
                try {
                    params[i] = Integer.parseInt(fields[i].getText());
                    System.out.println(params[i]);
                } catch (NumberFormatException ex) {
                    params[i] = 0;
                }
            }
            System.out.println(params[0]);
            this.controller.setParameters(params[0],params[1],params[2],params[3],params[4]);
            stage.setScene(new Scene(createCounterGrid(), 600, 500));
        });

        grid.add(confirmBtn, 0, 6, 2, 1);
        return new Scene(grid, 400, 300);
    }

    public void updateCounters(ServicePointController[] servicePointControllers){
        Arrays.stream(servicePointControllers).forEach(servicePointController -> {
            this.queueCounters.get(servicePointController.getType())
                    .setText(String.valueOf(servicePointController.getTotalQueue()));
        });
        Arrays.stream(servicePointControllers).forEach(servicePointController -> {
            this.reservedCounters.get(servicePointController.getType())
                    .setText(String.valueOf(servicePointController.reservedAmount()));
        });
    }

    public void terminate(){
        this.d.getBalls().forEach(b -> b.setRunning(false));
    }
}
